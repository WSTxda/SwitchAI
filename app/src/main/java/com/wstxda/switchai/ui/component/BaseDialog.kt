package com.wstxda.switchai.ui.component

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseDialog<VB : ViewBinding> : DialogFragment() {

    companion object {
        fun DialogFragment.showSafely(
            fragmentManager: FragmentManager,
            tag: String,
        ) {
            if (fragmentManager.findFragmentByTag(tag) == null) {
                show(fragmentManager, tag)
            }
        }
    }

    private var _binding: VB? = null

    protected val binding: VB
        get() = requireNotNull(_binding)

    protected abstract fun inflateBinding(inflater: LayoutInflater): VB
    protected abstract fun onSetupDialog(savedInstanceState: Bundle?)

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = inflateBinding(requireActivity().layoutInflater)

        onSetupDialog(savedInstanceState)

        return MaterialAlertDialogBuilder(requireContext()).setView(binding.root).create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}