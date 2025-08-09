package com.wstxda.switchai.ui.component

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.AssistantSetupDialogBinding
import com.wstxda.switchai.fragment.preferences.DigitalAssistantPreference
import com.wstxda.switchai.utils.Constants

class DigitalAssistantSetupDialog : DialogFragment() {

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var _binding: AssistantSetupDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            launcher: ActivityResultLauncher<Intent>,
        ) {
            if (fragmentManager.findFragmentByTag(Constants.DIGITAL_ASSISTANT_DIALOG) != null) return
            DigitalAssistantSetupDialog().apply { this.launcher = launcher }
                .show(fragmentManager, Constants.DIGITAL_ASSISTANT_DIALOG)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = AssistantSetupDialogBinding.inflate(requireActivity().layoutInflater)

        binding.dialogIcon.setImageResource(R.mipmap.ic_launcher)
        binding.dialogTitle.text = getString(R.string.digital_assistant_setup_title)
        binding.dialogMessage.text = getString(R.string.digital_assistant_setup_message)
        binding.positiveButton.text = getString(R.string.digital_assistant_setup_button)
        binding.negativeButton.text = getString(android.R.string.cancel)

        binding.positiveButton.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                (parentFragment as? PreferenceFragmentCompat)?.let {
                    DigitalAssistantPreference(it).setDigitalAssistSetupStatus(true)
                }
            }
            runCatching {
                launcher.launch(Intent(Settings.ACTION_VOICE_INPUT_SETTINGS))
            }
            dismiss()
        }

        binding.negativeButton.setOnClickListener {
            dismiss()
        }

        return MaterialAlertDialogBuilder(requireContext()).setView(binding.root).create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}