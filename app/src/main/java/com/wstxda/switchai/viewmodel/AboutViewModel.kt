package com.wstxda.switchai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class AboutViewModel (application: Application) : AndroidViewModel(application) {

    val links: Map<String, String> = mapOf(
        "developer" to "https://github.com/WSTxda",
        "github_repository" to "https://github.com/WSTxda/SwitchAI",
        "license" to "https://github.com/WSTxda/SwitchAI/blob/main/LICENSE"
    )
}