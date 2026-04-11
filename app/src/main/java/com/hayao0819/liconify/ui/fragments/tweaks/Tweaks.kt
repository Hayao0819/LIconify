package com.hayao0819.liconify.ui.fragments.tweaks

import com.hayao0819.liconify.R
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat

class Tweaks : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.navbar_tweaks)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.tweaks

    override val hasMenu: Boolean
        get() = true
}
