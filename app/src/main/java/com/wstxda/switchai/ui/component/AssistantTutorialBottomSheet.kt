package com.wstxda.switchai.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.FragmentTutorialDialogBinding
import com.wstxda.switchai.fragment.TutorialFragment
import com.wstxda.switchai.utils.Constants

class AssistantTutorialBottomSheet : BaseBottomSheet<FragmentTutorialDialogBinding>(),
    TutorialFragment.ScrollListener {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTutorialDialogBinding.inflate(inflater, container, false)

    companion object {
        fun show(fragmentManager: FragmentManager) {
            val tutorialSheet = AssistantTutorialBottomSheet()
            tutorialSheet.show(fragmentManager, Constants.TUTORIAL_DIALOG)
        }
    }

    override val topDivider: View get() = binding.dividerTop
    override val bottomDivider: View get() = binding.dividerBottom
    override val titleTextView: TextView get() = binding.bottomSheetTitle
    override val titleResId: Int get() = R.string.pref_assistant_tutorial
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(binding.tutorialFragmentContainer.id, TutorialFragment()).commit()
        }
    }

    override fun onScrollChanged(canScrollUp: Boolean, canScrollDown: Boolean) {
        updateDividerVisibility(canScrollUp, canScrollDown)
    }
}