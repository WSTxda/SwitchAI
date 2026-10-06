package com.wstxda.switchai.data

import com.wstxda.switchai.constants.Constants

data class SettingsState(
    val loaded: Boolean = false,
    val loadError: Boolean = false,
    val revision: Long = 0,
    val appearance: AppearanceSettings = AppearanceSettings(),
    val assistant: String = Constants.DEFAULT_ASSISTANT,
    val selectorEnabled: Boolean = Constants.DEFAULT_SELECTOR_ENABLED,
    val dynamicManager: Boolean = Constants.DEFAULT_DYNAMIC_MANAGER,
    val components: Set<String> = emptySet(),
    val visibleAssistants: Set<String> = emptySet(),
    val portraitColumns: Int = Constants.DEFAULT_GRID_COLUMNS_PORT,
    val landscapeColumns: Int = Constants.DEFAULT_GRID_COLUMNS_LAND,
    val voiceInput: Boolean = Constants.DEFAULT_VOICE_INPUT,
    val shizukuVoiceInput: Boolean = Constants.DEFAULT_SHIZUKU_VOICE_INPUT,
    val vibration: Boolean = Constants.DEFAULT_VIBRATION,
    val sound: Boolean = Constants.DEFAULT_SOUND,
    val setupDone: Boolean = Constants.DEFAULT_SETUP_DONE,
    val warningDismissed: Boolean = Constants.DEFAULT_WARNING_DISMISSED,
    val pinnedAssistants: List<String> = emptyList(),
    val recentlyUsedAssistants: List<RecentAssistant> = emptyList(),
    val reorderTipDismissed: Boolean = Constants.DEFAULT_REORDER_TIP_DISMISSED,
)

internal enum class SettingsOverlay {
    Setup, Assistant, Components, Assistants, Grid, Tutorial, SupportedAssistants,
}