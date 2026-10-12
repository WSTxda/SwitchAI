package com.wstxda.switchai.ui.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.wstxda.switchai.R
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.utils.Constants

object SoundService {

    fun Context.openAssistantSound() {
        if (!isSoundEnabled()) return
        playSound()
    }

    private fun Context.playSound() {
        try {
            val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANT)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            MediaPlayer().apply {
                setAudioAttributes(attributes)
                setDataSource(
                    this@playSound, "android.resource://${packageName}/${R.raw.open_sound}".toUri()
                )
                setOnPreparedListener { mp ->
                    mp.start()
                }
                setOnCompletionListener { mp ->
                    mp.release()
                }
                setOnErrorListener { mp, _, _ ->
                    mp.release()
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun Context.isSoundEnabled(): Boolean {
        return PreferenceHelper(this).getBoolean(Constants.ACCESSIBILITY_SOUND_PREF_KEY, false)
    }
}