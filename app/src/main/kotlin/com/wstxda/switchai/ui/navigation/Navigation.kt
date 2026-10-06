package com.wstxda.switchai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.wstxda.switchai.R
import com.wstxda.switchai.data.SettingsOverlay
import com.wstxda.switchai.data.SettingsState
import com.wstxda.switchai.ui.screens.LibrariesScreen
import com.wstxda.switchai.ui.screens.preferences.AboutScreen
import com.wstxda.switchai.ui.screens.preferences.AccessibilityScreen
import com.wstxda.switchai.ui.screens.preferences.AppearanceScreen
import com.wstxda.switchai.ui.screens.preferences.HomeScreen
import com.wstxda.switchai.ui.screens.preferences.SelectorSettingsScreen
import com.wstxda.switchai.ui.screens.preferences.ShortcutsScreen
import com.wstxda.switchai.ui.screens.preferences.VoiceInputScreen
import com.wstxda.switchai.ui.utils.requestAddAssistantWidget
import com.wstxda.switchai.ui.utils.requestAddTile
import com.wstxda.switchai.utils.openAppInfo
import com.wstxda.switchai.viewmodel.SettingsViewModel
import kotlinx.serialization.Serializable
import top.yukonga.miuix.kmp.nav.core.NavController
import top.yukonga.miuix.kmp.nav.core.NavDisplay
import top.yukonga.miuix.kmp.nav.core.NavDisplayEffects
import top.yukonga.miuix.kmp.nav.core.NavKey
import top.yukonga.miuix.kmp.nav.core.rememberNavSystemCornerRadius
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data object Appearance : Route

    @Serializable
    data object Selector : Route

    @Serializable
    data object VoiceInput : Route

    @Serializable
    data object Accessibility : Route

    @Serializable
    data object Shortcuts : Route

    @Serializable
    data object About : Route

    @Serializable
    data object Libraries : Route
}

@Composable
internal fun SettingsNavigation(
    navigator: NavController,
    state: SettingsState,
    viewModel: SettingsViewModel,
    onCheckUpdates: () -> Unit,
    checking: Boolean,
    onNavigate: (Route) -> Unit,
    onShowOverlay: (SettingsOverlay) -> Unit,
    onBack: () -> Unit,
    openUrl: (String) -> Unit,
    showMessage: (Int) -> Unit,
) {
    val context = LocalContext.current
    NavDisplay(
        navController = navigator,
        effects = NavDisplayEffects(
            enableCornerClip = true,
            cornerClipRadius = rememberNavSystemCornerRadius(),
            dimAmount = 0.5f,
            backdropColor = MiuixTheme.colorScheme.surface,
        ),
    ) {
        entry<Route.Home> {
            HomeScreen(
                state.assistant,
                state.setupDone,
                onNavigate,
                { onShowOverlay(SettingsOverlay.Setup) },
                { onShowOverlay(SettingsOverlay.Assistant) },
                { onShowOverlay(SettingsOverlay.Tutorial) },
            )
        }
        entry<Route.Appearance> {
            AppearanceScreen(
                appearance = state.appearance,
                onThemeChange = viewModel::setTheme,
                onMonetChange = viewModel::setMonet,
                onKeyColorChange = viewModel::setKeyColor,
                onPaletteStyleChange = viewModel::setPaletteStyle,
                onColorSpecChange = viewModel::setColorSpec,
                onBlurChange = viewModel::setBlur,
                onTopBarBlurStyleChange = viewModel::setTopBarBlurStyle,
                onBack = onBack,
            )
        }
        entry<Route.Selector> {
            SelectorSettingsScreen(
                state.selectorEnabled,
                state.dynamicManager,
                viewModel::setSelector,
                viewModel::setDynamicManager,
                { onShowOverlay(SettingsOverlay.Components) },
                { onShowOverlay(SettingsOverlay.Assistants) },
                { onShowOverlay(SettingsOverlay.Grid) },
                onBack,
            )
        }
        entry<Route.VoiceInput> {
            VoiceInputScreen(
                state.voiceInput,
                state.shizukuVoiceInput,
                viewModel::setVoiceInput,
                viewModel::setShizukuVoiceInput,
                { onShowOverlay(SettingsOverlay.SupportedAssistants) },
                onBack,
            )
        }
        entry<Route.Accessibility> {
            AccessibilityScreen(
                state.vibration,
                state.sound,
                viewModel::setVibration,
                viewModel::setSound,
                onBack,
            )
        }
        entry<Route.Shortcuts> {
            ShortcutsScreen(
                { context.requestAddTile(showMessage) },
                { context.requestAddAssistantWidget(showMessage) },
                onBack,
            )
        }
        entry<Route.About> {
            AboutScreen(
                checking,
                onCheckUpdates,
                { onNavigate(Route.Libraries) },
                openUrl,
                onAppInfo = {
                    context.openAppInfo { showMessage(R.string.updater_generic_error_message) }
                },
                onBack = onBack,
            )
        }
        entry<Route.Libraries> { LibrariesScreen(openUrl, onBack) }
    }
}