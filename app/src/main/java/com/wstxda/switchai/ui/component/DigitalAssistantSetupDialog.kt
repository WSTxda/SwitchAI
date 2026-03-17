package com.wstxda.switchai.ui.component

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceFragmentCompat
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.DialogAssistantSetupBinding
import com.wstxda.switchai.preference.DigitalAssistantPreference
import com.wstxda.switchai.utils.Constants

class DigitalAssistantSetupDialog : BaseDialog<DialogAssistantSetupBinding>() {

    private lateinit var launcher: ActivityResultLauncher<Intent>

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

    override fun inflateBinding(inflater: LayoutInflater) =
        DialogAssistantSetupBinding.inflate(inflater)

    override fun onSetupDialog(savedInstanceState: Bundle?) {
        binding.apply {
            dialogIcon.setImageResource(R.mipmap.ic_launcher)
            dialogTitle.text = getString(R.string.digital_assistant_setup_title)
            dialogMessage.text = getString(R.string.digital_assistant_setup_message)
            positiveButton.text = getString(R.string.digital_assistant_setup_button)
            negativeButton.text = getString(android.R.string.cancel)

            positiveButton.setOnClickListener { onPositiveClicked() }
            negativeButton.setOnClickListener { dismiss() }
        }
    }

    private fun onPositiveClicked() {
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
}