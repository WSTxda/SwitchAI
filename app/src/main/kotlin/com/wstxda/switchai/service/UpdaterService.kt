package com.wstxda.switchai.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.wstxda.switchai.R
import com.wstxda.switchai.data.ReleaseInfo
import com.wstxda.switchai.repository.GitHubReleaseRepository
import com.wstxda.switchai.repository.SettingsRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UpdaterService {

    fun checkForUpdates(
        scope: CoroutineScope,
        context: Context,
        onAvailable: (ReleaseInfo) -> Unit,
        onMessage: ((Int) -> Unit)? = null,
    ) = scope.launch(Dispatchers.Main) {
        if (!isNetworkAvailable(context)) {
            onMessage?.invoke(R.string.updater_no_internet_message)
            return@launch
        }
        try {
            val release = GitHubReleaseRepository.fetchLatestRelease()
            val current = getInstalledVersion(context)
            if (compareVersions(current, release.version) < 0) onAvailable(release)
            else onMessage?.invoke(R.string.updater_no_update_message)
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            onMessage?.invoke(R.string.updater_generic_error_message)
        }
    }

    fun checkForUpdatesAuto(
        scope: CoroutineScope,
        context: Context,
        settings: SettingsRepository,
        onAvailable: (ReleaseInfo) -> Unit,
        onStorageError: () -> Unit,
    ) = scope.launch(Dispatchers.Main) {
        try {
            if (settings.claimAutomaticUpdate(System.currentTimeMillis())) checkForUpdates(
                this, context, onAvailable
            ).join()
        } catch (_: IOException) {
            onStorageError()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService<ConnectivityManager>() ?: return false
        val caps = cm.getNetworkCapabilities(cm.activeNetwork ?: return false) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @Suppress("DEPRECATION")
    private fun getInstalledVersion(context: Context): String = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "Unknown"
    }.getOrDefault("Unknown")

    private fun compareVersions(current: String, latest: String): Int {
        if (current == "Unknown") return -1
        val c = current.split(".").map { it.toIntOrNull() ?: 0 }
        val l = latest.split(".").map { it.toIntOrNull() ?: 0 }
        for (i in 0 until maxOf(c.size, l.size)) {
            val diff = c.getOrElse(i) { 0 } - l.getOrElse(i) { 0 }
            if (diff != 0) return diff
        }
        return 0
    }
}