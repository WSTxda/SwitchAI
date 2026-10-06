package com.wstxda.switchai.service

import android.service.quicksettings.Tile
import androidx.core.graphics.drawable.IconCompat
import com.wstxda.switchai.R
import com.wstxda.switchai.switchAI
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class AssistantTileService : BaseTileService() {

    private val settings by lazy { switchAI.settings.state }
    private val assistantResources by lazy { AssistantResourcesManager(applicationContext) }

    override fun onStartListening() {
        switchAI.settings.refreshRole()
        super.onStartListening()
    }

    override fun onClick() = startActivityAndCollapse(DigitalAssistantService::class.java)

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        settings.distinctUntilChanged { previous, next ->
            previous.loaded == next.loaded && previous.loadError == next.loadError && previous.setupDone == next.setupDone && previous.assistant == next.assistant && previous.selectorEnabled == next.selectorEnabled
        })

    override fun updateTile() {
        val saved = settings.value
        if (!saved.loaded || saved.loadError || !saved.setupDone) {
            setTileState(Tile.STATE_UNAVAILABLE, getString(R.string.assistant_label))
            return
        }
        val selector = saved.selectorEnabled
        val label =
            if (selector) getString(R.string.assistant_label) else assistantResources.getAssistantName(
                saved.assistant
            )
        val icon =
            if (selector) R.drawable.ic_assistant else assistantResources.getAssistantIcon(saved.assistant)
        val subtitle = if (selector) getString(R.string.assistant_label_select)
        else getString(R.string.assistant_label_open)
        setTileState(
            state = Tile.STATE_ACTIVE,
            label = label,
            subtitle = subtitle,
            icon = IconCompat.createWithResource(this, icon).toIcon(this),
            description = label,
        )
    }
}