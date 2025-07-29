package com.wstxda.switchai.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wstxda.switchai.ui.utils.TutorialItemList
import com.wstxda.switchai.databinding.FragmentTutorialBinding
import com.wstxda.switchai.ui.adapter.TutorialItemViewBuilder

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
            scrollListener?.onScrollChanged(
                v.canScrollVertically(-1), v.canScrollVertically(1)
            )
        }

        binding.tutorialScrollView.post {
            scrollListener?.onScrollChanged(
                binding.tutorialScrollView.canScrollVertically(-1),
                binding.tutorialScrollView.canScrollVertically(1)
            )
        }
    }

    private fun populateTutorials() {
        val items = TutorialItemList.getTutorialItems()
        items.forEach { item ->
            val view = TutorialItemViewBuilder.build(requireContext(), item)
            binding.tutorialContainer.addView(view)
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
}