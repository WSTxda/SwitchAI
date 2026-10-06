package com.wstxda.switchai.logic

import android.content.Context
import android.content.pm.PackageManager
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.data.ShizukuState
import rikka.shizuku.Shizuku

object ShizukuChecker {

    fun getState(context: Context): ShizukuState {
        if (!Shizuku.pingBinder()) {
            return if (isInstalled(context)) {
                ShizukuState.NOT_RUNNING
            } else {
                ShizukuState.NOT_INSTALLED
            }
        }

        return runCatching {
            when {
                Shizuku.isPreV11() -> ShizukuState.UNSUPPORTED
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED -> ShizukuState.READY

                else -> ShizukuState.PERMISSION_REQUIRED
            }
        }.getOrElse {
            if (isInstalled(context)) ShizukuState.NOT_RUNNING else ShizukuState.NOT_INSTALLED
        }
    }

    @Suppress("DEPRECATION")
    private fun isInstalled(context: Context): Boolean = runCatching {
        context.packageManager.getApplicationInfo(Constants.SHIZUKU_MANAGER_PACKAGE, 0)
    }.isSuccess
}