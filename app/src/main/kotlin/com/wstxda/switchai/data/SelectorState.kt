package com.wstxda.switchai.data

data class SelectorState(
    val items: List<AssistantListItem> = emptyList(),
    val query: String = "",
    val loading: Boolean = true,
    val searchEmpty: Boolean = false,
    val loadError: Boolean = false,
    val errorRes: Int? = null,
)