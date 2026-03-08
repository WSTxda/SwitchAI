package com.wstxda.switchai.logic

import android.util.Log
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun isRootAvailable(): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val process = ProcessBuilder("su", "-c", "id").redirectErrorStream(true).start()

        process.inputStream.bufferedReader().readText()
        process.waitFor() == 0
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
            ).redirectErrorStream(true).start()

            process.inputStream.bufferedReader().readText()
            process.waitFor() == 0
        }.getOrElse {
            Log.e(Constants.ROOT_CHECKER, "Failed to launch root activity", it)
            false
        }
    }
