package com.wstxda.switchai.logic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.wstxda.switchai.constants.AssistantsMap
import com.wstxda.switchai.data.AssistantItem
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PackageChecker(context: Context, private val applicationScope: CoroutineScope) {

    private val app = context.applicationContext
    private val resources = AssistantResourcesManager(app)
    private val cacheLock = Any()
    private val loads = Mutex()
    private val installedPackages = mutableMapOf<String, Boolean>()
    private val packageRevisions = AssistantsMap.assistantPackage.values.associateWith { 0L }.toMutableMap()
    private val presentations = mutableMapOf<String, AssistantItem>()
    private var presentationLocale = locale
    private val _changes = MutableStateFlow(0L)
    val changes = _changes.asStateFlow()

    val locale: String get() = app.resources.configuration.locales.toLanguageTags()

    init {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        }
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent == null || !filter.hasAction(intent.action) || intent.data?.scheme != "package") return
                if (intent.action != Intent.ACTION_PACKAGE_REPLACED && intent.getBooleanExtra(
                        Intent.EXTRA_REPLACING, false
                    )
                ) return
                val packageName = intent.data?.schemeSpecificPart ?: return
                synchronized(cacheLock) {
                    val revision = packageRevisions[packageName] ?: return
                    packageRevisions[packageName] = revision + 1
                    installedPackages.remove(packageName)
                    presentations.keys.removeAll {
                        AssistantsMap.assistantPackage[it] == packageName
                    }
                    _changes.update { it + 1 }
                }
            }
        }
        ContextCompat.registerReceiver(app, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }

    fun cachedAssistants(dynamic: Boolean, visible: Set<String>): List<AssistantItem>? =
        synchronized(cacheLock) { snapshot(candidates(dynamic, visible), dynamic) }

    suspend fun loadAssistants(dynamic: Boolean, visible: Set<String>): List<AssistantItem> {
        cachedAssistants(dynamic, visible)?.let { return it }
        return applicationScope.async(Dispatchers.IO) {
            loads.withLock {
                val candidates = candidates(dynamic, visible)
                val packages = candidates.mapNotNull(AssistantsMap.assistantPackage::get).toSet()
                var result = synchronized(cacheLock) { snapshot(candidates, dynamic) }
                while (result == null) {
                    currentCoroutineContext().ensureActive()
                    for (packageName in packages) {
                        currentCoroutineContext().ensureActive()
                        val revision = synchronized(cacheLock) {
                            if (packageName in installedPackages) null else packageRevisions.getValue(
                                packageName
                            )
                        } ?: continue
                        val installed = try {
                            app.packageManager.getPackageInfo(packageName, 0)
                            true
                        } catch (_: PackageManager.NameNotFoundException) {
                            false
                        }
                        synchronized(cacheLock) {
                            if (packageRevisions[packageName] == revision) {
                                installedPackages[packageName] = installed
                            }
                        }
                    }
                    result = synchronized(cacheLock) {
                        refreshPresentationLocale()
                        for (key in candidates) {
                            val installed =
                                installedPackages[AssistantsMap.assistantPackage[key]] ?: continue
                            if (dynamic && !installed) continue
                            presentations.getOrPut(key) {
                                AssistantItem(
                                    key = key,
                                    name = resources.getAssistantName(key),
                                    iconRes = resources.getAssistantIcon(key),
                                    isPinned = false,
                                    isInstalled = installed,
                                    lastUsedTime = 0,
                                )
                            }
                        }
                        snapshot(candidates, dynamic)
                    }
                }
                result
            }
        }.await()
    }

    private fun candidates(dynamic: Boolean, visible: Set<String>): List<String> =
        AssistantsMap.assistantActivity.keys.filter { dynamic || it in visible }

    private fun snapshot(candidates: List<String>, dynamic: Boolean): List<AssistantItem>? {
        refreshPresentationLocale()
        val result = ArrayList<AssistantItem>(candidates.size)
        for (key in candidates) {
            val installed = installedPackages[AssistantsMap.assistantPackage[key]] ?: return null
            if (dynamic && !installed) continue
            result.add(presentations[key] ?: return null)
        }
        return result
    }

    private fun refreshPresentationLocale() {
        val current = locale
        if (current != presentationLocale) {
            presentationLocale = current
            presentations.clear()
        }
    }
}