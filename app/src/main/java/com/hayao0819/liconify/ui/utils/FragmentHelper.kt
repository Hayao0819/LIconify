package com.hayao0819.liconify.ui.utils

import androidx.fragment.app.Fragment
import com.hayao0819.liconify.ui.fragments.home.BrightnessBar
import com.hayao0819.liconify.ui.fragments.home.BrightnessBarPixel
import com.hayao0819.liconify.ui.fragments.home.CellularIcons
import com.hayao0819.liconify.ui.fragments.home.Home
import com.hayao0819.liconify.ui.fragments.home.IconPack
import com.hayao0819.liconify.ui.fragments.home.IconShape
import com.hayao0819.liconify.ui.fragments.home.MediaIcons
import com.hayao0819.liconify.ui.fragments.home.Notification
import com.hayao0819.liconify.ui.fragments.home.NotificationPixel
import com.hayao0819.liconify.ui.fragments.home.ProgressBar
import com.hayao0819.liconify.ui.fragments.home.QsPanelTile
import com.hayao0819.liconify.ui.fragments.home.QsPanelTilePixel
import com.hayao0819.liconify.ui.fragments.home.SettingsIcons
import com.hayao0819.liconify.ui.fragments.home.Switch
import com.hayao0819.liconify.ui.fragments.home.ToastFrame
import com.hayao0819.liconify.ui.fragments.home.WiFiIcons
import com.hayao0819.liconify.ui.fragments.settings.AppUpdates
import com.hayao0819.liconify.ui.fragments.settings.Changelog
import com.hayao0819.liconify.ui.fragments.settings.Credits
import com.hayao0819.liconify.ui.fragments.settings.Experimental
import com.hayao0819.liconify.ui.fragments.settings.Settings
import com.hayao0819.liconify.ui.fragments.tweaks.BasicColors
import com.hayao0819.liconify.ui.fragments.tweaks.ColorEngine
import com.hayao0819.liconify.ui.fragments.tweaks.ColoredBattery
import com.hayao0819.liconify.ui.fragments.tweaks.MediaPlayer
import com.hayao0819.liconify.ui.fragments.tweaks.Miscellaneous
import com.hayao0819.liconify.ui.fragments.tweaks.NavigationBar
import com.hayao0819.liconify.ui.fragments.tweaks.QsIconLabel
import com.hayao0819.liconify.ui.fragments.tweaks.QsRowColumn
import com.hayao0819.liconify.ui.fragments.tweaks.QsTileSize
import com.hayao0819.liconify.ui.fragments.tweaks.Statusbar
import com.hayao0819.liconify.ui.fragments.tweaks.Tweaks
import com.hayao0819.liconify.ui.fragments.tweaks.UiRoundness
import com.hayao0819.liconify.ui.fragments.tweaks.VolumePanel
import com.hayao0819.liconify.ui.fragments.xposed.AlbumArt
import com.hayao0819.liconify.ui.fragments.xposed.BackgroundChip
import com.hayao0819.liconify.ui.fragments.xposed.BatteryStyle
import com.hayao0819.liconify.ui.fragments.xposed.ClockChip
import com.hayao0819.liconify.ui.fragments.xposed.DepthWallpaper
import com.hayao0819.liconify.ui.fragments.xposed.DualStatusbar
import com.hayao0819.liconify.ui.fragments.xposed.HeaderClock
import com.hayao0819.liconify.ui.fragments.xposed.HeaderImage
import com.hayao0819.liconify.ui.fragments.xposed.Launcher
import com.hayao0819.liconify.ui.fragments.xposed.Lockscreen
import com.hayao0819.liconify.ui.fragments.xposed.LockscreenClock
import com.hayao0819.liconify.ui.fragments.xposed.LockscreenWeather
import com.hayao0819.liconify.ui.fragments.xposed.LockscreenWidget
import com.hayao0819.liconify.ui.fragments.xposed.OpQsHeader
import com.hayao0819.liconify.ui.fragments.xposed.Others
import com.hayao0819.liconify.ui.fragments.xposed.QsMargins
import com.hayao0819.liconify.ui.fragments.xposed.QuickSettings
import com.hayao0819.liconify.ui.fragments.xposed.StatusIconsChip
import com.hayao0819.liconify.ui.fragments.xposed.Themes
import com.hayao0819.liconify.ui.fragments.xposed.TransparencyBlur
import com.hayao0819.liconify.ui.fragments.xposed.Xposed

enum class FragmentGroup {
    HOME, TWEAKS, XPOSED, SETTINGS
}

fun isInGroup(fragment: Fragment, group: FragmentGroup): Boolean {
    return when (group) {
        FragmentGroup.HOME -> {
            fragment is Home ||
                    fragment is IconPack ||
                    fragment is MediaIcons ||
                    fragment is SettingsIcons ||
                    fragment is CellularIcons ||
                    fragment is WiFiIcons ||
                    fragment is BrightnessBar ||
                    fragment is BrightnessBarPixel ||
                    fragment is QsPanelTile ||
                    fragment is QsPanelTilePixel ||
                    fragment is Notification ||
                    fragment is NotificationPixel ||
                    fragment is ProgressBar ||
                    fragment is Switch ||
                    fragment is ToastFrame ||
                    fragment is IconShape
        }

        FragmentGroup.TWEAKS -> {
            fragment is Tweaks ||
                    fragment is ColorEngine ||
                    fragment is BasicColors ||
                    fragment is UiRoundness ||
                    fragment is ColoredBattery ||
                    fragment is QsRowColumn ||
                    fragment is QsIconLabel ||
                    fragment is QsTileSize ||
                    fragment is Statusbar ||
                    fragment is NavigationBar ||
                    fragment is MediaPlayer ||
                    fragment is VolumePanel ||
                    fragment is Miscellaneous
        }

        FragmentGroup.XPOSED -> {
            fragment is Xposed ||
                    fragment is BackgroundChip ||
                    fragment is ClockChip ||
                    fragment is StatusIconsChip ||
                    fragment is TransparencyBlur ||
                    fragment is QuickSettings ||
                    fragment is Lockscreen ||
                    fragment is Themes ||
                    fragment is OpQsHeader ||
                    fragment is BatteryStyle ||
                    fragment is QsMargins ||
                    fragment is com.hayao0819.liconify.ui.fragments.xposed.Statusbar ||
                    fragment is com.hayao0819.liconify.ui.fragments.xposed.VolumePanel ||
                    fragment is DualStatusbar ||
                    fragment is HeaderImage ||
                    fragment is HeaderClock ||
                    fragment is LockscreenClock ||
                    fragment is LockscreenWeather ||
                    fragment is LockscreenWidget ||
                    fragment is DepthWallpaper ||
                    fragment is AlbumArt ||
                    fragment is Launcher ||
                    fragment is Others
        }

        FragmentGroup.SETTINGS -> {
            fragment is Settings ||
                    fragment is AppUpdates ||
                    fragment is Changelog ||
                    fragment is Credits ||
                    fragment is Experimental
        }
    }
}