package com.wstxda.switchai.fragment.preferences

import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.transition.MaterialSharedAxis
import com.wstxda.switchai.R
import com.wstxda.switchai.fragment.BasePreferenceFragment
import com.wstxda.switchai.preference.DigitalAssistantPreference
import com.wstxda.switchai.ui.component.AssistantTutorialBottomSheet
import com.wstxda.switchai.ui.component.DigitalAssistantSetupDialog
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.viewmodel.SettingsViewModel
import com.wstxda.switchai.widget.utils.AssistantWidgetUpdater
import kotlinx.coroutines.launch

class MainPreferencesFragment : BasePreferenceFragment() {

    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private lateinit var assistantResourcesManager: AssistantResourcesManager
    private val digitalAssistantPreference by lazy { DigitalAssistantPreference(this) }

    private val digitalAssistantLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            val isDone = digitalAssistantPreference.checkDigitalAssistSetupStatus()
            settingsViewModel.setAssistSetupDone(isDone)
            digitalAssistantPreference.updateDigitalAssistantPreferences(isDone)
            if (isDone) {
                AssistantTutorialBottomSheet.show(childFragmentManager)
            } else {
                setupDigitalAssistantClickListener()
            }
        }
    }

    override val preferencesResId: Int get() = R.xml.preferences_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = null
        returnTransition = null
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            val isDone = digitalAssistantPreference.checkDigitalAssistSetupStatus()
            settingsViewModel.setAssistSetupDone(isDone)
            digitalAssistantPreference.updateDigitalAssistantPreferences(isDone)
        }
    }

    override fun setupViews() {
        assistantResourcesManager = AssistantResourcesManager(requireContext())
        setupInitialVisibility()
    }

    override fun setupListeners() {
        setupDigitalAssistantClickListener()
        setupDigitalAssistantIconPreferenceListener()
        setupNavigationPreferences()
    }

    override fun setupObservers() {
        settingsViewModel.isAssistSetupDone.observe(viewLifecycleOwner) { isDone ->
            findPreference<Preference>(Constants.DIGITAL_ASSISTANT_SETUP_PREF_KEY)?.isVisible =
                !isDone
        }
    }

    override fun getToolbarTitle(): String = getString(R.string.app_settings)

    override fun showNavigationIcon(): Boolean = false

    override fun onToolbarCreated(toolbar: MaterialToolbar) {
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_show_tutorial -> {
                    AssistantTutorialBottomSheet.show(childFragmentManager)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupNavigationPreferences() {
        val navActions = mapOf(
            Constants.NAV_SELECTOR_PREF_KEY to R.id.settingsFragmentSelector,
            Constants.NAV_VOICE_INPUT_PREF_KEY to R.id.settingsFragmentVoiceInput,
            Constants.NAV_ACCESSIBILITY_PREF_KEY to R.id.settingsFragmentAccessibility,
            Constants.NAV_SHORTCUTS_PREF_KEY to R.id.settingsFragmentShortcuts,
            Constants.NAV_APPEARANCE_PREF_KEY to R.id.settingsFragmentAppearance,
            Constants.NAV_ABOUT_PREF_KEY to R.id.settingsFragmentAbout,
        )

        navActions.forEach { (key, actionId) ->
            findPreference<Preference>(key)?.setOnPreferenceClickListener {
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.settingsFragment) {
                    navController.navigate(actionId)
                }
                true
            }
        }
    }

    private fun setupInitialVisibility() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            findPreference<Preference>(Constants.NAV_SHORTCUTS_PREF_KEY)?.isVisible = false
        }
    }

    private fun setupDigitalAssistantClickListener() {
        findPreference<Preference>(Constants.DIGITAL_ASSISTANT_SETUP_PREF_KEY)?.setOnPreferenceClickListener {
            DigitalAssistantSetupDialog.show(childFragmentManager, digitalAssistantLauncher)
            true
        }
    }

    private fun setupDigitalAssistantIconPreferenceListener() {
        val listPreference =
            findPreference<ListPreference>(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY)
        listPreference?.setOnPreferenceChangeListener { preference, newValue ->
            if (preference is ListPreference) {
                assistantResourcesManager.updatePreferenceIcon(preference, newValue.toString())
            }
            AssistantWidgetUpdater.updateAllWidgets(requireContext())
            true
        }
        listPreference?.let { assistantResourcesManager.updatePreferenceIcon(it, it.value) }
    }
}