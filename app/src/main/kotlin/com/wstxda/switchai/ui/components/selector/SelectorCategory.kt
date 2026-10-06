package com.wstxda.switchai.ui.components.selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.data.AssistantListItem
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.SmallTitleDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun SelectorCategory(
    item: AssistantListItem.CategoryHeader,
    showCount: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(SmallTitleDefaults.InsideMargin),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SmallTitle(
            text = stringResource(item.category.titleRes),
            modifier = Modifier.weight(1f),
            insideMargin = PaddingValues(0.dp),
        )
        if (showCount) {
            Card(
                cornerRadius = 90.dp,
                insideMargin = PaddingValues(horizontal = 6.dp, vertical = 1.dp),
                colors = CardDefaults.defaultColors(
                    MiuixTheme.colorScheme.tertiaryContainer,
                    MiuixTheme.colorScheme.onTertiaryContainer,
                ),
            ) {
                Text(item.count.toString(), style = MiuixTheme.textStyles.subtitle)
            }
        }
    }
}