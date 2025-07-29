package com.wstxda.switchai.data

data class AssistantItem(
    val key: String,
    val name: String,
    val iconResId: Int,
    val isPinned: Boolean,
    val lastUsedTimestamp: Long,
)