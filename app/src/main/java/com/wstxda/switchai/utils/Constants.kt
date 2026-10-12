package com.wstxda.switchai.utils

object Constants {

    // -------------------------------------------------------------------------
    // Preferences keys — digital assistant
    // -------------------------------------------------------------------------

    const val DIGITAL_ASSISTANT_SETUP_PREF_KEY = "digital_assistant_setup"
    const val DIGITAL_ASSISTANT_SELECT_PREF_KEY = "digital_assistant_select"

    // -------------------------------------------------------------------------
    // Preferences keys — appearance
    // -------------------------------------------------------------------------

    const val THEME_PREF_KEY = "select_theme"

    // -------------------------------------------------------------------------
    // Preferences keys — selector
    // -------------------------------------------------------------------------

    const val SELECTOR_PREF_KEY = "assistant_selector"
    const val SELECTOR_COMPONENTS_PREF_KEY = "selector_components"
    const val SELECTOR_GRID_PREF_KEY = "selector_grid"
    const val SELECTOR_MANAGER_MANUAL_PREF_KEY = "selector_manager_manual"
    const val SELECTOR_MANAGER_DYNAMIC_PREF_KEY = "selector_manager_dynamic"

    // -------------------------------------------------------------------------
    // Preferences keys — voice input
    // -------------------------------------------------------------------------

    const val VOICE_INPUT_PREF_KEY = "voice_input"
    const val VOICE_INPUT_ROOT_PREF_KEY = "voice_input_root"

    // -------------------------------------------------------------------------
    // Preferences keys — accessibility
    // -------------------------------------------------------------------------

    const val ACCESSIBILITY_VIBRATION_PREF_KEY = "accessibility_vibration"
    const val ACCESSIBILITY_SOUND_PREF_KEY = "accessibility_sound"

    // -------------------------------------------------------------------------
    // Preferences keys — shortcuts
    // -------------------------------------------------------------------------

    const val SHORTCUT_TILE_PREF_KEY = "shortcut_tile"
    const val SHORTCUT_WIDGET_PREF_KEY = "shortcut_widget"

    // -------------------------------------------------------------------------
    // Preferences keys — about
    // -------------------------------------------------------------------------

    const val LIBRARY_PREF_KEY = "library"
    const val UPDATER_PREF_KEY = "updater"

    // -------------------------------------------------------------------------
    // Navigation preference keys (entries on the main screen)
    // Each key corresponds to a Preference that triggers navigation to the
    // matching sub-screen fragment via the Navigation component.
    // -------------------------------------------------------------------------

    const val NAV_SELECTOR_PREF_KEY = "nav_selector"
    const val NAV_VOICE_INPUT_PREF_KEY = "nav_voice_input"
    const val NAV_ACCESSIBILITY_PREF_KEY = "nav_accessibility"
    const val NAV_SHORTCUTS_PREF_KEY = "nav_shortcuts"
    const val NAV_APPEARANCE_PREF_KEY = "nav_appearance"
    const val NAV_ABOUT_PREF_KEY = "nav_about"

    // -------------------------------------------------------------------------
    // Theme values
    // -------------------------------------------------------------------------

    const val THEME_SYSTEM = "system"
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"

    // -------------------------------------------------------------------------
    // Assistant selector component values
    // -------------------------------------------------------------------------

    const val SELECTOR_COMPONENT_SEARCH_BAR = "selector_search_bar"
    const val SELECTOR_COMPONENT_SELECTOR_TITLE = "selector_title"
    const val SELECTOR_COMPONENT_COUNTER = "selector_counter"

    // -------------------------------------------------------------------------
    // Assistant selector RecyclerView/Adapter configurations
    // -------------------------------------------------------------------------

    const val VIEW_TYPE_CATEGORY_HEADER = 0
    const val VIEW_TYPE_ASSISTANT_ITEM = 1
    const val VIEW_TYPE_REORDER_TIP = 2
    const val VIEW_TYPE_ASSISTANT_GRID_ITEM = 3

    /** Maximum number of recently used assistants shown in the selector. */
    const val CAT_MAX_RECENTLY_USED = 3

    /** Category keys for AssistantSelectorBottomSheet. */
    const val CAT_PINNED_ASSISTANTS_KEY = "pinned_assistants"
    const val CAT_RECENTLY_USED_ASSISTANTS_KEY = "recently_used_assistants"

    /** Persistence key for the reorder tip dismissal state. */
    const val REORDER_TIP_DISMISSED_KEY = "reorder_tip_dismissed"

    // -------------------------------------------------------------------------
    // Assistant selector grid columns configurations
    // -------------------------------------------------------------------------

    const val MAX_GRID_COLUMNS = 5
    const val MAX_GRID_COLUMNS_WITH_LABEL = 3
    const val GRID_PREVIEW_ROW_COUNT = 2

    /** Default grid columns based on device orientation */
    const val DEFAULT_GRID_COLUMNS_PORT = 1
    const val DEFAULT_GRID_COLUMNS_LAND = 3

    /** Grid size based on device orientation */
    const val GRID_COLUMNS_PORT = "grid_columns_portrait"
    const val GRID_COLUMNS_LAND = "grid_columns_landscape"

    // -------------------------------------------------------------------------
    // Dialog / fragment tags
    // -------------------------------------------------------------------------

    const val ASSISTANT_MANAGER_DIALOG = "AssistantManagerDialog"
    const val DIGITAL_ASSISTANT_DIALOG = "DigitalAssistantSetupDialog"
    const val DIGITAL_ASSISTANT_SELECTOR_DIALOG = "AssistantSelectorBottomSheet"
    const val TUTORIAL_DIALOG = "AssistantTutorialBottomSheet"
    const val PREFERENCE_DIALOG = "PreferenceDialog"
    const val GRID_COLUMNS_DIALOG = "GridColumnsDialog"
    const val FREE_ANDROID_WARN_DIALOG = "FreeAndroidWarnDialog"
    const val UPDATER_DIALOG = "UpdaterBottomSheet"

    // -------------------------------------------------------------------------
    // Assistant widget
    // -------------------------------------------------------------------------

    const val ACTION_ASSISTANT_SELECTED = "com.wstxda.switchai.ACTION_ASSISTANT_SELECTED"

    // -------------------------------------------------------------------------
    // SharedPreferences
    // -------------------------------------------------------------------------

    const val IS_ASSIST_SETUP_DONE = "is_assist_setup_done"
    const val IS_WARN_DISMISSED = "is_warn_dismissed"
    const val PREFS_NAME = "assistant_selector_prefs"

    // -------------------------------------------------------------------------
    // Logs tags
    // -------------------------------------------------------------------------

    const val ROOT_CHECKER = "RootChecker"

    // -------------------------------------------------------------------------
    // Updater GitHub API
    // -------------------------------------------------------------------------

    const val GITHUB_TITLE = "title"
    const val GITHUB_VERSION = "version"
    const val GITHUB_CHANGELOG = "changelog"
    const val GITHUB_DOWNLOAD_URL = "download_url"
    const val GITHUB_PAGE_URL = "page_url"
    const val GITHUB_UPDATE_CHECKED = "update_checked"

    const val GITHUB_API_URL = "https://api.github.com/repos/WSTxda/SwitchAI/releases/latest"
    const val GITHUB_RELEASE_URL = "https://github.com/WSTxda/SwitchAI/releases/latest"
}