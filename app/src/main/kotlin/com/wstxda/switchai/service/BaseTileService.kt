package com.wstxda.switchai.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

abstract class BaseTileService : TileService() {

    private var listeningScope: CoroutineScope? = null

    @CallSuper
    override fun onStartListening() {
        super.onStartListening()
        listeningScope?.cancel()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        listeningScope = scope
        updateTile()

        val flows = flowsToCollect()
        if (flows.isEmpty()) return

        flows.merge().conflate().onEach { updateTile() }.launchIn(scope)
    }

    @CallSuper
    override fun onStopListening() {
        listeningScope?.cancel()
        listeningScope = null
        super.onStopListening()
    }

    @CallSuper
    override fun onDestroy() {
        listeningScope?.cancel()
        listeningScope = null
        super.onDestroy()
    }

    abstract fun updateTile()

    protected open fun flowsToCollect(): List<Flow<*>> = emptyList()

    protected fun setTileState(
        state: Int,
        label: CharSequence,
        subtitle: CharSequence? = null,
        icon: Icon? = null,
        description: CharSequence? = null,
    ) {
        if (listeningScope == null) return
        val tile = qsTile ?: return
        tile.state = state
        tile.label = label
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.subtitle = subtitle
        }
        icon?.let { tile.icon = it }
        tile.contentDescription = description
        tile.updateTile()
    }

    protected fun startActivityAndCollapse(cls: Class<*>) {
        launchActivityAndCollapse(Intent(this, cls))
    }

    @SuppressLint("StartActivityAndCollapseDeprecated")
    private fun launchActivityAndCollapse(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            @Suppress("DEPRECATION") startActivityAndCollapse(intent)
        }
    }
}