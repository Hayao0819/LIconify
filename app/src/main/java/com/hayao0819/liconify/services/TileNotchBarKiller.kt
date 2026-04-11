package com.hayao0819.liconify.services

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Const.FRAMEWORK_PACKAGE
import com.hayao0819.liconify.data.common.Preferences.NOTCH_BAR_KILLER_SWITCH
import com.hayao0819.liconify.data.config.RPrefs.getBoolean
import com.hayao0819.liconify.data.config.RPrefs.putBoolean
import com.hayao0819.liconify.utils.SystemUtils
import com.hayao0819.liconify.utils.overlay.manager.resource.ResourceEntry
import com.hayao0819.liconify.utils.overlay.manager.resource.ResourceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TileNotchBarKiller : TileService() {

    private var isNotchBarKillerEnabled = getBoolean(NOTCH_BAR_KILLER_SWITCH, false)

    override fun onStartListening() {
        super.onStartListening()

        val tile = qsTile
        tile.state = if (isNotchBarKillerEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()

        if (!SystemUtils.hasStoragePermission()) {
            val tile = qsTile
            tile.subtitle = resources.getString(R.string.need_storage_perm_title)
            tile.updateTile()
            return
        }

        isNotchBarKillerEnabled = !isNotchBarKillerEnabled

        putBoolean(NOTCH_BAR_KILLER_SWITCH, isNotchBarKillerEnabled)

        val tile = qsTile
        tile.state = if (isNotchBarKillerEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = resources.getString(R.string.notch_bar_killer_title)
        tile.subtitle =
            if (isNotchBarKillerEnabled) resources.getString(R.string.general_on) else resources.getString(
                R.string.general_off
            )
        tile.updateTile()

        val resources = listOf(
            ResourceEntry(
                FRAMEWORK_PACKAGE,
                "bool",
                "config_fillMainBuiltInDisplayCutout",
                "false"
            ),
            ResourceEntry(
                FRAMEWORK_PACKAGE,
                "bool",
                "config_maskMainBuiltInDisplayCutout",
                "true"
            ),
            ResourceEntry(
                FRAMEWORK_PACKAGE,
                "string",
                "config_mainBuiltInDisplayCutout",
                "M 0,0 L 0, 0 C 0,0 0,0 0,0"
            ),
            ResourceEntry(
                FRAMEWORK_PACKAGE,
                "string",
                "config_mainBuiltInDisplayCutoutRectApproximation",
                "@string/config_mainBuiltInDisplayCutout"
            )
        )

        CoroutineScope(Dispatchers.IO).launch {
            if (isNotchBarKillerEnabled) {
                ResourceManager.buildOverlayWithResource(*resources.toTypedArray())
            } else {
                ResourceManager.removeResourceFromOverlay(*resources.toTypedArray())
            }
        }
    }
}
