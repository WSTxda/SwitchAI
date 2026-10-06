package com.wstxda.switchai.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import com.wstxda.switchai.activity.MainActivity

class WidgetConfigureActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val result = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, result)
        if (intent?.action != AppWidgetManager.ACTION_APPWIDGET_CONFIGURE || appWidgetId <= 0) {
            finish()
            return
        }
        val manager = AppWidgetManager.getInstance(this)
        val provider = manager.getAppWidgetInfo(appWidgetId)?.provider
        val allowed = setOf(
            ComponentName(this, AssistantMaterialWidgetProvider::class.java),
            ComponentName(this, AssistantInvisibleWidgetProvider::class.java),
        )
        if (provider !in allowed || appWidgetId !in manager.getAppWidgetIds(provider)) {
            finish()
            return
        }
        sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
            component = provider
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
        })
        setResult(RESULT_OK, result)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}