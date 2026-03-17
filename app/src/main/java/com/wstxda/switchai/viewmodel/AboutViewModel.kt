package com.wstxda.switchai.viewmodel

import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {

    val links: Map<String, String> = mapOf(
        "developer" to "https://github.com/WSTxda",
        "github_repository" to "https://github.com/WSTxda/SwitchAI",
        "license" to "https://github.com/WSTxda/SwitchAI/blob/main/LICENSE"
    )
}