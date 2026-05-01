package com.wstxda.switchai.repository

import com.wstxda.switchai.data.ReleaseInfo
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

object GitHubReleaseRepository {

    suspend fun fetchLatestRelease(): ReleaseInfo = withContext(Dispatchers.IO) {
        val json = JSONObject(URL(Constants.GITHUB_API_URL).readText())
        val asset = json.getJSONArray("assets").getJSONObject(0)

        val tagName = json.optString("tag_name")
        val releaseName = json.optString("name").takeIf { it.isNotBlank() } ?: tagName

        ReleaseInfo(
            title = releaseName.trimStart { !it.isDigit() },
            version = tagName.trimStart { !it.isDigit() },
            changelog = json.optString("body"),
            downloadUrl = asset.optString("browser_download_url"),
            pageUrl = json.optString("html_url").takeIf { it.isNotBlank() }
                ?: Constants.GITHUB_RELEASE_URL)
    }
}