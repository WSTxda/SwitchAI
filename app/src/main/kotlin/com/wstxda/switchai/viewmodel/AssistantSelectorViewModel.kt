package com.wstxda.switchai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wstxda.switchai.R
import com.wstxda.switchai.data.AssistantItem
import com.wstxda.switchai.data.AssistantListItem
import com.wstxda.switchai.data.RecentAssistant
import com.wstxda.switchai.data.SelectorState
import com.wstxda.switchai.logic.PackageChecker
import com.wstxda.switchai.logic.buildAssistantCategories
import com.wstxda.switchai.logic.filterAssistants
import com.wstxda.switchai.logic.reorderPinnedAssistants
import com.wstxda.switchai.logic.reorderPinnedSection
import com.wstxda.switchai.logic.sortAssistantsByName
import com.wstxda.switchai.repository.SettingsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AssistantSelectorViewModel(
    private val settings: SettingsRepository,
    private val packageChecker: PackageChecker,
    private val applicationScope: CoroutineScope,
) : ViewModel() {

    private val _state = MutableStateFlow(SelectorState())
    val state = _state.asStateFlow()
    private var available = emptyList<AssistantItem>()
    private var sortedByName = emptyList<AssistantItem>()
    private var master = emptyList<AssistantListItem>()
    private var saved = settings.savedState.value
    private var pinned = emptyList<String>()
    private var tipDismissed = false
    private var dragOrder: List<String>? = null
    private var discoveryFinished = false
    private val locale = MutableStateFlow(packageChecker.locale)
    private val writes = Mutex()
    private var pendingWrites = 0
    private var categoryInputs: Triple<List<String>, List<RecentAssistant>, Boolean>? = null
    private var categoriesChanged = true

    init {
        if (saved.loaded && !saved.loadError) {
            packageChecker.cachedAssistants(saved.dynamicManager, saved.visibleAssistants)?.let {
                updateAvailable(it)
                discoveryFinished = true
            }
        }
        restoreSavedPins()
        rebuild()
        viewModelScope.launch {
            settings.savedState.collect { next ->
                if (next.revision < saved.revision) return@collect
                saved = next
                if (pendingWrites == 0) restoreSavedPins()
                if (next.loadError) _state.update {
                    it.copy(loadError = true, errorRes = R.string.settings_storage_error)
                }
                rebuild()
            }
        }
        viewModelScope.launch {
            combine(
                settings.savedState.filter { it.loaded && !it.loadError }
                    .map { it.dynamicManager to it.visibleAssistants }.distinctUntilChanged(),
                packageChecker.changes,
                locale,
            ) { visibility, _, _ ->
                visibility
            }.collectLatest { (dynamic, visible) -> loadAssistants(dynamic, visible) }
        }
    }

    fun refreshLocale(value: String) {
        locale.value = value
    }

    private suspend fun loadAssistants(dynamic: Boolean, visible: Set<String>) {
        try {
            val generation = packageChecker.changes.value
            val requestedLocale = locale.value
            val next = packageChecker.loadAssistants(dynamic, visible)
            val current = settings.savedState.value
            if (generation != packageChecker.changes.value || requestedLocale != locale.value || !current.loaded || current.loadError || current.dynamicManager != dynamic || current.visibleAssistants != visible) return
            updateAvailable(next)
            discoveryFinished = true
            _state.update { it.copy(loadError = false) }
            rebuild()
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            discoveryFinished = true
            _state.update {
                it.copy(
                    loading = false,
                    loadError = true,
                    errorRes = R.string.settings_storage_error,
                )
            }
        }
    }

    fun searchAssistants(query: String) {
        finishReordering()
        if (query == _state.value.query) return
        applySearch(query)
    }

    fun togglePinAssistant(key: String) {
        if (available.none { it.key == key && it.isInstalled }) return
        finishReordering()
        val shouldPin = key !in pinned
        pinned = if (shouldPin) pinned + key else pinned - key
        persist { settings.setPin(key, shouldPin) }
    }

    fun movePinnedAssistant(fromKey: String, toKey: String) {
        if (_state.value.query.isNotBlank()) return
        val order = dragOrder ?: pinned
        val from = order.indexOf(fromKey)
        val to = order.indexOf(toKey)
        if (from < 0 || to < 0 || from == to) return
        dragOrder = order.toMutableList().apply { add(to, removeAt(from)) }
        val displayOrder = reorderPinnedAssistants(pinned, dragOrder.orEmpty())
        master = reorderPinnedSection(master, displayOrder)
        categoryInputs = Triple(displayOrder, saved.recentlyUsedAssistants, tipDismissed)
        applySearch()
    }

    fun finishReordering() {
        val order = dragOrder ?: return
        dragOrder = null
        if (order == pinned) return
        pinned = reorderPinnedAssistants(pinned, order)
        persist { settings.setPinnedOrder(order) }
    }

    fun updateRecentlyUsedAssistants(key: String) {
        if (available.any { it.key == key }) persist {
            settings.recordRecent(
                key, System.currentTimeMillis()
            )
        }
    }

    fun dismissReorderTip() {
        if (tipDismissed) return
        tipDismissed = true
        persist { settings.dismissReorderTip() }
    }

    fun consumeError() {
        _state.update { it.copy(errorRes = null) }
    }

    private fun persist(write: suspend () -> Unit) {
        pendingWrites++
        rebuild()
        val committed = applicationScope.async(start = CoroutineStart.UNDISPATCHED) {
            writes.withLock {
                write()
                settings.awaitSavedSettings()
            }
        }
        viewModelScope.launch {
            try {
                val next = committed.await()
                if (next.revision >= saved.revision) saved = next
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _state.update { it.copy(errorRes = R.string.settings_storage_error) }
            } finally {
                pendingWrites--
                if (pendingWrites == 0) restoreSavedPins()
                rebuild()
            }
        }
    }

    private fun restoreSavedPins() {
        pinned = saved.pinnedAssistants
        tipDismissed = saved.reorderTipDismissed
    }

    private fun updateAvailable(next: List<AssistantItem>) {
        if (available == next) return
        available = next
        sortedByName = sortAssistantsByName(next)
        categoriesChanged = true
    }

    private fun rebuild() {
        val order = dragOrder?.let { reorderPinnedAssistants(pinned, it) } ?: pinned
        val inputs = Triple(order, saved.recentlyUsedAssistants, tipDismissed)
        val rebuildCategories = categoriesChanged || inputs != categoryInputs
        if (rebuildCategories) {
            master = buildAssistantCategories(
                available,
                sortedByName,
                saved.copy(pinnedAssistants = order, reorderTipDismissed = tipDismissed),
            )
            categoryInputs = inputs
            categoriesChanged = false
        }
        _state.update { it.copy(loading = !it.loadError && (!saved.loaded || !discoveryFinished)) }
        if (rebuildCategories) applySearch()
    }

    private fun applySearch(query: String = _state.value.query) {
        val items = filterAssistants(master, query)
        _state.update { it.copy(query = query, items = items, searchEmpty = items.isEmpty()) }
    }
}