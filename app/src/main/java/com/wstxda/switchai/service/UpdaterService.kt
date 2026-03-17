package com.wstxda.switchai.service

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar
import com.wstxda.switchai.R
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

object UpdaterService {

    fun checkForUpdates(context: Context, anchorView: View) {
        CoroutineScope(Dispatchers.Main).launch {
            if (!isNetworkAvailable(context)) {
                showNoInternetSnackbar(anchorView)
                return@launch
            }

            try {
                val latestVersion = withContext(Dispatchers.IO) { fetchLatestVersion() }
                val currentVersion = getCurrentVersion(context)
                if (compareVersions(currentVersion, latestVersion) < 0) {
                    showUpdateAvailableSnackbar(anchorView, latestVersion)
                } else {
                    showNoUpdateSnackbar(anchorView)
                }
            } catch (_: Exception) {
                showGenericErrorSnackbar(anchorView)
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private suspend fun fetchLatestVersion(): String = withContext(Dispatchers.IO) {
        val jsonString = URL(Constants.GITHUB_RELEASE_URL).readText()
        val jsonObject = JSONObject(jsonString)
        jsonObject.optString("tag_name").removePrefix("v")
    }

    private fun getCurrentVersion(context: Context): String = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "N/A"
    }.getOrDefault("N/A")

    private fun compareVersions(current: String, latest: String): Int {
        if (current == "N/A") return -1
        val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
        val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }
        for (i in 0 until maxOf(currentParts.size, latestParts.size)) {
            val curr = currentParts.getOrElse(i) { 0 }
            val late = latestParts.getOrElse(i) { 0 }
            if (curr != late) return curr - late
        }
        return 0
    }

    private fun showNoUpdateSnackbar(anchorView: View) {
        Snackbar.make(anchorView, R.string.update_checker_no_update, Snackbar.LENGTH_SHORT).show()
    }

    private fun showUpdateAvailableSnackbar(anchorView: View, latestVersion: String) {
        Snackbar.make(
            anchorView,
            anchorView.context.getString(R.string.update_checker_update_available, latestVersion),
            Snackbar.LENGTH_LONG
        ).apply {
            setAction(R.string.update_checker_download_button) {
                val intent = Intent(
                    Intent.ACTION_VIEW, Constants.GITHUB_RELEASE_PAGE_URL.toUri()
                )
                anchorView.context.startActivity(intent)
            }
            addCallback(object : Snackbar.Callback() {
                override fun onShown(snackBar: Snackbar) {
                    (snackBar.view.layoutParams as? CoordinatorLayout.LayoutParams)?.behavior = null
                }
            })
        }.show()
    }

    private fun showNoInternetSnackbar(anchorView: View) {
        Snackbar.make(anchorView, R.string.update_checker_no_internet, Snackbar.LENGTH_LONG).show()
    }

    private fun showGenericErrorSnackbar(anchorView: View) {
        Snackbar.make(anchorView, R.string.update_checker_generic_error, Snackbar.LENGTH_LONG)
            .show()
    }
}