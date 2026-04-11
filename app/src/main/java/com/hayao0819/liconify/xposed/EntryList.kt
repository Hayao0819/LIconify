package com.hayao0819.liconify.xposed

import android.os.Build
import com.hayao0819.liconify.data.common.Const.LAUNCHER3_PACKAGE
import com.hayao0819.liconify.data.common.Const.PIXEL_LAUNCHER_PACKAGE
import com.hayao0819.liconify.data.common.Const.SETTINGS_PACKAGE
import com.hayao0819.liconify.data.common.Const.SYSTEMUI_PACKAGE
import com.hayao0819.liconify.xposed.modules.BackgroundChip
import com.hayao0819.liconify.xposed.modules.BatteryStyleManager
import com.hayao0819.liconify.xposed.modules.extras.callbacks.ControllersProvider
import com.hayao0819.liconify.xposed.modules.extras.callbacks.ThemeChange
import com.hayao0819.liconify.xposed.modules.extras.utils.MyConstraintSet
import com.hayao0819.liconify.xposed.modules.extras.utils.SettingsLibUtils
import com.hayao0819.liconify.xposed.modules.launcher.GestureMod
import com.hayao0819.liconify.xposed.modules.launcher.HotseatMod
import com.hayao0819.liconify.xposed.modules.launcher.IconLabels
import com.hayao0819.liconify.xposed.modules.launcher.IconUpdater
import com.hayao0819.liconify.xposed.modules.launcher.OpacityModifier
import com.hayao0819.liconify.xposed.modules.launcher.ThemedIcons
import com.hayao0819.liconify.xposed.modules.lockscreen.AlbumArt
import com.hayao0819.liconify.xposed.modules.lockscreen.Lockscreen
import com.hayao0819.liconify.xposed.modules.lockscreen.clock.LockscreenClock
import com.hayao0819.liconify.xposed.modules.lockscreen.clock.LockscreenClockA15
import com.hayao0819.liconify.xposed.modules.lockscreen.depthwallpaper.DepthWallpaper
import com.hayao0819.liconify.xposed.modules.lockscreen.depthwallpaper.DepthWallpaperA14
import com.hayao0819.liconify.xposed.modules.lockscreen.depthwallpaper.DepthWallpaperA15
import com.hayao0819.liconify.xposed.modules.lockscreen.weather.LockscreenWeather
import com.hayao0819.liconify.xposed.modules.lockscreen.weather.LockscreenWeatherA15
import com.hayao0819.liconify.xposed.modules.lockscreen.widgets.LockscreenWidgets
import com.hayao0819.liconify.xposed.modules.lockscreen.widgets.LockscreenWidgetsA15
import com.hayao0819.liconify.xposed.modules.misc.Miscellaneous
import com.hayao0819.liconify.xposed.modules.quicksettings.AppIconInNotification
import com.hayao0819.liconify.xposed.modules.quicksettings.ColorizeNotificationView
import com.hayao0819.liconify.xposed.modules.quicksettings.HeaderImage
import com.hayao0819.liconify.xposed.modules.quicksettings.OpQsHeader
import com.hayao0819.liconify.xposed.modules.quicksettings.QSTransparency
import com.hayao0819.liconify.xposed.modules.quicksettings.QuickSettings
import com.hayao0819.liconify.xposed.modules.quicksettings.headerclock.HeaderClock
import com.hayao0819.liconify.xposed.modules.quicksettings.headerclock.HeaderClockA14
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSBlackThemeA13
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSBlackThemeA14
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSBlackThemeA15
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSFluidThemeA13
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSFluidThemeA14
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSFluidThemeA15
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSLightThemeA12
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSLightThemeA13
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSLightThemeA14
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.QSLightThemeA15
import com.hayao0819.liconify.xposed.modules.settings.GoogleIcon
import com.hayao0819.liconify.xposed.modules.settings.ZenPriorityModeIcon
import com.hayao0819.liconify.xposed.modules.statusbar.AppIconsInStatusbar
import com.hayao0819.liconify.xposed.modules.statusbar.DualStatusbar
import com.hayao0819.liconify.xposed.modules.statusbar.StatusbarMisc
import com.hayao0819.liconify.xposed.modules.statusbar.SwapSignalNetworkType
import com.hayao0819.liconify.xposed.modules.statusbar.SwapWiFiCellular
import com.hayao0819.liconify.xposed.modules.volume.VolumePanel
import com.hayao0819.liconify.xposed.modules.volume.VolumePanelStyle
import com.hayao0819.liconify.xposed.utils.HookCheck

object EntryList {

