package com.wstxda.switchai.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wstxda.switchai.R
import com.wstxda.switchai.data.ShizukuSetupState
import com.wstxda.switchai.data.ShizukuState
import com.wstxda.switchai.ui.components.preferences.SettingsCard
import com.wstxda.switchai.ui.components.preferences.SettingsPage
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton

@Composable
fun ShizukuSetupScreen(
    state: ShizukuSetupState,
    loading: Boolean,
    storageError: Boolean,
    onBack: () -> Unit,
    onManager: () -> Unit,
    onPermission: () -> Unit,
    onContinue: () -> Unit,
) {
    if (loading || storageError) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (storageError) Text(stringResource(R.string.settings_storage_error))
            else CircularProgressIndicator()
        }
        return
    }
    val ready = state.access == ShizukuState.READY
    val serviceMessage = when (state.access) {
        ShizukuState.NOT_INSTALLED -> R.string.shizuku_not_installed
        ShizukuState.NOT_RUNNING -> R.string.shizuku_not_running
        ShizukuState.UNSUPPORTED -> R.string.shizuku_unsupported
        ShizukuState.PERMISSION_REQUIRED, ShizukuState.READY -> R.string.shizuku_running

        null -> R.string.shizuku_not_running
    }
    SettingsPage(R.string.shizuku_setup_title, onBack) {
        item(key = "shizuku_service", contentType = "card") {
            SettingsCard {
                BasicComponent(
                    title = stringResource(R.string.shizuku_service_title),
                    summary = stringResource(serviceMessage),
                )
                TextButton(
                    text = stringResource(
                        when (state.access) {
                            ShizukuState.NOT_INSTALLED -> R.string.shizuku_install
                            ShizukuState.PERMISSION_REQUIRED, ShizukuState.READY -> R.string.shizuku_ready

                            else -> R.string.shizuku_open
                        }
                    ),
                    onClick = onManager,
                    enabled = state.access in setOf(
                        ShizukuState.NOT_INSTALLED,
                        ShizukuState.NOT_RUNNING,
                        ShizukuState.UNSUPPORTED,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(BasicComponentDefaults.InsideMargin),
                )
            }
        }
        item(key = "shizuku_permission", contentType = "card") {
            SettingsCard {
                BasicComponent(
                    title = stringResource(R.string.shizuku_permission_title),
                    summary = stringResource(R.string.shizuku_permission_message),
                )
                TextButton(
                    text = stringResource(
                        if (ready) R.string.shizuku_granted else R.string.shizuku_grant_access
                    ),
                    onClick = onPermission,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(BasicComponentDefaults.InsideMargin),
                    enabled = state.access == ShizukuState.PERMISSION_REQUIRED && !state.permissionPending,
                )
            }
        }
        item(key = "shizuku_continue", contentType = "action") {
            TextButton(
                text = stringResource(R.string.shizuku_continue),
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(BasicComponentDefaults.InsideMargin),
                enabled = ready && !state.launching && !state.permissionPending,
                colors = ButtonDefaults.textButtonColorsPrimary(),
            )
        }
        if (state.launching || state.permissionPending) item(
            key = "shizuku_loading",
            contentType = "progress",
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(BasicComponentDefaults.InsideMargin),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}