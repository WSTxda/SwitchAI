package com.wstxda.switchai.ui.utils

import com.wstxda.switchai.R
import com.wstxda.switchai.data.TutorialItem

object TutorialItemList {
    fun getTutorialItems() = listOf(

        // Gestures tutorial
        TutorialItem(
            iconRes = R.drawable.ic_corners,
            imageRes = R.drawable.tutorial_gestures_card,
            titleRes = R.string.tutorial_edge_gestures,
            summaryRes = R.string.tutorial_edge_gestures_summary),

        // Home button tutorial
        TutorialItem(
            iconRes = R.drawable.ic_home,
            imageRes = R.drawable.tutorial_button_card,
            titleRes = R.string.tutorial_home_button,
            summaryRes = R.string.tutorial_home_button_summary),

        // Power button tutorial
        TutorialItem(
            iconRes = R.drawable.ic_power,
            imageRes = R.drawable.tutorial_power_card,
            titleRes = R.string.tutorial_power_button,
            summaryRes = R.string.tutorial_power_button_summary),

        // Headset button tutorial
        TutorialItem(
            iconRes = R.drawable.ic_headset,
            imageRes = R.drawable.tutorial_headset_card,
            titleRes = R.string.tutorial_headset_button,
            summaryRes = R.string.tutorial_headset_button_summary
        )
    )
}