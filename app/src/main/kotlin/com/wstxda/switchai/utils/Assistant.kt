package com.wstxda.switchai.utils

import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.launchAssistantEntry

object Assistant {

    fun open(context: AssistantActivity) {
        context.launchAssistantEntry(context.launchSettings.assistant)
    }
}