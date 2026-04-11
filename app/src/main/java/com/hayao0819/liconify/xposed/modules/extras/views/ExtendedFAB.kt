package com.hayao0819.liconify.xposed.modules.extras.views

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import com.hayao0819.liconify.R
import com.hayao0819.liconify.xposed.HookRes.Companion.modRes
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ExtendedFAB @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ExtendedFloatingActionButton(
    ContextThemeWrapper(object : ContextWrapper(context) {
        override fun getResources(): Resources {
            return modRes
        }
    }, R.style.Theme_MaterialComponents_DayNight),
    attrs,
    defStyleAttr
)