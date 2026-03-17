package com.wstxda.switchai.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.wstxda.switchai.R
import com.wstxda.switchai.data.AssistantItem
import com.wstxda.switchai.logic.PackageChecker
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.adapter.AssistantSelectorRecyclerView
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.AssistantsMap
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class AssistantSelectorViewModel(application: Application) : AndroidViewModel(application),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val _assistantItems = MutableLiveData<List<AssistantSelectorRecyclerView>>()
    val assistantItems: LiveData<List<AssistantSelectorRecyclerView>> = _assistantItems

    private var allAssistantItems: List<AssistantSelectorRecyclerView> = emptyList()

    private val _searchResultEmpty = MutableLiveData<Boolean>()
    val searchResultEmpty: LiveData<Boolean> = _searchResultEmpty

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDynamicModeEnabled = MutableLiveData<Boolean>()
    val isDynamicModeEnabled: LiveData<Boolean> = _isDynamicModeEnabled

    private val pinnedAssistantKeys = mutableListOf<String>()
    private val recentlyUsedAssistants = mutableListOf<Pair<String, Long>>()

    private val assistantStatePreferences by lazy {
        getApplication<Application>().getSharedPreferences(
            Constants.PREFS_NAME, Application.MODE_PRIVATE
        )
    }

    private val preferenceHelper: PreferenceHelper =
        PreferenceHelper(application.applicationContext)

    private val defaultSharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application.applicationContext)

    private val assistantResourcesManager: AssistantResourcesManager =
        AssistantResourcesManager(application.applicationContext)

    private val packageChecker: PackageChecker = PackageChecker(application.applicationContext)

    private val packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isDynamicMode) {
                loadAssistants()
            }
        }
    }

    private val isDynamicMode: Boolean
        get() = defaultSharedPreferences.getBoolean(
            Constants.SELECTOR_MANAGER_DYNAMIC_PREF_KEY, false
        )

    init {
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this)
        _isDynamicModeEnabled.value = isDynamicMode

        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getApplication<Application>().registerReceiver(
                packageChangeReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            getApplication<Application>().registerReceiver(packageChangeReceiver, intentFilter)
        }

        loadAssistants()
    }

    private fun loadAssistants() {
        viewModelScope.launch {
            _isLoading.value = true
            val installedKeys = withContext(Dispatchers.IO) {
                loadStateFromPreferences()
                packageChecker.installedAssistants()
            }
            val allVisibleAssistantDetails = getVisibleAssistantDetails(installedKeys)
            val categorizedItems = buildCategorizedList(allVisibleAssistantDetails)
            allAssistantItems = categorizedItems
            _assistantItems.value = categorizedItems
            _isLoading.value = false
        }
    }

    fun searchAssistants(query: String?) {
        if (query.isNullOrBlank()) {
            _assistantItems.value = allAssistantItems
            _searchResultEmpty.value = false
            return
        }

        val filteredList =
            allAssistantItems.filterIsInstance<AssistantSelectorRecyclerView.AssistantSelector>()
                .filter { assistant ->
                    assistant.assistantItem.name.contains(query, ignoreCase = true)
                }

        _assistantItems.value = filteredList
        _searchResultEmpty.value = filteredList.isEmpty()
    }

    fun togglePinAssistant(assistantKey: String) {
        viewModelScope.launch {
            val currentAssistantItems =
                _assistantItems.value?.filterIsInstance<AssistantSelectorRecyclerView.AssistantSelector>()
                    ?.map { it.assistantItem } ?: return@launch

            val isCurrentlyPinned = pinnedAssistantKeys.contains(assistantKey)

            val updatedAssistantItems = currentAssistantItems.map { item ->
                if (item.key == assistantKey) {
                    item.copy(isPinned = !isCurrentlyPinned)
                } else {
                    item
                }
            }

            withContext(Dispatchers.IO) {
                if (isCurrentlyPinned) {
                    pinnedAssistantKeys.remove(assistantKey)
                } else {
                    pinnedAssistantKeys.add(assistantKey)
                }
                savePinnedAssistantsOrder()
            }

            val newCategorizedList = buildCategorizedList(updatedAssistantItems)
            allAssistantItems = newCategorizedList
            _assistantItems.value = newCategorizedList
        }
    }

    fun updatePinnedAssistantsOrder(newOrder: List<AssistantItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            pinnedAssistantKeys.clear()
            pinnedAssistantKeys.addAll(newOrder.map { it.key })
            savePinnedAssistantsOrder()
        }
    }

    fun updateRecentlyUsedAssistants(assistantKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            recentlyUsedAssistants.removeAll { it.first == assistantKey }
            recentlyUsedAssistants.add(0, Pair(assistantKey, System.currentTimeMillis()))
            while (recentlyUsedAssistants.size > Constants.CAT_MAX_RECENTLY_USED) {
                recentlyUsedAssistants.removeAt(recentlyUsedAssistants.size - 1)
            }
            val jsonString = serializeRecentlyUsedAssistantsToJson(recentlyUsedAssistants)
            assistantStatePreferences.edit {
                putString(Constants.CAT_RECENTLY_USED_ASSISTANTS_KEY, jsonString)
            }
        }
    }

    private fun savePinnedAssistantsOrder() {
        assistantStatePreferences.edit {
            putString(Constants.CAT_PINNED_ASSISTANTS_KEY, pinnedAssistantKeys.joinToString(","))
        }
    }

    private fun loadStateFromPreferences() {
        val pinnedAssistantsValue =
            assistantStatePreferences.all[Constants.CAT_PINNED_ASSISTANTS_KEY]

        when (pinnedAssistantsValue) {
            is Set<*> -> {
                pinnedAssistantKeys.clear()
                pinnedAssistantKeys.addAll(pinnedAssistantsValue.mapNotNull { it as? String })
                savePinnedAssistantsOrder()
            }

            is String -> {
                pinnedAssistantKeys.clear()
                pinnedAssistantKeys.addAll(
                    pinnedAssistantsValue.split(',').filter { it.isNotEmpty() })
            }

            else -> {
                pinnedAssistantKeys.clear()
            }
        }

        val recentJsonString = assistantStatePreferences.getString(
            Constants.CAT_RECENTLY_USED_ASSISTANTS_KEY, null
        )
        if (recentJsonString != null) {
            try {
                val loadedRecentAssistants = parseRecentlyUsedAssistantsFromJson(recentJsonString)
                recentlyUsedAssistants.clear()
                recentlyUsedAssistants.addAll(loadedRecentAssistants)
            } catch (_: Exception) {
                assistantStatePreferences.edit { remove(Constants.CAT_RECENTLY_USED_ASSISTANTS_KEY) }
            }
        }
    }

    private fun getVisibleAssistantDetails(installedKeys: Set<String>): List<AssistantItem> {
        val context = getApplication<Application>().applicationContext

        val keysToShow: Set<String> = if (isDynamicMode) {
            installedKeys
        } else {
            val defaultVisibleAssistants =
                context.resources.getStringArray(R.array.assistant_values).toSet()
            preferenceHelper.getStringSet(
                Constants.SELECTOR_MANAGER_MANUAL_PREF_KEY, defaultVisibleAssistants
            )
        }

        return AssistantsMap.assistantActivity.filterKeys { it in keysToShow }.map { (key, _) ->
            AssistantItem(
                key = key,
                name = assistantResourcesManager.getAssistantName(key),
                iconRes = assistantResourcesManager.getAssistantIcon(key),
                isInstalled = key in installedKeys,
                isPinned = key in pinnedAssistantKeys,
                lastUsedTime = recentlyUsedAssistants.find { it.first == key }?.second ?: 0L
            )
        }
    }

    private fun buildCategorizedList(assistants: List<AssistantItem>): List<AssistantSelectorRecyclerView> {
        val context = getApplication<Application>().applicationContext
        val (installedAssistants, notInstalledAssistants) = assistants.partition { it.isInstalled }
        val nameComparator =
            compareBy(String.CASE_INSENSITIVE_ORDER) { it: AssistantItem -> it.name }

        return buildList {
            val pinnedAssistants = installedAssistants.filter { it.isPinned }
            val unpinnedItems = installedAssistants.filterNot { it.isPinned }

            val pinnedItems =
                pinnedAssistantKeys.mapNotNull { key -> pinnedAssistants.find { it.key == key } }
                    .map { AssistantSelectorRecyclerView.AssistantSelector(it) }

            val recentKeys = recentlyUsedAssistants.map { it.first }.toSet()
            val (recentItems, otherItems) = unpinnedItems.partition { it.key in recentKeys }

            if (pinnedItems.isNotEmpty()) {
                add(
                    AssistantSelectorRecyclerView.CategoryHeader(
                        title = context.getString(R.string.selector_category_pin),
                        count = pinnedItems.size
                    )
                )
                val tipDismissed = assistantStatePreferences.getBoolean(
                    Constants.REORDER_TIP_DISMISSED_KEY, false
                )
                if (pinnedItems.size >= 2 && !tipDismissed) {
                    add(AssistantSelectorRecyclerView.ReorderTip)
                }
                addAll(pinnedItems)
            }
            if (recentItems.isNotEmpty()) {
                add(
                    AssistantSelectorRecyclerView.CategoryHeader(
                        title = context.getString(R.string.selector_category_recent),
                        count = recentItems.size
                    )
                )
                val sortedRecent = recentItems.sortedByDescending { it.lastUsedTime }
                    .map { AssistantSelectorRecyclerView.AssistantSelector(it) }
                addAll(sortedRecent)
            }
            if (otherItems.isNotEmpty()) {
                add(
                    AssistantSelectorRecyclerView.CategoryHeader(
                        title = context.getString(R.string.selector_category_all),
                        count = otherItems.size
                    )
                )
                val sortedOthers = otherItems.sortedWith(nameComparator)
                    .map { AssistantSelectorRecyclerView.AssistantSelector(it) }
                addAll(sortedOthers)
            }
            if (notInstalledAssistants.isNotEmpty()) {
                add(
                    AssistantSelectorRecyclerView.CategoryHeader(
                        title = context.getString(R.string.selector_category_not_installed),
                        count = notInstalledAssistants.size
                    )
                )
                val sortedNotInstalled = notInstalledAssistants.sortedWith(nameComparator)
                    .map { AssistantSelectorRecyclerView.AssistantSelector(it) }
                addAll(sortedNotInstalled)
            }
        }
    }

    private fun serializeRecentlyUsedAssistantsToJson(list: List<Pair<String, Long>>): String {
        val jsonArray = JSONArray()
        list.forEach { pair ->
            val jsonObject = JSONObject()
            jsonObject.put("key", pair.first)
            jsonObject.put("ts", pair.second)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    private fun parseRecentlyUsedAssistantsFromJson(jsonString: String): List<Pair<String, Long>> {
        val list = mutableListOf<Pair<String, Long>>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val key = jsonObject.getString("key")
            val ts = jsonObject.getLong("ts")
            list.add(Pair(key, ts))
        }
        return list
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            Constants.SELECTOR_MANAGER_MANUAL_PREF_KEY -> loadAssistants()
            Constants.SELECTOR_MANAGER_DYNAMIC_PREF_KEY -> {
                _isDynamicModeEnabled.value = isDynamicMode
                loadAssistants()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        getApplication<Application>().unregisterReceiver(packageChangeReceiver)
    }

    fun dismissReorderTip() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                assistantStatePreferences.edit {
                    putBoolean(Constants.REORDER_TIP_DISMISSED_KEY, true)
                }
            }
            val currentItems = _assistantItems.value ?: return@launch
            val updatedItems =
                currentItems.filterNot { it is AssistantSelectorRecyclerView.ReorderTip }
            allAssistantItems = updatedItems
            _assistantItems.value = updatedItems
        }
    }
}