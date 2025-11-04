package com.wstxda.switchai.utils

import com.wstxda.switchai.assistant.*

object AssistantsMap {

    internal val assistantActivity = mapOf(
        "alexa_assistant" to AlexaAssistant::class.java,
        "alice_assistant" to AliceAssistant::class.java,
        "chatgpt_assistant" to ChatGPTAssistant::class.java,
        "claude_assistant" to ClaudeAssistant::class.java,
        "copilot_assistant" to CopilotAssistant::class.java,
        "deepseek_assistant" to DeepSeekAssistant::class.java,
        "delphi_assistant" to DelphiAssistant::class.java,
        "doubao_assistant" to DoubaoAssistant::class.java,
        "gemini_assistant" to GeminiAssistant::class.java,
        "grok_assistant" to GrokAssistant::class.java,
        "home_assistant" to HomeAssistant::class.java,
        "le_chat_assistant" to LeChatAssistant::class.java,
        "lumo_assistant" to LumoAssistant::class.java,
        "kimi_assistant" to KimiAssistant::class.java,
        "manus_assistant" to ManusAssistant::class.java,
        "marusya_assistant" to MarusyaAssistant::class.java,
        "meta_assistant" to MetaAssistant::class.java,
        "minimax_assistant" to MiniMaxAssistant::class.java,
        "perplexity_assistant" to PerplexityAssistant::class.java,
        "pi_assistant" to PiAssistant::class.java,
        "qingyan_assistant" to QingyanAssistant::class.java,
        "qwen_assistant" to QwenAssistant::class.java,
        "ultimate_alexa_assistant" to UltimateAlexaAssistant::class.java,
        "venice_assistant" to VeniceAssistant::class.java,
        "wenxin_yiyan_assistant" to WenxinYiyanAssistant::class.java,
        "yuanbao_assistant" to YuanbaoAssistant::class.java,
        "zapia_assistant" to ZapiaAssistant::class.java,
    )

    internal val assistantPackage = mapOf(
        "alexa_assistant" to "com.amazon.dee.app",
        "alice_assistant" to "com.yandex.aliceapp",
        "chatgpt_assistant" to "com.openai.chatgpt",
        "claude_assistant" to "com.anthropic.claude",
        "copilot_assistant" to "com.microsoft.copilot",
        "deepseek_assistant" to "com.deepseek.chat",
        "delphi_assistant" to "ai.oo.delphi",
        "doubao_assistant" to "com.larus.nova",
        "gemini_assistant" to "com.google.android.apps.bard",
        "grok_assistant" to "ai.x.grok",
        "home_assistant" to "io.homeassistant.companion.android",
        "le_chat_assistant" to "ai.mistral.chat",
        "lumo_assistant" to "me.proton.android.lumo",
        "kimi_assistant" to "com.moonshot.kimichat",
        "manus_assistant" to "tech.butterfly.app",
        "marusya_assistant" to "ru.mail.search.electroscope",
        "meta_assistant" to "com.facebook.stella",
        "minimax_assistant" to "com.minimax.ai",
        "perplexity_assistant" to "ai.perplexity.app.android",
        "pi_assistant" to "ai.inflection.pi",
        "qingyan_assistant" to "com.zhipuai.qingyan",
        "qwen_assistant" to "ai.qwenlm.chat.android",
        "ultimate_alexa_assistant" to "com.customsolutions.android.alexa",
        "venice_assistant" to "com.ai.venice",
        "wenxin_yiyan_assistant" to "com.baidu.newapp",
        "yuanbao_assistant" to "com.tencent.hunyuan.app.chat",
        "zapia_assistant" to "com.brainlogic.zapia"
    )
}