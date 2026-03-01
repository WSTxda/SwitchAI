package com.wstxda.switchai.logic

import android.util.Log
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun isRootAvailable(): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val process = ProcessBuilder("su", "-c", "id").start()
        val result = process.waitFor() == 0
        process.destroy()
        result
    }.getOrElse {
        Log.e(Constants.ROOT_CHECKER, "Root check failed", it)
        false
    }
}

suspend fun launchRootActivity(packageName: String, activityName: String): Boolean =
    withContext(Dispatchers.IO) {
        runCatching {
            val process = ProcessBuilder(
                "su", "-c", "am", "start", "-n", "$packageName/$activityName"
            ).start()
            val result = process.waitFor() == 0
            process.destroy()
            result
        }.getOrElse {
            Log.e(Constants.ROOT_CHECKER, "Failed to launch root activity", it)
            false
        }
    }