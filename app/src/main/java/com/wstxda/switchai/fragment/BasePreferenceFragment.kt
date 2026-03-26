package com.wstxda.switchai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.XmlRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialSharedAxis
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.FragmentPreferencesBinding

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    private var _binding: FragmentPreferencesBinding? = null
    protected val binding get() = _binding!!

    @get:XmlRes
    protected abstract val preferencesResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        val prefsContainer = binding.preferencesContainer
        prefsContainer.addView(super.onCreateView(inflater, prefsContainer, savedInstanceState))
        return binding.root
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(preferencesResId, rootKey)
    }

    override fun onCreateRecyclerView(
        inflater: LayoutInflater, parent: ViewGroup, savedInstanceState: Bundle?
    ): RecyclerView = super.onCreateRecyclerView(inflater, parent, savedInstanceState).apply {
        isVerticalScrollBarEnabled = false
        clipToPadding = false
        applySystemBarInsets(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(MaterialColors.getColor(view, android.R.attr.colorBackground))
        setupViews()
        setupToolbar()
        setupListeners()
        setupObservers()
    }

    protected open fun setupViews() {}

    protected open fun setupToolbar() {
        val collapsingToolbar = binding.collapsingToolbar
        val toolbar = binding.toolbar
        val appBar = binding.appBar

        collapsingToolbar.title = getToolbarTitle()

        if (showNavigationIcon()) {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        } else {
            toolbar.navigationIcon = null
        }

        onToolbarCreated(toolbar)

        appBar.setLiftOnScrollTargetView(listView)
    }

    protected open fun setupListeners() {}
    protected open fun setupObservers() {}

    protected open fun getToolbarTitle(): String =
        findNavController().currentDestination?.label?.toString() ?: ""

    protected open fun showNavigationIcon(): Boolean = true

    protected open fun onToolbarCreated(toolbar: MaterialToolbar) = Unit

    protected fun applySystemBarInsets(target: View) {
        ViewCompat.setOnApplyWindowInsetsListener(target) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().theme.applyStyle(R.style.AppTheme, true)
    }
}