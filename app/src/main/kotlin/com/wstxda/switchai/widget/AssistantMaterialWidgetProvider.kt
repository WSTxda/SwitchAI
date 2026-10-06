package com.wstxda.switchai.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantSelectorActivity
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.service.AssistantService
import com.wstxda.switchai.switchAI
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class AssistantMaterialWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        updateAsync(context, appWidgetManager, appWidgetIds)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle,
    ) {
        updateAsync(context, appWidgetManager, intArrayOf(appWidgetId))
    }

    private fun updateAsync(context: Context, manager: AppWidgetManager, requested: IntArray) {
        val pending = goAsync()
        context.switchAI.applicationScope.launch {
            try {
                withTimeout(Constants.WIDGET_RECEIVER_TIMEOUT_MILLIS.milliseconds) {
                    val owned = manager.getAppWidgetIds(
                        ComponentName(
                            context,
                            AssistantMaterialWidgetProvider::class.java,
                        )
                    ).toSet()
                    val ids = requested.distinct().filter { it > 0 && it in owned }
                    if (ids.isEmpty()) return@withTimeout
                    val settings = context.switchAI.settings.awaitSavedSettings()
                    ids.forEach { updateWidget(context, manager, it, settings.assistant) }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                Log.e("SwitchAI", "Widget update failed", error)
            } finally {
                pending.finish()
            }
        }
    }

    private fun updateWidget(
        context: Context,
        manager: AppWidgetManager,
        widgetId: Int,
        assistant: String,
    ) {
        val width = manager.getAppWidgetOptions(widgetId)
            .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val layout = when {
            width < 150 -> R.layout.widget_assistant_material_small
            width >= 300 -> R.layout.widget_assistant_material_wide
            else -> R.layout.widget_assistant_material_default
        }
        val resources = AssistantResourcesManager(context)
        val views = RemoteViews(context.packageName, layout).apply {
            setImageViewResource(R.id.button_assistant_icon, resources.getAssistantIcon(assistant))
            setTextViewText(R.id.button_assistant_title, resources.getAssistantName(assistant))
            setOnClickPendingIntent(
                R.id.button_assistant,
                clickIntent(context, widgetId, AssistantService::class.java, 1),
            )
            setOnClickPendingIntent(
                R.id.button_assistant_select,
                clickIntent(context, widgetId, AssistantSelectorActivity::class.java, 2),
            )
        }
        manager.updateAppWidget(widgetId, views)
    }

    private fun clickIntent(
        context: Context,
        widgetId: Int,
        target: Class<*>,
        actionId: Int,
    ): PendingIntent = PendingIntent.getActivity(
        context,
        widgetId * 10 + actionId,
        Intent(context, target).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}

internal fun Context.requestMaterialWidgetUpdates() {
    val appWidgetManager = AppWidgetManager.getInstance(this)
    val componentName = ComponentName(this, AssistantMaterialWidgetProvider::class.java)
    val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
    if (appWidgetIds.isNotEmpty()) {
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            component = componentName
        }
        sendBroadcast(intent)
    }
}