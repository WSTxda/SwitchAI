package com.wstxda.switchai.ui.component

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceDialogFragmentCompat
import com.wstxda.switchai.R
import com.wstxda.switchai.fragment.preferences.MultiSelectListPreference
import com.wstxda.switchai.utils.Constants

class AssistantManagerDialog : PreferenceDialogFragmentCompat() {

    private lateinit var pref: MultiSelectListPreference
    private val currentSelections = HashSet<String>()
    private var toastShownForInvalidSelection = false

    companion object {
        fun newInstance(key: String) = AssistantManagerDialog().apply {
            arguments = Bundle().apply { putString(ARG_KEY, key) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pref = preference as MultiSelectListPreference

        currentSelections.clear()
        val restored = savedInstanceState?.getStringArray(Constants.ASSISTANT_MANAGER_DIALOG)
        if (restored != null) {
            currentSelections.addAll(restored)
        } else {
            currentSelections.addAll(pref.values)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray(
            Constants.ASSISTANT_MANAGER_DIALOG, currentSelections.toTypedArray()
        )
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder) {
        super.onPrepareDialogBuilder(builder)

        val entries = pref.entries
        val entryValues = pref.entryValues
        val checkedItems = BooleanArray(entryValues.size) { index ->
            currentSelections.contains(entryValues[index].toString())
        }

        builder.setMultiChoiceItems(entries, checkedItems) { _, which, isChecked ->
            val value = entryValues[which].toString()
            if (isChecked) currentSelections.add(value) else currentSelections.remove(value)
            updatePositiveButtonState()
        }
    }

    override fun onStart() {
        super.onStart()
        updatePositiveButtonState()
    }

    private fun updatePositiveButtonState() {
        val okButton = (dialog as? AlertDialog)?.getButton(AlertDialog.BUTTON_POSITIVE) ?: return

        val count = currentSelections.size
        val isValid = count >= pref.minSelection && count <= pref.maxSelection
        okButton.isEnabled = isValid

        if (isValid) {
            toastShownForInvalidSelection = false
            return
        }

        if (!toastShownForInvalidSelection) {
            val limit: Int
            val msgRes: Int
            when {
                count < pref.minSelection -> {
                    limit = pref.minSelection
                    msgRes = R.string.error_min_selection
                }

                count > pref.maxSelection -> {
                    limit = pref.maxSelection
                    msgRes = R.string.error_max_selection
                }

                else -> return
            }
            Toast.makeText(requireContext(), getString(msgRes, limit), Toast.LENGTH_SHORT).show()
            toastShownForInvalidSelection = true
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) saveSelections()
    }

    private fun saveSelections() {
        if (pref.callChangeListener(currentSelections)) {
            pref.values = currentSelections
        }
    }
}