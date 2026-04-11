package com.hayao0819.liconify.ui.fragments.xposed

import android.os.Bundle
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.CUSTOM_BATTERY_CHARGING_ICON_STYLE
import com.hayao0819.liconify.data.common.Preferences.CUSTOM_BATTERY_HEIGHT
import com.hayao0819.liconify.data.common.Preferences.CUSTOM_BATTERY_STYLE
import com.hayao0819.liconify.data.common.Preferences.CUSTOM_BATTERY_WIDTH
import com.hayao0819.liconify.data.config.RPrefs
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.adapters.ListPreferenceAdapter
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat
import com.hayao0819.liconify.ui.preferences.BottomSheetListPreference
import com.hayao0819.liconify.ui.utils.ViewHelper.getBatteryDrawables
import com.hayao0819.liconify.ui.utils.ViewHelper.getChargingIcons

class BatteryStyle : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_battery_style)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_battery_style

    override val hasMenu: Boolean
        get() = true

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            CUSTOM_BATTERY_STYLE -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }

            CUSTOM_BATTERY_WIDTH,
            CUSTOM_BATTERY_HEIGHT -> {
                if (RPrefs.getString(CUSTOM_BATTERY_STYLE, "0")!!.toInt() < 3) {
                    MainActivity.showOrHidePendingActionButton(
                        activityBinding = (requireActivity() as MainActivity).binding,
                        requiresSystemUiRestart = true
                    )
                }
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<BottomSheetListPreference>(CUSTOM_BATTERY_STYLE)?.apply {
            createDefaultAdapter(getBatteryDrawables(requireContext()))
            setAdapterType(ListPreferenceAdapter.TYPE_BATTERY_ICONS)
        }

        findPreference<BottomSheetListPreference>(CUSTOM_BATTERY_CHARGING_ICON_STYLE)?.apply {
            createDefaultAdapter(getChargingIcons(requireContext()))
            setAdapterType(ListPreferenceAdapter.TYPE_BATTERY_ICONS)
        }
    }
}
