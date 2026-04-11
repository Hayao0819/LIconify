package com.hayao0819.liconify.xposed.modules.volume

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.XResources
import android.content.res.XResources.DrawableLoader
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.hayao0819.liconify.data.common.Const.SYSTEMUI_PACKAGE
import com.hayao0819.liconify.data.common.Preferences.VOLUME_PANEL_STYLE
import com.hayao0819.liconify.xposed.HookRes.Companion.resParams
import com.hayao0819.liconify.xposed.ModPack
import com.hayao0819.liconify.xposed.modules.extras.utils.SettingsLibUtils.Companion.getColorAttrDefaultColor
import com.hayao0819.liconify.xposed.modules.extras.utils.toolkit.XposedHook.Companion.findClass
import com.hayao0819.liconify.xposed.modules.extras.utils.toolkit.callMethod
import com.hayao0819.liconify.xposed.modules.extras.utils.toolkit.getField
import com.hayao0819.liconify.xposed.modules.extras.utils.toolkit.hookMethod
import com.hayao0819.liconify.xposed.modules.extras.utils.toolkit.log
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.Utils.disableOverlay
import com.hayao0819.liconify.xposed.modules.quicksettings.themes.Utils.enableOverlay
import com.hayao0819.liconify.xposed.modules.volume.styles.VolumeDoubleLayer
import com.hayao0819.liconify.xposed.modules.volume.styles.VolumeGradient
import com.hayao0819.liconify.xposed.modules.volume.styles.VolumeNeumorph
import com.hayao0819.liconify.xposed.modules.volume.styles.VolumeNeumorphOutline
import com.hayao0819.liconify.xposed.modules.volume.styles.VolumeOutline
import com.hayao0819.liconify.xposed.modules.volume.styles.VolumeShadedLayer
import com.hayao0819.liconify.xposed.modules.volume.styles.VolumeStyleBase
import com.hayao0819.liconify.xposed.utils.XPrefs.Xprefs
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

@SuppressLint("DiscouragedApi", "DefaultLocale")
class VolumePanelStyle(context: Context) : ModPack(context) {

    private var volumePanelStyle = 0
    private var volumePanelStyleEnabled = false

    override fun updatePrefs(vararg key: String) {
        Xprefs.apply {
            volumePanelStyle = getString(VOLUME_PANEL_STYLE, "0")!!.toInt()
            volumePanelStyleEnabled = volumePanelStyle != 0
        }

        when (key.firstOrNull()) {
            VOLUME_PANEL_STYLE -> {
                val styleSixOverlay = "IconifyComponentIXCC.overlay"

                if (volumePanelStyle == 6) {
                    enableOverlay(styleSixOverlay)
                } else {
                    disableOverlay(styleSixOverlay)
                }
            }
        }
    }

    override fun handleLoadPackage(loadPackageParam: LoadPackageParam) {
        val roundedCornerProgressDrawable =
            findClass("$SYSTEMUI_PACKAGE.util.RoundedCornerProgressDrawable")!!
        val alphaTintDrawableWrapperClass =
            findClass("$SYSTEMUI_PACKAGE.util.AlphaTintDrawableWrapper")!!

        val xResources: XResources = resParams[SYSTEMUI_PACKAGE]?.res
            ?: run {
                log(this@VolumePanelStyle, "$SYSTEMUI_PACKAGE resources not found")
                return
            }

        val volumeStyle: VolumeStyleBase? = when (volumePanelStyle) {
            1 -> VolumeGradient(
                mContext,
                roundedCornerProgressDrawable,
                alphaTintDrawableWrapperClass
            )

            2 -> VolumeDoubleLayer(
                mContext,
                roundedCornerProgressDrawable,
                alphaTintDrawableWrapperClass
            )

            3 -> VolumeShadedLayer(
                mContext,
                roundedCornerProgressDrawable,
                alphaTintDrawableWrapperClass
            )

            4 -> VolumeNeumorph(
                mContext,
                roundedCornerProgressDrawable,
                alphaTintDrawableWrapperClass
            )

            5 -> VolumeOutline(
                mContext,
                roundedCornerProgressDrawable,
                alphaTintDrawableWrapperClass
            )

            6 -> VolumeNeumorphOutline(
                mContext,
                roundedCornerProgressDrawable,
                alphaTintDrawableWrapperClass
            )

            else -> null
        }

        xResources.setReplacement(
            SYSTEMUI_PACKAGE,
            "drawable",
            "volume_drawer_selection_bg",
            object : DrawableLoader() {
                override fun newDrawable(res: XResources, id: Int): Drawable? {
                    return volumeStyle?.createVolumeDrawerSelectionBgDrawable()
                        ?.constantState?.newDrawable()
                }
            }
        )

        xResources.setReplacement(
            SYSTEMUI_PACKAGE,
            "drawable",
            "volume_row_seekbar",
            object : DrawableLoader() {
                override fun newDrawable(res: XResources, id: Int): Drawable? {
                    return volumeStyle?.createVolumeRowSeekbarDrawable()
                        ?.constantState?.newDrawable()
                }
            }
        )

        val volumeDialogImplClass = findClass("$SYSTEMUI_PACKAGE.volume.VolumeDialogImpl")

        volumeDialogImplClass
            .hookMethod("initDialog")
            .runAfter { param ->
                if (!volumePanelStyleEnabled) return@runAfter

                val mSelectedRingerIcon = param.thisObject.getField(
                    "mSelectedRingerIcon"
                ) as ImageView

                mSelectedRingerIcon.imageTintList = ColorStateList.valueOf(
                    iconColor(
                        param.thisObject.getField("mContext") as Context
                    )
                )
            }

        volumeDialogImplClass
            .hookMethod("updateVolumeRowTintH")
            .runAfter { param ->
                if (!volumePanelStyleEnabled) return@runAfter

                param.args[0]
                    .getField("sliderProgressIcon")
                    .callMethod(
                        "setTintList",
                        ColorStateList.valueOf(
                            iconColor(
                                param.thisObject.getField("mContext") as Context
                            )
                        )
                    )
            }
    }

    private fun iconColor(context: Context): Int {
        return if (volumePanelStyle == 5 || volumePanelStyle == 6) {
            getColorAttrDefaultColor(context, android.R.attr.textColorPrimary)
        } else {
            getColorAttrDefaultColor(context, android.R.attr.textColorPrimaryInverse)
        }
    }
}