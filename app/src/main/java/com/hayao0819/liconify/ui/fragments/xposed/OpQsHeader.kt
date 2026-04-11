package com.hayao0819.liconify.ui.fragments.xposed

import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.OP_QS_HEADER_EXPANSION_Y
import com.hayao0819.liconify.data.common.Preferences.OP_QS_HEADER_GAP_EXPANDED
import com.hayao0819.liconify.data.common.Preferences.OP_QS_HEADER_SWITCH
import com.hayao0819.liconify.data.common.Preferences.OP_QS_HEADER_TOP_MARGIN
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.base.ControlledPreferenceFragmentCompat

class OpQsHeader : ControlledPreferenceFragmentCompat() {

    override val title: String
        get() = getString(R.string.activity_title_op_qs_header)

    override val backButtonEnabled: Boolean
        get() = true

    override val layoutResource: Int
        get() = R.xml.xposed_op_qs_header

    override val hasMenu: Boolean
        get() = true

    override fun updateScreen(key: String?) {
        super.updateScreen(key)

        when (key) {
            OP_QS_HEADER_SWITCH,
            OP_QS_HEADER_TOP_MARGIN,
            OP_QS_HEADER_EXPANSION_Y,
            OP_QS_HEADER_GAP_EXPANDED -> {
                MainActivity.showOrHidePendingActionButton(
                    activityBinding = (requireActivity() as MainActivity).binding,
                    requiresSystemUiRestart = true
                )
            }
        }
    }
}
