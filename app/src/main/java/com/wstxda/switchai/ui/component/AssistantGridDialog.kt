package com.wstxda.switchai.ui.component

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import androidx.core.content.edit
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.google.android.material.chip.Chip
import com.google.android.material.color.MaterialColors
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.DialogAssistantGridBinding
import com.wstxda.switchai.utils.Constants

class AssistantGridDialog : BaseDialog<DialogAssistantGridBinding>() {

    private var portraitColumns = Constants.DEFAULT_GRID_COLUMNS_PORT
    private var landscapeColumns = Constants.DEFAULT_GRID_COLUMNS_LAND
    private var isLandscapeSelected = false
    private var isProgrammaticChipUpdate = false

    companion object {
        fun show(fragmentManager: FragmentManager) {
            if (fragmentManager.findFragmentByTag(Constants.GRID_COLUMNS_DIALOG) != null) return
            AssistantGridDialog().show(fragmentManager, Constants.GRID_COLUMNS_DIALOG)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater) =
        DialogAssistantGridBinding.inflate(inflater)

    override fun onSetupDialog(savedInstanceState: Bundle?) {
        initUI()
        loadSettings()
        setupOrientationToggle()
        setupColumnChips()
        refreshPreview()

        binding.positiveButton.setOnClickListener { saveAndDismiss() }
        binding.negativeButton.setOnClickListener { dismiss() }
    }

    private fun initUI() {
        binding.apply {
            dialogIcon.setImageResource(R.drawable.ic_grid)
            dialogTitle.setText(R.string.selector_grid_title)
            positiveButton.setText(android.R.string.ok)
            negativeButton.setText(android.R.string.cancel)
            buttonPortrait.isChecked = true
        }
    }

    private fun loadSettings() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        portraitColumns = prefs.getString(
            Constants.GRID_COLUMNS_PORT, Constants.DEFAULT_GRID_COLUMNS_PORT.toString()
        )?.toIntOrNull() ?: Constants.DEFAULT_GRID_COLUMNS_PORT

        landscapeColumns = prefs.getString(
            Constants.GRID_COLUMNS_LAND, Constants.DEFAULT_GRID_COLUMNS_LAND.toString()
        )?.toIntOrNull() ?: Constants.DEFAULT_GRID_COLUMNS_LAND
    }

    private fun setupOrientationToggle() {
        binding.orientationToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            isLandscapeSelected = (checkedId == R.id.button_landscape)
            syncChipSelection()
            refreshPreview()
        }
    }

    @SuppressLint("UseGetLayoutInflater")
    private fun setupColumnChips() {
        val inflater = LayoutInflater.from(requireContext())

        for (count in 1..Constants.MAX_GRID_COLUMNS) {
            val chip = inflater.inflate(R.layout.chip_grid, binding.columnChipGroup, false) as Chip
            chip.apply {
                id = View.generateViewId()
                text = count.toString()
            }
            binding.columnChipGroup.addView(chip)
        }

        syncChipSelection()

        binding.columnChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (isProgrammaticChipUpdate) return@setOnCheckedStateChangeListener
            val checkedId = checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            val selectedCount = group.findViewById<Chip>(checkedId)?.text?.toString()?.toIntOrNull()
                ?: return@setOnCheckedStateChangeListener

            if (isLandscapeSelected) landscapeColumns = selectedCount
            else portraitColumns = selectedCount
            refreshPreview()
        }
    }

    private fun syncChipSelection() {
        val targetCount = if (isLandscapeSelected) landscapeColumns else portraitColumns
        isProgrammaticChipUpdate = true
        for (i in 0 until binding.columnChipGroup.childCount) {
            val chip = binding.columnChipGroup.getChildAt(i) as? Chip ?: continue
            chip.isChecked = (chip.text.toString().toIntOrNull() == targetCount)
        }
        isProgrammaticChipUpdate = false
    }

    private fun refreshPreview() {
        val columnCount = if (isLandscapeSelected) landscapeColumns else portraitColumns
        binding.previewGrid.apply {
            removeAllViews()
            this.columnCount = columnCount
        }

        val dm = resources.displayMetrics
        val cellHeight = (54 * dm.density).toInt()
        val cellMargin = (4 * dm.density).toInt()
        val cornerRadius = 8 * dm.density
        val bgColor = MaterialColors.getColor(
            binding.root, com.google.android.material.R.attr.colorSurfaceBright
        )

        repeat(Constants.GRID_PREVIEW_ROW_COUNT * columnCount) {
            val cell = View(requireContext()).apply {
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    this.cornerRadius = cornerRadius
                    setColor(bgColor)
                }
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = cellHeight
                    setMargins(cellMargin, cellMargin, cellMargin, cellMargin)
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
                }
            }
            binding.previewGrid.addView(cell)
        }
    }

    private fun saveAndDismiss() {
        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
            putString(Constants.GRID_COLUMNS_PORT, portraitColumns.toString())
            putString(Constants.GRID_COLUMNS_LAND, landscapeColumns.toString())
        }
        dismiss()
    }
}