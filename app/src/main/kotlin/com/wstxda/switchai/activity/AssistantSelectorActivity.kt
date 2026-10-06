package com.wstxda.switchai.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wstxda.switchai.R
import com.wstxda.switchai.logic.createEntryIntent
import com.wstxda.switchai.switchAI
import com.wstxda.switchai.ui.components.selector.AssistantSelectorBottomSheet
import com.wstxda.switchai.ui.theme.SwitchAITheme
import com.wstxda.switchai.viewmodel.AssistantSelectorViewModel
import com.wstxda.switchai.viewmodel.SettingsViewModel

class AssistantSelectorActivity : BaseActivity() {

    private val settings: SettingsViewModel by viewModels { switchAI.settingsViewModelFactory }
    private val selector: AssistantSelectorViewModel by viewModels {
        viewModelFactory {
            initializer {
                AssistantSelectorViewModel(
                    switchAI.settings,
                    switchAI.packageChecker,
                    switchAI.applicationScope,
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by settings.savedState.collectAsStateWithLifecycle()
            val selectorState by selector.state.collectAsStateWithLifecycle()
            val locale = LocalConfiguration.current.locales.toLanguageTags()
            LaunchedEffect(locale) { selector.refreshLocale(locale) }
            LaunchedEffect(selectorState.errorRes) {
                selectorState.errorRes?.let {
                    Toast.makeText(this@AssistantSelectorActivity, it, Toast.LENGTH_SHORT).show()
                    selector.consumeError()
                }
            }
            var show by rememberSaveable { mutableStateOf(true) }
            SwitchAITheme(state.appearance) {
                AssistantSelectorBottomSheet(
                    show = show,
                    settings = state,
                    state = selectorState,
                    onSearch = selector::searchAssistants,
                    onPin = selector::togglePinAssistant,
                    onMovePinned = selector::movePinnedAssistant,
                    onReorderFinished = selector::finishReordering,
                    onDismissTip = selector::dismissReorderTip,
                    onAssistant = { key ->
                        val entryIntent = createEntryIntent(key)
                        if (entryIntent != null) {
                            selector.finishReordering()
                            selector.updateRecentlyUsedAssistants(key)
                            startActivity(entryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            show = false
                        } else Toast.makeText(
                            this,
                            R.string.assistant_open_error,
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    onDismiss = {
                        selector.finishReordering()
                        show = false
                    },
                    onDismissFinished = { if (!show) finish() },
                )
            }
        }
    }

    override fun onStop() {
        selector.finishReordering()
        super.onStop()
    }
}