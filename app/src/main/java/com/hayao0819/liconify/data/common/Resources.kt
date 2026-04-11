package com.hayao0819.liconify.data.common

import android.os.Build
import android.os.Environment
import com.hayao0819.liconify.BuildConfig
import com.hayao0819.liconify.Iconify.Companion.appContext
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.FIRST_INSTALL
import com.hayao0819.liconify.data.common.Preferences.UPDATE_DETECTED
import com.hayao0819.liconify.data.config.RPrefs.getBoolean
import com.hayao0819.liconify.data.models.SearchPreferenceItem
import com.hayao0819.liconify.ui.fragments.home.Home
import com.hayao0819.liconify.ui.fragments.settings.Settings
import com.hayao0819.liconify.ui.fragments.tweaks.Tweaks
import com.hayao0819.liconify.ui.fragments.xposed.AlbumArt
import com.hayao0819.liconify.ui.fragments.xposed.BackgroundChip
import com.hayao0819.liconify.ui.fragments.xposed.BatteryStyle
import com.hayao0819.liconify.ui.fragments.xposed.DepthWallpaper
import com.hayao0819.liconify.ui.fragments.xposed.DualStatusbar
import com.hayao0819.liconify.ui.fragments.xposed.HeaderClock
import com.hayao0819.liconify.ui.fragments.xposed.Launcher
import com.hayao0819.liconify.ui.fragments.xposed.Lockscreen
import com.hayao0819.liconify.ui.fragments.xposed.LockscreenClockParent
import com.hayao0819.liconify.ui.fragments.xposed.LockscreenWeather
import com.hayao0819.liconify.ui.fragments.xposed.LockscreenWidget
import com.hayao0819.liconify.ui.fragments.xposed.OpQsHeader
import com.hayao0819.liconify.ui.fragments.xposed.Others
import com.hayao0819.liconify.ui.fragments.xposed.QuickSettings
import com.hayao0819.liconify.ui.fragments.xposed.Statusbar
import com.hayao0819.liconify.ui.fragments.xposed.Themes
import com.hayao0819.liconify.ui.fragments.xposed.TransparencyBlur
import com.hayao0819.liconify.ui.fragments.xposed.VolumePanelParent
import com.hayao0819.liconify.ui.fragments.xposed.Xposed
import com.hayao0819.liconify.ui.preferences.preferencesearch.SearchConfiguration
import com.hayao0819.liconify.utils.RootUtils.folderExists

object Resources {

    // Preference files
    const val SHARED_XPREFERENCES = BuildConfig.APPLICATION_ID + "_xpreference"

    // Storage location
    val DOCUMENTS_DIR: String =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath

    val DOWNLOADS_DIR: String =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

    val LOG_DIR = "$DOCUMENTS_DIR/Iconify"
    const val MODULE_DIR = "/data/adb/modules/Iconify"
    const val SYSTEM_OVERLAY_DIR = "/system/product/overlay"

    val DATA_DIR: String = appContext.filesDir.absolutePath
    const val OVERLAY_DIR = "$MODULE_DIR/system/product/overlay"

    val BIN_DIR = appContext.dataDir.toString() + "/bin"

    val BACKUP_DIR = Environment.getExternalStorageDirectory().absolutePath + "/.iconify_backup"

    val TEMP_DIR = Environment.getExternalStorageDirectory().absolutePath + "/.iconify"

    val TEMP_MODULE_DIR = "$TEMP_DIR/Iconify"

    val TEMP_MODULE_OVERLAY_DIR = "$TEMP_MODULE_DIR/system/product/overlay"

    val TEMP_OVERLAY_DIR = "$TEMP_DIR/overlays"

    val TEMP_CACHE_DIR = "$TEMP_OVERLAY_DIR/cache"

    val UNSIGNED_UNALIGNED_DIR = "$TEMP_OVERLAY_DIR/unsigned_unaligned"

    val UNSIGNED_DIR = "$TEMP_OVERLAY_DIR/unsigned"

    val SIGNED_DIR = "$TEMP_OVERLAY_DIR/signed"

    // File resources
    const val FRAMEWORK_DIR = "/system/framework/framework-res.apk"

    // Xposed resource dir
    val XPOSED_RESOURCE_TEMP_DIR = "${Environment.getExternalStorageDirectory()}/.iconify_files"

    val LSCLOCK_FONT_DIR = "$XPOSED_RESOURCE_TEMP_DIR/lsclock_font.ttf"

    val HEADER_CLOCK_FONT_DIR = "$XPOSED_RESOURCE_TEMP_DIR/headerclock_font.ttf"

    val HEADER_IMAGE_DIR = "$XPOSED_RESOURCE_TEMP_DIR/header_image.png"

