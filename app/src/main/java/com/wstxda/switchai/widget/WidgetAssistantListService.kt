package com.wstxda.switchai.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.wstxda.switchai.R
import com.wstxda.switchai.logic.PackageChecker
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.runBlocking

class WidgetAssistantListService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetAssistantListFactory(this.applicationContext)
    }
}

class WidgetAssistantListFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var assistants: List<String> = emptyList()
    private lateinit var packageChecker: PackageChecker
    private lateinit var assistantResourcesManager: AssistantResourcesManager

    override fun onCreate() {
        packageChecker = PackageChecker(context)
        assistantResourcesManager = AssistantResourcesManager(context)
    }

    override fun onDataSetChanged() {
        runBlocking {
            assistants = packageChecker.installedAssistants().toList()
        }
    }

    override fun onDestroy() {
        // Do nothing for now :)
    }

    override fun getCount(): Int {
        return assistants.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val assistant = assistants[position]
        val assistantName = assistantResourcesManager.getAssistantName(assistant)
        val assistantIcon = assistantResourcesManager.getAssistantIcon(assistant)

        val fillInIntent = Intent().apply {
            putExtra(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, assistant)
        }

        return RemoteViews(context.packageName, R.layout.widget_list_item_assistant).apply {
            setTextViewText(R.id.assistant_name, assistantName)
            setImageViewResource(R.id.assistant_icon, assistantIcon)
            setOnClickFillInIntent(R.id.assistant_item_container, fillInIntent)
        }
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}