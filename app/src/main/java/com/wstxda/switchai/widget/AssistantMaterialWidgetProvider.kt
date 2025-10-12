package com.wstxda.switchai.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantSelectorActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.services.AssistantService
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.widget.utils.WidgetAssistantListService

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
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Constants.ACTION_ASSISTANT_SELECTED) {
            val selectedAssistant =
                intent.getStringExtra(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY)
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )

            if (selectedAssistant != null && appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val preferenceHelper = PreferenceHelper(context)
                preferenceHelper.putString(
                    Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, selectedAssistant
                )

                val appWidgetManager = AppWidgetManager.getInstance(context)
                updateWidget(context, appWidgetManager, appWidgetId)
                @Suppress("DEPRECATION") appWidgetManager.notifyAppWidgetViewDataChanged(
                    appWidgetId, R.id.assistant_list
                )
            }
        }
        super.onReceive(context, intent)
    }

    private fun updateWidget(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int,
    ) {
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

        val layoutId = when {
            minWidth < 150 -> R.layout.widget_assistant_material_small
            minWidth >= 300 -> R.layout.widget_assistant_material_wide
            else -> R.layout.widget_assistant_material_default
        }

        val views = RemoteViews(context.packageName, layoutId)
        val preferenceHelper = PreferenceHelper(context)
        val assistantResourcesManager = AssistantResourcesManager(context)

        val assistantValue =
            preferenceHelper.getString(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null)
        views.setImageViewResource(
            R.id.button_assistant_icon, assistantResourcesManager.getAssistantIcon(assistantValue)
        )
        views.setTextViewText(
            R.id.button_assistant_title, assistantResourcesManager.getAssistantName(assistantValue)
        )

        setupClickIntents(context, appWidgetId, views)

        setupAssistantList(context, appWidgetManager, appWidgetId, views, layoutId, minHeight)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun setupClickIntents(context: Context, appWidgetId: Int, views: RemoteViews) {
        val assistantIntent = Intent(context, AssistantService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val assistantPendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId * 10 + 1,
            assistantIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val selectorIntent = Intent(context, AssistantSelectorActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val selectorPendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId * 10 + 2,
            selectorIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        views.setOnClickPendingIntent(R.id.button_assistant, assistantPendingIntent)
        views.setOnClickPendingIntent(R.id.button_assistant_select, selectorPendingIntent)
    }

    private fun setupAssistantList(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        views: RemoteViews,
        layoutId: Int,
        minHeight: Int
    ) {
        val isWideAndTall = layoutId == R.layout.widget_assistant_material_wide && minHeight > 100

        if (isWideAndTall) {
            views.setViewVisibility(R.id.assistant_list, View.VISIBLE)

            val serviceIntent = Intent(context, WidgetAssistantListService::class.java)
            @Suppress("DEPRECATION") views.setRemoteAdapter(R.id.assistant_list, serviceIntent)

            val clickIntent = Intent(context, AssistantMaterialWidgetProvider::class.java).apply {
                action = Constants.ACTION_ASSISTANT_SELECTED
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val clickPendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setPendingIntentTemplate(R.id.assistant_list, clickPendingIntent)
            @Suppress("DEPRECATION") appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetId, R.id.assistant_list
            )
        } else if (layoutId == R.layout.widget_assistant_material_wide) {
            views.setViewVisibility(R.id.assistant_list, View.GONE)
        }
    }
}