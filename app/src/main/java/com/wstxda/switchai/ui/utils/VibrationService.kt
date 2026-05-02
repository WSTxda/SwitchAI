package com.wstxda.switchai.ui.utils

import android.content.Context
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.utils.Constants

object VibrationService {

    fun Context.openAssistantVibration() {
        if (!isVibrationEnabled()) return
        performVibration(HapticType.CLICK, duration = 12)
    }

    fun Context.buttonVibration() {
        if (!isVibrationEnabled()) return
        performVibration(HapticType.TICK, duration = 8)
    }

    private fun Context.performVibration(type: HapticType, duration: Long) {
        val vibrator = getVibrator() ?: return
        val effect = createVibrationEffect(type, duration)
        vibrateCompat(vibrator, effect)
    }

    private fun Context.getVibrator(): Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = getSystemService(VibratorManager::class.java)
        vibratorManager?.defaultVibrator ?: getSystemService(Vibrator::class.java)
    } else {
        getSystemService(Vibrator::class.java)
    }

    private fun createVibrationEffect(type: HapticType, duration: Long): VibrationEffect {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val effectId = when (type) {
                HapticType.CLICK -> VibrationEffect.EFFECT_CLICK
                HapticType.TICK -> VibrationEffect.EFFECT_TICK
            }
            VibrationEffect.createPredefined(effectId)
        } else {
            VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
        }
    }

    private fun vibrateCompat(vibrator: Vibrator, effect: VibrationEffect) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val attributes = VibrationAttributes.Builder()
                .setUsage(VibrationAttributes.USAGE_HARDWARE_FEEDBACK)
                .build()
            vibrator.vibrate(effect, attributes)
        } else
            @Suppress("DEPRECATION")
            vibrator.vibrate(effect)
    }

    private fun Context.isVibrationEnabled(): Boolean {
        return PreferenceHelper(this).getBoolean(Constants.ACCESSIBILITY_VIBRATION_PREF_KEY, true)
    }

    private enum class HapticType {
        CLICK, TICK
    }
}