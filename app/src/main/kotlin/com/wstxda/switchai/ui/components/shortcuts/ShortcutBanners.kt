package com.wstxda.switchai.ui.components.shortcuts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.data.ShortcutBanner
import top.yukonga.miuix.kmp.theme.Colors
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun ShortcutBannerImage(banner: ShortcutBanner) {
    val colors = MiuixTheme.colorScheme
    val image = remember(
        banner,
        colors.surface,
        colors.onSurface,
        colors.outline,
        colors.surfaceContainer,
        colors.surfaceContainerHigh,
        colors.primary,
        colors.onPrimary,
        colors.tertiaryContainer,
        colors.secondaryContainer,
        colors.onSecondaryContainer,
        colors.primaryContainer,
    ) {
        when (banner) {
            ShortcutBanner.Tile -> tileBanner(colors)
            ShortcutBanner.Widget -> widgetBanner(colors)
        }
    }
    Image(
        imageVector = image,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(280f / 160f),
    )
}

private fun tileBanner(colors: Colors): ImageVector = ImageVector.Builder(
    name = "ShortcutTile",
    defaultWidth = 280.dp,
    defaultHeight = 160.dp,
    viewportWidth = 280f,
    viewportHeight = 160f,
).apply {
    addPath(
        pathData = PathParser().parsePathString("M230 92c2.2 0 4 1.8 4 4v29c0 2.2-1.8 4-4 4V92Z")
            .toNodes(),
        fill = SolidColor(colors.onSurface),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M56 43c0-13.81 11.19-25 25-25h120c13.81 0 25 11.19 25 25v306c0 13.81-11.19 25-25 25h-120c-13.81 0-25-11.19-25-25z"
        ).toNodes(),
        fill = SolidColor(colors.surface),
        stroke = SolidColor(colors.outline),
        strokeLineWidth = 10f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M69 111c0-7.18 5.82-13 13-13h42c7.18 0 13 5.82 13 13v0c0 7.18-5.82 13-13 13h-42c-7.18 0-13-5.82-13-13z"
        ).toNodes(),
        fill = SolidColor(colors.surfaceContainerHigh),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M141 111c0-7.18 5.82-13 13-13h42c7.18 0 13 5.82 13 13v0c0 7.18-5.82 13-13 13h-42c-7.18 0-13-5.82-13-13z"
        ).toNodes(),
        fill = SolidColor(colors.primary),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M69 141c0-7.18 5.82-13 13-13h42c7.18 0 13 5.82 13 13v0c0 7.18-5.82 13-13 13h-42c-7.18 0-13-5.82-13-13z"
        ).toNodes(),
        fill = SolidColor(colors.surfaceContainerHigh),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M141 141c0-7.18 5.82-13 13-13h42c7.18 0 13 5.82 13 13v0c0 7.18-5.82 13-13 13h-42c-7.18 0-13-5.82-13-13z"
        ).toNodes(),
        fill = SolidColor(colors.surfaceContainerHigh),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M69 41c0-3.87 3.13-7 7-7h25c3.87 0 7 3.13 7 7v0c0 3.87-3.13 7-7 7h-25c-3.87 0-7-3.13-7-7z"
        ).toNodes(),
        fill = SolidColor(colors.surfaceContainer),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M69 80c0-4.97 4.03-9 9-9h122c4.97 0 9 4.03 9 9v0c0 4.97-4.03 9-9 9h-122c-4.97 0-9-4.03-9-9z"
        ).toNodes(),
        fill = SolidColor(colors.surfaceContainerHigh),
    )
    addPath(
        pathData = PathParser().parsePathString("M202 34A7 7 0 1 0 202 48 7 7 0 1 0 202 34z")
            .toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M154.58 115.61l0.31-0.7c0.54-1.25 1.53-2.26 2.78-2.82l0.94-0.42c0.52-0.23 0.52-0.98 0-1.2l-0.9-0.4c-1.27-0.57-2.28-1.61-2.8-2.9l-0.32-0.77c-0.05-0.12-0.13-0.22-0.24-0.3-0.1-0.06-0.22-0.1-0.35-0.1-0.13 0-0.25 0.04-0.35 0.1-0.11 0.08-0.2 0.18-0.24 0.3l-0.31 0.76c-0.53 1.3-1.54 2.34-2.82 2.9l-0.9 0.4c-0.5 0.23-0.5 0.98 0 1.21l0.96 0.42c1.24 0.56 2.23 1.56 2.77 2.82l0.3 0.7c0.06 0.12 0.14 0.22 0.24 0.28 0.1 0.07 0.23 0.11 0.35 0.11 0.12 0 0.25-0.04 0.35-0.1 0.1-0.07 0.19-0.17 0.23-0.29Z"
        ).toNodes(),
        fill = SolidColor(colors.onPrimary),
    )
}.build()

