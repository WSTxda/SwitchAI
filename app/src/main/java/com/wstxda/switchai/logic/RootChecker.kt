package com.wstxda.switchai.logic

import android.util.Log
import com.wstxda.switchai.utils.Constants
import kotlin.getOrElse
import kotlin.runCatching

fun isRootAvailable(): Boolean = runCatching {
    val process = ProcessBuilder("su", "-c", "id").start()
    process.waitFor() == 0
}.getOrElse {
    Log.e(Constants.ROOT_CHECKER, "Root check failed", it)
    false
}

fun launchRootActivity(packageName: String, activityName: String): Boolean = runCatching {
    val process =
        ProcessBuilder("su", "-c", "am", "start", "-n", "$packageName/$activityName").start()
    process.waitFor() == 0
}.getOrElse {
    Log.e(Constants.ROOT_CHECKER, "Failed to launch root activity", it)
    false
}