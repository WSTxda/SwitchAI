package com.wstxda.switchai.widget.utils

import android.content.Intent
import android.widget.RemoteViewsService
import com.wstxda.switchai.widget.WidgetAssistantList

class WidgetAssistantListService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetAssistantList(this.applicationContext)
    }
}