package com.wstxda.switchai.service

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.File

object ApkDownloader {

    suspend fun download(
        context: Context,
        url: String,
        fileName: String,
        onProgress: (Int) -> Unit,
        onComplete: (File) -> Unit,
        onError: () -> Unit
    ) = withContext(Dispatchers.IO) {
        val dm = context.getSystemService<DownloadManager>() ?: run {
            withContext(Dispatchers.Main) { onError() }
            return@withContext
        }

        val request = DownloadManager.Request(url.toUri()).setTitle(fileName)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        val downloadId = dm.enqueue(request)
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

        pollProgress(dm, downloadId, file, onProgress, onComplete, onError)
    }

    private suspend fun pollProgress(
        dm: DownloadManager,
        downloadId: Long,
        file: File,
        onProgress: (Int) -> Unit,
        onComplete: (File) -> Unit,
        onError: () -> Unit
    ) {
        while (currentCoroutineContext().isActive) {
            val cursor = dm.query(DownloadManager.Query().setFilterById(downloadId))

            if (cursor == null || !cursor.moveToFirst()) {
                cursor?.close()
                withContext(Dispatchers.Main) { onError() }
                return
            }

            val statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val downloadedIdx =
                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
            val totalIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

            if (statusIdx == -1) {
                cursor.close()
                withContext(Dispatchers.Main) { onError() }
                return
            }

            val status = cursor.getInt(statusIdx)
            val downloaded = if (downloadedIdx != -1) cursor.getLong(downloadedIdx) else 0L
            val total = if (totalIdx != -1) cursor.getLong(totalIdx) else 0L
            cursor.close()

            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    withContext(Dispatchers.Main) { onComplete(file) }
                    return
                }

                DownloadManager.STATUS_FAILED -> {
                    withContext(Dispatchers.Main) { onError() }
                    return
                }

                DownloadManager.STATUS_PAUSED -> {
                    delay(1_000)
                }

                else -> {
                    if (total > 0) {
                        val percent = ((downloaded * 100) / total).toInt()
                        withContext(Dispatchers.Main) { onProgress(percent) }
                    }
                    delay(300)
                }
            }
        }

        dm.remove(downloadId)
    }

    fun installApk(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        })
    }
}