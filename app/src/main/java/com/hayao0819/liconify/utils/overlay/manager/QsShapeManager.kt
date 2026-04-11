package com.hayao0819.liconify.utils.overlay.manager

import com.hayao0819.liconify.data.common.Dynamic.TOTAL_QSSHAPES
import com.hayao0819.liconify.data.common.Dynamic.isAtleastA14
import com.hayao0819.liconify.data.common.Preferences.FIX_QS_TILE_COLOR
import com.hayao0819.liconify.data.config.RPrefs
import com.hayao0819.liconify.utils.SystemUtils
import com.hayao0819.liconify.utils.overlay.OverlayUtils.changeOverlayState
import com.hayao0819.liconify.utils.overlay.OverlayUtils.disableOverlay
import com.hayao0819.liconify.utils.overlay.OverlayUtils.enableOverlayExclusiveInCategory
import com.hayao0819.liconify.utils.overlay.OverlayUtils.enableOverlays
import com.hayao0819.liconify.utils.overlay.OverlayUtils.isOverlayEnabled

object QsShapeManager {

    fun enableOverlay(n: Int) {
        disableOthers(n)
        enableOverlayExclusiveInCategory("IconifyComponentQSSN$n.overlay")

        if (!isOverlayEnabled("IconifyComponentCR1.overlay") || !isOverlayEnabled("IconifyComponentCR2.overlay")) {
            enableOverlays("IconifyComponentCR1.overlay", "IconifyComponentCR2.overlay")
        }

        changeOverlayState(
            "IconifyComponentQSNT1.overlay",
            !isOverlayEnabled("IconifyComponentQSNT1.overlay"),
            "IconifyComponentQSNT1.overlay",
            isOverlayEnabled("IconifyComponentQSNT1.overlay"),
            "IconifyComponentQSNT2.overlay",
            !isOverlayEnabled("IconifyComponentQSNT2.overlay"),
            "IconifyComponentQSNT2.overlay",
            isOverlayEnabled("IconifyComponentQSNT2.overlay"),
            "IconifyComponentQSNT3.overlay",
            !isOverlayEnabled("IconifyComponentQSNT3.overlay"),
            "IconifyComponentQSNT3.overlay",
            isOverlayEnabled("IconifyComponentQSNT3.overlay"),
            "IconifyComponentQSNT4.overlay",
            !isOverlayEnabled("IconifyComponentQSNT4.overlay"),
            "IconifyComponentQSNT4.overlay",
            isOverlayEnabled("IconifyComponentQSNT4.overlay")
        )

        if (isAtleastA14 && !RPrefs.getBoolean(FIX_QS_TILE_COLOR, false)) {
            RPrefs.putBoolean(FIX_QS_TILE_COLOR, true)
            SystemUtils.restartSystemUI()
        }
    }

    fun disableOverlay(n: Int) {
        disableOverlay("IconifyComponentQSSN$n.overlay")

        if (isAtleastA14 && RPrefs.getBoolean(FIX_QS_TILE_COLOR, false)) {
            RPrefs.putBoolean(FIX_QS_TILE_COLOR, false)
            SystemUtils.restartSystemUI()
        }
    }

    private fun disableOthers(n: Int) {
        for (i in 1..TOTAL_QSSHAPES) {
            RPrefs.putBoolean("IconifyComponentQSSN$i.overlay", i == n)
            RPrefs.putBoolean("IconifyComponentQSSP$i.overlay", false)
        }
    }
}