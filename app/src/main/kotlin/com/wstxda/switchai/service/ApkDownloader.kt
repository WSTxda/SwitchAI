package com.wstxda.switchai.service

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import java.io.File
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

object ApkDownloader {

    suspend fun download(
        context: Context,
        url: String,
        fileName: String,
        onProgress: (Int) -> Unit,
        onComplete: (File) -> Unit,
        onError: () -> Unit,
    ) = withContext(Dispatchers.IO) {
        val dm = context.getSystemService<DownloadManager>()
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (dm == null || directory == null) {
            withContext(Dispatchers.Main) { onError() }
            return@withContext
        }
        var downloadId: Long? = null
        var complete = false
        try {
            val request = DownloadManager.Request(url.toUri()).setTitle(fileName)
                .setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS,
                    fileName,
                ).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            downloadId = dm.enqueue(request)
            complete = pollProgress(dm, downloadId, onProgress)
            withContext(Dispatchers.Main) {
                if (complete) onComplete(File(directory, fileName)) else onError()
            }
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            withContext(Dispatchers.Main) { onError() }
        } finally {
            if (!complete) downloadId?.let { runCatching { dm.remove(it) } }
        }
    }

    private suspend fun pollProgress(
        dm: DownloadManager,
        downloadId: Long,
        onProgress: (Int) -> Unit,
    ): Boolean {
        var lastProgress: Int? = null
        while (currentCoroutineContext().isActive) {
            val status: Int
            val downloaded: Long
            val total: Long
            dm.query(DownloadManager.Query().setFilterById(downloadId))?.use { cursor ->
                if (!cursor.moveToFirst()) return false
                val statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val downloadedIdx =
                    cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                val totalIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                if (statusIdx == -1) return false
                status = cursor.getInt(statusIdx)
                downloaded = if (downloadedIdx != -1) cursor.getLong(downloadedIdx) else 0L
                total = if (totalIdx != -1) cursor.getLong(totalIdx) else 0L
            } ?: return false
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> return true
                DownloadManager.STATUS_FAILED -> return false
                DownloadManager.STATUS_PAUSED -> delay(1_000.milliseconds)
                else -> {
                    if (total > 0) {
                        val progress = ((downloaded * 100) / total).toInt()
                        if (progress != lastProgress) {
                            lastProgress = progress
                            withContext(Dispatchers.Main) { onProgress(progress) }
                        }
                    }
                    delay(300.milliseconds)
                }
            }
        }
        return false
    }

    fun installApk(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        context.startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            })
    }
}