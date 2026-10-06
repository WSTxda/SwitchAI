package com.wstxda.switchai.constants

import androidx.compose.ui.graphics.Color
import com.wstxda.switchai.data.ThemeMode
import com.wstxda.switchai.data.TopBarBlurStyle
import top.yukonga.miuix.kmp.theme.ThemeColorSpec
import top.yukonga.miuix.kmp.theme.ThemePaletteStyle

object Constants {
    // Preference defaults and storage
    const val DATASTORE_FILE_NAME = "switchai_settings"
    const val DEFAULT_GRID_COLUMNS_PORT = 1
    const val DEFAULT_GRID_COLUMNS_LAND = 3
    const val DEFAULT_ASSISTANT = "chatgpt_assistant"
    const val DEFAULT_SELECTOR_ENABLED = true
    const val DEFAULT_DYNAMIC_MANAGER = true
    const val DEFAULT_VOICE_INPUT = true
    const val DEFAULT_SHIZUKU_VOICE_INPUT = false
    const val DEFAULT_VIBRATION = true
    const val DEFAULT_SOUND = true
    const val DEFAULT_SETUP_DONE = false
    const val DEFAULT_WARNING_DISMISSED = false
    const val DEFAULT_REORDER_TIP_DISMISSED = false
    val DEFAULT_THEME = ThemeMode.System
    const val DEFAULT_MONET = false
    const val DEFAULT_KEY_COLOR = 0
    val DEFAULT_PALETTE_STYLE = ThemePaletteStyle.TonalSpot
    val DEFAULT_COLOR_SPEC = ThemeColorSpec.Spec2021
    const val DEFAULT_BLUR = true
    val DEFAULT_TOP_BAR_BLUR_STYLE = TopBarBlurStyle.Gaussian

    // Appearance values and demo seed colors
    val THEME_VALUES = ThemeMode.entries.map { it.storedValue }.toSet()
    val PALETTE_VALUES = ThemePaletteStyle.entries.map { it.name }.toSet()
    val COLOR_SPEC_VALUES = ThemeColorSpec.entries.map { it.name }.toSet()
    val KEY_COLORS =
        listOf(
            Color(0xFF3482FF),
            Color(0xFF36D167),
            Color(0xFF7C4DFF),
            Color(0xFFFFB21D),
            Color(0xFFFF5722),
            Color(0xFFE91E63),
            Color(0xFF00BCD4),
        )

    // Selector configuration
    const val SELECTOR_COMPONENT_SEARCH_BAR = "selector_search_bar"
    const val SELECTOR_COMPONENT_SELECTOR_TITLE = "selector_title"
    const val SELECTOR_COMPONENT_COUNTER = "selector_counter"
    const val CAT_MAX_RECENTLY_USED = 3
    const val MAX_GRID_COLUMNS = 5
    const val MAX_GRID_COLUMNS_WITH_LABEL = 3
    const val GRID_PREVIEW_ROW_COUNT = 2

    // Android shortcuts and widgets
    const val WIDGET_RECEIVER_TIMEOUT_MILLIS = 8_000L
    const val ASSISTANT_SHORTCUT_ID = "assistant_shortcut"
    const val SELECTOR_SHORTCUT_ID = "assistant_selector_shortcut"
    const val SHORTCUT_ICON_SIZE_DP = 108
    const val SHORTCUT_ICON_INSET_RATIO = 0.54f

    // Shizuku GSL contracts, adapted to the SwitchAI package.
    const val EXTRA_SHIZUKU_TARGET_COMPONENT = "shizuku_target_component"
    const val EXTRA_SHIZUKU_ERROR_MESSAGE = "shizuku_error_message"
    const val STATE_SHIZUKU_SETUP_SHOWN = "shizuku_setup_shown"
    const val STATE_SHIZUKU_LAUNCH_STARTED = "shizuku_launch_started"
    const val STATE_SHIZUKU_PERMISSION_INVALIDATED = "shizuku_permission_invalidated"
    const val SHIZUKU_MANAGER_PACKAGE = "moe.shizuku.privileged.api"
    const val SHIZUKU_PERMISSION_REQUEST_CODE = 1
    const val SHIZUKU_SERVICE_PROCESS = "shizuku"
    const val SHIZUKU_SERVICE_TAG = "switchai_shizuku"
    const val SHIZUKU_SERVICE_VERSION = 1
    const val SHIZUKU_DOWNLOAD_URL = "https://shizuku.rikka.app/download/"

    // GitHub Updater (Views policy)
    const val UPDATE_INTERVAL_MILLIS = 43_200_000L
    const val RELEASE_CONNECT_TIMEOUT_MILLIS = 5_000
    const val RELEASE_READ_TIMEOUT_MILLIS = 10_000
    const val GITHUB_API_URL = "https://api.github.com/repos/WSTxda/SwitchAI/releases/latest"
    const val GITHUB_RELEASE_URL = "https://github.com/WSTxda/SwitchAI/releases/latest"

    // Preference links

    const val DEVELOPER_URL = "https://github.com/WSTxda"
    const val SOURCE_URL = "https://github.com/WSTxda/SwitchAI"
    const val LICENSE_URL = "https://github.com/WSTxda/SwitchAI/blob/main/LICENSE"
    const val KEEP_ANDROID_OPEN_URL = "https://keepandroidopen.org"
    const val ANDROID_OPEN_SOLUTIONS_URL = "https://github.com/woheller69/FreeDroidWarn?tab=readme-ov-file#solutions"
}