package com.wstxda.switchai.ui.utils

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.wstxda.switchai.R
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.utils.Constants

object SoundService {

    fun Context.openAssistantSound() {
        if (!canPlaySound()) return
        performSound()
    }

    private fun Context.performSound() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.open_sound)
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.start()
    }

    private fun Context.canPlaySound(): Boolean {
        val preferenceHelper = PreferenceHelper(this)

        val isAppSoundEnabled =
            preferenceHelper.getBoolean(Constants.ASSISTANT_SOUND_PREF_KEY, false)
        if (!isAppSoundEnabled) {
            return false
        }

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            return false
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val currentFilter = notificationManager.currentInterruptionFilter
        return currentFilter == NotificationManager.INTERRUPTION_FILTER_ALL
    }
}