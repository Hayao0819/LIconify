package com.hayao0819.liconify.ui.fragments.xposed

import android.os.Build
import android.os.Bundle
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.FIXED_STATUS_ICONS_SWITCH
import com.hayao0819.liconify.data.common.Preferences.FIXED_STATUS_ICONS_TOPMARGIN
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat
import com.hayao0819.liconify.ui.preferences.SliderPreference

class Others : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_xposed_others)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_others

    override val hasMenu: Boolean
        get() = true

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            FIXED_STATUS_ICONS_SWITCH -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            findPreference<SliderPreference>(FIXED_STATUS_ICONS_TOPMARGIN)?.setMax(250f)
        }
    }
}
