package com.hayao0819.liconify.utils.overlay.manager

import com.hayao0819.liconify.data.common.Dynamic.TOTAL_NOTIFICATIONS
import com.hayao0819.liconify.data.common.Dynamic.isAndroid14
import com.hayao0819.liconify.data.common.Dynamic.isAtleastA14
import com.hayao0819.liconify.data.common.Preferences.FIX_NOTIFICATION_COLOR
import com.hayao0819.liconify.data.common.Preferences.FIX_NOTIFICATION_FOOTER_BUTTON_COLOR
import com.hayao0819.liconify.data.config.RPrefs
import com.hayao0819.liconify.utils.SystemUtils
import com.hayao0819.liconify.utils.SystemUtils.isSecurityPatchBeforeJune2024
import com.hayao0819.liconify.utils.overlay.OverlayUtils.disableOverlay
import com.hayao0819.liconify.utils.overlay.OverlayUtils.enableOverlayExclusiveInCategory
import com.hayao0819.liconify.utils.overlay.OverlayUtils.enableOverlays
import com.hayao0819.liconify.utils.overlay.OverlayUtils.isOverlayEnabled

object NotificationManager {

    fun enableOverlay(n: Int) {
        disableOthers(n)
        enableOverlayExclusiveInCategory("IconifyComponentNFN$n.overlay")

        if (!isOverlayEnabled("IconifyComponentCR1.overlay") || !isOverlayEnabled("IconifyComponentCR2.overlay")) {
            enableOverlays("IconifyComponentCR1.overlay", "IconifyComponentCR2.overlay")
        }

        if (isAtleastA14) {
            var requireReload = false

            if (!RPrefs.getBoolean(FIX_NOTIFICATION_COLOR, false) &&
                isAndroid14 && isSecurityPatchBeforeJune2024()
            ) {
                RPrefs.putBoolean(FIX_NOTIFICATION_COLOR, true)
                requireReload = true
            }

            if (!RPrefs.getBoolean(FIX_NOTIFICATION_FOOTER_BUTTON_COLOR, false)) {
                RPrefs.putBoolean(FIX_NOTIFICATION_FOOTER_BUTTON_COLOR, true)
                requireReload = true
            }

            if (requireReload) {
                SystemUtils.restartSystemUI()
            }
        }
    }

    fun disableOverlay(n: Int) {
        disableOverlay("IconifyComponentNFN$n.overlay")

        if (isAtleastA14) {
            var requireReload = false

            if (RPrefs.getBoolean(FIX_NOTIFICATION_COLOR, false) &&
                isAndroid14 && isSecurityPatchBeforeJune2024()
            ) {
                RPrefs.putBoolean(FIX_NOTIFICATION_COLOR, false)
                requireReload = true
            }

            if (RPrefs.getBoolean(FIX_NOTIFICATION_FOOTER_BUTTON_COLOR, false)) {
                RPrefs.putBoolean(FIX_NOTIFICATION_FOOTER_BUTTON_COLOR, false)
                requireReload = true
            }

            if (requireReload) {
                SystemUtils.restartSystemUI()
            }
        }
    }

    private fun disableOthers(n: Int) {
        for (i in 1..TOTAL_NOTIFICATIONS) {
            RPrefs.putBoolean("IconifyComponentNFN$i.overlay", i == n)
            RPrefs.putBoolean("IconifyComponentNFP$i.overlay", false)
        }
    }
}