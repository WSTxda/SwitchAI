package com.wstxda.switchai.ui

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.wstxda.switchai.R
import com.wstxda.switchai.widget.AssistantMaterialWidgetProvider

class WidgetManager(private val context: Context) {

    fun requestAddAssistantWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetProvider = ComponentName(context, AssistantMaterialWidgetProvider::class.java)

        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            appWidgetManager.requestPinAppWidget(widgetProvider, null, null)
        } else {
            showSnackBar(
                message = context.getString(R.string.widget_launcher_not_supported)
            )
        }
    }

    private fun showSnackBar(message: String) {
        (context as? Activity)?.findViewById<View>(android.R.id.content)?.let { rootView ->
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}