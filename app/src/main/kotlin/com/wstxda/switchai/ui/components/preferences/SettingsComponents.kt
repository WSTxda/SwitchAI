@file:OptIn(ExperimentalScrollBarApi::class)

package com.wstxda.switchai.ui.components.preferences

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.ui.components.effects.BlurredBar
import com.wstxda.switchai.ui.components.effects.rememberBlurBackdrop
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun SettingsPage(
    @StringRes title: Int,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    SettingsScaffold(
        title,
        onBack,
        actions,
        horizontalContentPadding = 0.dp,
    ) { padding, listState, scrollBehavior ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .pageScrollModifiers(scrollBehavior),
            contentPadding = padding,
            content = content,
        )
    }
}

@Composable
fun SettingsScaffold(
    @StringRes title: Int,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    horizontalContentPadding: Dp = 12.dp,
    content: @Composable BoxScope.(PaddingValues, LazyListState, ScrollBehavior) -> Unit,
) {
    val scrollBehavior = MiuixScrollBehavior()
    val listState = rememberLazyListState()
    val backdrop = rememberBlurBackdrop()
    val wide = isWideScreen()
    val layoutDirection = LocalLayoutDirection.current
    Scaffold(
        topBar = {
            BlurredBar(backdrop) {
                val navigationIcon: @Composable () -> Unit = {
                    if (onBack != null) BackNavigationIcon(onBack)
                }
                val color =
                    if (backdrop != null) Color.Transparent else MiuixTheme.colorScheme.surface
                if (wide) SmallTopAppBar(
                    title = stringResource(title),
                    scrollBehavior = scrollBehavior,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    color = color,
                )
                else TopAppBar(
                    title = stringResource(title),
                    scrollBehavior = scrollBehavior,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    color = color,
                )
            }
        }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier),
            contentAlignment = Alignment.TopCenter,
        ) {
            val contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
                start = padding.calculateStartPadding(layoutDirection) + horizontalContentPadding,
                end = padding.calculateEndPadding(layoutDirection) + horizontalContentPadding,
            )
            content(contentPadding, listState, scrollBehavior)
            VerticalScrollBar(
                adapter = rememberScrollBarAdapter(listState),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                trackPadding = contentPadding,
            )
        }
    }
}

fun Modifier.pageScrollModifiers(scrollBehavior: ScrollBehavior): Modifier =
    scrollEndHaptic()
        .overScrollVertical()
        .nestedScroll(scrollBehavior.nestedScrollConnection)
        .fillMaxHeight()

fun Modifier.overlayScrollModifiers(): Modifier = scrollEndHaptic().overScrollVertical()

@Composable
fun Modifier.bottomSheetContentInsets(): Modifier = windowInsetsPadding(
    WindowInsets.navigationBars.union(WindowInsets.captionBar).only(WindowInsetsSides.Bottom)
)

@Composable
fun isWideScreen(): Boolean = with(LocalDensity.current) {
    val size = LocalWindowInfo.current.containerSize
    val width = size.width.toDp()
    val height = size.height.toDp()
    width >= 840.dp || (width >= 600.dp && height / width < 1.2f)
}

@Composable
fun BackNavigationIcon(onBack: () -> Unit) {
    IconButton(onClick = onBack) {
        Icon(MiuixIcons.Back, stringResource(com.wstxda.switchai.R.string.navigate_back))
    }
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
        content = content,
    )
}

@Composable
fun OverlayCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 12.dp,
        colors = CardDefaults.defaultColors(MiuixTheme.colorScheme.secondaryContainer),
        content = content,
    )
}

@Composable
fun PreferenceIcon(icon: ImageVector, background: Color) {
    Card(
        modifier = Modifier
            .padding(end = 8.dp)
            .size(28.dp),
        cornerRadius = 8.dp,
        insideMargin = PaddingValues(4.dp),
        colors = CardDefaults.defaultColors(background, Color.White),
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
    }
}

@Composable
fun AssistantIcon(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    tint: Color = MiuixTheme.colorScheme.onSurface,
    size: Dp = 32.dp,
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        modifier = modifier.size(size),
        tint = tint,
    )
}

@Composable
fun SupportingText(@StringRes text: Int) {
    Text(
        text = stringResource(text),
        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
        style = MiuixTheme.textStyles.footnote1,
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp),
    )
}