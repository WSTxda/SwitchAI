package com.wstxda.switchai.repository

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.AssistantsMap
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.constants.PreferenceKeys
import com.wstxda.switchai.data.AppearanceSettings
import com.wstxda.switchai.data.RecentAssistant
import com.wstxda.switchai.data.SettingsState
import com.wstxda.switchai.data.ThemeMode
import com.wstxda.switchai.data.TopBarBlurStyle
import com.wstxda.switchai.logic.isAssistantSetupDone
import com.wstxda.switchai.logic.reorderPinnedAssistants
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import top.yukonga.miuix.kmp.theme.ThemeColorSpec
import top.yukonga.miuix.kmp.theme.ThemePaletteStyle

private val Context.switchAISettings by preferencesDataStore(
    name = Constants.DATASTORE_FILE_NAME,
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)

class SettingsRepository(context: Context, private val scope: CoroutineScope) {
    private val app = context.applicationContext
    private val store = app.switchAISettings
    private val assistantKeys = AssistantsMap.assistantActivity.keys
    private val defaultComponents = app.resources.getStringArray(R.array.selector_components_default_values).toSet()
    private val allowedComponents = app.resources.getStringArray(R.array.selector_components_values).toSet()
    private val defaults = SettingsState(
        components = defaultComponents,
        visibleAssistants = assistantKeys.toSet(),
    )

    internal val savedState = store.data.map(::decode).catch { error ->
        if (error is IOException) emit(defaults.copy(loaded = true, loadError = true))
        else throw error
    }.stateIn(scope, SharingStarted.Eagerly, defaults)

    private val roleRefresh = MutableStateFlow(0L)
    private var roleInputs: Pair<Boolean, Long>? = null
    private var roleSetupDone = defaults.setupDone
    val state = combine(savedState, roleRefresh) { saved, refresh ->
        if (!saved.loaded || saved.loadError) {
            roleInputs = null
            saved
        } else {
            val inputs = saved.setupDone to refresh
            if (inputs != roleInputs) {
                roleSetupDone = app.isAssistantSetupDone(saved.setupDone)
                roleInputs = inputs
            }
            saved.copy(setupDone = roleSetupDone)
        }
    }.stateIn(scope, SharingStarted.Eagerly, defaults)

    fun refreshRole() {
        roleRefresh.update { it + 1 }
    }

    suspend fun selectAssistant(key: String) = persist { setString(PreferenceKeys.Assistant, key) }

    suspend fun setTheme(value: ThemeMode) = persist {
        setString(PreferenceKeys.Theme, value.storedValue)
    }

    suspend fun setMonet(enabled: Boolean) = persist { setBoolean(PreferenceKeys.Monet, enabled) }

    suspend fun setKeyColor(index: Int) = persist { setInt(PreferenceKeys.KeyColor, index) }

    suspend fun setPaletteStyle(value: ThemePaletteStyle) = persist {
        setString(PreferenceKeys.PaletteStyle, value.name)
    }

    suspend fun setColorSpec(value: ThemeColorSpec) = persist {
        setString(PreferenceKeys.ColorSpec, value.name)
    }

    suspend fun setBlur(enabled: Boolean) = persist { setBoolean(PreferenceKeys.Blur, enabled) }

    suspend fun setTopBarBlurStyle(value: TopBarBlurStyle) = persist {
        setInt(PreferenceKeys.TopBarBlurStyle, value.value)
    }

    suspend fun setSelector(enabled: Boolean) = persist {
        setBoolean(PreferenceKeys.Selector, enabled)
    }

    suspend fun setDynamicManager(enabled: Boolean) = persist {
        setBoolean(PreferenceKeys.DynamicManager, enabled)
    }

    suspend fun setVoiceInput(enabled: Boolean) = persist {
        setBoolean(PreferenceKeys.VoiceInput, enabled)
    }

    suspend fun setShizukuVoiceInput(enabled: Boolean) = persist {
        setBoolean(PreferenceKeys.PrivilegedVoiceInput, enabled)
    }

    suspend fun setVibration(enabled: Boolean) = persist {
        setBoolean(PreferenceKeys.Vibration, enabled)
    }

    suspend fun setSound(enabled: Boolean) = persist { setBoolean(PreferenceKeys.Sound, enabled) }

    suspend fun setSelectorComponents(values: Set<String>) = persist {
        setStringSet(PreferenceKeys.Components, values)
    }