private fun widgetBanner(colors: Colors): ImageVector = ImageVector.Builder(
    name = "ShortcutWidget",
    defaultWidth = 280.dp,
    defaultHeight = 160.dp,
    viewportWidth = 280f,
    viewportHeight = 160f,
).apply {
    addPath(
        pathData = PathParser().parsePathString(
            "M55-183c0-13.81 11.19-25 25-25h120c13.81 0 25 11.19 25 25v300c0 13.81-11.19 25-25 25h-120c-13.81 0-25-11.19-25-25z"
        ).toNodes(),
        fill = SolidColor(colors.surface),
        stroke = SolidColor(colors.outline),
        strokeLineWidth = 10f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M119 130c0-0.55 0.45-1 1-1h40c0.55 0 1 0.45 1 1v0c0 0.55-0.45 1-1 1h-40c-0.55 0-1-0.45-1-1z"
        ).toNodes(),
        fill = SolidColor(colors.onSurface),
    )
    addPath(
        pathData = PathParser().parsePathString("M87 82A14 14 0 1 0 87 110 14 14 0 1 0 87 82z")
            .toNodes(),
        fill = SolidColor(colors.surfaceContainerHigh),
    )
    addPath(
        pathData = PathParser().parsePathString("M123 82A14 14 0 1 0 123 110 14 14 0 1 0 123 82z")
            .toNodes(),
        fill = SolidColor(colors.surfaceContainerHigh),
    )
    addPath(
        pathData = PathParser().parsePathString("M159 82A14 14 0 1 0 159 110 14 14 0 1 0 159 82z")
            .toNodes(),
        fill = SolidColor(colors.surfaceContainerHigh),
    )
    addPath(
        pathData = PathParser().parsePathString("M195 82A14 14 0 1 0 195 110 14 14 0 1 0 195 82z")
            .toNodes(),
        fill = SolidColor(colors.surfaceContainerHigh),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M92.33 102.57c-1.45 0-2.9-0.32-4.31-0.95-1.43-0.64-2.73-1.54-3.9-2.7-1.18-1.18-2.09-2.48-2.72-3.91-0.63-1.43-0.95-2.87-0.95-4.33 0-0.35 0.12-0.65 0.35-0.89 0.23-0.24 0.53-0.36 0.88-0.36h2.69c0.4 0 0.74 0.1 1 0.3 0.25 0.2 0.42 0.5 0.51 0.87l0.42 1.98c0.07 0.35 0.06 0.64 0 0.89-0.08 0.24-0.23 0.46-0.45 0.65l-1.72 1.5c0.18 0.29 0.39 0.58 0.63 0.87 0.24 0.3 0.52 0.6 0.84 0.91 0.29 0.29 0.57 0.54 0.83 0.76 0.27 0.22 0.54 0.4 0.8 0.56l1.69-1.64c0.22-0.2 0.47-0.35 0.74-0.42s0.57-0.08 0.89-0.01l1.85 0.42c0.39 0.1 0.68 0.28 0.88 0.51 0.19 0.24 0.29 0.54 0.29 0.92v2.82c0 0.35-0.12 0.65-0.36 0.89s-0.53 0.36-0.88 0.36Z"
        ).toNodes(),
        fill = SolidColor(colors.onSecondaryContainer),
        fillAlpha = 0.4f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M118.43 101.57l-0.86 0.86c-0.34 0.34-0.72 0.41-1.15 0.24-0.44-0.18-0.65-0.5-0.65-0.97v-9.83c0-0.6 0.2-1.09 0.6-1.5 0.41-0.4 0.9-0.6 1.5-0.6h10.26c0.6 0 1.09 0.2 1.5 0.6 0.4 0.41 0.6 0.9 0.6 1.5v7.6c0 0.59-0.2 1.08-0.6 1.49-0.41 0.4-0.9 0.6-1.5 0.6h-9.7Zm1.24-3.24h4c0.19 0 0.34-0.06 0.47-0.19 0.13-0.13 0.2-0.28 0.2-0.47 0-0.2-0.07-0.35-0.2-0.48S123.86 97 123.67 97h-4c-0.2 0-0.35 0.06-0.48 0.2-0.13 0.12-0.19 0.28-0.19 0.47 0 0.19 0.06 0.34 0.2 0.47 0.12 0.13 0.28 0.2 0.47 0.2Zm0-2h6.66c0.2 0 0.35-0.06 0.48-0.19s0.19-0.28 0.19-0.47c0-0.2-0.06-0.35-0.2-0.48-0.12-0.13-0.28-0.19-0.47-0.19h-6.66c-0.2 0-0.35 0.06-0.48 0.2-0.13 0.12-0.19 0.28-0.19 0.47 0 0.19 0.06 0.34 0.2 0.47 0.12 0.13 0.28 0.2 0.47 0.2Zm0-2h6.66c0.2 0 0.35-0.06 0.48-0.19s0.19-0.28 0.19-0.47c0-0.2-0.06-0.35-0.2-0.48-0.12-0.13-0.28-0.19-0.47-0.19h-6.66c-0.2 0-0.35 0.06-0.48 0.2-0.13 0.12-0.19 0.28-0.19 0.47 0 0.19 0.06 0.34 0.2 0.47 0.12 0.13 0.28 0.2 0.47 0.2Z"
        ).toNodes(),
        fill = SolidColor(colors.onSecondaryContainer),
        fillAlpha = 0.4f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M156.2 102.66c-0.88-0.38-1.64-0.9-2.3-1.56-0.66-0.66-1.18-1.42-1.56-2.3-0.38-0.88-0.57-1.81-0.57-2.8 0-0.99 0.19-1.92 0.57-2.8 0.38-0.88 0.9-1.64 1.56-2.3 0.66-0.66 1.42-1.18 2.3-1.56 0.88-0.38 1.81-0.57 2.8-0.57 0.99 0 1.92 0.19 2.8 0.57 0.88 0.38 1.64 0.9 2.3 1.56 0.66 0.66 1.18 1.42 1.56 2.3 0.38 0.88 0.57 1.81 0.57 2.8 0 0.99-0.19 1.92-0.57 2.8-0.38 0.88-0.9 1.64-1.56 2.3-0.66 0.66-1.42 1.18-2.3 1.56-0.88 0.38-1.81 0.57-2.8 0.57-0.99 0-1.92-0.19-2.8-0.57Zm2.77-1.54c0.24-0.38 0.47-0.78 0.68-1.21 0.21-0.43 0.38-0.88 0.52-1.34h-2.35c0.13 0.46 0.3 0.91 0.49 1.34 0.2 0.43 0.41 0.83 0.66 1.2Zm-1.65-0.27c-0.23-0.34-0.4-0.71-0.53-1.1-0.13-0.4-0.24-0.8-0.34-1.18h-1.87c0.3 0.53 0.67 1 1.14 1.4 0.46 0.4 1 0.7 1.6 0.88Zm3.31 0c0.6-0.19 1.14-0.48 1.62-0.88 0.48-0.4 0.86-0.87 1.15-1.4h-1.87c-0.1 0.39-0.22 0.78-0.35 1.17-0.14 0.4-0.32 0.77-0.55 1.11Zm-6.6-3.57h2.15c-0.03-0.22-0.06-0.43-0.07-0.64-0.02-0.2-0.03-0.42-0.03-0.64 0-0.23 0.01-0.45 0.03-0.65 0.01-0.2 0.04-0.41 0.07-0.63h-2.15c-0.05 0.2-0.1 0.42-0.12 0.62-0.03 0.2-0.04 0.43-0.04 0.66 0 0.22 0.01 0.44 0.04 0.65l0.12 0.63Zm3.49 0h2.95l0.06-0.64V96v-0.65l-0.06-0.63h-2.95c-0.04 0.22-0.06 0.43-0.08 0.63L157.42 96c0 0.22 0 0.44 0.02 0.64s0.04 0.42 0.08 0.64Zm4.28 0h2.13c0.06-0.2 0.1-0.42 0.13-0.63 0.03-0.21 0.04-0.43 0.04-0.65 0-0.23-0.01-0.45-0.04-0.66-0.03-0.2-0.07-0.41-0.13-0.62h-2.13l0.05 0.63 0.02 0.65c0 0.22 0 0.44-0.02 0.64l-0.05 0.64Zm-0.27-3.86h1.87c-0.29-0.54-0.67-1-1.15-1.4-0.48-0.39-1.02-0.68-1.62-0.87 0.23 0.34 0.4 0.71 0.55 1.1 0.13 0.39 0.25 0.78 0.35 1.17Zm-3.71 0h2.35c-0.13-0.47-0.3-0.91-0.5-1.34-0.21-0.42-0.45-0.82-0.7-1.2-0.25 0.38-0.47 0.78-0.66 1.2-0.2 0.43-0.36 0.87-0.5 1.34Zm-3.24 0h1.87c0.1-0.4 0.21-0.78 0.34-1.17 0.13-0.39 0.3-0.76 0.53-1.1-0.6 0.19-1.14 0.48-1.6 0.88-0.47 0.39-0.85 0.85-1.14 1.39Z"
        ).toNodes(),
        fill = SolidColor(colors.onSecondaryContainer),
        fillAlpha = 0.4f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M195 99.8c0.87 0 1.6-0.3 2.22-0.92 0.6-0.6 0.91-1.35 0.91-2.21 0-0.87-0.3-1.6-0.91-2.22-0.61-0.61-1.35-0.92-2.22-0.92-0.87 0-1.6 0.3-2.22 0.92-0.6 0.61-0.91 1.35-0.91 2.22 0 0.86 0.3 1.6 0.91 2.21 0.61 0.61 1.35 0.92 2.22 0.92Zm0-1.77c-0.39 0-0.71-0.13-0.97-0.39-0.27-0.26-0.4-0.58-0.4-0.97 0-0.4 0.13-0.72 0.4-0.98 0.26-0.26 0.58-0.39 0.97-0.39s0.71 0.13 0.97 0.4c0.27 0.25 0.4 0.58 0.4 0.97s-0.13 0.71-0.4 0.97c-0.26 0.26-0.58 0.4-0.97 0.4Zm-5.13 4.54c-0.6 0-1.09-0.2-1.5-0.61-0.4-0.4-0.6-0.9-0.6-1.5v-7.6c0-0.58 0.2-1.08 0.6-1.48 0.41-0.41 0.9-0.61 1.5-0.61h1.6l0.86-0.79c0.2-0.17 0.41-0.31 0.66-0.4 0.25-0.1 0.5-0.15 0.76-0.15h2.5c0.26 0 0.5 0.05 0.76 0.14 0.25 0.1 0.47 0.24 0.66 0.41l0.86 0.79h1.6c0.6 0 1.09 0.2 1.5 0.6 0.4 0.41 0.6 0.9 0.6 1.5v7.6c0 0.59-0.2 1.08-0.6 1.49-0.41 0.4-0.9 0.6-1.5 0.6h-10.26Z"
        ).toNodes(),
        fill = SolidColor(colors.onSecondaryContainer),
        fillAlpha = 0.4f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M134.62 16H81.39C76.75 16 73 19.74 73 24.36v27.28c0 4.62 3.75 8.36 8.39 8.36h53.23c4.63 0 8.38-3.74 8.38-8.36V24.36c0-4.62-3.75-8.36-8.38-8.36Z"
        ).toNodes(),
        fill = SolidColor(colors.secondaryContainer),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M104.72 21.45H89.4c-6.04 0-10.94 4.89-10.94 10.91V44c0 6.02 4.9 10.9 10.94 10.9h15.3c6.05 0 10.95-4.88 10.95-10.9V32.36c0-6.02-4.9-10.9-10.94-10.9Z"
        ).toNodes(),
        fill = SolidColor(colors.primary),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M137.53 30.73c0-5.12-4.16-9.28-9.3-9.28-5.13 0-9.3 4.16-9.3 9.28v14.9c0 5.13 4.17 9.28 9.3 9.28 5.14 0 9.3-4.15 9.3-9.27V30.73Z"
        ).toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M97.77 43.77l0.38-0.85c0.65-1.52 1.85-2.74 3.37-3.41l1.15-0.51c0.63-0.28 0.63-1.19 0-1.46l-1.09-0.49c-1.55-0.69-2.77-1.95-3.42-3.52l-0.38-0.93c-0.06-0.14-0.16-0.26-0.29-0.35-0.12-0.08-0.27-0.13-0.43-0.13-0.15 0-0.3 0.05-0.43 0.13-0.13 0.09-0.22 0.21-0.28 0.35l-0.39 0.93c-0.64 1.57-1.86 2.83-3.42 3.52l-1.09 0.49c-0.62 0.27-0.62 1.18 0 1.46l1.16 0.5c1.51 0.68 2.71 1.9 3.37 3.42l0.37 0.85c0.06 0.14 0.16 0.26 0.29 0.35 0.12 0.08 0.27 0.12 0.42 0.12 0.15 0 0.3-0.04 0.43-0.12 0.12-0.09 0.22-0.2 0.28-0.35Z"
        ).toNodes(),
        fill = SolidColor(colors.primaryContainer),
    )
}.build()