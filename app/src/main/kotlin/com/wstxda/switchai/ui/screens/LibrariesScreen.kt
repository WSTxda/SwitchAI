package com.wstxda.switchai.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.wstxda.switchai.R
import com.wstxda.switchai.ui.components.preferences.OverlayCard
import com.wstxda.switchai.ui.components.preferences.SettingsScaffold
import com.wstxda.switchai.ui.components.preferences.overlayScrollModifiers
import com.wstxda.switchai.ui.components.preferences.pageScrollModifiers
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.squircle.squircleSurface
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun LibrariesScreen(
    onUrl: (String) -> Unit,
    onBack: () -> Unit,
) {
    val libraries by produceLibraries(R.raw.aboutlibraries)

    var showLicense by rememberSaveable { mutableStateOf(false) }
    var selectedLicenseHash by rememberSaveable { mutableStateOf<String?>(null) }

    val selectedLicense = libraries?.licenses?.firstOrNull { it.hash == selectedLicenseHash }

    SettingsScaffold(
        R.string.pref_used_library,
        onBack,
    ) { padding, listState, scrollBehavior ->
        LazyColumn(
            state = listState,
            modifier = Modifier.pageScrollModifiers(scrollBehavior),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (libraries == null) {
                item {
                    CircularProgressIndicator(modifier = Modifier.padding(24.dp))
                }
            }

            items(
                items = libraries?.libraries.orEmpty(),
                key = { it.uniqueId },
            ) { library ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .squircleSurface(
                            MiuixTheme.colorScheme.surfaceContainer,
                            cornerRadius = CardDefaults.CornerRadius,
                        )
                        .clickable {
                            selectedLicenseHash = library.licenses.firstOrNull()?.hash

                            showLicense = selectedLicenseHash != null
                        }
                        .padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = library.name,
                            modifier = Modifier.weight(1f),
                            style = MiuixTheme.textStyles.headline1,
                        )

                        library.artifactVersion?.takeIf(String::isNotBlank)?.let { version ->
                            Text(
                                text = version,
                                modifier = Modifier.padding(start = 12.dp),
                                style = MiuixTheme.textStyles.subtitle,
                                color = MiuixTheme.colorScheme.primary,
                            )
                        }
                    }

                    library.developers.mapNotNull { it.name }.joinToString()
                        .takeIf(String::isNotBlank)?.let { author ->
                            Text(
                                text = author,
                                style = MiuixTheme.textStyles.body2,
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            )
                        }

                    FlowRow {
                        library.licenses.forEach { license ->
                            Card(
                                cornerRadius = 90.dp,
                                insideMargin = PaddingValues(
                                    horizontal = 8.dp,
                                    vertical = 4.dp,
                                ),
                                colors = CardDefaults.defaultColors(
                                    MiuixTheme.colorScheme.secondaryContainer
                                ),
                                modifier = Modifier.padding(
                                    top = 6.dp,
                                    end = 6.dp,
                                ),
                                onClick = {
                                    selectedLicenseHash = license.hash
                                    showLicense = true
                                },
                            ) {
                                Text(
                                    text = license.spdxId ?: license.name,
                                    style = MiuixTheme.textStyles.body2,
                                )
                            }
                        }
                    }
                }
            }
        }

        OverlayDialog(
            show = showLicense && selectedLicense != null,
            title = selectedLicense?.name,
            onDismissRequest = {
                showLicense = false
            },
            onDismissFinished = {
                selectedLicenseHash = null
            },
        ) {
            selectedLicense?.let { license ->
                Column {
                    license.licenseContent?.takeIf(String::isNotBlank)?.let { content ->
                        OverlayCard(
                            Modifier.weight(
                                weight = 1f,
                                fill = false,
                            )
                        ) {
                            SelectionContainer(
                                modifier = Modifier
                                    .overlayScrollModifiers()
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = content,
                                    style = MiuixTheme.textStyles.body2,
                                )
                            }
                        }
                    }

                    license.url?.takeIf(String::isNotBlank)?.let { url ->
                        TextButton(
                            text = stringResource(R.string.pref_license),
                            onClick = {
                                onUrl(url)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                        )
                    }

                    TextButton(
                        text = stringResource(android.R.string.ok),
                        onClick = {
                            showLicense = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                    )
                }
            }
        }
    }
}