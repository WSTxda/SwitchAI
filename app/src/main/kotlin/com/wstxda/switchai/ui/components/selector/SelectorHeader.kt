package com.wstxda.switchai.ui.components.selector

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.R
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.basic.SearchBar

@Composable
internal fun SelectorHeader(showSearch: Boolean, query: String, onQueryChange: (String) -> Unit) {
    if (!showSearch) return
    var active by rememberSaveable { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        insideMargin = DpSize.Zero,
        inputField = {
            InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { keyboard?.hide() },
                expanded = active,
                onExpandedChange = { active = it },
                label = stringResource(R.string.selector_search_hint),
            )
        },
        expanded = active,
        onExpandedChange = { active = it },
    ) {}
}