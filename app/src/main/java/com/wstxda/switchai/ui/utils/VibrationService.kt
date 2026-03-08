package com.wstxda.switchai.ui.utils

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.utils.Constants

object VibrationService {

    fun Context.openAssistantVibration() {
        if (!isVibrationAllowed()) return
        performVibration(effectId = getEffectClick(), fallbackDuration = 12)
    }

    fun Context.buttonVibration() {
        if (!isVibrationAllowed()) return
        performVibration(effectId = getEffectTick(), fallbackDuration = 8)
    }

    private fun Context.performVibration(effectId: Int, fallbackDuration: Long) {
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
            vibrator.vibrate(
                VibrationEffect.createOneShot(fallbackDuration, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        }
    }

    private fun getEffectClick(): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) VibrationEffect.EFFECT_CLICK else 1

    private fun getEffectTick(): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) VibrationEffect.EFFECT_TICK else 2

    private fun Context.isVibrationAllowed(): Boolean {
        if (!PreferenceHelper(this).getBoolean(Constants.ASSISTANT_VIBRATION_PREF_KEY, true)) {
            return false
        }

        val vibrator = getSystemService(Vibrator::class.java)
        if (vibrator == null || !vibrator.hasVibrator()) {
            return false
        }

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.ringerMode != AudioManager.RINGER_MODE_SILENT
    }
}