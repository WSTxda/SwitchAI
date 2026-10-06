package com.wstxda.switchai.ui.components.tutorial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.data.TutorialBanner
import top.yukonga.miuix.kmp.theme.Colors
import top.yukonga.miuix.kmp.theme.MiuixTheme

// Original tutorial artwork, with its colors supplied by the current Miuix palette.

@Composable
fun rememberTutorialBanner(banner: TutorialBanner): ImageVector {
    val colors = MiuixTheme.colorScheme
    return remember(
        banner,
        colors.surfaceContainer,
        colors.outline,
        colors.tertiaryContainer,
        colors.onSurface,
        colors.primary,
        colors.onSurfaceVariantSummary,
        colors.onSurfaceContainer,
    ) {
        when (banner) {
            TutorialBanner.Gestures -> gesturesBanner(colors)
            TutorialBanner.HomeButton -> buttonBanner(colors)
            TutorialBanner.PowerButton -> powerBanner(colors)
            TutorialBanner.Headset -> headsetBanner(colors)
        }
    }
}

private fun gesturesBanner(colors: Colors): ImageVector = ImageVector.Builder(
    name = "TutorialGestures",
    defaultWidth = 280.dp,
    defaultHeight = 160.dp,
    viewportWidth = 280f,
    viewportHeight = 160f,
).apply {
    addPath(
        pathData = PathParser().parsePathString(
            "M55-183c0-13.81 11.19-25 25-25h120c13.81 0 25 11.19 25 25v300c0 13.81-11.19 25-25 25h-120c-13.81 0-25-11.19-25-25z"
        ).toNodes(),
        fill = SolidColor(colors.surfaceContainer),
        stroke = SolidColor(colors.outline),
        strokeLineWidth = 10f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M60 65h160v52c0 11.05-8.95 20-20 20H80c-11.05 0-20-8.95-20-20V65Z"
        ).toNodes(),
        fill = Brush.linearGradient(
            0.0f to Color.Transparent,
            1.0f to colors.tertiaryContainer,
            start = Offset(140.0f, 65.0f),
            end = Offset(140.0f, 137.0f),
        ),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M219 108v9c0 10.5-8.5 19-19 19H80c-10.5 0-19-8.5-19-19v-9h158Z"
        ).toNodes(),
        fill = null,
        stroke = Brush.linearGradient(
            0.0f to colors.primary,
            1.0f to Color.Transparent,
            start = Offset(140.0f, 137.0f),
            end = Offset(140.0f, 118.5f),
        ),
        strokeLineWidth = 2f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M119 130c0-0.55 0.45-1 1-1h40c0.55 0 1 0.45 1 1v0c0 0.55-0.45 1-1 1h-40c-0.55 0-1-0.45-1-1z"
        ).toNodes(),
        fill = SolidColor(colors.onSurface),
        stroke = null,
    )
}.build()

private fun buttonBanner(colors: Colors): ImageVector = ImageVector.Builder(
    name = "TutorialButton",
    defaultWidth = 280.dp,
    defaultHeight = 160.dp,
    viewportWidth = 280f,
    viewportHeight = 160f,
).apply {
    addPath(
        pathData = PathParser().parsePathString(
            "M55-183c0-13.81 11.19-25 25-25h120c13.81 0 25 11.19 25 25v300c0 13.81-11.19 25-25 25h-120c-13.81 0-25-11.19-25-25z"
        ).toNodes(),
        fill = SolidColor(colors.surfaceContainer),
        stroke = SolidColor(colors.outline),
        strokeLineWidth = 10f,
    )
    addPath(
        pathData = PathParser().parsePathString("M140 106A18 18 0 1 0 140 142 18 18 0 1 0 140 106z")
            .toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
        stroke = null,
        fillAlpha = 0.4f,
    )
    addPath(
        pathData = PathParser().parsePathString("M140 115A9 9 0 1 0 140 133 9 9 0 1 0 140 115z")
            .toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString("M174 120H182V128H174z").toNodes(),
        fill = SolidColor(colors.onSurface),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString("M99 124l7-4v8l-7-4Z").toNodes(),
        fill = SolidColor(colors.onSurface),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString("M140 120A4 4 0 1 0 140 128 4 4 0 1 0 140 120z")
            .toNodes(),
        fill = SolidColor(colors.primary),
        stroke = null,
    )
}.build()

