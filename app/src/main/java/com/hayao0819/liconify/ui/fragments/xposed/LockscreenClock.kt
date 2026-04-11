package com.hayao0819.liconify.ui.fragments.xposed

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.hayao0819.liconify.Iconify.Companion.appContext
import com.hayao0819.liconify.Iconify.Companion.appContextLocale
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Const.RESET_LOCKSCREEN_CLOCK_COMMAND
import com.hayao0819.liconify.data.common.Dynamic.isAtleastA14
import com.hayao0819.liconify.data.common.Preferences.LSCLOCK_FONT_SWITCH
import com.hayao0819.liconify.data.common.Preferences.LSCLOCK_MOVE_NOTIFICATION_ICONS
import com.hayao0819.liconify.data.common.Preferences.LSCLOCK_SWITCH
import com.hayao0819.liconify.data.common.Resources.LSCLOCK_FONT_DIR
import com.hayao0819.liconify.data.config.RPrefs.getBoolean
import com.hayao0819.liconify.data.config.RPrefs.putBoolean
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat
import com.hayao0819.liconify.ui.preferences.FilePickerPreference
import com.hayao0819.liconify.utils.FileUtils.getRealPath
import com.hayao0819.liconify.utils.FileUtils.launchFilePicker
import com.hayao0819.liconify.utils.FileUtils.moveToIconifyHiddenDir
import com.topjohnwu.superuser.Shell

class LockscreenClock : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_lockscreen_clock)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_lockscreen_clock

    override val themeResource: Int
        get() = R.style.PrefsThemeNoToolbar

    override val hasMenu: Boolean
        get() = true

    private lateinit var startActivityIntent: ActivityResultLauncher<Intent?>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        startActivityIntent = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val path = getRealPath(data)

                if (path != null && moveToIconifyHiddenDir(path, LSCLOCK_FONT_DIR)) {
                    putBoolean(LSCLOCK_FONT_SWITCH, false)
                    putBoolean(LSCLOCK_FONT_SWITCH, true)

                    Toast.makeText(
                        appContext,
                        appContextLocale.resources.getString(R.string.toast_applied),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        appContext,
                        appContextLocale.resources.getString(R.string.toast_rename_file),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            LSCLOCK_SWITCH -> {
                if (getBoolean(key) && isAtleastA14) {
                    Shell.cmd(RESET_LOCKSCREEN_CLOCK_COMMAND).exec()
                }

                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }

            LSCLOCK_MOVE_NOTIFICATION_ICONS -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<FilePickerPreference>("xposed_lockscreenclockfontpicker")?.apply {
            setOnButtonClick {
                launchFilePicker(context, "font", startActivityIntent)
            }
        }
    }
}
