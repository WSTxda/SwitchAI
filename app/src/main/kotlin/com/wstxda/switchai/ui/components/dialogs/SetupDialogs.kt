package com.wstxda.switchai.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.Constants
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayDialog

@Composable
fun SetupDialog(show: Boolean, onSetup: () -> Unit, onDismiss: () -> Unit) {
    OverlayDialog(
        show = show,
        title = stringResource(R.string.digital_assistant_setup_title),
        summary = stringResource(R.string.digital_assistant_setup_message),
        onDismissRequest = onDismiss,
    ) {
        ConfirmButtons(
            onDismiss,
            {
                onSetup()
                onDismiss()
            },
            stringResource(R.string.digital_assistant_setup_button),
        )
    }
}

@Composable
fun FreeAndroidDialog(
    show: Boolean,
    onAcknowledge: () -> Unit,
    onUrl: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    OverlayDialog(
        show = show,
        title = stringResource(R.string.free_android_warn_title),
        summary = stringResource(R.string.free_android_warn_message),
        onDismissRequest = onDismiss,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TextButton(
                stringResource(R.string.free_android_warn_link_button),
                {
                    onUrl(Constants.KEEP_ANDROID_OPEN_URL)
                    onDismiss()
                },
                Modifier.fillMaxWidth(),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                TextButton(
                    stringResource(R.string.free_android_warn_solution_button),
                    {
                        onUrl(Constants.ANDROID_OPEN_SOLUTIONS_URL)
                        onDismiss()
                    },
                    Modifier.weight(1f),
                )
                TextButton(
                    stringResource(android.R.string.ok),
                    onAcknowledge,
                    Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary(),
                )
            }
        }
    }
}