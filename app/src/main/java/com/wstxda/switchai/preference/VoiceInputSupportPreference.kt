package com.wstxda.switchai.preference

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.PreferenceMaterialVoiceInputSupportBinding
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.AssistantsMap

class VoiceInputSupportPreference @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        layoutResource = R.layout.preference_material_voice_input_support
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val binding = PreferenceMaterialVoiceInputSupportBinding.bind(holder.itemView)
        val resourcesManager = AssistantResourcesManager(context)

        binding.buttonsContainer.removeAllViews()

        val isPreferenceEnabled = isEnabled

        val sortedKeys = AssistantsMap.assistantsVoiceInput.sortedBy { key ->
            resourcesManager.getAssistantName(key).lowercase()
        }

        val inflater = LayoutInflater.from(context)

        sortedKeys.forEach { key ->
            val iconResId = resourcesManager.getAssistantIcon(key)
            val displayName = resourcesManager.getAssistantName(key)

            val button = inflater.inflate(
                R.layout.button_assistant_voice_input_support, binding.buttonsContainer, false
            ) as MaterialButton

            val drawable = androidx.core.content.ContextCompat.getDrawable(context, iconResId)
            button.icon = drawable
            button.isEnabled = isPreferenceEnabled
            button.alpha = if (isPreferenceEnabled) 1.0f else 0.4f

            button.setOnClickListener {
                val snackbar = Snackbar.make(
                    holder.itemView, displayName, Snackbar.LENGTH_LONG
                )
                snackbar.setAction(context.getString(R.string.assistant_label_open)) {
                    val activityClass = AssistantsMap.assistantActivity[key]
                    if (activityClass != null) {
                        val intent = Intent(context, activityClass).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    }
                }
                snackbar.show()
            }

            binding.buttonsContainer.addView(button)
        }
    }
}