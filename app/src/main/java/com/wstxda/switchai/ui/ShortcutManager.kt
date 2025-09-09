package com.wstxda.switchai.ui

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.InsetDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toDrawable
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantSelectorActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.services.AssistantService
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.Constants

class ShortcutManager(private val context: Context) {
    private val prefs by lazy { PreferenceHelper(context) }
    private val resources by lazy { AssistantResourcesManager(context) }

    fun updateDynamicShortcuts() {
        if (ShortcutManagerCompat.isRateLimitingActive(context)) {
            return
        }

        val assistantShortcut = createShortcut(
            id = "assistant_shortcut",
            label = prefs.getString(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null)
                ?.let { resources.getAssistantName(it) },
            longLabel = null,
            iconRes = prefs.getString(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null)
                ?.let { resources.getAssistantIcon(it) },
            target = AssistantService::class.java
        )

        val selectorShortcut = createShortcut(
            id = "assistant_selector_shortcut",
            label = context.getString(R.string.assistant_label_selector),
            longLabel = context.getString(R.string.assistant_label_long_selector),
            iconRes = R.drawable.ic_select,
            target = AssistantSelectorActivity::class.java
        )

        assistantShortcut?.let {
            ShortcutManagerCompat.pushDynamicShortcut(context, it)
        }
        selectorShortcut?.let {
            ShortcutManagerCompat.pushDynamicShortcut(context, it)
        }
    }

    private fun createShortcut(
        id: String, label: String?, longLabel: String?, iconRes: Int?, target: Class<*>
    ): ShortcutInfoCompat? {
        if (label.isNullOrEmpty() || iconRes == null) return null

        val intent = Intent(context, target).apply {
            action = Intent.ACTION_VIEW
            putExtra(ShortcutManagerCompat.EXTRA_SHORTCUT_ID, id)
        }

        return ShortcutInfoCompat.Builder(context, id).setShortLabel(label)
            .setLongLabel(longLabel ?: label).setIcon(createAdaptiveIcon(iconRes)).setIntent(intent)
            .build()
    }

    private fun createAdaptiveIcon(iconRes: Int): IconCompat {
        val size = dp108
        val inset = (size * 0.54f).toInt()
        val background =
            ContextCompat.getColor(context, R.color.ic_shortcut_background).toDrawable()
        val foreground = ContextCompat.getDrawable(context, iconRes) ?: ContextCompat.getDrawable(
            context, R.drawable.ic_assistant_default
        )!!
        foreground.mutate().setTint(ContextCompat.getColor(context, R.color.ic_shortcut_icon))
        val insetDrawable = InsetDrawable(foreground, inset)
        val adaptiveIcon = AdaptiveIconDrawable(background, insetDrawable)
        val bitmap = createBitmap(size, size).apply {
            val canvas = Canvas(this)
            adaptiveIcon.setBounds(0, 0, size, size)
            adaptiveIcon.draw(canvas)
        }
        return IconCompat.createWithAdaptiveBitmap(bitmap)
    }

    private val dp108: Int
        get() = (108 * context.resources.displayMetrics.density).toInt()
}