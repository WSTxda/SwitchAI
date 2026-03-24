package com.wstxda.switchai.fragment.preferences

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import com.wstxda.switchai.R
import com.wstxda.switchai.fragment.BasePreferenceFragment
import com.wstxda.switchai.preference.MultiSelectListPreference
import com.wstxda.switchai.ui.component.AssistantManagerDialog
import com.wstxda.switchai.ui.component.AssistantGridDialog
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.viewmodel.AssistantSelectorViewModel

class SelectorPreferencesFragment : BasePreferenceFragment() {

    private val assistantSelectorViewModel: AssistantSelectorViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override val preferencesResId: Int get() = R.xml.preferences_selector

    override fun setupObservers() {
        assistantSelectorViewModel.isDynamicModeEnabled.observe(viewLifecycleOwner) { isDynamic ->
            findPreference<MultiSelectListPreference>(
                Constants.SELECTOR_MANAGER_MANUAL_PREF_KEY
            )?.isVisible = !isDynamic
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == Constants.SELECTOR_GRID_PREF_KEY) {
            AssistantGridDialog.show(parentFragmentManager)
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (parentFragmentManager.findFragmentByTag(Constants.PREFERENCE_DIALOG) != null) return

        if (preference is MultiSelectListPreference) {
            val dialog = AssistantManagerDialog.newInstance(preference.key)
            @Suppress("DEPRECATION") dialog.setTargetFragment(this, 0)
            dialog.show(parentFragmentManager, Constants.PREFERENCE_DIALOG)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}