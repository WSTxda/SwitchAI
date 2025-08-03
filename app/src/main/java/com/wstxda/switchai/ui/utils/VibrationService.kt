package com.wstxda.switchai.ui.utils

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.PowerManager
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.utils.Constants

object VibrationService {

    private const val EFFECT_CLICK = 1
    private const val EFFECT_TICK = 2

    fun Context.openAssistantVibration() {
        if (!isVibrationAllowed()) return
        performVibration(effectId = getEffectClick(), fallbackDuration = 12)
    }

    fun Context.buttonVibration() {
        if (!isVibrationAllowed()) return
        performVibration(effectId = getEffectTick(), fallbackDuration = 8)
    }

    private fun Context.performVibration(
        effectId: Int,
        fallbackDuration: Long,
    ) {
        val vibrator = getSystemService(Vibrator::class.java) ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val effect = VibrationEffect.createPredefined(effectId)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val attrs =
                    VibrationAttributes.Builder().setUsage(VibrationAttributes.USAGE_TOUCH).build()
                vibrator.vibrate(effect, attrs)
            } else {
                vibrator.vibrate(effect)
            }
        } else {
            val effect = VibrationEffect.createOneShot(
                fallbackDuration, VibrationEffect.DEFAULT_AMPLITUDE
            )
            vibrator.vibrate(effect)
        }
    }

    private fun getEffectClick(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_CLICK
        } else {
            EFFECT_CLICK
        }
    }

    private fun getEffectTick(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VibrationEffect.EFFECT_TICK
        } else {
            EFFECT_TICK
        }
    }

    private fun Context.isVibrationAllowed(): Boolean {
        val preferenceHelper = PreferenceHelper(this)
        if (!preferenceHelper.getBoolean(Constants.ASSISTANT_VIBRATION_PREF_KEY, true)) {
            return false
        }

        val vibrator = getSystemService(Vibrator::class.java)
        if (vibrator == null || !vibrator.hasVibrator()) {
            return false
        }

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (powerManager.isPowerSaveMode) {
            return false
        }

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.ringerMode == AudioManager.RINGER_MODE_SILENT) {
            return false
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val currentFilter = notificationManager.currentInterruptionFilter
        return currentFilter == NotificationManager.INTERRUPTION_FILTER_ALL
    }
}