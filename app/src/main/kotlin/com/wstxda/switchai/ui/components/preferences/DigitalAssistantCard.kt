package com.wstxda.switchai.ui.components.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wstxda.switchai.R
import com.wstxda.switchai.ui.utils.rememberAssistantResourcesManager
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType

@Composable
fun DigitalAssistantCard(assistant: String, setupDone: Boolean, onClick: () -> Unit) {
    val resources = rememberAssistantResourcesManager()
    val colors = MiuixTheme.colorScheme
    val containerColor = if (setupDone) colors.primaryVariant else colors.errorContainer
    val contentColor = if (setupDone) colors.onPrimaryVariant else colors.onErrorContainer
    val titleColor = when {
        !setupDone -> contentColor
        MiuixTheme.isDynamicColor -> colors.onPrimaryVariant
        else -> colors.onPrimary
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
        colors = CardDefaults.defaultColors(containerColor, contentColor),
        insideMargin = PaddingValues(16.dp),
        showIndication = true,
        pressFeedbackType = PressFeedbackType.Tilt,
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    if (setupDone) resources.getAssistantName(assistant)
                    else stringResource(R.string.pref_setup_digital_assistant),
                    color = titleColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    if (setupDone) stringResource(R.string.pref_digital_assistant)
                    else stringResource(R.string.pref_setup_digital_assistant_summary),
                    color = contentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
            if (setupDone) {
                AssistantIcon(
                    resources.getAssistantIcon(assistant), tint = contentColor, size = 60.dp
                )
            } else {
                Icon(
                    MiuixIcons.Demibold.Close,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = contentColor,
                )
            }
        }
    }
}