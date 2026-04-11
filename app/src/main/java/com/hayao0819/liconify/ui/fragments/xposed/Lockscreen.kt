package com.hayao0819.liconify.ui.fragments.xposed

import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.HIDE_LOCKSCREEN_CARRIER
import com.hayao0819.liconify.data.common.Preferences.HIDE_LOCKSCREEN_LOCK_ICON
import com.hayao0819.liconify.data.common.Preferences.HIDE_LOCKSCREEN_STATUSBAR
import com.hayao0819.liconify.data.common.Preferences.HIDE_QS_ON_LOCKSCREEN
import com.hayao0819.liconify.data.common.Preferences.LOCKSCREEN_WALLPAPER_BLUR
import com.hayao0819.liconify.data.common.Preferences.LOCKSCREEN_WALLPAPER_BLUR_RADIUS
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat

class Lockscreen : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_lockscreen)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_lockscreen

    override val hasMenu: Boolean
        get() = true

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            HIDE_LOCKSCREEN_LOCK_ICON,
            HIDE_QS_ON_LOCKSCREEN,
            HIDE_LOCKSCREEN_CARRIER,
            HIDE_LOCKSCREEN_STATUSBAR,
            LOCKSCREEN_WALLPAPER_BLUR,
            LOCKSCREEN_WALLPAPER_BLUR_RADIUS -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }
        }
    }
}
