package com.wstxda.switchai.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.divider.MaterialDivider
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.DialogAssistantTutorialBinding
import com.wstxda.switchai.fragment.TutorialFragment
import com.wstxda.switchai.utils.Constants

class AssistantTutorialBottomSheet : BaseBottomSheet<DialogAssistantTutorialBinding>(),
    TutorialFragment.ScrollListener {

    override val topDivider: MaterialDivider get() = binding.dividerTop
    override val bottomDivider: MaterialDivider get() = binding.dividerBottom
    override val titleTextView: TextView get() = binding.dialogTitle
    override val titleResId: Int get() = R.string.digital_assistant_tutorial_title

    companion object {
        fun show(fragmentManager: FragmentManager) {
            AssistantTutorialBottomSheet().show(fragmentManager, Constants.TUTORIAL_DIALOG)
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        DialogAssistantTutorialBinding.inflate(inflater, container, false)

    override fun setupContentFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(binding.dialogFragmentContainer.id, TutorialFragment()).commit()
        }
    }

    override fun onScrollChanged(canScrollUp: Boolean, canScrollDown: Boolean) {
        updateDividerVisibility(canScrollUp, canScrollDown)
    }
}