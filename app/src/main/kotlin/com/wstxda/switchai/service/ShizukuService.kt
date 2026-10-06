package com.wstxda.switchai.service

import androidx.annotation.Keep
import kotlin.system.exitProcess

@Keep
class ShizukuService : IShizukuService.Stub() {

    override fun launchActivity(packageName: String, activityName: String): Boolean {
        return !(packageName.isBlank() || activityName.isBlank()) && runCatching {
            val process = ProcessBuilder(
                "/system/bin/am",
                "start",
                "-n",
                "$packageName/$activityName",
            ).redirectErrorStream(true).start()

            val output = process.inputStream.bufferedReader().use { it.readText() }
            process.waitFor() == 0 && output.lineSequence().none { line ->
                line.startsWith("Error:") || line.contains("Exception")
            }
        }.getOrDefault(false)
    }

    override fun destroy() {
        exitProcess(0)
    }
}