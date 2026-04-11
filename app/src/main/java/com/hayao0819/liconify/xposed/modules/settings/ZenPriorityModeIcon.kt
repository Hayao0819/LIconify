package com.hayao0819.liconify.xposed.modules.settings

import android.annotation.SuppressLint
import android.content.Context
import com.hayao0819.liconify.data.common.Const.SETTINGS_PACKAGE
import com.hayao0819.liconify.xposed.ModPack
import com.hayao0819.liconify.xposed.modules.extras.utils.toolkit.XposedHook.Companion.findClass
import com.hayao0819.liconify.xposed.modules.extras.utils.toolkit.callMethod
import com.hayao0819.liconify.xposed.modules.extras.utils.toolkit.hookMethod
import com.hayao0819.liconify.xposed.utils.XPrefs.Xprefs
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

@SuppressLint("DiscouragedApi")
class ZenPriorityModeIcon(context: Context) : ModPack(context) {

    private var replaceZenModeIcon = false

    override fun updatePrefs(vararg key: String) {
        Xprefs.apply {
            replaceZenModeIcon = getBoolean("IconifyComponentSIP1.overlay", false)
        }
    }

    override fun handleLoadPackage(loadPackageParam: LoadPackageParam) {
        val topLevelSettingsClass = findClass("$SETTINGS_PACKAGE.homepage.TopLevelSettings")

        topLevelSettingsClass
            .hookMethod("onCreateAdapter")
            .runBefore { param ->
                if (!replaceZenModeIcon) return@runBefore

                val preferenceScreen = param.args[0]

                preferenceScreen.callMethod(
                    "findPreference",
                    "top_level_priority_modes"
                )?.callMethod(
                    "setIcon",
                    mContext.resources.getIdentifier(
                        "ic_suggestion_dnd",
                        "drawable",
                        mContext.packageName
                    )
                )
            }
    }
}