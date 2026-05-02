package com.wstxda.switchai.preference

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.wstxda.switchai.R
import com.wstxda.switchai.service.UpdaterService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class UpdaterPreference @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    var fragmentManager: FragmentManager? = null

    private val scope = MainScope()

    init {
        layoutResource = R.layout.preference_material_updater
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        (holder.findViewById(android.R.id.summary) as? TextView)?.text =
            context.getString(R.string.pref_version_number, getInstalledVersion())

        holder.itemView.setOnClickListener { openAppInfo() }

        (holder.findViewById(R.id.check_updates) as? Button)?.setOnClickListener {
            val fm = fragmentManager ?: return@setOnClickListener
            UpdaterService.checkForUpdates(
                scope = scope, context = context, fragmentManager = fm, anchorView = holder.itemView
            )
        }
    }

    override fun onDetached() {
        super.onDetached()
        scope.cancel()
    }

    private fun openAppInfo() {
        context.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            })
    }

    private fun getInstalledVersion(): String = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "Unknown"
    }.getOrDefault("Unknown")
}