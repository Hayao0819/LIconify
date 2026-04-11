package com.hayao0819.liconify.ui.fragments.xposed

import android.os.Bundle
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.COLORED_NOTIFICATION_ALTERNATIVE_SWITCH
import com.hayao0819.liconify.data.common.Preferences.COLORED_NOTIFICATION_ICON_SWITCH
import com.hayao0819.liconify.data.common.Preferences.COLORED_NOTIFICATION_VIEW_SWITCH
import com.hayao0819.liconify.data.common.Preferences.CUSTOM_QS_TEXT_COLOR
import com.hayao0819.liconify.data.common.Preferences.FIX_NOTIFICATION_COLOR
import com.hayao0819.liconify.data.common.Preferences.FIX_NOTIFICATION_FOOTER_BUTTON_COLOR
import com.hayao0819.liconify.data.common.Preferences.FIX_QS_TILE_COLOR
import com.hayao0819.liconify.data.common.Preferences.HIDE_QSLABEL_SWITCH
import com.hayao0819.liconify.data.common.Preferences.HIDE_QS_SILENT_TEXT
import com.hayao0819.liconify.data.common.Preferences.HIDE_STATUS_ICONS_SWITCH
import com.hayao0819.liconify.data.common.Preferences.HORIZONTAL_QSTILE_SWITCH
import com.hayao0819.liconify.data.common.Preferences.QSPANEL_HIDE_CARRIER
import com.hayao0819.liconify.data.common.Preferences.SELECTED_QS_TEXT_COLOR
import com.hayao0819.liconify.data.common.Preferences.VERTICAL_QSTILE_SWITCH
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat
import com.hayao0819.liconify.ui.preferences.SwitchPreference

class QuickSettings : ControlledPreferenceFragmentCompat() {

    private var verticalPref: SwitchPreference? = null
    private var horizontalPref: SwitchPreference? = null

    override val title: String
        get() = getString(R.string.activity_title_quick_settings)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_quick_settings

    override val hasMenu: Boolean
        get() = true

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        verticalPref = findPreference(VERTICAL_QSTILE_SWITCH)
        horizontalPref = findPreference(HORIZONTAL_QSTILE_SWITCH)

        updateMutualExclusion()
    }

    private fun updateMutualExclusion() {
        verticalPref?.let { v ->
            horizontalPref?.let { h ->
                h.isEnabled = !v.isChecked
                v.isEnabled = !h.isChecked
            }
        }
    }

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            VERTICAL_QSTILE_SWITCH,
            HORIZONTAL_QSTILE_SWITCH -> updateMutualExclusion()
        }

        when (key) {
            VERTICAL_QSTILE_SWITCH,
            HORIZONTAL_QSTILE_SWITCH,
            CUSTOM_QS_TEXT_COLOR,
            SELECTED_QS_TEXT_COLOR,
            HIDE_QSLABEL_SWITCH,
            COLORED_NOTIFICATION_ICON_SWITCH,
            COLORED_NOTIFICATION_VIEW_SWITCH,
            COLORED_NOTIFICATION_ALTERNATIVE_SWITCH,
            HIDE_QS_SILENT_TEXT,
            QSPANEL_HIDE_CARRIER,
            HIDE_STATUS_ICONS_SWITCH,
            FIX_QS_TILE_COLOR,
            FIX_NOTIFICATION_COLOR,
            FIX_NOTIFICATION_FOOTER_BUTTON_COLOR -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }
        }
    }
}
