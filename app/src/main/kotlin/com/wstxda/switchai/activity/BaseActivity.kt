package com.wstxda.switchai.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.pm.ShortcutManagerCompat

abstract class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        intent.getStringExtra(ShortcutManagerCompat.EXTRA_SHORTCUT_ID)?.takeIf(String::isNotEmpty)
            ?.let { ShortcutManagerCompat.reportShortcutUsed(this, it) }
    }
}