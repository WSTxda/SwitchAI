package com.wstxda.switchai.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Icon
import android.graphics.drawable.InsetDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantSelectorActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.services.AssistantService
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.Constants

internal class ShortcutManager(private val context: Context) {

    private val prefs by lazy { PreferenceHelper(context) }
    private val resources by lazy { AssistantResourcesManager(context) }

    fun updateShortcuts() {
        val manager = context.getSystemService(ShortcutManager::class.java) ?: return

        val shortcuts = listOfNotNull(
            createShortcut(
                id = "assistant_shortcut",
                label = prefs.getString(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null)
                    ?.let { resources.getAssistantName(it) },
                iconRes = prefs.getString(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null)
                    ?.let { resources.getAssistantIcon(it) },
                target = AssistantService::class.java
            ), createShortcut(
                id = "assistant_selector_shortcut",
                label = context.getString(R.string.assistant_label_select),
                iconRes = R.drawable.ic_select,
                target = AssistantSelectorActivity::class.java
            )
        )

        manager.dynamicShortcuts = shortcuts
    }

    private fun createShortcut(
        id: String, label: String?, iconRes: Int?, target: Class<*>
    ): ShortcutInfo? {
        if (label.isNullOrEmpty() || iconRes == null) return null

        val intent = Intent(context, target).setAction(Intent.ACTION_VIEW)
        return ShortcutInfo.Builder(context, id).setShortLabel(label).setLongLabel(label)
            .setIcon(createAdaptiveIcon(iconRes)).setIntent(intent).build()
    }

    private fun createAdaptiveIcon(iconRes: Int): Icon {
        val size = dp108
        val inset = (size * 0.50f).toInt()

        val background =
            ContextCompat.getColor(context, R.color.ic_shortcut_background).toDrawable()
        val foreground = ContextCompat.getDrawable(context, iconRes) ?: ContextCompat.getDrawable(
            context, R.drawable.ic_assistant_default
        )!!

        foreground.mutate().setTint(ContextCompat.getColor(context, R.color.ic_shortcut_foreground))

        val adaptive = AdaptiveIconDrawable(background, InsetDrawable(foreground, inset))

        val bitmap = createBitmap(size, size).apply {
            Canvas(this).apply {
                adaptive.setBounds(0, 0, size, size)
                adaptive.draw(this)
            }
        }

        return Icon.createWithAdaptiveBitmap(bitmap)
    }

    private val dp108: Int
        get() = (108 * context.resources.displayMetrics.density).toInt()
}