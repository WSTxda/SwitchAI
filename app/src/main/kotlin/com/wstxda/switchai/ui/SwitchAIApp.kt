package com.wstxda.switchai.ui

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.AssistantsMap
import com.wstxda.switchai.data.ReleaseInfo
import com.wstxda.switchai.data.SettingsOverlay
import com.wstxda.switchai.data.SettingsState
import com.wstxda.switchai.service.UpdaterService
import com.wstxda.switchai.switchAI
import com.wstxda.switchai.ui.components.dialogs.AssistantChoiceDialog
import com.wstxda.switchai.ui.components.dialogs.FreeAndroidDialog
import com.wstxda.switchai.ui.components.dialogs.GridDialog
import com.wstxda.switchai.ui.components.dialogs.MultiChoiceDialog
import com.wstxda.switchai.ui.components.dialogs.SetupDialog
import com.wstxda.switchai.ui.components.dialogs.SupportedAssistantsDialog
import com.wstxda.switchai.ui.components.tutorial.TutorialSheet
import com.wstxda.switchai.ui.components.updater.UpdaterBottomSheet
import com.wstxda.switchai.ui.navigation.Route
import com.wstxda.switchai.ui.navigation.SettingsNavigation
import com.wstxda.switchai.ui.theme.SwitchAITheme
import com.wstxda.switchai.ui.utils.rememberAssistantResourcesManager
import com.wstxda.switchai.utils.openPreferenceUrl
import com.wstxda.switchai.utils.openSupportedAssistant
import com.wstxda.switchai.viewmodel.SettingsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.nav.core.rememberNavController

@Composable
fun SwitchAIApp(viewModel: SettingsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    if (!state.loaded || state.loadError) {
        SwitchAITheme(state.appearance) {
            Scaffold {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (state.loadError) Text(stringResource(R.string.settings_storage_error))
                    else CircularProgressIndicator()
                }
            }
        }
        return
    }
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val showMessage = rememberSettingsMessages(snackbar, viewModel.events)
    val scope = rememberCoroutineScope()
    var checking by remember { mutableStateOf(false) }
    var release by rememberSaveable(
        stateSaver = listSaver(
            save = { value ->
                value?.let {
                    listOf(it.title, it.version, it.changelog, it.downloadUrl, it.pageUrl)
                } ?: emptyList()
            },
            restore = { values ->
                values.takeIf { it.size == 5 }
                    ?.let { ReleaseInfo(it[0], it[1], it[2], it[3], it[4]) }
            },
        )
    ) {
        mutableStateOf<ReleaseInfo?>(null)
    }
    val onAvailable: (ReleaseInfo) -> Unit = { if (release == null) release = it }
    val onCheckUpdates: () -> Unit = {
        if (!checking) {
            checking = true
            scope.launch {
                try {
                    UpdaterService.checkForUpdates(this, context, onAvailable, showMessage).join()
                } finally {
                    checking = false
                }
            }
        }
    }
    val navigator = rememberNavController<Route>(Route.Home)
    var overlay by rememberSaveable { mutableStateOf<SettingsOverlay?>(null) }
    var warningClosed by rememberSaveable { mutableStateOf(false) }
    var returningFromSetup by rememberSaveable { mutableStateOf(false) }
    val showWarning = !state.warningDismissed && !warningClosed
    val onBack: () -> Unit = { navigator.pop() }
    val dismiss: () -> Unit = { overlay = null }
    val onNavigate: (Route) -> Unit = { route ->
        if (route !in navigator.backStack) navigator.push(route)
    }
    val openUrl: (String) -> Unit = { url ->
        context.openPreferenceUrl(url) { showMessage(R.string.updater_generic_error_message) }
    }
    val setupLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            returningFromSetup = true
            viewModel.refreshSetup()
        }
    LaunchedEffect(returningFromSetup, state.setupDone) {
        if (returningFromSetup && state.setupDone) {
            overlay = SettingsOverlay.Tutorial
            returningFromSetup = false
        }
    }
    LifecycleResumeEffect(Unit) {
        viewModel.refreshSetup()
        onPauseOrDispose {}
    }
    LaunchedEffect(Unit) {
        UpdaterService.checkForUpdatesAuto(
            this,
            context,
            context.switchAI.settings,
            onAvailable,
            onStorageError = { showMessage(R.string.settings_storage_error) },
        ).join()
    }
    SwitchAITheme(state.appearance) {
        Scaffold(snackbarHost = { SnackbarHost(snackbar) }) {
            SettingsNavigation(
                navigator = navigator,
                state = state,
                viewModel = viewModel,
                onCheckUpdates = onCheckUpdates,
                checking = checking,
                onNavigate = onNavigate,
                onShowOverlay = { overlay = it },
                onBack = onBack,
                openUrl = openUrl,
                showMessage = showMessage,
            )
            SettingsOverlays(
                state = state,
                overlay = overlay,
                onSelectAssistant = viewModel::selectAssistant,
                onSetComponents = viewModel::setSelectorComponents,
                onSetVisibleAssistants = viewModel::setVisibleAssistants,
                onSetGrid = viewModel::setGrid,
                onSetup = {
                    runCatching {
                        setupLauncher.launch(Intent(Settings.ACTION_VOICE_INPUT_SETTINGS))
                    }.onSuccess {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            viewModel.completeSetup()
                        }
                    }.onFailure { showMessage(R.string.assistant_open_error) }
                },
                onOpenAssistant = { key ->
                    context.openSupportedAssistant(key) {
                        showMessage(R.string.assistant_open_error)
                    }
                },
                onDismiss = dismiss,
            )
            FreeAndroidDialog(
                showWarning,
                onAcknowledge = viewModel::dismissWarning,
                onUrl = openUrl,
                onDismiss = { warningClosed = true },
            )
            UpdaterBottomSheet(
                release,
                !showWarning && overlay == null,
                onDismiss = { release = null },
                onUrl = openUrl,
                onMessage = showMessage,
            )
        }
    }
}

