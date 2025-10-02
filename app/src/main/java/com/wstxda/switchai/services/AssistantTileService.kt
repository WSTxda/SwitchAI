package com.wstxda.switchai.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.core.graphics.drawable.IconCompat
import com.wstxda.switchai.R
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.Constants

class AssistantTileService : TileService() {

    private val assistantResourcesManager by lazy { AssistantResourcesManager(this) }
    private val preferenceHelper by lazy { PreferenceHelper(this) }

    override fun onStartListening() {
        super.onStartListening()
        refreshTileContent()
    }

    override fun onTileAdded() {
        super.onTileAdded()
        refreshTileContent()
    }

    @SuppressLint("StartActivityAndCollapseDeprecated")
    override fun onClick() {
        val intent = Intent(this, DigitalAssistantService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            @Suppress("DEPRECATION") startActivityAndCollapse(intent)
        }
    }

    private fun refreshTileContent() {
        val tile = qsTile ?: return

        val isSelectorEnabled =
            preferenceHelper.getBoolean(Constants.ASSISTANT_SELECTOR_DIALOG_PREF_KEY, false)

        val assistantSubtitle = getString(R.string.assistant_label_open)

        if (isSelectorEnabled) {
            applyTileResources(
                icon = R.drawable.ic_assistant,
                title = getString(R.string.assistant_label),
                subtitle = getString(R.string.assistant_label_select),
            )
        } else {
            val assistantValue = preferenceHelper.getString(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null)
            val iconRes = assistantResourcesManager.getAssistantIcon(assistantValue)
            val name = assistantResourcesManager.getAssistantName(assistantValue)

            applyTileResources(
                icon = iconRes, title = name, subtitle = assistantSubtitle
            )
        }

        tile.state = if (isDefaultAssistant()) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_UNAVAILABLE
        }
        tile.updateTile()
    }

    private fun applyTileResources(icon: Int, title: String, subtitle: String) {
        val tile = qsTile ?: return
        tile.icon = IconCompat.createWithResource(this, icon).toIcon(this)

        tile.label = title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.subtitle = subtitle
        }
    }

    private fun isDefaultAssistant(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSystemService(android.app.role.RoleManager::class.java)?.isRoleHeld(android.app.role.RoleManager.ROLE_ASSISTANT) == true
        } else {
            preferenceHelper.getBoolean(Constants.IS_ASSIST_SETUP_DONE, false)
        }
}