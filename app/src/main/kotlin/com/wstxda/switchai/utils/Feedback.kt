package com.wstxda.switchai.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.wstxda.switchai.R

fun Context.openAssistantSound(enabled: Boolean) {
    if (!enabled) return
    playSound()
}

private fun Context.playSound() {
    var player: MediaPlayer? = null
    try {
        val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANT)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

        val app = applicationContext
        MediaPlayer().also { player = it }.apply {
            setAudioAttributes(attributes)
            setDataSource(
                app, "android.resource://${app.packageName}/${R.raw.open_sound}".toUri()
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
    } catch (error: Exception) {
        player?.release()
        Log.e("SwitchAI", "Assistant sound could not be prepared", error)
    }
}

fun Context.openAssistantVibration(enabled: Boolean) {
    if (!enabled) return
    performVibration(duration = 12)
}

private fun Context.performVibration(duration: Long) {
    val vibrator = getVibrator() ?: return
    val effect = createVibrationEffect(duration)
    vibrateCompat(vibrator, effect)
}

private fun Context.getVibrator(): Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    val vibratorManager = getSystemService(VibratorManager::class.java)
    vibratorManager?.defaultVibrator ?: getSystemService(Vibrator::class.java)
} else {
    getSystemService(Vibrator::class.java)
}

private fun createVibrationEffect(duration: Long): VibrationEffect {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
    } else {
        VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
    }
}

private fun vibrateCompat(vibrator: Vibrator, effect: VibrationEffect) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val attributes =
            VibrationAttributes.Builder().setUsage(VibrationAttributes.USAGE_HARDWARE_FEEDBACK)
                .build()
        vibrator.vibrate(effect, attributes)
    } else @Suppress("DEPRECATION") vibrator.vibrate(effect)
}

fun Context.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}