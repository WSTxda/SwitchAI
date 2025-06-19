package com.wstxda.switchai.ui.utils

import android.content.Context
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.preference.PreferenceManager
import com.wstxda.switchai.utils.Constants

object VibrationService {

    fun performVibration(context: Context) {
        if (!isVibrationEnabled(context)) return

        val vibrator = context.getSystemService(Vibrator::class.java) ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val attrs =
                    VibrationAttributes.Builder().setUsage(VibrationAttributes.USAGE_TOUCH).build()
                vibrator.vibrate(effect, attrs)
            } else {
                vibrator.vibrate(effect)
            }

        } else {
            val effect = VibrationEffect.createOneShot(
                40, VibrationEffect.DEFAULT_AMPLITUDE
            )
            vibrator.vibrate(effect)
        }
    }

    private fun isVibrationEnabled(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(Constants.ASSISTANT_VIBRATION_PREF_KEY, true)
    }

    fun Context.openAssistantVibration() {
        performVibration(this)
    }
}