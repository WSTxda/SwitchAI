package com.wstxda.switchai.widget.utils

import android.annotation.SuppressLint
import android.content.Context
import com.wstxda.switchai.R

object AssistantResourcesHelper {

    @SuppressLint("DiscouragedApi")
    fun getAssistantIcon(context: Context, assistantValue: String?): Int {
        if (assistantValue.isNullOrEmpty()) return R.drawable.ic_assistant

        val resourceName = "ic_assistant_" + assistantValue.replace("_assistant", "")
        val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

        return if (resId != 0) resId else R.drawable.ic_assistant
    }

    @SuppressLint("DiscouragedApi")
    fun getAssistantName(context: Context, assistantValue: String?): String {
        if (assistantValue.isNullOrEmpty()) {
            return context.getString(R.string.app_name)
        }

        val resourceName = assistantValue.replace("_assistant", "")
        val stringResId =
            context.resources.getIdentifier(resourceName, "string", context.packageName)

        return if (stringResId != 0) {
            context.getString(stringResId)
        } else {
            resourceName.replace("_", " ").replaceFirstChar { it.uppercaseChar() }
        }
    }
}