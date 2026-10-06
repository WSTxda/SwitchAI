package com.wstxda.switchai.repository

import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.data.ReleaseInfo
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

object GitHubReleaseRepository {

    suspend fun fetchLatestRelease(): ReleaseInfo = withContext(Dispatchers.IO) {
        val connection = URL(Constants.GITHUB_API_URL).openConnection() as HttpURLConnection
        connection.connectTimeout = Constants.RELEASE_CONNECT_TIMEOUT_MILLIS
        connection.readTimeout = Constants.RELEASE_READ_TIMEOUT_MILLIS
        val json = try {
            connection.inputStream.bufferedReader().use { JSONObject(it.readText()) }
        } finally {
            connection.disconnect()
        }
        val asset = json.getJSONArray("assets").getJSONObject(0)
        val tagName = json.optString("tag_name")
        val releaseName = json.optString("name").takeIf { it.isNotBlank() } ?: tagName
        ReleaseInfo(
            title = releaseName.trimStart { !it.isDigit() },
            version = tagName.trimStart { !it.isDigit() },
            changelog = json.optString("body"),
            downloadUrl = asset.optString("browser_download_url"),
            pageUrl = json.optString("html_url").takeIf { it.isNotBlank() }
                ?: Constants.GITHUB_RELEASE_URL,
        )
    }
}