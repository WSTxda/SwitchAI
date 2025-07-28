package com.wstxda.switchai.ui.viewholder

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AssistantTutorialItemViewHolder(
    @DrawableRes val iconRes: Int,
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val summaryRes: Int,
)