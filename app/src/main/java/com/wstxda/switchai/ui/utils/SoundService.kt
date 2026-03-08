package com.wstxda.switchai.ui.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.wstxda.switchai.R
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.utils.Constants
import androidx.core.net.toUri

object SoundService {

    fun Context.openAssistantSound() {
        if (!canPlaySound()) return
        performSound()
    }

    private fun Context.performSound() {
        val usage = AudioAttributes.USAGE_ASSISTANT

        val attrs = AudioAttributes.Builder().setUsage(usage)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

        MediaPlayer().apply {
            setAudioAttributes(attrs)
            setDataSource(
                this@performSound, "android.resource://${packageName}/${R.raw.open_sound}".toUri()
            )
            setOnPreparedListener { it.start() }
            setOnCompletionListener { it.release() }
            setOnErrorListener { mp, _, _ -> mp.release(); true }
            prepareAsync()
        }
    }

    private fun Context.canPlaySound(): Boolean {
        return PreferenceHelper(this).getBoolean(Constants.ASSISTANT_SOUND_PREF_KEY, false)
    }
}