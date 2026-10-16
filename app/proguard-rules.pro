# Preserve the assistant maps during this diagnostic test.
-keep class com.wstxda.switchai.utils.AssistantsMap {
    *;
}

# Preserve assistant companions used by the reflection fallback.
-keep class com.wstxda.switchai.assistant.**$Companion {
    *;
}

# Preserve the fields exposing companion instances.
-keepclassmembers class com.wstxda.switchai.assistant.** {
    public static final ** Companion;
}