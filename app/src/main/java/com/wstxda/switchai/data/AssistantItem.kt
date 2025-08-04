package com.wstxda.switchai.data

data class AssistantItem(
    val key: String,
    val name: String,
    val iconRes: Int,
    val isPinned: Boolean,
    val isInstalled: Boolean,
    val lastUsedTime: Long
)