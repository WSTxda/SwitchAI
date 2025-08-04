package com.wstxda.switchai.logic

import android.content.Context
import android.content.pm.PackageManager
import com.wstxda.switchai.utils.AssistantsMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PackageChecker(private val context: Context) {

    suspend fun installedAssistants(): Set<String> = withContext(Dispatchers.IO) {
        val installedKeys = mutableSetOf<String>()
        val packageManager = context.packageManager

        AssistantsMap.assistantPackage.forEach { (key, packageName) ->
            try {
                packageManager.getPackageInfo(packageName, 0)
                installedKeys.add(key)
            } catch (_: PackageManager.NameNotFoundException) {
            }
        }
        installedKeys
    }
}