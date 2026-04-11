package com.hayao0819.liconify.utils.overlay.manager

import com.hayao0819.liconify.data.common.Dynamic.TOTAL_QSSHAPESPIXEL
import com.hayao0819.liconify.data.common.Dynamic.isAtleastA14
import com.hayao0819.liconify.data.common.Preferences.FIX_QS_TILE_COLOR
import com.hayao0819.liconify.data.config.RPrefs
import com.hayao0819.liconify.utils.SystemUtils
import com.hayao0819.liconify.utils.overlay.OverlayUtils.changeOverlayState
import com.hayao0819.liconify.utils.overlay.OverlayUtils.disableOverlay
import com.hayao0819.liconify.utils.overlay.OverlayUtils.enableOverlayExclusiveInCategory
import com.hayao0819.liconify.utils.overlay.OverlayUtils.enableOverlays
import com.hayao0819.liconify.utils.overlay.OverlayUtils.isOverlayEnabled

object QsShapePixelManager {
    fun enableOverlay(n: Int) {
        disableOthers(n)
        enableOverlayExclusiveInCategory("IconifyComponentQSSP$n.overlay")

        if (!isOverlayEnabled("IconifyComponentCR1.overlay") || !isOverlayEnabled("IconifyComponentCR2.overlay")) {
            enableOverlays("IconifyComponentCR1.overlay", "IconifyComponentCR2.overlay")
        }

        changeOverlayState(
            "IconifyComponentQSPT1.overlay",
            !isOverlayEnabled("IconifyComponentQSPT1.overlay"),
            "IconifyComponentQSPT1.overlay",
            isOverlayEnabled("IconifyComponentQSPT1.overlay"),
            "IconifyComponentQSPT2.overlay",
            !isOverlayEnabled("IconifyComponentQSPT2.overlay"),
            "IconifyComponentQSPT2.overlay",
            isOverlayEnabled("IconifyComponentQSPT2.overlay"),
            "IconifyComponentQSPT3.overlay",
            !isOverlayEnabled("IconifyComponentQSPT3.overlay"),
            "IconifyComponentQSPT3.overlay",
            isOverlayEnabled("IconifyComponentQSPT3.overlay"),
            "IconifyComponentQSPT4.overlay",
            !isOverlayEnabled("IconifyComponentQSPT4.overlay"),
            "IconifyComponentQSPT4.overlay",
            isOverlayEnabled("IconifyComponentQSPT4.overlay")
        )

        if (isAtleastA14 && !RPrefs.getBoolean(FIX_QS_TILE_COLOR, false)) {
            RPrefs.putBoolean(FIX_QS_TILE_COLOR, true)
            SystemUtils.restartSystemUI()
        }
    }

    fun disableOverlay(n: Int) {
        disableOverlay("IconifyComponentQSSP$n.overlay")

        if (isAtleastA14 && RPrefs.getBoolean(FIX_QS_TILE_COLOR, false)) {
            RPrefs.putBoolean(FIX_QS_TILE_COLOR, false)
            SystemUtils.restartSystemUI()
        }
    }

    private fun disableOthers(n: Int) {
        for (i in 1..TOTAL_QSSHAPESPIXEL) {
            RPrefs.putBoolean("IconifyComponentQSSP$i.overlay", i == n)
            RPrefs.putBoolean("IconifyComponentQSSN$i.overlay", false)
        }
    }
}