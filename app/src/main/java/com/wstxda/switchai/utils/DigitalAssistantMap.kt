package com.wstxda.switchai.utils

import android.content.Context
import android.content.Intent
import com.wstxda.switchai.assistant.*
import com.wstxda.switchai.logic.PreferenceHelper

object DigitalAssistantMap {
    internal val assistantsMap = mapOf(
        "alexa_assistant" to AlexaAssistant::class.java,
        "alice_assistant" to AliceAssistant::class.java,
        "chatgpt_assistant" to ChatGPTAssistant::class.java,
        "claude_assistant" to ClaudeAssistant::class.java,
        "copilot_assistant" to CopilotAssistant::class.java,
        "deepseek_assistant" to DeepSeekAssistant::class.java,
        "doubao_assistant" to DoubaoAssistant::class.java,
        "gemini_assistant" to GeminiAssistant::class.java,
        "grok_assistant" to GrokAssistant::class.java,
        "home_assistant" to HomeAssistant::class.java,
        "le_chat_assistant" to LeChatAssistant::class.java,
        "kimi_assistant" to KimiAssistant::class.java,
        "manus_assistant" to ManusAssistant::class.java,
        "marusya_assistant" to MarusyaAssistant::class.java,
        "meta_assistant" to MetaAssistant::class.java,
        "minimax_assistant" to MiniMaxAssistant::class.java,
        "perplexity_assistant" to PerplexityAssistant::class.java,
        "qingyan_assistant" to QingyanAssistant::class.java,
        "qwen_assistant" to QwenAssistant::class.java,
        "ultimate_alexa_assistant" to UltimateAlexaAssistant::class.java,
        "wenxin_yiyan_assistant" to WenxinYiyanAssistant::class.java,
        "yuanbao_assistant" to YuanbaoAssistant::class.java,
        "zapia_assistant" to ZapiaAssistant::class.java,
    )

    internal val assistantsPackages = mapOf(
        "alexa_assistant" to "com.amazon.dee.app",
        "alice_assistant" to "com.yandex.aliceapp",
        "chatgpt_assistant" to "com.openai.chatgpt",
        "claude_assistant" to "com.anthropic.claude",
        "copilot_assistant" to "com.microsoft.copilot",
        "deepseek_assistant" to "com.deepseek.chat",
        "doubao_assistant" to "com.larus.nova",
        "gemini_assistant" to "com.google.android.apps.bard",
        "grok_assistant" to "ai.x.grok",
        "home_assistant" to "io.homeassistant.companion.android",
        "kimi_assistant" to "com.moonshot.kimichat",
        "le_chat_assistant" to "ai.mistral.chat",
        "manus_assistant" to "tech.butterfly.app",
        "marusya_assistant" to "ru.mail.search.electroscope",
        "meta_assistant" to "com.facebook.stella",
        "minimax_assistant" to "com.minimax.ai",
        "perplexity_assistant" to "ai.perplexity.app.android",
        "qingyan_assistant" to "com.zhipuai.qingyan",
        "qwen_assistant" to "ai.qwenlm.chat.android",
        "ultimate_alexa_assistant" to "com.customsolutions.android.alexa",
        "wenxin_yiyan_assistant" to "com.baidu.newapp",
        "yuanbao_assistant" to "com.tencent.hunyuan.app.chat",
        "zapia_assistant" to "com.brainlogic.zapia",
    )

    fun launchSelectedAssistant(context: Context) {
        val preferenceHelper = PreferenceHelper(context)
        val selectedShortcut =
            preferenceHelper.getString(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null) ?: return
        val activityClass = assistantsMap[selectedShortcut] ?: return
        val intent = Intent(context, activityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}