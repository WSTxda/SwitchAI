package com.wstxda.switchai.ui.utils

import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.content.ContextCompat
import com.wstxda.switchai.R
import com.wstxda.switchai.service.AssistantTileService

internal fun Context.requestAddTile(onMessage: (Int) -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    this.getSystemService(StatusBarManager::class.java)?.requestAddTileService(
        ComponentName(this, AssistantTileService::class.java),
        this.getString(R.string.assistant_label),
        Icon.createWithResource(this, R.drawable.ic_assistant),
        ContextCompat.getMainExecutor(this),
    ) { result ->
        when (result) {
            StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED -> onMessage(R.string.tile_added_success)

            StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED -> onMessage(R.string.tile_already_added)
        }
    }
}