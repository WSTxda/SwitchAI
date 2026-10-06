package com.wstxda.switchai.ui.components.selector

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.R
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun SelectorReorderTip(modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    val colors = MiuixTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.defaultColors(colors.tertiaryContainer, colors.onTertiaryContainer),
    ) {
        BasicComponent(
            summary = stringResource(R.string.selector_tip_reorder),
            summaryColor = BasicComponentDefaults.summaryColor(colors.onTertiaryContainer),
            endActions = {
                IconButton(
                    onClick = onDismiss,
                    backgroundColor = colors.tertiaryContainer,
                    minWidth = 30.dp,
                    minHeight = 30.dp,
                ) {
                    Icon(
                        MiuixIcons.Demibold.Close,
                        stringResource(R.string.selector_tip_close),
                        Modifier.size(16.dp),
                        tint = colors.onTertiaryContainer,
                    )
                }
            },
        )
    }
}