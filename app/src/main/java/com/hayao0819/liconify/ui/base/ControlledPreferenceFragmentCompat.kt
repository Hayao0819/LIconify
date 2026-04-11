package com.hayao0819.liconify.ui.base

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import androidx.recyclerview.widget.RecyclerView
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Dynamic
import com.hayao0819.liconify.data.common.Resources.SHARED_XPREFERENCES
import com.hayao0819.liconify.data.common.Resources.searchConfiguration
import com.hayao0819.liconify.data.common.Resources.searchableFragments
import com.hayao0819.liconify.data.config.PrefsHelper
import com.hayao0819.liconify.data.config.RPrefs
import com.hayao0819.liconify.ui.activities.MainActivity
import com.hayao0819.liconify.ui.activities.MainActivity.Companion.popCurrentFragment
import com.hayao0819.liconify.ui.activities.MainActivity.Companion.replaceFragment
import com.hayao0819.liconify.ui.dialogs.LoadingDialog
import com.hayao0819.liconify.ui.fragments.settings.Changelog
import com.hayao0819.liconify.ui.fragments.settings.Experimental
import com.hayao0819.liconify.ui.fragments.xposed.LockscreenClockParent
import com.hayao0819.liconify.ui.fragments.xposed.VolumePanelParent
import com.hayao0819.liconify.ui.preferences.preferencesearch.SearchPreferenceResult
import com.hayao0819.liconify.utils.SystemUtils.restartSystemUI
import com.hayao0819.liconify.utils.helper.ImportExport.exportSettings
import com.hayao0819.liconify.utils.helper.ImportExport.handleExportResult
import com.hayao0819.liconify.utils.helper.ImportExport.handleImportResult
import com.hayao0819.liconify.utils.helper.ImportExport.importSettings
import com.hayao0819.liconify.utils.helper.LocaleHelper

abstract class ControlledPreferenceFragmentCompat : PreferenceFragmentCompat() {

    private var loadingDialog: LoadingDialog? = null

    private val changeListener =
        OnSharedPreferenceChangeListener { _: SharedPreferences, key: String? ->
            updateScreen(
                key
            )
        }

    abstract val title: String

    abstract val backButtonEnabled: Boolean

    abstract val layoutResource: Int

    open val themeResource: Int
        get() = R.style.PrefsThemeToolbar

    abstract val hasMenu: Boolean

    open val menuResource: Int
        get() = R.menu.default_menu

    private var startExportActivityIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        handleExportResult(
            result = result,
            context = requireContext()
        )
    }

    private var startImportActivityIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        handleImportResult(
            result = result,
            fragment = this,
            loadingDialog = loadingDialog
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.setStorageDeviceProtected()
        preferenceManager.sharedPreferencesName = SHARED_XPREFERENCES
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE

        try {
            setPreferencesFromResource(layoutResource, rootKey)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load preference from resource", e)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(LocaleHelper.setLocale(context))

        if (activity != null) {
            val window = requireActivity().window
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inflater.context.setTheme(themeResource)

        // Initialize loading dialog
        loadingDialog = LoadingDialog(requireActivity())

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val baseContext = context as AppCompatActivity
        view.findViewById<Toolbar?>(R.id.toolbar)?.let {
            baseContext.setSupportActionBar(it)
            it.title = title
        }
        baseContext.supportActionBar?.setDisplayHomeAsUpEnabled(backButtonEnabled)

        if (hasMenu) {
            val menuHost: MenuHost = requireActivity()
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                    menuInflater.inflate(menuResource, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_search -> {
                            searchConfiguration.showSearchFragment()
                            true
                        }

                        R.id.menu_changelog -> {
                            replaceFragment(parentFragmentManager, Changelog())
                            true
                        }

                        R.id.menu_export_settings -> {
                            exportSettings(
                                this@ControlledPreferenceFragmentCompat,
                                startExportActivityIntent
                            )
                            true
                        }

                        R.id.menu_import_settings -> {
                            importSettings(
                                this@ControlledPreferenceFragmentCompat,
                                startImportActivityIntent
                            )
                            true
                        }

                        R.id.restart_systemui -> {
                            Dynamic.requiresSystemUiRestart = false

                            MainActivity.showOrHidePendingActionButton(
                                activityBinding = (requireActivity() as MainActivity).binding,
                                requiresSystemUiRestart = false
                            )

                            Handler(Looper.getMainLooper()).postDelayed({
                                restartSystemUI()
                            }, 300)
                            true
                        }

                        R.id.experimental_features -> {
                            replaceFragment(parentFragmentManager, Experimental())
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    public override fun onCreateAdapter(preferenceScreen: PreferenceScreen): RecyclerView.Adapter<*> {
        RPrefs.registerOnSharedPreferenceChangeListener(changeListener)

        updateScreen(null)

        return super.onCreateAdapter(preferenceScreen)
    }

    fun onSearchResultClicked(result: SearchPreferenceResult) {
        if (result.resourceFile == layoutResource) {
            popCurrentFragment(parentFragmentManager)
            SearchPreferenceResult.highlight(this, result.key)
        } else {
            for (searchableFragment in searchableFragments) {
                if (searchableFragment.xml == result.resourceFile) {
                    replaceFragment(parentFragmentManager, searchableFragment.fragment)
                    val fragment = searchableFragment.fragment
                    val resultFragment: ControlledPreferenceFragmentCompat?

                    when (fragment) {
                        is LockscreenClockParent -> {
                            resultFragment = LockscreenClockParent.getPreferenceFragment()
                            fragment.scrollToPreference()
                        }

                        is VolumePanelParent -> {
                            resultFragment = VolumePanelParent.getPreferenceFragment()
                            fragment.scrollToPreference()
                        }

                        else -> {
                            resultFragment = fragment as ControlledPreferenceFragmentCompat
                        }
                    }

                    SearchPreferenceResult.highlight(resultFragment, result.key)
                    break
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateScreen(null)
    }

    override fun onDestroy() {
        loadingDialog?.hide()

        RPrefs.unregisterOnSharedPreferenceChangeListener(changeListener)

        super.onDestroy()
    }

    open fun updateScreen(key: String?) {
        PrefsHelper.setupAllPreferences(this.preferenceScreen)
    }

    override fun setDivider(divider: Drawable?) {
        super.setDivider(ColorDrawable(Color.TRANSPARENT))
    }

    override fun setDividerHeight(height: Int) {
        super.setDividerHeight(0)
    }

    companion object {
        private val TAG = ControlledPreferenceFragmentCompat::class.java.simpleName
    }
}
