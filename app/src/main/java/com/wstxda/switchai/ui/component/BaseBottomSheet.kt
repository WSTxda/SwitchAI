package com.wstxda.switchai.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<VB : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected abstract val topDivider: View
    protected abstract val bottomDivider: View
    protected abstract val titleTextView: TextView
    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    @get:StringRes
    protected abstract val titleResId: Int
    protected open val defaultExpanded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleTextView.setText(titleResId)

        setupContentFragment(savedInstanceState)
        setupScrollListener()

        if (defaultExpanded) {
            setupExpandedBehavior()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupExpandedBehavior() {
        dialog?.setOnShowListener { dialog ->
            val bottomSheetDialog =
                dialog as? com.google.android.material.bottomsheet.BottomSheetDialog
            val bottomSheet = bottomSheetDialog?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    protected open fun setupContentFragment(savedInstanceState: Bundle?) {}

    protected open fun setupScrollListener() {}

    protected fun updateDividerVisibility(canScrollUp: Boolean, canScrollDown: Boolean) {
        topDivider.isVisible = canScrollUp
        bottomDivider.isVisible = canScrollDown
    }
}