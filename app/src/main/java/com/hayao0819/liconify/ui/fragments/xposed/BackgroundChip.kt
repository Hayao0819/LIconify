package com.hayao0819.liconify.ui.fragments.xposed

import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.CHIP_STATUSBAR_CLOCK_SWITCH
import com.hayao0819.liconify.data.common.Preferences.CHIP_STATUS_ICONS_SWITCH
import com.hayao0819.liconify.data.config.RPrefs.getBoolean
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat

class BackgroundChip : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_background_chip)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_background_chip

    override val hasMenu: Boolean
        get() = true

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            CHIP_STATUSBAR_CLOCK_SWITCH -> {
                if (!getBoolean(key)) {
                    MainActivity.showOrHidePendingActionButton(
                        activityBinding = (requireActivity() as MainActivity).binding,
                        requiresSystemUiRestart = true
                    )
                }
            }

            CHIP_STATUS_ICONS_SWITCH -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }
        }
    }
}
