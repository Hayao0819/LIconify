package com.hayao0819.liconify.ui.fragments.xposed

import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.COLORED_STATUSBAR_ICON
import com.hayao0819.liconify.data.common.Preferences.DUAL_STATUSBAR
import com.hayao0819.liconify.data.common.Preferences.NOTIFICATION_ICONS_LIMIT
import com.hayao0819.liconify.data.common.Preferences.SHOW_4G_INSTEAD_OF_LTE
import com.hayao0819.liconify.data.common.Preferences.SHOW_CLOCK_ON_RIGHT_SIDE
import com.hayao0819.liconify.data.common.Preferences.STATUSBAR_SWAP_CELLULAR_NETWORK_TYPE
import com.hayao0819.liconify.data.common.Preferences.STATUSBAR_SWAP_WIFI_CELLULAR
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat

class Statusbar : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_statusbar)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_statusbar

    override val hasMenu: Boolean
        get() = true

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            DUAL_STATUSBAR,
            COLORED_STATUSBAR_ICON,
            STATUSBAR_SWAP_WIFI_CELLULAR,
            STATUSBAR_SWAP_CELLULAR_NETWORK_TYPE,
            SHOW_CLOCK_ON_RIGHT_SIDE,
            SHOW_4G_INSTEAD_OF_LTE,
            NOTIFICATION_ICONS_LIMIT -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }
        }
    }
}