@Composable
private fun SettingsOverlays(
    state: SettingsState,
    overlay: SettingsOverlay?,
    onSelectAssistant: (String) -> Unit,
    onSetComponents: (Set<String>) -> Unit,
    onSetVisibleAssistants: (Set<String>) -> Unit,
    onSetGrid: (Int, Int) -> Unit,
    onSetup: () -> Unit,
    onOpenAssistant: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val resources = rememberAssistantResourcesManager()
    val locale = LocalConfiguration.current.locales.toLanguageTags()
    val assistantEntries = remember(resources, locale) {
        AssistantsMap.preferenceKeys.map(resources::getAssistantName)
    }
    AssistantChoiceDialog(
        show = overlay == SettingsOverlay.Assistant,
        selected = state.assistant,
        onSelect = onSelectAssistant,
        onDismiss = onDismiss,
    )
    SupportedAssistantsDialog(
        show = overlay == SettingsOverlay.SupportedAssistants,
        onOpen = onOpenAssistant,
        onDismiss = onDismiss,
    )
    MultiChoiceDialog(
        show = overlay == SettingsOverlay.Components,
        title = stringResource(R.string.pref_selector_manager_components),
        entries = stringArrayResource(R.array.selector_components_entries).toList(),
        values = stringArrayResource(R.array.selector_components_values).toList(),
        initial = state.components,
        onSave = onSetComponents,
        onDismiss = onDismiss,
    )
    MultiChoiceDialog(
        show = overlay == SettingsOverlay.Assistants,
        title = stringResource(R.string.pref_selector_manager_manual),
        entries = assistantEntries,
        values = AssistantsMap.preferenceKeys,
        initial = state.visibleAssistants,
        minimum = 2,
        assistantIcons = true,
        onSave = onSetVisibleAssistants,
        onDismiss = onDismiss,
    )
    GridDialog(
        show = overlay == SettingsOverlay.Grid,
        portraitColumns = state.portraitColumns,
        landscapeColumns = state.landscapeColumns,
        onSave = onSetGrid,
        onDismiss = onDismiss,
    )
    SetupDialog(overlay == SettingsOverlay.Setup, onSetup, onDismiss)
    TutorialSheet(overlay == SettingsOverlay.Tutorial, onDismiss)
}

@Composable
private fun rememberSettingsMessages(
    snackbar: SnackbarHostState,
    events: Flow<Int>,
): (Int) -> Unit {
    val context by rememberUpdatedState(LocalContext.current)
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(events, snackbar, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            events.collect { snackbar.showSnackbar(context.getString(it)) }
        }
    }
    return remember(snackbar, scope) {
        { res -> scope.launch { snackbar.showSnackbar(context.getString(res)) } }
    }
}