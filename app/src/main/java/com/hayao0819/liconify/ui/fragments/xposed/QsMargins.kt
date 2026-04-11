package com.hayao0819.liconify.ui.fragments.xposed

import com.hayao0819.liconify.R
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat

class QsMargins : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.custom_qs_margin_title)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_qs_margins

    override val hasMenu: Boolean
        get() = true
}