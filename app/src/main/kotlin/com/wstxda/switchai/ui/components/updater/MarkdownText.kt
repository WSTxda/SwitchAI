package com.wstxda.switchai.ui.components.updater

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun MarkdownText(markdown: String, modifier: Modifier = Modifier) {
    val colors = MiuixTheme.colorScheme
    val styles = MiuixTheme.textStyles
    Markdown(
        content = markdown,
        modifier = modifier,
        colors = DefaultMarkdownColors(
            text = colors.onSurface,
            codeBackground = colors.secondaryContainer,
            inlineCodeBackground = colors.secondaryContainer,
            dividerColor = colors.dividerLine,
            tableBackground = colors.surfaceContainer,
        ),
        typography = DefaultMarkdownTypography(
            h1 = styles.title1,
            h2 = styles.title2,
            h3 = styles.title3,
            h4 = styles.title4,
            h5 = styles.headline1,
            h6 = styles.subtitle,
            text = styles.paragraph,
            code = styles.body2.copy(fontFamily = FontFamily.Monospace),
            inlineCode = styles.paragraph.copy(fontFamily = FontFamily.Monospace),
            quote = styles.paragraph.copy(fontStyle = FontStyle.Italic),
            paragraph = styles.paragraph,
            ordered = styles.paragraph,
            bullet = styles.paragraph,
            list = styles.paragraph,
            textLink = TextLinkStyles(
                SpanStyle(color = colors.primary, textDecoration = TextDecoration.Underline)
            ),
            table = styles.paragraph,
        ),
    )
}