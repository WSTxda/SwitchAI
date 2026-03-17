package com.wstxda.switchai.preference

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.wstxda.switchai.R
import com.wstxda.switchai.service.UpdaterService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdaterPreference @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        layoutResource = R.layout.preference_material_updater
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val versionName = getVersionName(context)
        val versionText = context.getString(R.string.pref_version_number, versionName)

        (holder.findViewById(android.R.id.summary) as? TextView)?.text = versionText

        holder.itemView.setOnClickListener {
            openAppInfo(context)
        }

        (holder.findViewById(R.id.check_updates) as? Button)?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                UpdaterService.checkForUpdates(context, holder.itemView)
            }
        }
    }

    private fun openAppInfo(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    private fun getVersionName(context: Context): String = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "N/A"
    }.getOrDefault("N/A")
}