    private val topPriorityCommonModPacks: List<Class<out ModPack>> = listOf(
        SettingsLibUtils::class.java,
        HookCheck::class.java
    )

    private val systemUICommonModPacks: List<Class<out ModPack>> = listOf(
        MyConstraintSet::class.java,
        ControllersProvider::class.java,
        ThemeChange::class.java,
        BackgroundChip::class.java,
        HeaderImage::class.java,
        Lockscreen::class.java,
        LockscreenClock::class.java,
        LockscreenWidgets::class.java,
        LockscreenWeather::class.java,
        AlbumArt::class.java,
        Miscellaneous::class.java,
        QSTransparency::class.java,
        QuickSettings::class.java,
        AppIconsInStatusbar::class.java,
        SwapWiFiCellular::class.java,
        SwapSignalNetworkType::class.java,
        DualStatusbar::class.java,
        StatusbarMisc::class.java,
        BatteryStyleManager::class.java,
        VolumePanel::class.java,
        VolumePanelStyle::class.java,
        ColorizeNotificationView::class.java,
        AppIconInNotification::class.java
    )

    private val systemUiAndroid12ModPacks: List<Class<out ModPack>> = listOf(
        DepthWallpaper::class.java,
        QSFluidThemeA13::class.java,
        QSBlackThemeA13::class.java,
        QSLightThemeA12::class.java,
        HeaderClock::class.java
    )

    private val systemUiAndroid13ModPacks: List<Class<out ModPack>> = listOf(
        DepthWallpaper::class.java,
        QSFluidThemeA13::class.java,
        QSBlackThemeA13::class.java,
        QSLightThemeA13::class.java,
        HeaderClock::class.java
    )

    private val systemUiAndroid14ModPacks: List<Class<out ModPack>> = listOf(
        DepthWallpaperA14::class.java,
        QSFluidThemeA14::class.java,
        QSBlackThemeA14::class.java,
        QSLightThemeA14::class.java,
        HeaderClockA14::class.java,
        OpQsHeader::class.java
    )

    private val systemUiAndroid15ModPacks: List<Class<out ModPack>> = listOf(
        DepthWallpaperA15::class.java,
        QSFluidThemeA15::class.java,
        QSBlackThemeA15::class.java,
        QSLightThemeA15::class.java,
        HeaderClockA14::class.java,
        LockscreenClockA15::class.java,
        LockscreenWeatherA15::class.java,
        LockscreenWidgetsA15::class.java,
        OpQsHeader::class.java
    )

    private val pixelLauncherModPacks: List<Class<out ModPack>> = listOf(
        IconUpdater::class.java,
        ThemedIcons::class.java,
        OpacityModifier::class.java,
        GestureMod::class.java,
        IconLabels::class.java,
        HotseatMod::class.java
    )

    private val launcher3ModPacks: List<Class<out ModPack>> = listOf(
        ThemedIcons::class.java,
        OpacityModifier::class.java,
        GestureMod::class.java,
        IconLabels::class.java,
        HotseatMod::class.java
    )

    private val settingsCommonModPacks: List<Class<out ModPack>> = listOf(
        GoogleIcon::class.java
    )

    private val settingsAndroid15ModPacks: List<Class<out ModPack>> = listOf(
        ZenPriorityModeIcon::class.java
    )

    fun getEntries(packageName: String): ArrayList<Class<out ModPack>> {
        val modPacks = ArrayList<Class<out ModPack>>()

        modPacks.addAll(topPriorityCommonModPacks)

        when (packageName) {
            SYSTEMUI_PACKAGE -> {
                if (!HookEntry.isChildProcess) {
                    modPacks.addAll(systemUICommonModPacks)

                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM -> { // android 15+
                            modPacks.addAll(systemUiAndroid15ModPacks)
                        }

                        Build.VERSION.SDK_INT == Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> { // Android 14
                            modPacks.addAll(systemUiAndroid14ModPacks)
                        }

                        Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU -> { // Android 13
                            modPacks.addAll(systemUiAndroid13ModPacks)
                        }

                        else -> { // Android 12.0 and 12.1
                            modPacks.addAll(systemUiAndroid12ModPacks)
                        }
                    }
                }
            }

            PIXEL_LAUNCHER_PACKAGE -> {
                modPacks.addAll(pixelLauncherModPacks)
            }

            LAUNCHER3_PACKAGE -> {
                modPacks.addAll(launcher3ModPacks)
            }

            SETTINGS_PACKAGE -> {
                modPacks.addAll(settingsCommonModPacks)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    modPacks.addAll(settingsAndroid15ModPacks)
                }
            }
        }

        return modPacks
    }
}
