package com.hayao0819.liconify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hayao0819.liconify.data.common.Preferences.XPOSED_ONLY_MODE
import com.hayao0819.liconify.data.config.RPrefs
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.activities.OnboardingActivity
import com.hayao0819.liconify.utils.ModuleUtils
import com.hayao0819.liconify.utils.RootUtils
import com.hayao0819.liconify.utils.SystemUtils
import com.hayao0819.liconify.utils.overlay.OverlayUtils
import com.google.android.material.color.DynamicColors
import com.topjohnwu.superuser.Shell

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var keepShowing = true
    private val runner = Runnable {
        Shell.getShell { _: Shell? ->
            val isRooted = RootUtils.deviceProperlyRooted()
            val isModuleInstalled = ModuleUtils.moduleExists()
            val isOverlayInstalled = OverlayUtils.overlayExists()
            var isXposedOnlyMode = RPrefs.getBoolean(XPOSED_ONLY_MODE, false)
            val isVersionCodeCorrect = BuildConfig.VERSION_CODE == SystemUtils.savedVersionCode

            if (isRooted) {
                if (isOverlayInstalled) {
                    RPrefs.putBoolean(XPOSED_ONLY_MODE, false)
                } else if (isModuleInstalled) {
                    RPrefs.putBoolean(XPOSED_ONLY_MODE, true)
                    isXposedOnlyMode = true
                }
            }

            val isModuleProperlyInstalled = isModuleInstalled &&
                    (isOverlayInstalled || isXposedOnlyMode)

            val intent: Intent =
                if (SKIP_TO_HOMEPAGE_FOR_TESTING ||
                    (isRooted &&
                            isModuleProperlyInstalled &&
                            isVersionCodeCorrect)
                ) {
                    keepShowing = false
                    Intent(
                        this@SplashActivity,
                        if (FORCE_OVERLAY_INSTALLATION) OnboardingActivity::class.java
                        else MainActivity::class.java
                    )
                } else {
                    keepShowing = false
                    Intent(this@SplashActivity, OnboardingActivity::class.java)
                }

            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepShowing }
        DynamicColors.applyToActivitiesIfAvailable(application)
        Thread(runner).start()
    }

    companion object {
        // For testing purposes
        private const val SKIP_INSTALLATION = false
        const val FORCE_OVERLAY_INSTALLATION = false
        val SKIP_TO_HOMEPAGE_FOR_TESTING = SKIP_INSTALLATION &&
                !FORCE_OVERLAY_INSTALLATION &&
                BuildConfig.DEBUG

        init {
            Shell.enableVerboseLogging = BuildConfig.DEBUG
            @Suppress("DEPRECATION")
            if (Shell.getCachedShell() == null) {
                Shell.setDefaultBuilder(
                    Shell.Builder.create()
                        .setFlags(Shell.FLAG_MOUNT_MASTER)
                        .setFlags(Shell.FLAG_REDIRECT_STDERR)
                        .setTimeout(20)
                )
            }
        }
    }
}
