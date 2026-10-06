@file:OptIn(ExperimentalScrollBarApi::class)

// Copyright 2025–2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package com.wstxda.switchai.ui.screens.preferences

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.ui.components.effects.BgEffectBackground
import com.wstxda.switchai.ui.components.effects.BlurredBar
import com.wstxda.switchai.ui.components.effects.rememberBlurBackdrop
import com.wstxda.switchai.ui.components.preferences.BackNavigationIcon
import com.wstxda.switchai.ui.components.preferences.pageScrollModifiers
import com.wstxda.switchai.ui.theme.LocalDarkTheme
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurBlendMode
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AboutScreen(
    checking: Boolean,
    onUpdate: () -> Unit,
    onLibraries: () -> Unit,
    onUrl: (String) -> Unit,
    onAppInfo: () -> Unit,
    onBack: () -> Unit,
) {
    val scrollBehavior = MiuixScrollBehavior()
    val listState = rememberLazyListState()
    val scrollProgress by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else {
                val spacer =
                    listState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == "logoSpacer" }
                if (spacer != null && spacer.size > 0) {
                    (listState.firstVisibleItemScrollOffset.toFloat() / spacer.size).coerceIn(
                        0f,
                        1f,
                    )
                } else 0f
            }
        }
    }
    val collapsed by remember { derivedStateOf { scrollProgress == 1f } }
    val backdrop = rememberBlurBackdrop()
    val blurActive = backdrop != null && collapsed
    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurEnabled = blurActive) {
                SmallTopAppBar(
                    title = stringResource(R.string.pref_screen_about),
                    scrollBehavior = scrollBehavior,
                    color = if (blurActive || !collapsed) Color.Transparent
                    else MiuixTheme.colorScheme.surface,
                    titleColor = MiuixTheme.colorScheme.onSurface.copy(
                        alpha = ((scrollProgress - 0.35f) / 0.65f).coerceIn(0f, 1f)
                    ),
                    navigationIcon = { BackNavigationIcon(onBack) },
                )
            }
        }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier)
        ) {
            AboutContent(
                padding,
                scrollBehavior,
                listState,
                { scrollProgress },
                checking,
                onUpdate,
                onLibraries,
                onUrl,
                onAppInfo,
            )
        }
    }
}

