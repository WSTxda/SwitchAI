package com.wstxda.switchai.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.wstxda.switchai.R
import java.util.Locale

class AssistantResourcesManager(private val context: Context) {

    @SuppressLint("DiscouragedApi")
    fun getAssistantIcon(assistantValue: String?): Int {
        if (assistantValue.isNullOrEmpty()) return R.drawable.ic_assistant_default

        val resourceName = "ic_assistant_" + assistantValue.replace("_assistant", "")
        val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

        return if (resId != 0) resId else R.drawable.ic_assistant_default
    }

    @SuppressLint("DiscouragedApi")
    fun getAssistantName(assistantValue: String?): String {
        if (assistantValue.isNullOrEmpty()) {
            return context.getString(R.string.app_name)
        }

        val resourceName = assistantValue.replace("_assistant", "")
        val stringResId =
            context.resources.getIdentifier(resourceName, "string", context.packageName)

        return if (stringResId != 0) {
            context.getString(stringResId)
        } else {
            resourceName.replace("_", " ").capitalizeWords()
        }
    }

    private fun String.capitalizeWords(): String = split(" ").joinToString(" ") {
        it.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
        }
    }
}

@Composable
internal fun rememberAssistantResourcesManager(): AssistantResourcesManager {
    val context = LocalContext.current
    return remember(context) { AssistantResourcesManager(context) }
}