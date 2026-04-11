package com.hayao0819.liconify.ui.fragments.xposed

import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Const.LAUNCHER3_PACKAGE
import com.hayao0819.liconify.data.common.Const.PIXEL_LAUNCHER_PACKAGE
import com.hayao0819.liconify.data.common.Preferences.DESKTOP_DOCK_SPACING
import com.hayao0819.liconify.data.common.Preferences.DESKTOP_SEARCH_BAR
import com.hayao0819.liconify.data.config.RPrefs
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat
import com.hayao0819.liconify.xposed.utils.BootLoopProtector.LOAD_TIME_KEY_KEY
import com.hayao0819.liconify.xposed.utils.BootLoopProtector.PACKAGE_STRIKE_KEY_KEY
import java.util.Calendar

class Launcher : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_xposed_launcher)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_launcher

    override val hasMenu: Boolean
        get() = true

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            DESKTOP_SEARCH_BAR,
            DESKTOP_DOCK_SPACING -> {
                resetBootloopProtectorForPackage(PIXEL_LAUNCHER_PACKAGE)
                resetBootloopProtectorForPackage(LAUNCHER3_PACKAGE)
            }
        }
    }

    private fun resetBootloopProtectorForPackage(packageName: String) {
        val loadTimeKey = String.format("%s%s", LOAD_TIME_KEY_KEY, packageName)
        val strikeKey = String.format("%s%s", PACKAGE_STRIKE_KEY_KEY, packageName)
        val currentTime = Calendar.getInstance().time.time

        RPrefs.putLong(loadTimeKey, currentTime)
        RPrefs.putInt(strikeKey, 0)
    }
}
