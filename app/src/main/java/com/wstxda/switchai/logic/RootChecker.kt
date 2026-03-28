package com.wstxda.switchai.logic

import android.util.Log
import com.wstxda.switchai.utils.Constants

fun isRootAvailable(): Boolean = runCatching {
    val process = ProcessBuilder("su", "-c", "id").start()
    process.waitFor() == 0
}.getOrElse {
    Log.e(Constants.ROOT_CHECKER, "Root check failed", it)
    false
}

fun launchRootActivity(packageName: String, activityName: String): Boolean = runCatching {
    val process = ProcessBuilder(
        "su", "-c", "am", "start", "-n", "$packageName/$activityName"
    ).redirectErrorStream(true).start()

    val output = process.inputStream.bufferedReader().readText()
    val exitCode = process.waitFor()

    Log.d(Constants.ROOT_CHECKER, "am start output: $output | exit: $exitCode")
    exitCode == 0
}.getOrElse {
    Log.e(Constants.ROOT_CHECKER, "Failed to launch root activity", it)
    false
}