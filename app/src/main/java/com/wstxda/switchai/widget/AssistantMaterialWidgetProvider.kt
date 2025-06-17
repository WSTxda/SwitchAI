package com.wstxda.switchai.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.preference.PreferenceManager
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantSelectorActivity
import com.wstxda.switchai.services.AssistantWidgetService
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.utils.AssistantResourcesManager

class AssistantMaterialWidgetProvider : AppWidgetProvider() {

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
    }

    private fun updateWidget(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int,
    ) {
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)

        val layoutId = when {
            minWidth < 150 -> R.layout.widget_assistant_material_small
            minWidth >= 300 -> R.layout.widget_assistant_material_wide
            else -> R.layout.widget_assistant_material_default
        }

        val views = RemoteViews(context.packageName, layoutId)

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val assistantValue = prefs.getString(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null)
        val assistantResourcesManager = AssistantResourcesManager(context)
        val assistantIconRes = assistantResourcesManager.getAssistantIcon(assistantValue)
        val assistantName = assistantResourcesManager.getAssistantName(assistantValue)

        views.setImageViewResource(R.id.button_assistant_icon, assistantIconRes)
        views.setTextViewText(R.id.button_assistant_title, assistantName)

        val assistantIntent = Intent(context, AssistantWidgetService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val assistantPendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId + 200000,
            assistantIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val assistantSelectorIntent = Intent(context, AssistantSelectorActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val assistantSelectorPendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId + 210000,
            assistantSelectorIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        views.setOnClickPendingIntent(R.id.button_assistant, assistantPendingIntent)
        views.setOnClickPendingIntent(R.id.button_assistant_select, assistantSelectorPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}