// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package com.wstxda.switchai.ui.components.effects

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.wstxda.switchai.data.TopBarBlurStyle
import com.wstxda.switchai.ui.theme.LocalAppearance
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.ProgressiveBlur
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.progressiveTextureBlur
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun rememberBlurBackdrop(): LayerBackdrop? {
    if (!LocalAppearance.current.blur || !isRuntimeShaderSupported()) return null
    val surface = MiuixTheme.colorScheme.surface
    return rememberLayerBackdrop {
        drawRect(surface)
        drawContent()
    }
}

@Composable
fun BlurredBar(
    backdrop: LayerBackdrop?,
    modifier: Modifier = Modifier,
    blurEnabled: Boolean = backdrop != null,
    blurStyle: TopBarBlurStyle = LocalAppearance.current.topBarBlurStyle,
    content: @Composable () -> Unit,
) {
    val progressive = blurStyle == TopBarBlurStyle.Progressive
    val active = blurEnabled && backdrop != null
    val colors = BlurDefaults.blurColors(
        blendColors = listOf(
            BlendColorEntry(
                MiuixTheme.colorScheme.surface.copy(alpha = if (progressive) 0.3f else 0.8f)
            )
        )
    )
    Box(
        modifier = if (active && !progressive) {
            modifier.textureBlur(
                backdrop = backdrop,
                shape = RectangleShape,
                blurRadius = 25f,
                colors = colors,
            )
        } else modifier
    ) {
        if (active && progressive) {
            Box(
                Modifier
                    .matchParentSize()
                    .progressiveTextureBlur(
                        backdrop = backdrop,
                        shape = RectangleShape,
                        gradient = ProgressiveBlur.Top.copy(curve = 2.2f),
                        blurRadius = 10f,
                        colors = colors,
                    )
            )
        }
        content()
    }
}