@Composable
private fun AboutContent(
    padding: PaddingValues,
    scrollBehavior: ScrollBehavior,
    listState: LazyListState,
    progress: () -> Float,
    checking: Boolean,
    onUpdate: () -> Unit,
    onLibraries: () -> Unit,
    onUrl: (String) -> Unit,
    onAppInfo: () -> Unit,
) {
    val backdrop = rememberBlurBackdrop()
    val dark = LocalDarkTheme.current
    val density = LocalDensity.current
    val context = LocalContext.current

    @Suppress("DEPRECATION") val versionName = remember(context) {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
    }
    val direction = LocalLayoutDirection.current
    val logoTopPadding = padding.calculateTopPadding() + 40.dp
    var logoHeight by remember { mutableStateOf(300.dp) }
    val logoBlend = remember(dark) {
        if (dark) listOf(
            BlendColorEntry(Color(0xE6A1A1A1), BlurBlendMode.ColorDodge),
            BlendColorEntry(Color(0x4DE6E6E6), BlurBlendMode.LinearLight),
            BlendColorEntry(Color(0xFF1AF500), BlurBlendMode.Lab),
        )
        else listOf(
            BlendColorEntry(Color(0xCC4A4A4A), BlurBlendMode.ColorBurn),
            BlendColorEntry(Color(0xFF4F4F4F), BlurBlendMode.LinearLight),
            BlendColorEntry(Color(0xFF1AF200), BlurBlendMode.Lab),
        )
    }
    val logoEffect = if (backdrop != null) Modifier.textureBlur(
        backdrop = backdrop,
        shape = RoundedCornerShape(16.dp),
        blurRadius = 150f,
        colors = BlurDefaults.blurColors(blendColors = logoBlend),
        contentBlendMode = BlendMode.DstIn,
    )
    else Modifier
    BgEffectBackground(
        dynamicBackground = isRuntimeShaderSupported(),
        isFullSize = true,
        modifier = Modifier.fillMaxSize(),
        bgModifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier,
        alpha = { 1f - progress() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = logoTopPadding + 52.dp,
                    start = padding.calculateStartPadding(direction),
                    end = padding.calculateEndPadding(direction),
                )
                .onSizeChanged { with(density) { logoHeight = it.height.toDp() } },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(88.dp)
                    .graphicsLayer {
                        val p = ((progress() - 0.35f) / 0.15f).coerceIn(0f, 1f)
                        clip = true
                        shape = RoundedCornerShape(24.dp)
                        alpha = 1f - p
                        scaleX = 1f - p * 0.05f
                        scaleY = 1f - p * 0.05f
                    },
            ) {
                Icon(
                    painterResource(R.drawable.ic_switchai),
                    contentDescription = null,
                    modifier = Modifier
                        .size(88.dp)
                        .then(logoEffect),
                    tint = MiuixTheme.colorScheme.onBackground,
                )
            }
            Text(
                text = stringResource(R.string.app_name),
                color = MiuixTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 5.dp)
                    .graphicsLayer {
                        val p = ((progress() - 0.20f) / 0.15f).coerceIn(0f, 1f)
                        alpha = 1f - p
                        scaleX = 1f - p * 0.05f
                        scaleY = 1f - p * 0.05f
                    }
                    .then(logoEffect),
            )
            Text(
                text = versionName,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val p = ((progress() - 0.05f) / 0.15f).coerceIn(0f, 1f)
                        alpha = 1f - p
                        scaleX = 1f - p * 0.05f
                        scaleY = 1f - p * 0.05f
                    },
            )
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .pageScrollModifiers(scrollBehavior),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = padding.calculateStartPadding(direction),
                end = padding.calculateEndPadding(direction),
            ),
        ) {
            item("logoSpacer") {
                val appInfoLabel = stringResource(R.string.navigate_app_info)
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(
                            logoHeight + 52.dp + logoTopPadding - padding.calculateTopPadding() + 126.dp
                        )
                        .pointerInput(onAppInfo) { detectTapGestures { onAppInfo() } }
                        .semantics {
                            onClick(label = appInfoLabel) {
                                onAppInfo()
                                true
                            }
                        })
            }
            item("about") {
                Box {
                    Spacer(Modifier.fillParentMaxHeight())
                    Column(Modifier.padding(bottom = padding.calculateBottomPadding() + 12.dp)) {
                        AboutCard(backdrop) {
                            ArrowPreference(
                                title = stringResource(
                                    if (checking) R.string.pref_checking_updates
                                    else R.string.pref_check_updates
                                ),
                                enabled = !checking,
                                onClick = onUpdate,
                            )
                        }
                        AboutCard(backdrop, Modifier.padding(top = 12.dp)) {
                            ArrowPreference(
                                title = stringResource(R.string.pref_wstxda),
                                endActions = { ValueText(stringResource(R.string.pref_developer)) },
                                onClick = { onUrl(Constants.DEVELOPER_URL) },
                            )
                            ArrowPreference(
                                title = stringResource(R.string.pref_source_code),
                                endActions = { ValueText(stringResource(R.string.pref_github)) },
                                onClick = { onUrl(Constants.SOURCE_URL) },
                            )
                        }
                        AboutCard(backdrop, Modifier.padding(top = 12.dp)) {
                            ArrowPreference(
                                title = stringResource(R.string.pref_license),
                                endActions = {
                                    ValueText(stringResource(R.string.pref_license_version))
                                },
                                onClick = {
                                    onUrl(Constants.LICENSE_URL)
                                },
                            )
                            ArrowPreference(
                                title = stringResource(R.string.pref_used_library),
                                onClick = onLibraries,
                            )
                        }
                    }
                }
            }
        }
        VerticalScrollBar(
            adapter = rememberScrollBarAdapter(listState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            trackPadding = padding,
        )
    }
}

@Composable
private fun AboutCard(
    backdrop: LayerBackdrop?,
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    val dark = LocalDarkTheme.current
    val blend = remember(dark) {
        if (dark) listOf(
            BlendColorEntry(Color(0x4DA9A9A9), BlurBlendMode.Luminosity),
            BlendColorEntry(Color(0x1A9C9C9C), BlurBlendMode.PlusDarker),
        )
        else listOf(
            BlendColorEntry(Color(0x340034F9), BlurBlendMode.Overlay),
            BlendColorEntry(Color(0xB3FFFFFF), BlurBlendMode.HardLight),
        )
    }
    Card(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .then(
                if (backdrop != null) Modifier.textureBlur(
                    backdrop = backdrop,
                    shape = RoundedCornerShape(16.dp),
                    blurRadius = 60f,
                    colors = BlurDefaults.blurColors(blendColors = blend),
                )
                else Modifier
            ),
        colors = CardDefaults.defaultColors(
            if (backdrop != null) Color.Transparent
            else MiuixTheme.colorScheme.surfaceContainer,
            Color.Transparent,
        ),
        content = content,
    )
}

@Composable
private fun ValueText(text: String) {
    Text(
        text,
        style = MiuixTheme.textStyles.body2,
        color = MiuixTheme.colorScheme.onSurfaceVariantActions,
    )
}