private fun powerBanner(colors: Colors): ImageVector = ImageVector.Builder(
    name = "TutorialPower",
    defaultWidth = 280.dp,
    defaultHeight = 160.dp,
    viewportWidth = 280f,
    viewportHeight = 160f,
).apply {
    addPath(
        pathData = PathParser().parsePathString("M234 74A36 36 0 1 0 234 146 36 36 0 1 0 234 74z")
            .toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
        stroke = null,
        fillAlpha = 0.4f,
    )
    addPath(
        pathData = PathParser().parsePathString("M234 98A12 12 0 1 0 234 122 12 12 0 1 0 234 98z")
            .toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString("M230 92c2.2 0 4 1.8 4 4v29c0 2.2-1.8 4-4 4V92Z")
            .toNodes(),
        fill = SolidColor(colors.primary),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M55 43c0-13.81 11.19-25 25-25h120c13.81 0 25 11.19 25 25v306c0 13.81-11.19 25-25 25h-120c-13.81 0-25-11.19-25-25z"
        ).toNodes(),
        fill = SolidColor(colors.surfaceContainer),
        stroke = SolidColor(colors.outline),
        strokeLineWidth = 10f,
    )
}.build()

private fun headsetBanner(colors: Colors): ImageVector = ImageVector.Builder(
    name = "TutorialHeadset",
    defaultWidth = 280.dp,
    defaultHeight = 160.dp,
    viewportWidth = 280f,
    viewportHeight = 160f,
).apply {
    addPath(
        pathData = PathParser().parsePathString(
            "M58.33 107.4c-8.77 8.78-22.99 8.78-31.75 0-8.77-8.77-8.77-23 0-31.78 8.76-8.78 22.98-8.78 31.75 0 8.77 8.77 8.77 23 0 31.79Z"
        ).toNodes(),
        fill = SolidColor(Color.Black),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M53.38 56.24c13.64-13.65 35.75-13.65 49.4 0 13.63 13.66 13.63 35.8 0 49.45l-7.07 7.07c-13.63 13.65-35.75 13.65-49.39 0-13.64-13.66-13.64-35.8 0-49.45l7.06-7.07Z"
        ).toNodes(),
        fill = SolidColor(colors.onSurfaceVariantSummary),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M99.24 102.16c-11.69 11.7-30.64 11.7-42.33 0-11.7-11.7-11.7-30.68 0-42.39 11.69-11.7 30.64-11.7 42.33 0 11.7 11.7 11.7 30.69 0 42.4Z"
        ).toNodes(),
        fill = SolidColor(colors.onSurfaceContainer),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M54.18 107.29c-0.78 0.78-2.04 0.78-2.82 0-0.78-0.78-0.78-2.05 0-2.83 0.78-0.78 2.04-0.78 2.82 0 0.78 0.78 0.78 2.05 0 2.83Z"
        ).toNodes(),
        fill = SolidColor(colors.onSurfaceContainer),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M78.5 24A56.5 56.5 0 1 0 78.5 137 56.5 56.5 0 1 0 78.5 24z"
        ).toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
        stroke = null,
        fillAlpha = 0.4f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M78.5 55.39A25.11 25.11 0 1 0 78.5 105.61 25.11 25.11 0 1 0 78.5 55.39z"
        ).toNodes(),
        fill = SolidColor(colors.primary),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M222.67 107.4c8.77 8.78 22.99 8.78 31.75 0 8.77-8.77 8.77-23 0-31.78-8.76-8.78-22.98-8.78-31.75 0-8.76 8.77-8.76 23 0 31.79Z"
        ).toNodes(),
        fill = SolidColor(Color.Black),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M227.62 56.24c-13.64-13.65-35.75-13.65-49.4 0-13.63 13.66-13.63 35.8 0 49.45l7.06 7.07c13.64 13.65 35.76 13.65 49.4 0 13.63-13.66 13.63-35.8 0-49.45l-7.06-7.07Z"
        ).toNodes(),
        fill = SolidColor(colors.onSurfaceVariantSummary),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M181.76 102.16c11.69 11.7 30.64 11.7 42.33 0 11.7-11.7 11.7-30.68 0-42.39-11.69-11.7-30.64-11.7-42.33 0-11.7 11.7-11.7 30.69 0 42.4Z"
        ).toNodes(),
        fill = SolidColor(colors.onSurfaceContainer),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M226.82 107.29c0.78 0.78 2.04 0.78 2.82 0 0.78-0.78 0.78-2.05 0-2.83-0.78-0.78-2.04-0.78-2.82 0-0.78 0.78-0.78 2.05 0 2.83Z"
        ).toNodes(),
        fill = SolidColor(colors.onSurfaceContainer),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M203.5 24A56.5 56.5 0 1 0 203.5 137 56.5 56.5 0 1 0 203.5 24z"
        ).toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
        stroke = null,
        fillAlpha = 0.4f,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M203.5 55.39A25.11 25.11 0 1 0 203.5 105.61 25.11 25.11 0 1 0 203.5 55.39z"
        ).toNodes(),
        fill = SolidColor(colors.primary),
        stroke = null,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M202.95 65.19c2.73 0 5.28 0.8 7.41 2.18 0.23 0.14 0.49 0.24 0.76 0.3 0.26 0.04 0.54 0.03 0.8-0.03 0.27-0.06 0.52-0.17 0.75-0.33 0.22-0.15 0.4-0.35 0.55-0.59 0.15-0.23 0.25-0.49 0.3-0.76 0.04-0.27 0.04-0.55-0.02-0.81-0.12-0.55-0.45-1.02-0.91-1.32-2.72-1.74-5.84-2.72-9.06-2.82-3.22-0.1-6.4 0.67-9.22 2.23-2.82 1.57-5.17 3.87-6.8 6.66-1.65 2.8-2.51 6-2.51 9.24 0 4.65 0.92 9.08 2.59 13.13 0.2 0.51 0.61 0.92 1.12 1.13 0.5 0.21 1.08 0.21 1.58 0 0.51-0.21 0.92-0.62 1.12-1.14 0.21-0.51 0.21-1.09 0-1.6-1.46-3.54-2.27-7.43-2.27-11.52 0-3.7 1.46-7.25 4.05-9.86 2.59-2.62 6.1-4.1 9.76-4.1Zm12.3 3.58c-0.23 0.14-0.43 0.34-0.59 0.56-0.16 0.23-0.26 0.48-0.32 0.75s-0.07 0.55-0.02 0.82c0.05 0.27 0.15 0.53 0.3 0.76 1.4 2.23 2.15 4.83 2.14 7.48 0 0.56 0.22 1.1 0.61 1.48 0.4 0.4 0.92 0.62 1.47 0.62 0.55 0 1.07-0.22 1.46-0.62 0.4-0.39 0.6-0.92 0.6-1.48 0-3.57-1.02-6.92-2.8-9.73-0.14-0.23-0.33-0.43-0.55-0.6-0.22-0.15-0.48-0.26-0.74-0.32-0.27-0.06-0.54-0.07-0.81-0.02s-0.52 0.15-0.75 0.3Zm-12.3 4.44c1.56 0 3.05 0.63 4.16 1.74 1.1 1.11 1.71 2.62 1.71 4.2 0 2.68 1.06 5.25 2.94 7.15 1.88 1.9 4.42 2.96 7.08 2.96h0.1c0.27 0 0.54-0.06 0.8-0.16 0.24-0.11 0.47-0.27 0.66-0.46 0.2-0.2 0.34-0.43 0.45-0.68 0.1-0.26 0.15-0.53 0.15-0.8 0-0.28-0.06-0.55-0.16-0.8-0.1-0.26-0.26-0.49-0.46-0.68-0.19-0.2-0.42-0.35-0.67-0.45-0.25-0.1-0.52-0.16-0.8-0.16h-0.08c-0.77 0-1.53-0.15-2.24-0.45-0.71-0.3-1.36-0.73-1.9-1.28-0.55-0.55-0.98-1.2-1.28-1.93-0.3-0.72-0.44-1.49-0.44-2.27 0-2.68-1.06-5.25-2.94-7.15-1.88-1.9-4.42-2.96-7.08-2.96-2.65 0-5.2 1.06-7.08 2.96s-2.93 4.47-2.93 7.15v0.65c0.01 0.28 0.07 0.55 0.18 0.8 0.11 0.25 0.27 0.48 0.47 0.67 0.2 0.19 0.43 0.34 0.68 0.44 0.26 0.1 0.53 0.14 0.8 0.14 0.27-0.01 0.54-0.07 0.79-0.18 0.25-0.11 0.47-0.27 0.66-0.47 0.19-0.2 0.33-0.44 0.43-0.7 0.1-0.25 0.15-0.52 0.14-0.8v-0.55c0-0.78 0.14-1.55 0.44-2.27s0.73-1.37 1.27-1.92c0.55-0.55 1.2-0.99 1.9-1.29 0.72-0.3 1.48-0.45 2.25-0.45Zm0.02 3.84c-0.55 0-1.08 0.22-1.47 0.61-0.38 0.4-0.6 0.93-0.6 1.48 0 3.76 1.15 7.43 3.3 10.5 2.15 3.06 5.19 5.38 8.7 6.62 0.25 0.1 0.52 0.13 0.8 0.12 0.27-0.02 0.53-0.09 0.78-0.2 0.24-0.12 0.46-0.3 0.64-0.5 0.19-0.2 0.33-0.44 0.42-0.7 0.09-0.26 0.13-0.53 0.11-0.8-0.01-0.28-0.08-0.55-0.2-0.8-0.12-0.25-0.28-0.47-0.49-0.65-0.2-0.19-0.44-0.33-0.7-0.42-2.69-0.96-5.03-2.74-6.68-5.1-1.65-2.36-2.54-5.18-2.54-8.07 0-0.55-0.22-1.08-0.6-1.48-0.4-0.39-0.92-0.61-1.47-0.61Zm-7.32 8.01c-0.26 0.09-0.5 0.23-0.7 0.41-0.2 0.19-0.37 0.4-0.5 0.65-0.1 0.25-0.18 0.52-0.2 0.8-0.01 0.27 0.03 0.55 0.12 0.8 1.6 4.67 4.49 8.78 8.3 11.87 0.44 0.32 0.98 0.46 1.5 0.4 0.54-0.08 1.03-0.35 1.36-0.78 0.34-0.42 0.5-0.96 0.44-1.5-0.04-0.54-0.3-1.04-0.7-1.4-3.22-2.58-5.64-6.04-6.99-9.96-0.18-0.52-0.56-0.95-1.05-1.2-0.5-0.23-1.06-0.27-1.58-0.09"
        ).toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
        stroke = null,
        pathFillType = PathFillType.EvenOdd,
    )
    addPath(
        pathData = PathParser().parsePathString(
            "M78.05 65.19c-2.73 0-5.27 0.8-7.41 2.18-0.23 0.14-0.49 0.24-0.76 0.3-0.26 0.04-0.54 0.03-0.8-0.03-0.27-0.06-0.52-0.17-0.75-0.33-0.22-0.15-0.4-0.35-0.55-0.59-0.15-0.23-0.25-0.49-0.3-0.76-0.04-0.27-0.04-0.55 0.02-0.81 0.12-0.55 0.45-1.02 0.91-1.32 2.72-1.74 5.84-2.72 9.06-2.82 3.22-0.1 6.4 0.67 9.22 2.23 2.82 1.57 5.17 3.87 6.8 6.66 1.65 2.8 2.51 6 2.51 9.24 0 4.65-0.92 9.08-2.59 13.13-0.2 0.51-0.61 0.92-1.12 1.13-0.5 0.21-1.08 0.21-1.59 0-0.5-0.21-0.9-0.62-1.11-1.14-0.21-0.51-0.21-1.09 0-1.6 1.46-3.54 2.27-7.43 2.27-11.52 0-3.7-1.46-7.25-4.05-9.86-2.59-2.62-6.1-4.1-9.76-4.1Zm-12.3 3.58c0.23 0.14 0.43 0.34 0.59 0.56 0.15 0.23 0.27 0.48 0.32 0.75 0.06 0.27 0.07 0.55 0.02 0.82-0.05 0.27-0.14 0.53-0.3 0.76-1.4 2.23-2.15 4.83-2.15 7.48 0 0.56-0.21 1.1-0.6 1.48-0.4 0.4-0.92 0.62-1.47 0.62-0.55 0-1.07-0.22-1.46-0.62-0.4-0.39-0.6-0.92-0.6-1.48 0-3.57 1.02-6.92 2.8-9.73 0.14-0.23 0.33-0.43 0.55-0.6 0.22-0.15 0.48-0.26 0.74-0.32 0.27-0.06 0.54-0.07 0.81-0.02s0.52 0.15 0.75 0.3Zm12.3 4.44c-1.56 0-3.05 0.63-4.15 1.74-1.1 1.11-1.72 2.62-1.72 4.2 0 2.68-1.06 5.25-2.94 7.15-1.87 1.9-4.42 2.96-7.08 2.96h-0.1c-0.27 0-0.54-0.06-0.8-0.16-0.24-0.11-0.47-0.27-0.66-0.46-0.2-0.2-0.34-0.43-0.45-0.68-0.1-0.26-0.15-0.53-0.15-0.8 0-0.28 0.06-0.55 0.16-0.8 0.1-0.26 0.26-0.49 0.45-0.68 0.2-0.2 0.43-0.35 0.68-0.45 0.25-0.1 0.52-0.16 0.8-0.16h0.08c0.77 0 1.53-0.15 2.24-0.45 0.71-0.3 1.36-0.73 1.9-1.28 0.55-0.55 0.98-1.2 1.28-1.93 0.3-0.72 0.45-1.49 0.45-2.27 0-2.68 1.05-5.25 2.93-7.15 1.88-1.9 4.42-2.96 7.08-2.96 2.65 0 5.2 1.06 7.08 2.96s2.93 4.47 2.93 7.15v0.65c-0.01 0.28-0.07 0.55-0.18 0.8-0.11 0.25-0.27 0.48-0.47 0.67-0.2 0.19-0.43 0.34-0.68 0.44-0.26 0.1-0.53 0.14-0.8 0.14-0.27-0.01-0.54-0.07-0.79-0.18-0.25-0.11-0.47-0.27-0.66-0.47-0.19-0.2-0.33-0.44-0.43-0.7-0.1-0.25-0.15-0.52-0.14-0.8v-0.55c0-0.78-0.14-1.55-0.44-2.27s-0.73-1.37-1.27-1.92c-0.55-0.55-1.2-0.99-1.9-1.29-0.72-0.3-1.48-0.45-2.25-0.45Zm-0.02 3.84c0.55 0 1.08 0.22 1.47 0.61 0.39 0.4 0.6 0.93 0.6 1.48 0 3.76-1.15 7.43-3.3 10.5-2.15 3.06-5.18 5.38-8.7 6.62-0.25 0.1-0.52 0.13-0.8 0.12-0.27-0.02-0.53-0.09-0.78-0.2-0.24-0.12-0.46-0.3-0.64-0.5-0.19-0.2-0.33-0.44-0.42-0.7-0.09-0.26-0.13-0.53-0.11-0.8 0.01-0.28 0.08-0.55 0.2-0.8 0.12-0.25 0.28-0.47 0.49-0.65 0.2-0.19 0.43-0.33 0.7-0.42 2.69-0.96 5.03-2.74 6.68-5.1 1.65-2.36 2.54-5.18 2.54-8.07 0-0.55 0.22-1.08 0.6-1.48 0.4-0.39 0.92-0.61 1.47-0.61Zm7.32 8.01c0.26 0.09 0.5 0.23 0.7 0.41 0.2 0.19 0.37 0.4 0.5 0.65 0.1 0.25 0.18 0.52 0.2 0.8 0.01 0.27-0.03 0.55-0.12 0.8-1.6 4.67-4.49 8.78-8.3 11.87-0.44 0.32-0.97 0.46-1.5 0.4-0.54-0.08-1.03-0.35-1.36-0.78-0.34-0.42-0.5-0.96-0.44-1.5 0.05-0.54 0.3-1.04 0.7-1.4 3.22-2.58 5.64-6.04 6.99-9.96 0.18-0.52 0.56-0.95 1.05-1.2 0.5-0.23 1.07-0.27 1.58-0.09"
        ).toNodes(),
        fill = SolidColor(colors.tertiaryContainer),
        stroke = null,
        pathFillType = PathFillType.EvenOdd,
    )
}.build()