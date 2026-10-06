package com.wstxda.switchai.ui.components.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.R
import com.wstxda.switchai.data.TutorialBanner
import com.wstxda.switchai.ui.components.preferences.OverlayCard
import com.wstxda.switchai.ui.components.preferences.bottomSheetContentInsets
import com.wstxda.switchai.ui.components.preferences.overlayScrollModifiers
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet

@Composable
fun TutorialSheet(show: Boolean, onDismiss: () -> Unit) {
    OverlayBottomSheet(
        show = show,
        title = stringResource(R.string.digital_assistant_tutorial_title),
        onDismissRequest = onDismiss,
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .bottomSheetContentInsets()
                .overlayScrollModifiers(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item {
                OverlayCard {
                    TutorialImage(TutorialBanner.Gestures)
                    BasicComponent(
                        title = stringResource(R.string.assistant_tutorial_edge_gestures),
                        summary = stringResource(R.string.assistant_tutorial_edge_gestures_summary),
                    )
                }
            }
            item {
                OverlayCard {
                    TutorialImage(TutorialBanner.HomeButton)
                    BasicComponent(
                        title = stringResource(R.string.assistant_tutorial_home_button),
                        summary = stringResource(R.string.assistant_tutorial_home_button_summary),
                    )
                }
            }
            item {
                OverlayCard {
                    TutorialImage(TutorialBanner.PowerButton)
                    BasicComponent(
                        title = stringResource(R.string.assistant_tutorial_power_button),
                        summary = stringResource(R.string.assistant_tutorial_power_button_summary),
                    )
                }
            }
            item {
                OverlayCard {
                    TutorialImage(TutorialBanner.Headset)
                    BasicComponent(
                        title = stringResource(R.string.assistant_tutorial_headset_button),
                        summary = stringResource(R.string.assistant_tutorial_headset_button_summary),
                    )
                }
            }
        }
    }
}

@Composable
private fun TutorialImage(banner: TutorialBanner) {
    Image(
        rememberTutorialBanner(banner),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(280f / 160f),
    )
}