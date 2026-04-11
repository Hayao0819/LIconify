package com.hayao0819.liconify.ui.fragments.xposed

import android.os.Bundle
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.VOLUME_PANEL_PERCENTAGE
import com.hayao0819.liconify.data.common.Preferences.VOLUME_PANEL_STYLE
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat

class VolumePanel : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_volume_panel)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_volume_panel

    override val hasMenu: Boolean
        get() = true

    override val themeResource: Int
        get() = R.style.PrefsThemeNoToolbar

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            VOLUME_PANEL_STYLE,
            VOLUME_PANEL_PERCENTAGE -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
    }
}
