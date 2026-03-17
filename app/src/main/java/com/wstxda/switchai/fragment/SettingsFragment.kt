package com.wstxda.switchai.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialSharedAxis
import com.wstxda.switchai.R
import com.wstxda.switchai.preference.DigitalAssistantPreference
import com.wstxda.switchai.ui.component.AssistantTutorialBottomSheet
import com.wstxda.switchai.ui.component.DigitalAssistantSetupDialog
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.viewmodel.SettingsViewModel
import com.wstxda.switchai.widget.utils.AssistantWidgetUpdater
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {

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

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_main, rootKey)
        assistantResourcesManager = AssistantResourcesManager(requireContext())
        setupInitialVisibility()
        setupPreferences()
        observeViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(MaterialColors.getColor(view, android.R.attr.colorBackground))
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            val isDone = digitalAssistantPreference.checkDigitalAssistSetupStatus()
            settingsViewModel.setAssistSetupDone(isDone)
            digitalAssistantPreference.updateDigitalAssistantPreferences(isDone)
        }
    }

    private fun setupPreferences() {
        setupDigitalAssistantClickListener()
        setupDigitalAssistantIconPreferenceListener()
        setupNavigationPreferences()
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
                findNavController().navigate(actionId)
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
                assistantResourcesManager.updatePreferenceIcon(
                    preference, newValue.toString()
                )
            }
            AssistantWidgetUpdater.updateAllWidgets(requireContext())
            true
        }
        listPreference?.let { pref ->
            assistantResourcesManager.updatePreferenceIcon(pref, pref.value)
        }
    }

    private fun observeViewModel() {
        settingsViewModel.isAssistSetupDone.observe(this) { isDone ->
            findPreference<Preference>(Constants.DIGITAL_ASSISTANT_SETUP_PREF_KEY)?.isVisible =
                !isDone
        }
    }
}