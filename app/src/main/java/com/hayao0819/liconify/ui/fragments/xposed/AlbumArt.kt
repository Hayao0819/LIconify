package com.hayao0819.liconify.ui.fragments.xposed

import com.hayao0819.liconify.R
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat

class AlbumArt : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_lockscreen_album_art)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_lockscreen_album_art

    override val hasMenu: Boolean
        get() = true
}
