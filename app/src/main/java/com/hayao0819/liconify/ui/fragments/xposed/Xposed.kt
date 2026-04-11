package com.hayao0819.liconify.ui.fragments.xposed

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences
import com.hayao0819.liconify.data.common.Preferences.FIRST_INSTALL
import com.hayao0819.liconify.data.common.Preferences.LSCLOCK_MOVE_NOTIFICATION_ICONS
import com.hayao0819.liconify.data.common.Preferences.UPDATE_DETECTED
import com.hayao0819.liconify.data.common.Preferences.XPOSED_HOOK_CHECK
import com.hayao0819.liconify.data.config.RPrefs.getBoolean
import com.hayao0819.liconify.data.config.RPrefs.putBoolean
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat
import com.hayao0819.liconify.ui.preferences.HookCheckPreference
import com.hayao0819.liconify.utils.SystemUtils.saveVersionCode

class Xposed : ControlledPreferenceFragmentCompat() {

    private var hookCheckPreference: HookCheckPreference? = null

    override val title: String
        get() = getString(R.string.navbar_xposed)

    override val backButtonEnabled: Boolean
        get() = !Preferences.isXposedOnlyMode

    override val layoutResource: Int
        get() = R.xml.xposed

    override val hasMenu: Boolean
        get() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        putBoolean(FIRST_INSTALL, false)
        putBoolean(UPDATE_DETECTED, false)
        saveVersionCode()

        // Disable this by default for older android versions
        putBoolean(
            LSCLOCK_MOVE_NOTIFICATION_ICONS,
            getBoolean(
                LSCLOCK_MOVE_NOTIFICATION_ICONS,
                Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU
            )
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<HookCheckPreference>(XPOSED_HOOK_CHECK)?.apply {
            hookCheckPreference = this

            setOnPreferenceClickListener {
                try {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.setComponent(
                        ComponentName(
                            "org.lsposed.manager",
                            "org.lsposed.manager.ui.activities.MainActivity"
                        )
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } catch (ignored: Exception) {
                }
                true
            }

            initializeHookCheck()
        }
    }

    override fun onResume() {
        super.onResume()

        HookCheckPreference.isHooked = false
        hookCheckPreference?.initializeHookCheck()
    }
}
