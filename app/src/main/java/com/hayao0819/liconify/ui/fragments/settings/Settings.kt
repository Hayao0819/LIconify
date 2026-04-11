package com.hayao0819.liconify.ui.fragments.settings

import android.content.ComponentName
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import com.hayao0819.liconify.Iconify.Companion.appContext
import com.hayao0819.liconify.Iconify.Companion.appContextLocale
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Const.GITHUB_REPO
import com.hayao0819.liconify.data.common.Const.ICONIFY_CROWDIN
import com.hayao0819.liconify.data.common.Const.TELEGRAM_GROUP
import com.hayao0819.liconify.data.common.Preferences.APP_ICON
import com.hayao0819.liconify.data.common.Preferences.APP_LANGUAGE
import com.hayao0819.liconify.data.common.Preferences.APP_THEME
import com.hayao0819.liconify.data.common.Preferences.FIRST_INSTALL
import com.hayao0819.liconify.data.common.Preferences.ON_HOME_PAGE
import com.hayao0819.liconify.data.common.Preferences.RESTART_SYSUI_AFTER_BOOT
import com.hayao0819.liconify.data.common.Resources.MODULE_DIR
import com.hayao0819.liconify.data.config.RPrefs
import com.hayao0819.liconify.data.database.DynamicResourceDatabase
import com.hayao0819.liconify.data.repository.DynamicResourceRepository
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat
import com.hayao0819.liconify.ui.dialogs.LoadingDialog
import com.hayao0819.liconify.ui.preferences.PreferenceMenu
import com.hayao0819.liconify.utils.AppUtils.openUrl
import com.hayao0819.liconify.utils.AppUtils.restartApplication
import com.hayao0819.liconify.utils.CacheUtils.clearCache
import com.hayao0819.liconify.utils.SystemUtils.disableBlur
import com.hayao0819.liconify.utils.SystemUtils.disableRestartSystemuiAfterBoot
import com.hayao0819.liconify.utils.SystemUtils.enableRestartSystemuiAfterBoot
import com.hayao0819.liconify.utils.SystemUtils.restartSystemUI
import com.hayao0819.liconify.utils.SystemUtils.saveBootId
import com.hayao0819.liconify.utils.SystemUtils.saveVersionCode
import com.hayao0819.liconify.utils.weather.WeatherConfig
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Settings : ControlledPreferenceFragmentCompat() {

    private var loadingDialog: LoadingDialog? = null

    override val title: String
        get() = getString(R.string.settings_title)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.settings

    override val hasMenu: Boolean
        get() = true

    override val menuResource: Int
        get() = R.menu.settings_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize loading dialog
        loadingDialog = LoadingDialog(requireActivity())
    }

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            APP_LANGUAGE -> {
                restartApplication(requireActivity())
            }

            APP_ICON -> {
                val splashActivities = appContextLocale.resources
                    .getStringArray(R.array.app_icon_identifier)
                changeIcon(RPrefs.getString(key, splashActivities[0])!!)
            }

            APP_THEME -> {
                restartApplication(requireActivity())
            }

            RESTART_SYSUI_AFTER_BOOT -> {
                if (RPrefs.getBoolean(key, false)) {
                    enableRestartSystemuiAfterBoot()
                } else {
                    disableRestartSystemuiAfterBoot()
                }
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<PreferenceMenu>("clearAppCache")?.setOnPreferenceClickListener {
            clearCache(appContext)
            Toast.makeText(
                appContext,
                appContextLocale.resources.getString(R.string.toast_clear_cache),
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        findPreference<PreferenceMenu>("disableEverything")?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireActivity())
                .setCancelable(true)
                .setTitle(requireContext().resources.getString(R.string.import_settings_confirmation_title))
                .setMessage(requireContext().resources.getString(R.string.import_settings_confirmation_desc))
                .setPositiveButton(getString(R.string.positive)) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()

                    CoroutineScope(Dispatchers.IO).launch {
                        // Show loading dialog
                        withContext(Dispatchers.Main) {
                            loadingDialog?.show(resources.getString(R.string.loading_dialog_wait))
                        }

                        disableEverything()

                        // Hide loading dialog
                        withContext(Dispatchers.Main) {
                            delay(3000)
                            loadingDialog?.hide()
                        }

                        // Restart SystemUI
                        restartSystemUI()
                    }
                }
                .setNegativeButton(getString(R.string.negative)) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                .show()
            true
        }

        findPreference<PreferenceMenu>("iconifyGitHub")?.setOnPreferenceClickListener {
            openUrl(
                requireActivity(),
                GITHUB_REPO
            )
            true
        }

        findPreference<PreferenceMenu>("iconifyTelegram")?.setOnPreferenceClickListener {
            openUrl(
                requireActivity(),
                TELEGRAM_GROUP
            )
            true
        }

        findPreference<PreferenceMenu>("iconifyTranslate")?.setOnPreferenceClickListener {
            openUrl(
                requireActivity(),
                ICONIFY_CROWDIN
            )
            true
        }
    }

    private fun changeIcon(splash: String) {
        val manager = requireActivity().packageManager
        val splashActivities = appContextLocale.resources
            .getStringArray(R.array.app_icon_identifier)

        for (splashActivity in splashActivities) {
            manager.setComponentEnabledSetting(
                ComponentName(
                    requireActivity(),
                    "com.hayao0819.liconify.$splashActivity"
                ),
                if (splash == splashActivity) {
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                } else {
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                },
                PackageManager.DONT_KILL_APP
            )
        }
    }

    override fun onDestroy() {
        loadingDialog?.hide()

        super.onDestroy()
    }

    private suspend fun disableEverything() {
        WeatherConfig.clear(appContext)

        // Clear shared preferences
        RPrefs.clearAllPrefs()

        // Clear dynamic resource database
        DynamicResourceRepository(
            DynamicResourceDatabase.getInstance().dynamicResourceDao()
        ).apply {
            deleteResources(getAllResources())
        }

        saveBootId
        disableBlur(false)
        saveVersionCode()

        RPrefs.putBoolean(ON_HOME_PAGE, true)
        RPrefs.putBoolean(FIRST_INSTALL, false)

        Shell.cmd(
            "> $MODULE_DIR/system.prop; > $MODULE_DIR/post-exec.sh; for ol in $(cmd overlay list | grep -E '.x.*IconifyComponent' | sed -E 's/^.x..//'); do cmd overlay disable \$ol; done; killall com.android.systemui"
        ).submit()
    }
}
