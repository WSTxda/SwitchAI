package com.wstxda.switchai.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.wstxda.switchai.R
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.adapter.AssistantSelectorRecyclerView
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.utils.AssistantsMap
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.edit
import com.wstxda.switchai.data.AssistantItem
import com.wstxda.switchai.ui.utils.AssistantResourcesManager

class AssistantSelectorViewModel(application: Application) : AndroidViewModel(application),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val _assistantItems = MutableLiveData<List<AssistantSelectorRecyclerView>>()
    val assistantItems: LiveData<List<AssistantSelectorRecyclerView>> = _assistantItems

    private val pinnedAssistantKeys = mutableSetOf<String>()
    private val recentlyUsedAssistants = mutableListOf<Pair<String, Long>>()

    private val assistantStatePreferences by lazy {
        getApplication<Application>().getSharedPreferences(
            Constants.PREFS_NAME, Application.MODE_PRIVATE
        )
    }

    private var preferenceHelper: PreferenceHelper =
        PreferenceHelper(application.applicationContext)

    private var defaultSharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application.applicationContext)

    private val assistantResourcesManager: AssistantResourcesManager =
        AssistantResourcesManager(application.applicationContext)

    init {
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val loadedPinnedKeys =
            assistantStatePreferences.getStringSet(Constants.CAT_PINNED_ASSISTANTS_KEY, emptySet())
                ?: emptySet()
        pinnedAssistantKeys.clear()
        pinnedAssistantKeys.addAll(loadedPinnedKeys)

        val recentJsonString =
            assistantStatePreferences.getString(Constants.CAT_RECENTLY_USED_ASSISTANTS_KEY, null)
        if (recentJsonString != null) {
            try {
                val loadedRecentAssistants =
                    parseJsonStringToRecentlyUsedAssistants(recentJsonString)
                recentlyUsedAssistants.clear()
                recentlyUsedAssistants.addAll(loadedRecentAssistants)
            } catch (_: Exception) {
                assistantStatePreferences.edit { remove(Constants.CAT_RECENTLY_USED_ASSISTANTS_KEY) }
            }
        }

        loadAssistants()
    }

    fun togglePinAssistant(assistantKey: String) {
        if (pinnedAssistantKeys.contains(assistantKey)) {
            pinnedAssistantKeys.remove(assistantKey)
        } else {
            pinnedAssistantKeys.add(assistantKey)
        }
        assistantStatePreferences.edit {
            putStringSet(Constants.CAT_PINNED_ASSISTANTS_KEY, pinnedAssistantKeys)
        }
        loadAssistants()
    }

    fun assistantLaunched(assistantKey: String) {
        recentlyUsedAssistants.removeAll { it.first == assistantKey }
        recentlyUsedAssistants.add(0, Pair(assistantKey, System.currentTimeMillis()))
        while (recentlyUsedAssistants.size > Constants.CAT_MAX_RECENTLY_USED) {
            recentlyUsedAssistants.removeAt(recentlyUsedAssistants.size - 1)
        }
        val jsonString = serializeRecentlyUsedAssistantsToJson(recentlyUsedAssistants)
        assistantStatePreferences.edit {
            putString(
                Constants.CAT_RECENTLY_USED_ASSISTANTS_KEY, jsonString
            )
        }
        loadAssistants()
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

    private fun parseJsonStringToRecentlyUsedAssistants(jsonString: String): List<Pair<String, Long>> {
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

    private fun loadAssistants() {
        val context = getApplication<Application>().applicationContext
        val resources = context.resources

        val assistantsMap = AssistantsMap.assistants

        val defaultVisibleAssistants =
            resources.getStringArray(R.array.assistant_visibility_values).toSet()
        val visibleAssistantKeys = preferenceHelper.getStringSet(
            Constants.ASSISTANT_MANAGER_DIALOG_PREF_KEY, defaultVisibleAssistants
        )

        val allVisibleAssistantDetails =
            assistantsMap.filterKeys { it in visibleAssistantKeys }.map { (key, _) ->
                val name = assistantResourcesManager.getAssistantName(key)
                val finalIconResId = assistantResourcesManager.getAssistantIcon(key)

                AssistantItem(
                    key = key,
                    name = name,
                    iconRes = finalIconResId,
                    isPinned = false,
                    lastUsedTimestamp = 0L
                )
            }

        val finalRecyclerViewItems = mutableListOf<AssistantSelectorRecyclerView>()

        val pinnedItems = mutableListOf<AssistantSelectorRecyclerView.AssistantSelector>()
        allVisibleAssistantDetails.filter { it.key in pinnedAssistantKeys }.forEach { item ->
            pinnedItems.add(AssistantSelectorRecyclerView.AssistantSelector(item.copy(isPinned = true)))
        }
        if (pinnedItems.isNotEmpty()) {
            finalRecyclerViewItems.add(
                AssistantSelectorRecyclerView.CategoryHeader(
                    context.getString(R.string.assistant_category_pin)
                )
            )
            finalRecyclerViewItems.addAll(pinnedItems)
        }

        val recentItems = mutableListOf<AssistantSelectorRecyclerView.AssistantSelector>()
        recentlyUsedAssistants.forEach { (key, timestamp) ->
            if (visibleAssistantKeys.contains(key) && !pinnedAssistantKeys.contains(key)) {
                allVisibleAssistantDetails.find { it.key == key }?.let { item ->
                    recentItems.add(
                        AssistantSelectorRecyclerView.AssistantSelector(
                            item.copy(lastUsedTimestamp = timestamp)
                        )
                    )
                }
            }
        }
        if (recentItems.isNotEmpty()) {
            finalRecyclerViewItems.add(
                AssistantSelectorRecyclerView.CategoryHeader(
                    context.getString(R.string.assistant_category_recent)
                )
            )
            finalRecyclerViewItems.addAll(recentItems)
        }

        val otherItems = mutableListOf<AssistantSelectorRecyclerView.AssistantSelector>()
        allVisibleAssistantDetails.forEach { item ->
            val isPinned = pinnedAssistantKeys.contains(item.key)
            val isRecent = recentlyUsedAssistants.any { it.first == item.key }
            if (!isPinned && !isRecent) {
                otherItems.add(AssistantSelectorRecyclerView.AssistantSelector(item.copy(isPinned = false)))
            }
        }

        if (otherItems.isNotEmpty()) {
            finalRecyclerViewItems.add(
                AssistantSelectorRecyclerView.CategoryHeader(
                    context.getString(R.string.assistant_category_all)
                )
            )
            finalRecyclerViewItems.addAll(otherItems)
        }

        _assistantItems.value = finalRecyclerViewItems
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == Constants.ASSISTANT_MANAGER_DIALOG_PREF_KEY) {
            loadAssistants()
        }
    }

    override fun onCleared() {
        super.onCleared()
        defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}