package com.wstxda.switchai.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.wstxda.switchai.R
import com.wstxda.switchai.services.DigitalAssistantService

class AssistantInvisibleWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray,
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle,
    ) {
        updateWidget(context, appWidgetManager, appWidgetId)
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    private fun updateWidget(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int,
    ) {
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)

        val layoutId = if (minWidth < 150) {
            R.layout.widget_assistant_invisible_small
        } else {
            R.layout.widget_assistant_invisible_default
        }

        val views = RemoteViews(context.packageName, layoutId)

        val pendingIntent = createClickPendingIntent(context, appWidgetId)
        views.setOnClickPendingIntent(R.id.widget_assistant_invisible, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun createClickPendingIntent(context: Context, appWidgetId: Int): PendingIntent {
        val intent = Intent(context, DigitalAssistantService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        return PendingIntent.getActivity(
            context,
            appWidgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}