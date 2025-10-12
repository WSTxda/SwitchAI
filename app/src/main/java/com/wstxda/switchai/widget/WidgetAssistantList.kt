package com.wstxda.switchai.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.wstxda.switchai.R
import com.wstxda.switchai.logic.PackageChecker
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking

class WidgetAssistantList(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    @Volatile
    private var assistants: List<String> = emptyList()
    private val packageChecker: PackageChecker by lazy { PackageChecker(context) }
    private val assistantResourcesManager: AssistantResourcesManager by lazy {
        AssistantResourcesManager(
            context
        )
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        runBlocking {
            fetchAssistants()
        }
    }

    private suspend fun fetchAssistants() {
        assistants = packageChecker.installedAssistants().toList()
    }

    override fun onDestroy() {
        scope.cancel()
    }

    override fun getCount(): Int = assistants.size

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

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}