package com.wstxda.switchai.service

import android.service.quicksettings.Tile
import androidx.core.graphics.drawable.IconCompat
import com.wstxda.switchai.R
import com.wstxda.switchai.logic.DigitalAssistantChecker
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.Constants

class AssistantTileService : BaseTileService() {

    private val assistantResourcesManager by lazy { AssistantResourcesManager(applicationContext) }
    private val preferenceHelper by lazy { PreferenceHelper(applicationContext) }

    override fun onClick() {
        startActivityAndCollapse(DigitalAssistantService::class.java)
    }

    override fun updateTile() {
        if (!DigitalAssistantChecker.isSetupDone(this)) {
            setTileState(
                state = Tile.STATE_UNAVAILABLE, label = getString(R.string.assistant_label)
            )
            return
        }

        val isSelectorEnabled = preferenceHelper.getBoolean(Constants.SELECTOR_PREF_KEY, false)
        val assistantValue =
            preferenceHelper.getString(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null)

        val tileState = if (isSelectorEnabled || assistantValue != null) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }

        if (isSelectorEnabled) {
            setTileState(
                state = tileState,
                label = getString(R.string.assistant_label),
                subtitle = getString(R.string.assistant_label_select),
                icon = IconCompat.createWithResource(this, R.drawable.ic_assistant).toIcon(this)
            )
        } else {
            val iconRes = assistantResourcesManager.getAssistantIcon(assistantValue)
            val name = assistantResourcesManager.getAssistantName(assistantValue)

            setTileState(
                state = tileState,
                label = name,
                subtitle = getString(R.string.assistant_label_open),
                icon = IconCompat.createWithResource(this, iconRes).toIcon(this)
            )
        }
    }
}