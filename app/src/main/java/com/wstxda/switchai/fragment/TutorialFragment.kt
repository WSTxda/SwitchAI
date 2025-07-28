package com.wstxda.switchai.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.FragmentTutorialBinding
import com.wstxda.switchai.databinding.ListItemAssistantTutorialBinding
import com.wstxda.switchai.ui.viewholder.AssistantTutorialItemViewHolder

class TutorialFragment : Fragment() {
    interface ScrollListener {
        fun onScrollChanged(canScrollUp: Boolean, canScrollDown: Boolean)
    }

    private var _binding: FragmentTutorialBinding? = null
    private val binding get() = _binding!!
    private var scrollListener: ScrollListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is ScrollListener) {
            scrollListener = parentFragment as ScrollListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTutorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateTutorials()

        binding.tutorialScrollView.setOnScrollChangeListener { v, _, _, _, _ ->
            val canScrollUp = v.canScrollVertically(-1)
            val canScrollDown = v.canScrollVertically(1)

            scrollListener?.onScrollChanged(canScrollUp, canScrollDown)
        }

        binding.tutorialScrollView.post {
            scrollListener?.onScrollChanged(
                binding.tutorialScrollView.canScrollVertically(-1),
                binding.tutorialScrollView.canScrollVertically(1)
            )
        }
    }

    private fun populateTutorials() {
        val tutorialItems = defaultTutorialItems()
        val inflater = LayoutInflater.from(requireContext())
        tutorialItems.forEach { item ->
            val itemBinding =
                ListItemAssistantTutorialBinding.inflate(inflater, binding.tutorialContainer, false)
            itemBinding.tutorialImage.setImageResource(item.imageRes)
            itemBinding.tutorialTitle.setText(item.titleRes)
            itemBinding.tutorialTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                item.iconRes, 0, 0, 0
            )
            itemBinding.tutorialSummary.setText(item.summaryRes)
            binding.tutorialContainer.addView(itemBinding.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tutorialContainer.removeAllViews()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        scrollListener = null
    }

    companion object {
        fun defaultTutorialItems() = listOf(
            AssistantTutorialItemViewHolder(
                iconRes = R.drawable.ic_corners,
                imageRes = R.drawable.tutorial_gestures_card,
                titleRes = R.string.tutorial_edge_gestures,
                summaryRes = R.string.tutorial_edge_gestures_summary
            ), AssistantTutorialItemViewHolder(
                iconRes = R.drawable.ic_home,
                imageRes = R.drawable.tutorial_button_card,
                titleRes = R.string.tutorial_home_button,
                summaryRes = R.string.tutorial_home_button_summary
            ), AssistantTutorialItemViewHolder(
                iconRes = R.drawable.ic_power,
                imageRes = R.drawable.tutorial_power_card,
                titleRes = R.string.tutorial_power_button,
                summaryRes = R.string.tutorial_power_button_summary
            )
        )
    }
}