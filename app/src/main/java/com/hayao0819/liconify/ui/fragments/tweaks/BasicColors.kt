package com.hayao0819.liconify.ui.fragments.tweaks

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hayao0819.liconify.Iconify.Companion.appContext
import com.hayao0819.liconify.Iconify.Companion.appContextLocale
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Const.FRAMEWORK_PACKAGE
import com.hayao0819.liconify.data.common.Preferences.COLOR_ACCENT_PRIMARY
import com.hayao0819.liconify.data.common.Preferences.COLOR_ACCENT_PRIMARY_LIGHT
import com.hayao0819.liconify.data.common.Preferences.COLOR_ACCENT_SECONDARY
import com.hayao0819.liconify.data.common.Preferences.COLOR_ACCENT_SECONDARY_LIGHT
import com.hayao0819.liconify.data.common.Preferences.CUSTOM_ACCENT
import com.hayao0819.liconify.data.common.Preferences.CUSTOM_PRIMARY_COLOR_SWITCH
import com.hayao0819.liconify.data.common.Preferences.CUSTOM_SECONDARY_COLOR_SWITCH
import com.hayao0819.liconify.data.common.References.ICONIFY_COLOR_ACCENT_PRIMARY
import com.hayao0819.liconify.data.common.References.ICONIFY_COLOR_ACCENT_SECONDARY
import com.hayao0819.liconify.data.config.RPrefs.clearPrefs
import com.hayao0819.liconify.data.config.RPrefs.getBoolean
import com.hayao0819.liconify.data.config.RPrefs.getString
import com.hayao0819.liconify.data.config.RPrefs.putBoolean
import com.hayao0819.liconify.data.config.RPrefs.putString
import com.hayao0819.liconify.databinding.FragmentBasicColorsBinding
import com.hayao0819.liconify.ui.base.BaseFragment
import com.hayao0819.liconify.ui.utils.ViewHelper.setHeader
import com.hayao0819.liconify.utils.color.ColorUtils.colorToSpecialHex
import com.hayao0819.liconify.utils.overlay.FabricatedUtils.buildAndEnableOverlays
import com.hayao0819.liconify.utils.overlay.FabricatedUtils.disableOverlays
import com.hayao0819.liconify.utils.overlay.OverlayUtils.isOverlayDisabled

class BasicColors : BaseFragment() {

    private lateinit var binding: FragmentBasicColorsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasicColorsBinding.inflate(inflater, container, false)
        val view: View = binding.getRoot()

        // Header
        setHeader(
            requireContext(),
            getParentFragmentManager(),
            binding.header.toolbar,
            R.string.activity_title_basic_colors
        )

        accentPrimary = getString(
            COLOR_ACCENT_PRIMARY,
            resources.getColor(android.R.color.holo_blue_light, appContext.theme).toString()
        )
        accentSecondary = getString(
            COLOR_ACCENT_SECONDARY,
            resources.getColor(android.R.color.holo_blue_dark, appContext.theme).toString()
        )

        updatePrimaryColor()
        updateSecondaryColor()

        // Primary color picker
        binding.primaryColor.setColorPickerListener(
            activity = requireActivity(), defaultColor = accentPrimary!!.toInt(),
            showPresets = true,
            showAlphaSlider = false,
            showColorShades = true
        )
        binding.primaryColor.setOnColorSelectedListener { color: Int ->
            isSelectedPrimary = true
            accentPrimary = color.toString()
            updatePrimaryColor()
            binding.enableCustomColor.visibility = View.VISIBLE
            putBoolean(CUSTOM_PRIMARY_COLOR_SWITCH, true)
            refreshVisibility()
        }

        // Secondary color picker
        binding.secondaryColor.setColorPickerListener(
            activity = requireActivity(), defaultColor = accentSecondary!!.toInt(),
            showPresets = true,
            showAlphaSlider = false,
            showColorShades = true
        )
        binding.secondaryColor.setOnColorSelectedListener { color: Int ->
            isSelectedSecondary = true
            accentSecondary = color.toString()
            updateSecondaryColor()
            binding.enableCustomColor.visibility = View.VISIBLE
            putBoolean(CUSTOM_SECONDARY_COLOR_SWITCH, true)
            refreshVisibility()
        }

