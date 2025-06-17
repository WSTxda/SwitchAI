package com.wstxda.switchai.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.ListPreference
import androidx.core.content.ContextCompat
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

    fun updatePreferenceIcon(
        preference: ListPreference?,
        assistantValue: String?,
    ) {
        if (preference == null) return

        val drawableId = getAssistantIcon(assistantValue)
        val drawable = ContextCompat.getDrawable(context, drawableId)
        preference.icon = drawable
    }

    private fun String.capitalizeWords(): String = split(" ").joinToString(" ") {
        it.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
        }
    }
}