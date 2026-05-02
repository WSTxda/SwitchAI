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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample

abstract class BaseTileService : TileService() {

    protected val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var collectionJob: Job? = null

    protected open val sampleIntervalMs: Long = 0L

    @OptIn(FlowPreview::class)
    @CallSuper
    override fun onStartListening() {
        super.onStartListening()

        updateTile()

        val flows = flowsToCollect()
        if (flows.isEmpty()) return

        collectionJob?.cancel()
        collectionJob = flows.merge()
            .let { flow -> if (sampleIntervalMs > 0L) flow.sample(sampleIntervalMs) else flow }
            .conflate().onEach { updateTile() }.launchIn(serviceScope)
    }

    @CallSuper
    override fun onStopListening() {
        collectionJob?.cancel()
        collectionJob = null
        super.onStopListening()
    }

    @CallSuper
    override fun onDestroy() {
        serviceScope.cancel()
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