        // Enable custom colors
        binding.enableCustomColor.setOnClickListener {
            binding.enableCustomColor.visibility = View.GONE
            refreshVisibility()

            Thread {
                applyMonetColors()

                if (activity != null) {
                    requireActivity().runOnUiThread {
                        putBoolean(CUSTOM_ACCENT, true)

                        Handler(Looper.getMainLooper()).postDelayed({
                            if (context != null) {
                                Toast.makeText(
                                    requireContext(),
                                    appContextLocale.resources.getString(R.string.toast_applied),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, 2000)
                    }
                }
            }.start()
        }

        // Disable custom colors
        binding.disableCustomColor.visibility =
            if (getBoolean(CUSTOM_ACCENT, false)) View.VISIBLE else View.GONE
        binding.disableCustomColor.setOnClickListener {
            binding.disableCustomColor.visibility = View.GONE
            refreshVisibility()

            Thread {
                disableAccentColors()

                if (shouldUseDefaultColors()) {
                    applyDefaultColors()
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    if (context != null) {
                        Toast.makeText(
                            requireContext(),
                            appContextLocale.resources.getString(R.string.toast_disabled),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, 2000)
            }.start()
        }

        refreshVisibility()

        return view
    }

    private fun refreshVisibility() {
        if (binding.enableCustomColor.visibility == View.VISIBLE ||
            binding.disableCustomColor.visibility == View.VISIBLE
        ) {
            binding.buttonContainer.visibility = View.VISIBLE
        } else {
            binding.buttonContainer.visibility = View.GONE
        }
    }

    private fun updatePrimaryColor() {
        val gd = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(accentPrimary!!.toInt(), accentSecondary!!.toInt())
        )
        gd.setCornerRadius(24 * appContextLocale.resources.displayMetrics.density)
        binding.colorPreviewLarge.background = gd
    }

    private fun updateSecondaryColor() {
        val gd = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(accentPrimary!!.toInt(), accentSecondary!!.toInt())
        )
        gd.setCornerRadius(24 * appContextLocale.resources.displayMetrics.density)
        binding.colorPreviewLarge.background = gd
    }

    private fun applyMonetColors() {
        putBoolean(CUSTOM_ACCENT, true)

        if (isSelectedPrimary) {
            putString(COLOR_ACCENT_PRIMARY, accentPrimary)
            putString(COLOR_ACCENT_PRIMARY_LIGHT, accentPrimary)
        }
        if (isSelectedSecondary) {
            putString(COLOR_ACCENT_SECONDARY, accentSecondary)
            putString(COLOR_ACCENT_SECONDARY_LIGHT, accentSecondary)
        }

        if (isSelectedPrimary) applyPrimaryColors()
        if (isSelectedSecondary) applySecondaryColors()
    }

    companion object {
        private var isSelectedPrimary = false
        private var isSelectedSecondary = false
        private var accentPrimary: String? = null
        private var accentSecondary: String? = null

        fun applyPrimaryColors() {
            buildAndEnableOverlays(
                arrayOf(
                    FRAMEWORK_PACKAGE,
                    COLOR_ACCENT_PRIMARY,
                    "color",
                    "holo_blue_light",
                    colorToSpecialHex(getString(COLOR_ACCENT_PRIMARY)!!.toInt())
                ), arrayOf(
                    FRAMEWORK_PACKAGE,
                    COLOR_ACCENT_PRIMARY_LIGHT,
                    "color",
                    "holo_green_light",
                    colorToSpecialHex(getString(COLOR_ACCENT_PRIMARY)!!.toInt())
                )
            )
        }

        fun applySecondaryColors() {
            buildAndEnableOverlays(
                arrayOf(
                    FRAMEWORK_PACKAGE,
                    COLOR_ACCENT_SECONDARY,
                    "color",
                    "holo_blue_dark",
                    colorToSpecialHex(getString(COLOR_ACCENT_SECONDARY)!!.toInt())
                ), arrayOf(
                    FRAMEWORK_PACKAGE,
                    COLOR_ACCENT_SECONDARY_LIGHT,
                    "color",
                    "holo_green_dark",
                    colorToSpecialHex(getString(COLOR_ACCENT_SECONDARY)!!.toInt())
                )
            )
        }

        fun disableAccentColors() {
            putBoolean(CUSTOM_ACCENT, false)
            clearPrefs(
                CUSTOM_PRIMARY_COLOR_SWITCH,
                CUSTOM_SECONDARY_COLOR_SWITCH,
                COLOR_ACCENT_PRIMARY,
                COLOR_ACCENT_PRIMARY_LIGHT,
                COLOR_ACCENT_SECONDARY,
                COLOR_ACCENT_SECONDARY_LIGHT
            )
            disableOverlays(
                COLOR_ACCENT_PRIMARY,
                COLOR_ACCENT_PRIMARY_LIGHT,
                COLOR_ACCENT_SECONDARY,
                COLOR_ACCENT_SECONDARY_LIGHT
            )
        }

        fun applyDefaultColors() {
            applyDefaultPrimaryColors()
            applyDefaultSecondaryColors()
        }

        fun applyDefaultPrimaryColors() {
            buildAndEnableOverlays(
                arrayOf(
                    FRAMEWORK_PACKAGE,
                    COLOR_ACCENT_PRIMARY,
                    "color",
                    "holo_blue_light",
                    ICONIFY_COLOR_ACCENT_PRIMARY
                ),
                arrayOf(
                    FRAMEWORK_PACKAGE,
                    COLOR_ACCENT_PRIMARY_LIGHT,
                    "color",
                    "holo_green_light",
                    ICONIFY_COLOR_ACCENT_PRIMARY
                )
            )
        }

        fun applyDefaultSecondaryColors() {
            buildAndEnableOverlays(
                arrayOf(
                    FRAMEWORK_PACKAGE,
                    COLOR_ACCENT_SECONDARY,
                    "color",
                    "holo_blue_dark",
                    ICONIFY_COLOR_ACCENT_SECONDARY
                ),
                arrayOf(
                    FRAMEWORK_PACKAGE,
                    COLOR_ACCENT_SECONDARY_LIGHT,
                    "color",
                    "holo_green_dark",
                    ICONIFY_COLOR_ACCENT_SECONDARY
                )
            )
        }

        private fun shouldUseDefaultColors(): Boolean {
            return isOverlayDisabled("IconifyComponentAMAC.overlay") &&
                    isOverlayDisabled("IconifyComponentAMGC.overlay")
        }
    }
}