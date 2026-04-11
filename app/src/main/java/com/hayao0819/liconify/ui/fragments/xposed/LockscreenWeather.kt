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
import com.hayao0819.liconify.data.common.Preferences.WEATHER_SWITCH
import com.hayao0819.liconify.data.common.Preferences.WEATHER_TRIGGER_UPDATE
import com.hayao0819.liconify.data.common.Resources.LOCKSCREEN_WEATHER_FONT_DIR
import com.hayao0819.liconify.data.config.RPrefs.getBoolean
import com.hayao0819.liconify.data.config.RPrefs.putBoolean
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.base.WeatherPreferenceFragment
import com.hayao0819.liconify.ui.preferences.FilePickerPreference
import com.hayao0819.liconify.utils.FileUtils.getRealPath
import com.hayao0819.liconify.utils.FileUtils.launchFilePicker
import com.hayao0819.liconify.utils.FileUtils.moveToIconifyHiddenDir

class LockscreenWeather : WeatherPreferenceFragment() {

    override val title: String
        get() = getString(R.string.activity_title_lockscreen_weather)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_lockscreen_weather

    override val hasMenu: Boolean
        get() = true

    private lateinit var startActivityIntent: ActivityResultLauncher<Intent?>

    override fun getMainSwitchKey(): String {
        return WEATHER_SWITCH
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        startActivityIntent = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val path = getRealPath(data)

                if (path != null && moveToIconifyHiddenDir(path, LOCKSCREEN_WEATHER_FONT_DIR)) {
                    putBoolean(WEATHER_TRIGGER_UPDATE, !getBoolean(WEATHER_TRIGGER_UPDATE))

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
            WEATHER_SWITCH -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<FilePickerPreference>("xposed_lockscreenweatherfontpicker")?.apply {
            setOnButtonClick {
                launchFilePicker(context, "font", startActivityIntent)
            }
        }
    }
}