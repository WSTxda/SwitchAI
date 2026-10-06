package com.wstxda.switchai.ui.components.selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.data.AssistantItem
import com.wstxda.switchai.ui.components.preferences.AssistantIcon
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Pin
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType

@Composable
internal fun AssistantCard(
    item: AssistantItem,
    columns: Int,
    modifier: Modifier,
    onClick: () -> Unit,
    onPin: () -> Unit,
) {
    val description =
        stringResource(if (item.isPinned) R.string.selector_unpin else R.string.selector_pin)
    val pinColors = if (item.isPinned) ButtonDefaults.textButtonColorsPrimary()
    else ButtonDefaults.textButtonColors()
    val pin: @Composable () -> Unit = {
        if (item.isInstalled) {
            IconButton(
                onClick = onPin,
                modifier = Modifier.semantics { selected = item.isPinned },
                backgroundColor = pinColors.color,
                minWidth = 30.dp,
                minHeight = 30.dp,
            ) {
                Icon(
                    MiuixIcons.Demibold.Pin,
                    description,
                    Modifier.size(16.dp),
                    tint = pinColors.textColor,
                )
            }
        }
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.defaultColors(
            color = MiuixTheme.colorScheme.secondaryContainer,
            contentColor = MiuixTheme.colorScheme.onSurface,
        ),
        onClick = onClick,
        showIndication = true,
        pressFeedbackType = PressFeedbackType.Sink,
    ) {
        if (columns == 1) {
            BasicComponent(
                title = item.name,
                startAction = {
                    AssistantIcon(
                        item.iconRes,
                        tint = MiuixTheme.colorScheme.onSurface,
                        size = 24.dp,
                    )
                },
                endActions = { pin() },
                insideMargin = PaddingValues(16.dp),
            )
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = item.name
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AssistantIcon(
                    item.iconRes,
                    tint = MiuixTheme.colorScheme.onSurface,
                    size = 34.dp,
                )
                if (columns <= Constants.MAX_GRID_COLUMNS_WITH_LABEL) {
                    Text(
                        item.name,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                pin()
            }
        }
    }
}