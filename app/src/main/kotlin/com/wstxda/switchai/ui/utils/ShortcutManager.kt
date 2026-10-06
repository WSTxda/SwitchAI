package com.wstxda.switchai.ui.utils

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
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.service.AssistantService

internal fun Context.updateDynamicShortcuts(assistant: String) {
    if (ShortcutManagerCompat.isRateLimitingActive(this)) {
        return
    }

    val resources = AssistantResourcesManager(this)

    val assistantShortcut = createShortcut(
        id = Constants.ASSISTANT_SHORTCUT_ID,
        label = resources.getAssistantName(assistant),
        longLabel = null,
        iconRes = resources.getAssistantIcon(assistant),
        target = AssistantService::class.java,
    )

    val selectorShortcut = createShortcut(
        id = Constants.SELECTOR_SHORTCUT_ID,
        label = this.getString(R.string.assistant_label_selector),
        longLabel = this.getString(R.string.assistant_label_long_selector),
        iconRes = R.drawable.ic_select,
        target = AssistantSelectorActivity::class.java,
    )

    assistantShortcut?.let {
        ShortcutManagerCompat.pushDynamicShortcut(this, it)
    }
    selectorShortcut?.let {
        ShortcutManagerCompat.pushDynamicShortcut(this, it)
    }
}

private fun Context.createShortcut(
    id: String,
    label: String?,
    longLabel: String?,
    iconRes: Int?,
    target: Class<*>,
): ShortcutInfoCompat? {
    if (label.isNullOrEmpty() || iconRes == null) return null

    val intent = Intent(this, target).apply {
        action = Intent.ACTION_VIEW
        putExtra(ShortcutManagerCompat.EXTRA_SHORTCUT_ID, id)
    }

    return ShortcutInfoCompat.Builder(this, id).setShortLabel(label)
        .setLongLabel(longLabel ?: label).setIcon(createAdaptiveIcon(iconRes)).setIntent(intent)
        .build()
}

private fun Context.createAdaptiveIcon(iconRes: Int): IconCompat {
    val size = (Constants.SHORTCUT_ICON_SIZE_DP * resources.displayMetrics.density).toInt()
    val inset = (size * Constants.SHORTCUT_ICON_INSET_RATIO).toInt()
    val background = ContextCompat.getColor(this, R.color.ic_shortcut_background).toDrawable()
    val foreground = ContextCompat.getDrawable(this, iconRes) ?: ContextCompat.getDrawable(
        this,
        R.drawable.ic_assistant_default,
    )!!
    foreground.mutate().setTint(ContextCompat.getColor(this, R.color.ic_shortcut_icon))
    val insetDrawable = InsetDrawable(foreground, inset)
    val adaptiveIcon = AdaptiveIconDrawable(background, insetDrawable)
    val bitmap = createBitmap(size, size).apply {
        val canvas = Canvas(this)
        adaptiveIcon.setBounds(0, 0, size, size)
        adaptiveIcon.draw(canvas)
    }
    return IconCompat.createWithAdaptiveBitmap(bitmap)
}