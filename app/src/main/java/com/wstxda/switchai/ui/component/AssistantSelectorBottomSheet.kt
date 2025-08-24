package com.wstxda.switchai.ui.component

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.FragmentAssistantDialogBinding
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.adapter.AssistantSelectorAdapter
import com.wstxda.switchai.utils.AssistantsMap
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.viewmodel.AssistantSelectorViewModel

class AssistantSelectorBottomSheet : BaseBottomSheet<FragmentAssistantDialogBinding>() {

    private val viewModel: AssistantSelectorViewModel by viewModels()
    private val preferenceHelper by lazy {
        PreferenceHelper(requireContext())
    }
    private lateinit var assistantSelectorAdapter: AssistantSelectorAdapter

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAssistantDialogBinding.inflate(inflater, container, false)

    override val topDivider: View get() = binding.dividerTop
    override val bottomDivider: View get() = binding.dividerBottom
    override val titleTextView: TextView get() = binding.bottomSheetTitle
    override val titleResId: Int get() = R.string.assistant_select

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupSearch()
    }

    private fun setupSearch() {
        val isSearchBarEnabled =
            preferenceHelper.getBoolean(Constants.ASSISTANT_SEARCH_BAR_PREF_KEY, true)

        binding.searchTextInputLayout.isVisible = isSearchBarEnabled

        if (isSearchBarEnabled) {
            binding.searchEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.searchAssistants(text?.toString())
            }
        }
    }

    private fun setupRecyclerView() {
        assistantSelectorAdapter = AssistantSelectorAdapter(onAssistantClicked = { assistantKey ->
            openAssistant(assistantKey)
            viewModel.updateRecentlyUsedAssistants(assistantKey)
            dismiss()
        }, onPinClicked = { assistantKey ->
            viewModel.togglePinAssistant(assistantKey)
        })

        binding.assistantsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = assistantSelectorAdapter
        }
    }

    private fun setupObservers() {
        viewModel.assistantItems.observe(viewLifecycleOwner) { items ->
            assistantSelectorAdapter.submitList(items)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.assistantLoading.isVisible = isLoading
            binding.assistantsRecyclerView.isInvisible = isLoading
        }

        viewModel.searchResultEmpty.observe(viewLifecycleOwner) { isResultEmpty ->
            if (isResultEmpty) {
                binding.searchTextInputLayout.error = getString(R.string.assistant_search_empty)
            } else {
                binding.searchTextInputLayout.error = null
            }
        }
    }

    override fun setupScrollListener() {
        binding.assistantsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lm = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val canScrollUp = lm.findFirstCompletelyVisibleItemPosition() > 0
                val canScrollDown = lm.findLastCompletelyVisibleItemPosition() < assistantSelectorAdapter.itemCount - 1
                updateDividerVisibility(canScrollUp, canScrollDown)
            }
        })
    }

    private fun openAssistant(assistantKey: String) {
        val context = this.context ?: return
        AssistantsMap.assistantActivity[assistantKey]?.let { activityClass ->
            val intent = Intent(context, activityClass).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } ?: Toast.makeText(context, R.string.assistant_open_error, Toast.LENGTH_SHORT).show()
    }
}