    val DEPTH_WALL_FG_DIR = "$XPOSED_RESOURCE_TEMP_DIR/depth_wallpaper_fg.png"

    val DEPTH_WALL_BG_DIR = "$XPOSED_RESOURCE_TEMP_DIR/depth_wallpaper_bg.png"

    val LOCKSCREEN_WEATHER_FONT_DIR = "$XPOSED_RESOURCE_TEMP_DIR/lockscreen_weather_font.ttf"

    // Resource names
    const val HEADER_CLOCK_LAYOUT = "preview_header_clock_"
    const val LOCKSCREEN_CLOCK_LAYOUT = "preview_lockscreen_clock_"

    // Database
    const val DYNAMIC_RESOURCE_DATABASE_NAME = "dynamic_resource_database"
    const val DYNAMIC_RESOURCE_TABLE = "dynamic_resource_table"

    fun shouldShowRebootDialog() = (!getBoolean(FIRST_INSTALL) && getBoolean(UPDATE_DETECTED)) ||
            folderExists("/data/adb/modules_update/Iconify")

    val searchConfiguration = SearchConfiguration()

    val searchableFragments = arrayOf(
        SearchPreferenceItem(
            R.xml.home,
            R.string.navbar_home,
            Home(),
            !Preferences.isXposedOnlyMode
        ),
        SearchPreferenceItem(
            R.xml.tweaks,
            R.string.navbar_tweaks,
            Tweaks(),
            !Preferences.isXposedOnlyMode
        ),
        SearchPreferenceItem(
            R.xml.xposed,
            R.string.navbar_xposed,
            Xposed()
        ),
        SearchPreferenceItem(
            R.xml.settings,
            R.string.navbar_settings,
            Settings()
        ),
        SearchPreferenceItem(
            R.xml.xposed_transparency_blur,
            R.string.activity_title_transparency_blur,
            TransparencyBlur()
        ),
        SearchPreferenceItem(
            R.xml.xposed_background_chip,
            R.string.activity_title_background_chip,
            BackgroundChip()
        ),
        SearchPreferenceItem(
            R.xml.xposed_quick_settings,
            R.string.activity_title_quick_settings,
            QuickSettings()
        ),
        SearchPreferenceItem(
            R.xml.xposed_lockscreen,
            R.string.activity_title_lockscreen,
            Lockscreen()
        ),
        SearchPreferenceItem(
            R.xml.xposed_themes,
            R.string.activity_title_themes,
            Themes()
        ),
        SearchPreferenceItem(
            R.xml.xposed_battery_style,
            R.string.activity_title_battery_style,
            BatteryStyle()
        ),
        SearchPreferenceItem(
            R.xml.xposed_statusbar,
            R.string.activity_title_statusbar,
            Statusbar()
        ),
        SearchPreferenceItem(
            R.xml.xposed_dual_statusbar,
            R.string.activity_title_dual_statusbar,
            DualStatusbar()
        ),
        SearchPreferenceItem(
            R.xml.xposed_volume_panel,
            R.string.activity_title_volume_panel,
            VolumePanelParent()
        ),
        SearchPreferenceItem(
            R.xml.xposed_op_qs_header,
            R.string.activity_title_op_qs_header,
            OpQsHeader(),
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
        ),
        SearchPreferenceItem(
            R.xml.xposed_header_clock,
            R.string.activity_title_header_clock,
            HeaderClock()
        ),
        SearchPreferenceItem(
            R.xml.xposed_lockscreen_clock,
            R.string.activity_title_lockscreen_clock,
            LockscreenClockParent()
        ),
        SearchPreferenceItem(
            R.xml.xposed_lockscreen_weather,
            R.string.activity_title_lockscreen_weather,
            LockscreenWeather()
        ),
        SearchPreferenceItem(
            R.xml.xposed_lockscreen_widget,
            R.string.activity_title_lockscreen_widget,
            LockscreenWidget()
        ),
        SearchPreferenceItem(
            R.xml.xposed_depth_wallpaper,
            R.string.activity_title_depth_wallpaper,
            DepthWallpaper()
        ),
        SearchPreferenceItem(
            R.xml.xposed_lockscreen_album_art,
            R.string.activity_title_lockscreen_album_art,
            AlbumArt()
        ),
        SearchPreferenceItem(
            R.xml.xposed_launcher,
            R.string.activity_title_xposed_launcher,
            Launcher()
        ),
        SearchPreferenceItem(
            R.xml.xposed_others,
            R.string.activity_title_xposed_others,
            Others(),
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        )
    ).filter { it.shouldAdd }
        .distinctBy { it.fragment::class.java }
        .toTypedArray()
}
