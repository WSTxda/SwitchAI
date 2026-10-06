package com.wstxda.switchai.ui.utils

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import com.wstxda.switchai.R
import com.wstxda.switchai.widget.AssistantMaterialWidgetProvider

internal fun Context.requestAddAssistantWidget(onMessage: (Int) -> Unit) {
    val manager = AppWidgetManager.getInstance(this)
    if (manager.isRequestPinAppWidgetSupported) {
        manager.requestPinAppWidget(
            ComponentName(this, AssistantMaterialWidgetProvider::class.java),
            null,
            null,
        )
    } else {
        onMessage(R.string.widget_launcher_not_supported)
    }
}