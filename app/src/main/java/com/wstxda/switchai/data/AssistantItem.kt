package com.wstxda.switchai.data

data class AssistantItem(
    val key: String,
    val name: String,
    val iconRes: Int,
    val isPinned: Boolean,
    var isInstalled: Boolean,
    val lastUsedTimestamp: Long,
)