    suspend fun setVisibleAssistants(keys: Set<String>) = persist {
        setStringSet(PreferenceKeys.VisibleAssistants, keys)
    }

    suspend fun setGrid(portrait: Int, landscape: Int) = persist { writeGrid(portrait, landscape) }

    suspend fun completeSetup() = persist { setBoolean(PreferenceKeys.SetupDone, true) }

    suspend fun dismissWarning() = persist { setBoolean(PreferenceKeys.WarningDismissed, true) }

    private suspend fun persist(write: suspend () -> Unit) = scope.async { write() }.await()

    internal suspend fun dismissReorderTip() = setBoolean(PreferenceKeys.ReorderTipDismissed, true)

    suspend fun awaitSavedSettings(): SettingsState = decode(store.data.first())

    private suspend fun setBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        updatePreferences { it[key] = value }
    }

    private suspend fun setString(key: Preferences.Key<String>, value: String) {
        val allowed = when (key) {
            PreferenceKeys.Assistant -> assistantKeys
            PreferenceKeys.Theme -> Constants.THEME_VALUES
            PreferenceKeys.PaletteStyle -> Constants.PALETTE_VALUES
            PreferenceKeys.ColorSpec -> Constants.COLOR_SPEC_VALUES
            else -> error("Unsupported text preference")
        }
        require(value in allowed)
        updatePreferences { it[key] = value }
    }

    private suspend fun setInt(key: Preferences.Key<Int>, value: Int) {
        val range = when (key) {
            PreferenceKeys.KeyColor -> 0..Constants.KEY_COLORS.size
            PreferenceKeys.TopBarBlurStyle -> TopBarBlurStyle.entries.indices
            else -> error("Unsupported number preference")
        }
        updatePreferences { it[key] = value.coerceIn(range) }
    }

    private suspend fun setStringSet(key: Preferences.Key<Set<String>>, value: Set<String>) {
        val allowed = when (key) {
            PreferenceKeys.Components -> allowedComponents
            PreferenceKeys.VisibleAssistants -> assistantKeys
            else -> error("Unsupported set preference")
        }
        require(allowed.containsAll(value))
        updatePreferences { it[key] = value.toSet() }
    }

    private suspend fun writeGrid(portrait: Int, landscape: Int) {
        updatePreferences {
            it[PreferenceKeys.PortraitColumns] = portrait.coerceIn(1, Constants.MAX_GRID_COLUMNS)
            it[PreferenceKeys.LandscapeColumns] = landscape.coerceIn(1, Constants.MAX_GRID_COLUMNS)
        }
    }

    internal suspend fun setPin(key: String, pinned: Boolean) {
        require(key in assistantKeys)
        updatePreferences {
            val order = pinned(it).toMutableList()
            if (pinned && key !in order) order.add(key)
            if (!pinned) order.remove(key)
            it[PreferenceKeys.PinnedAssistants] = Json.encodeToString(order)
        }
    }

    internal suspend fun setPinnedOrder(keys: List<String>) {
        require(assistantKeys.containsAll(keys))
        updatePreferences {
            val order = reorderPinnedAssistants(pinned(it), keys)
            it[PreferenceKeys.PinnedAssistants] = Json.encodeToString(order)
        }
    }

    internal suspend fun recordRecent(key: String, timestamp: Long) {
        require(key in assistantKeys && timestamp >= 0)
        updatePreferences {
            val recent = listOf(
                RecentAssistant(
                    key, timestamp
                )
            ) + recent(it).filterNot { item -> item.key == key }
            it[PreferenceKeys.RecentlyUsedAssistants] =
                Json.encodeToString(recent.take(Constants.CAT_MAX_RECENTLY_USED))
        }
    }

    internal suspend fun claimAutomaticUpdate(now: Long): Boolean {
        var claimed = false
        updatePreferences {
            val previous = it[PreferenceKeys.UpdateCheckedAt] ?: 0L
            if (now - previous > Constants.UPDATE_INTERVAL_MILLIS) {
                it[PreferenceKeys.UpdateCheckedAt] = now
                claimed = true
            }
        }
        return claimed
    }

    private fun decode(preferences: Preferences): SettingsState = with(preferences) {
        defaults.copy(
            loaded = true,
            revision = (this[PreferenceKeys.Revision] ?: 0L).coerceAtLeast(0),
            appearance = AppearanceSettings(
                theme = ThemeMode.entries.find { it.storedValue == this[PreferenceKeys.Theme] }
                    ?: defaults.appearance.theme,
                monet = this[PreferenceKeys.Monet] ?: defaults.appearance.monet,
                keyColor = (this[PreferenceKeys.KeyColor] ?: defaults.appearance.keyColor).coerceIn(
                    0, Constants.KEY_COLORS.size
                ),
                paletteStyle = ThemePaletteStyle.entries.find {
                    it.name == this[PreferenceKeys.PaletteStyle]
                } ?: defaults.appearance.paletteStyle,
                colorSpec = ThemeColorSpec.entries.find {
                    it.name == this[PreferenceKeys.ColorSpec]
                } ?: defaults.appearance.colorSpec,
                blur = this[PreferenceKeys.Blur] ?: defaults.appearance.blur,
                topBarBlurStyle = TopBarBlurStyle.entries[(this[PreferenceKeys.TopBarBlurStyle]
                    ?: defaults.appearance.topBarBlurStyle.value).coerceIn(
                    0, TopBarBlurStyle.entries.lastIndex
                )],
            ),
            assistant = this[PreferenceKeys.Assistant]?.takeIf { it in assistantKeys }
                ?: defaults.assistant,
            selectorEnabled = this[PreferenceKeys.Selector] ?: defaults.selectorEnabled,
            dynamicManager = this[PreferenceKeys.DynamicManager] ?: defaults.dynamicManager,
            components = this[PreferenceKeys.Components]?.intersect(allowedComponents)
                ?: defaults.components,
            visibleAssistants = this[PreferenceKeys.VisibleAssistants]?.intersect(assistantKeys)
                ?: defaults.visibleAssistants,
            portraitColumns = (this[PreferenceKeys.PortraitColumns]
                ?: defaults.portraitColumns).coerceIn(
                1,
                Constants.MAX_GRID_COLUMNS,
            ),
            landscapeColumns = (this[PreferenceKeys.LandscapeColumns]
                ?: defaults.landscapeColumns).coerceIn(
                1,
                Constants.MAX_GRID_COLUMNS,
            ),
            voiceInput = this[PreferenceKeys.VoiceInput] ?: defaults.voiceInput,
            shizukuVoiceInput = this[PreferenceKeys.PrivilegedVoiceInput]
                ?: defaults.shizukuVoiceInput,
            vibration = this[PreferenceKeys.Vibration] ?: defaults.vibration,
            sound = this[PreferenceKeys.Sound] ?: defaults.sound,
            setupDone = this[PreferenceKeys.SetupDone] ?: defaults.setupDone,
            warningDismissed = this[PreferenceKeys.WarningDismissed] ?: defaults.warningDismissed,
            pinnedAssistants = pinned(this),
            recentlyUsedAssistants = recent(this),
            reorderTipDismissed = this[PreferenceKeys.ReorderTipDismissed]
                ?: defaults.reorderTipDismissed,
        )
    }

    private suspend fun updatePreferences(write: (MutablePreferences) -> Unit) {
        store.edit { preferences ->
            val before = preferences.asMap().toMap()
            write(preferences)
            if (preferences.asMap() != before) {
                val revision = (preferences[PreferenceKeys.Revision] ?: 0L).coerceAtLeast(0)
                preferences[PreferenceKeys.Revision] =
                    if (revision == Long.MAX_VALUE) revision else revision + 1
            }
        }
    }

    private fun pinned(preferences: Preferences): List<String> =
        decodeList<String>(preferences[PreferenceKeys.PinnedAssistants]).filter { it in assistantKeys }
            .distinct()

    private fun recent(preferences: Preferences): List<RecentAssistant> =
        decodeList<RecentAssistant>(preferences[PreferenceKeys.RecentlyUsedAssistants]).filter { it.key in assistantKeys && it.lastUsedTime >= 0 }
            .distinctBy { it.key }.take(Constants.CAT_MAX_RECENTLY_USED)

    private inline fun <reified T> decodeList(encoded: String?): List<T> =
        if (encoded == null) emptyList()
        else try {
            Json.decodeFromString<List<T>>(encoded)
        } catch (_: SerializationException) {
            emptyList()
        } catch (_: IllegalArgumentException) {
            emptyList()
        }
}