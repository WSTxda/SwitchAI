package com.wstxda.switchai.ui.components.updater

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.wstxda.switchai.R
import com.wstxda.switchai.data.ReleaseInfo
import com.wstxda.switchai.service.ApkDownloader
import com.wstxda.switchai.ui.components.preferences.OverlayCard
import com.wstxda.switchai.ui.components.preferences.bottomSheetContentInsets
import com.wstxda.switchai.ui.components.preferences.overlayScrollModifiers
import java.io.File
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.LinearProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun UpdaterBottomSheet(
    release: ReleaseInfo?,
    visible: Boolean,
    onDismiss: () -> Unit,
    onUrl: (String) -> Unit,
    onMessage: (Int) -> Unit,
) {
    if (release == null) return
    key(release.downloadUrl) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val currentVisible by rememberUpdatedState(visible)
        var active by remember { mutableStateOf(true) }
        var downloadJob by remember { mutableStateOf<Job?>(null) }
        var isDownloading by remember { mutableStateOf(false) }
        var progress by remember { mutableStateOf<Int?>(null) }
        var downloadedPath by rememberSaveable { mutableStateOf<String?>(null) }
        var pendingInstallPath by rememberSaveable { mutableStateOf<String?>(null) }
        val permissionLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val path = pendingInstallPath
                pendingInstallPath = null
                if (active && currentVisible && path != null && path == downloadedPath && context.packageManager.canRequestPackageInstalls()) {
                    runCatching {
                        ApkDownloader.installApk(
                            context, File(path)
                        )
                    }.onFailure { onMessage(R.string.updater_generic_error_message) }
                }
            }
        val requestInstall: (File) -> Unit = { file ->
            if (active && currentVisible) {
                if (context.packageManager.canRequestPackageInstalls()) {
                    runCatching {
                        ApkDownloader.installApk(
                            context, file
                        )
                    }.onFailure { onMessage(R.string.updater_generic_error_message) }
                } else {
                    pendingInstallPath = file.path
                    runCatching {
                        permissionLauncher.launch(
                            Intent(
                                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                "package:${context.packageName}".toUri(),
                            )
                        )
                    }.onFailure {
                        pendingInstallPath = null
                        onMessage(R.string.updater_generic_error_message)
                    }
                }
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                active = false
                pendingInstallPath = null
                downloadJob?.cancel()
            }
        }
        OverlayBottomSheet(
            show = visible,
            title = stringResource(R.string.updater_title),
            onDismissRequest = onDismiss,
        ) {
            UpdateSheetContent(
                release,
                isDownloading,
                progress,
                downloadedPath,
                onDownload = {
                    val path = downloadedPath
                    if (path != null) requestInstall(File(path))
                    else if (!isDownloading) {
                        isDownloading = true
                        progress = null
                        downloadJob = scope.launch {
                            try {
                                ApkDownloader.download(
                                    context.applicationContext,
                                    release.downloadUrl,
                                    release.downloadUrl.substringAfterLast('/'),
                                    onProgress = { if (active) progress = it },
                                    onComplete = { file ->
                                        if (active) {
                                            downloadedPath = file.path
                                            requestInstall(file)
                                        }
                                    },
                                    onError = {
                                        if (active) onMessage(R.string.updater_generic_error_message)
                                    },
                                )
                            } finally {
                                if (active) isDownloading = false
                            }
                        }
                    }
                },
                onUrl = onUrl,
            )
        }
    }
}

@Composable
private fun UpdateSheetContent(
    release: ReleaseInfo,
    isDownloading: Boolean,
    downloadProgress: Int?,
    downloadedPath: String?,
    onDownload: () -> Unit,
    onUrl: (String) -> Unit,
) {
    val colorScheme = MiuixTheme.colorScheme
    Column(
        Modifier
            .fillMaxWidth()
            .bottomSheetContentInsets(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            cornerRadius = 90.dp,
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            colors = CardDefaults.defaultColors(
                colorScheme.tertiaryContainer,
                colorScheme.onTertiaryContainer,
            ),
        ) {
            Text(
                release.version,
                color = colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.SemiBold,
            )
        }
        OverlayCard(Modifier.weight(1f, fill = false)) {
            SelectionContainer(
                Modifier
                    .overlayScrollModifiers()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                MarkdownText(release.changelog)
            }
        }
        if (isDownloading) {
            LinearProgressIndicator(
                progress = downloadProgress?.div(100f),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            TextButton(
                stringResource(R.string.updater_github_button),
                { onUrl(release.pageUrl) },
                Modifier.weight(1f),
                enabled = !isDownloading,
            )
            TextButton(
                stringResource(
                    if (downloadedPath == null) R.string.updater_download_button
                    else R.string.updater_install_button
                ),
                onDownload,
                Modifier.weight(1f),
                enabled = !isDownloading,
                colors = ButtonDefaults.textButtonColorsPrimary(),
            )
        }
    }
}