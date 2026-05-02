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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDivider
import com.wstxda.switchai.R
import com.wstxda.switchai.data.AssistantItem
import com.wstxda.switchai.databinding.DialogAssistantSelectorBinding
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.adapter.AssistantSelectorAdapter
import com.wstxda.switchai.ui.adapter.AssistantSelectorRecyclerView
import com.wstxda.switchai.utils.AssistantsMap
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.viewmodel.AssistantSelectorViewModel

class AssistantSelectorBottomSheet : BaseBottomSheet<DialogAssistantSelectorBinding>() {

    private val viewModel: AssistantSelectorViewModel by viewModels()
    private val preferenceHelper by lazy { PreferenceHelper(requireContext()) }
    private lateinit var assistantSelectorAdapter: AssistantSelectorAdapter

    override val topDivider: MaterialDivider get() = binding.dividerTop
    override val bottomDivider: MaterialDivider get() = binding.dividerBottom
    override val titleTextView: TextView get() = binding.dialogTitle
    override val titleResId: Int get() = R.string.assistant_selector_title

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        DialogAssistantSelectorBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitle()
        setupSearch()
        setupRecyclerView()
        setupObservers()
        setupReorder()
    }

    private fun activeComponents(): Set<String> = preferenceHelper.getStringSet(
        Constants.SELECTOR_COMPONENTS_PREF_KEY, setOf(
            Constants.SELECTOR_COMPONENT_SEARCH_BAR,
            Constants.SELECTOR_COMPONENT_SELECTOR_TITLE,
            Constants.SELECTOR_COMPONENT_COUNTER
        )
    )

    private fun setupTitle() {
        titleTextView.isVisible = Constants.SELECTOR_COMPONENT_SELECTOR_TITLE in activeComponents()
    }

    private fun setupSearch() {
        val isSearchBarEnabled = Constants.SELECTOR_COMPONENT_SEARCH_BAR in activeComponents()
        binding.dialogSearchInput.isVisible = isSearchBarEnabled
        if (isSearchBarEnabled) {
            binding.dialogSearchEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.searchAssistants(text?.toString())
            }
        }
    }

    private fun setupRecyclerView() {
        val isLandscape =
            resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

        val columnPrefKey = if (isLandscape) Constants.GRID_COLUMNS_LAND
        else Constants.GRID_COLUMNS_PORT

        val defaultColumns = if (isLandscape) Constants.DEFAULT_GRID_COLUMNS_LAND
        else Constants.DEFAULT_GRID_COLUMNS_PORT

        val columnCount =
            preferenceHelper.getString(columnPrefKey, defaultColumns.toString())?.toIntOrNull()
                ?: defaultColumns

        val isGridMode = columnCount > 1

        assistantSelectorAdapter = AssistantSelectorAdapter(
            onAssistantClicked = { key ->
                openAssistant(key)
                viewModel.updateRecentlyUsedAssistants(key)
                dismiss()
            },
            onPinClicked = { key -> viewModel.togglePinAssistant(key) },
            onDismissTipClicked = { viewModel.dismissReorderTip() },
            isGridMode = isGridMode,
            showCounter = Constants.SELECTOR_COMPONENT_COUNTER in activeComponents(),
            columnCount = columnCount,
        )

        binding.dialogRecyclerView.layoutManager = if (isGridMode) {
            GridLayoutManager(requireContext(), columnCount).also { glm ->
                glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) =
                        if (assistantSelectorAdapter.getItemViewType(position) == Constants.VIEW_TYPE_ASSISTANT_GRID_ITEM) 1
                        else columnCount
                }
            }
        } else {
            LinearLayoutManager(requireContext())
        }
        binding.dialogRecyclerView.adapter = assistantSelectorAdapter
    }

    private fun setupObservers() {
        viewModel.assistantItems.observe(viewLifecycleOwner) { items ->
            assistantSelectorAdapter.submitList(items)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.dialogLoading.isVisible = isLoading
            binding.dialogRecyclerView.isInvisible = isLoading
        }

        viewModel.searchResultEmpty.observe(viewLifecycleOwner) { isEmpty ->
            binding.dialogSearchInput.error =
                if (isEmpty) getString(R.string.selector_search_empty) else null
        }
    }

    override fun setupScrollListener() {
        binding.dialogRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lm = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val canScrollUp = lm.findFirstCompletelyVisibleItemPosition() > 0
                val canScrollDown =
                    lm.findLastCompletelyVisibleItemPosition() < assistantSelectorAdapter.itemCount - 1
                updateDividerVisibility(canScrollUp, canScrollDown)
            }
        })
    }

    private fun setupReorder() {
        ItemTouchHelper(
            PinnedItemReorderCallback(assistantSelectorAdapter) { updatedList ->
                viewModel.updatePinnedAssistantsOrder(updatedList)
            }).attachToRecyclerView(binding.dialogRecyclerView)
    }

    private class PinnedItemReorderCallback(
        private val adapter: AssistantSelectorAdapter,
        private val onReorderFinished: (List<AssistantItem>) -> Unit,
    ) : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        0
    ) {

        override fun getDragDirs(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
        ): Int {
            val item = adapter.currentList.getOrNull(viewHolder.bindingAdapterPosition)
            return if (item is AssistantSelectorRecyclerView.AssistantSelector && item.assistantItem.isPinned) super.getDragDirs(
                recyclerView, viewHolder
            ) else 0
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            val from = viewHolder.bindingAdapterPosition
            val to = target.bindingAdapterPosition
            val targetItem = adapter.currentList.getOrNull(to)
            return when {
                from == RecyclerView.NO_POSITION || to == RecyclerView.NO_POSITION -> false
                targetItem is AssistantSelectorRecyclerView.AssistantSelector && targetItem.assistantItem.isPinned -> adapter.moveItem(
                    from, to
                ).let { true }

                else -> false
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            val newOrder =
                adapter.currentList.filterIsInstance<AssistantSelectorRecyclerView.AssistantSelector>()
                    .filter { it.assistantItem.isPinned }.map { it.assistantItem }
            onReorderFinished(newOrder)
        }
    }

    private fun openAssistant(assistantKey: String) {
        val context = this.context ?: return
        AssistantsMap.assistantActivity[assistantKey]?.let { activityClass ->
            context.startActivity(
                Intent(context, activityClass).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
        } ?: Toast.makeText(context, R.string.assistant_open_error, Toast.LENGTH_SHORT).show()
    }
}