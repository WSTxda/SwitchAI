package com.wstxda.switchai.utils

object Constants {

    // Preferences keys

    const val DIGITAL_ASSISTANT_SETUP_PREF_KEY = "digital_assistant_setup"
    const val DIGITAL_ASSISTANT_SELECT_PREF_KEY = "digital_assistant_select"
    const val ASSISTANT_SELECTOR_DIALOG_PREF_KEY = "assistant_selector_dialog"
    const val ASSISTANT_MANAGER_DIALOG_PREF_KEY = "assistant_selector_manager"
    const val ASSISTANT_TUTORIAL_PREF_KEY = "assistant_tutorial"
    const val OPEN_ASSISTANT_TILE_PREF_KEY = "open_assistant_tile"
    const val OPEN_ASSISTANT_WIDGET_PREF_KEY = "open_assistant_widget"
    const val ASSISTANT_VIBRATION_PREF_KEY = "assistant_vibration"
    const val ASSISTANT_ROOT_PREF_KEY = "assistant_root"
    const val LIBRARY_PREF_KEY = "library"
    const val THEME_PREF_KEY = "select_theme"

    // Theme values

    const val THEME_SYSTEM = "system"
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"

    // Used to hide category view with digital assistant tile preference

    const val SETTINGS_CATEGORY_SHORTCUTS = "shortcuts"

    // Dialog fragments

    const val ASSISTANT_MANAGER_DIALOG = "AssistantManagerDialog"
    const val DIGITAL_ASSISTANT_DIALOG = "DigitalAssistantSetupDialog"
    const val DIGITAL_ASSISTANT_SELECTOR_DIALOG = "AssistantSelectorBottomSheet"
    const val TUTORIAL_DIALOG = "AssistantTutorialBottomSheet"
    const val PREFERENCE_DIALOG = "PreferenceDialog"

    // Constants for preferences

    const val IS_ASSIST_SETUP_DONE = "is_assist_setup_done"
    const val PREFS_NAME = "assistant_selector_prefs"

    // AssistantSelector recyclerView

    const val VIEW_TYPE_CATEGORY_HEADER = 0
    const val VIEW_TYPE_ASSISTANT_ITEM = 1
    // Maximum number of recently used assistants
    const val CAT_MAX_RECENTLY_USED = 3
    // Category for AssistantSelectorBottomSheet
    const val CAT_PINNED_ASSISTANTS_KEY = "pinned_assistants"
    const val CAT_RECENTLY_USED_ASSISTANTS_KEY = "recently_used_assistants"

    // GitHub API releases URL

    const val GITHUB_RELEASE_URL = "https://api.github.com/repos/WSTxda/SwitchAI/releases/latest"
}