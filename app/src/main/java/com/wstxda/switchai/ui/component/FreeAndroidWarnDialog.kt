/*
 * Inspired by FreeDroidWarn by woheller69
 * https://github.com/woheller69/FreeDroidWarn
 * Redesigned dialog with Material 3 Expressive design
 */

package com.wstxda.switchai.ui.component

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.DialogFreeAndroidWarnBinding
import com.wstxda.switchai.utils.Constants

class FreeAndroidWarnDialog : BaseDialog<DialogFreeAndroidWarnBinding>() {

    companion object {
        fun show(fragmentManager: FragmentManager, context: Context) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            if (!prefs.getBoolean(Constants.IS_WARN_DISMISSED, false)) {
                FreeAndroidWarnDialog().showSafely(
                    fragmentManager, Constants.FREE_ANDROID_WARN_DIALOG
                )
            }
        }
    }

    override fun inflateBinding(inflater: LayoutInflater) =
        DialogFreeAndroidWarnBinding.inflate(inflater)

    override fun onSetupDialog(savedInstanceState: Bundle?) {
        binding.apply {
            dialogIcon.setImageResource(R.drawable.ic_warning)
            dialogTitle.text = getString(R.string.free_android_warn_title)
            dialogMessage.text = getString(R.string.free_android_warn_message)
            dialogButtonPositive.text = getString(android.R.string.ok)
            dialogButtonNegative.text = getString(R.string.free_android_warn_solution_button)

            dialogButtonPositive.setOnClickListener { onPositiveClicked() }
            dialogButtonNegative.setOnClickListener { openUrl("https://github.com/woheller69/FreeDroidWarn?tab=readme-ov-file#solutions") }
            dialogLinkMoreInfo.text = getString(R.string.free_android_warn_link)
            dialogLinkMoreInfo.setOnClickListener { openUrl("https://keepandroidopen.org") }
        }
    }

    private fun onPositiveClicked() {
        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
            putBoolean(Constants.IS_WARN_DISMISSED, true)
        }
        dismiss()
    }

    private fun openUrl(url: String) {
        runCatching { startActivity(Intent(Intent.ACTION_VIEW, url.toUri())) }
        dismiss()
    }
}