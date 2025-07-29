package com.wstxda.switchai.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TutorialItem(
    @DrawableRes val iconRes: Int,
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val summaryRes: Int,
)