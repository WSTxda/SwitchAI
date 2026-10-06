package com.wstxda.switchai.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

internal object PreferenceKeys {
    val Revision = longPreferencesKey("revision")
    val Assistant = stringPreferencesKey("digital_assistant_select")
    val Theme = stringPreferencesKey("select_theme")
    val Monet = booleanPreferencesKey("appearance_monet")
    val KeyColor = intPreferencesKey("appearance_key_color")
    val PaletteStyle = stringPreferencesKey("appearance_palette_style")
    val ColorSpec = stringPreferencesKey("appearance_color_spec")
    val Blur = booleanPreferencesKey("appearance_blur")
    val TopBarBlurStyle = intPreferencesKey("appearance_top_bar_blur_style")
    val Selector = booleanPreferencesKey("assistant_selector")
    val Components = stringSetPreferencesKey("selector_components")
    val VisibleAssistants = stringSetPreferencesKey("selector_manager_manual")
    val DynamicManager = booleanPreferencesKey("selector_manager_dynamic")
    val PortraitColumns = intPreferencesKey("grid_columns_portrait")
    val LandscapeColumns = intPreferencesKey("grid_columns_landscape")
    val VoiceInput = booleanPreferencesKey("voice_input")
    val PrivilegedVoiceInput = booleanPreferencesKey("voice_input_shizuku")
    val Vibration = booleanPreferencesKey("accessibility_vibration")
    val Sound = booleanPreferencesKey("accessibility_sound")
    val SetupDone = booleanPreferencesKey("is_assist_setup_done")
    val WarningDismissed = booleanPreferencesKey("is_warn_dismissed")
    val PinnedAssistants = stringPreferencesKey("pinned_assistants")
    val RecentlyUsedAssistants = stringPreferencesKey("recently_used_assistants")
    val ReorderTipDismissed = booleanPreferencesKey("reorder_tip_dismissed")
    val UpdateCheckedAt = longPreferencesKey("update_checked")
}