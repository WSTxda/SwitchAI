package com.wstxda.switchai.ui.component

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.R
import com.wstxda.switchai.data.ReleaseInfo
import com.wstxda.switchai.databinding.DialogUpdaterBinding
import com.wstxda.switchai.service.ApkDownloader
import com.wstxda.switchai.utils.Constants
import io.noties.markwon.Markwon
import io.noties.markwon.linkify.LinkifyPlugin
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class UpdaterBottomSheet : BaseBottomSheet<DialogUpdaterBinding>() {

    companion object {
        @Suppress("DEPRECATION")
        fun show(
            fragmentManager: FragmentManager,
            release: ReleaseInfo,
        ) {
            if (fragmentManager.findFragmentByTag(Constants.UPDATER_DIALOG) != null) return

            UpdaterBottomSheet().apply {
                arguments = bundleOf(
                    Constants.GITHUB_TITLE to release.title,
                    Constants.GITHUB_VERSION to release.version,
                    Constants.GITHUB_CHANGELOG to release.changelog,
                    Constants.GITHUB_DOWNLOAD_URL to release.downloadUrl,
                    Constants.GITHUB_PAGE_URL to release.pageUrl
                )
            }.show(fragmentManager, Constants.UPDATER_DIALOG)
        }
    }

    override val topDivider: View get() = binding.dividerTop
    override val bottomDivider: View get() = binding.dividerBottom
    override val titleTextView: TextView get() = binding.updaterTitle
    override val titleResId: Int get() = R.string.updater_title
    override val defaultExpanded: Boolean = true

    private var downloadedFile: File? = null
    private var downloadJob: Job? = null

    private val installPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val context = context?.applicationContext ?: return@registerForActivityResult
        val file = downloadedFile ?: return@registerForActivityResult

        if (context.packageManager.canRequestPackageInstalls()) {
            ApkDownloader.installApk(context, file)
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = DialogUpdaterBinding.inflate(inflater, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments ?: return

        setupMetadata(args)
        setupGithubButton(args)
        setupDownloadButton(args)
    }

    override fun setupScrollListener() {
        binding.scrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
                val canScrollUp = binding.scrollView.canScrollVertically(-1)
                val canScrollDown = binding.scrollView.canScrollVertically(1)

                updateDividerVisibility(canScrollUp, canScrollDown)
            })
    }

    private fun setupMetadata(args: Bundle) {
        val markwon = Markwon.builder(requireContext()).usePlugin(LinkifyPlugin.create()).build()

        val displayTitle =
            args.getString(Constants.GITHUB_TITLE) ?: args.getString(Constants.GITHUB_VERSION)

        binding.updaterVersion.text = getString(
            R.string.updater_version, displayTitle
        )

        markwon.setMarkdown(
            binding.updaterChangelog, args.getString(Constants.GITHUB_CHANGELOG).orEmpty()
        )
    }

    private fun setupGithubButton(args: Bundle) {
        binding.githubButton.setOnClickListener {
            openUrl(args.getString(Constants.GITHUB_PAGE_URL).orEmpty())
        }
    }

    private fun setupDownloadButton(args: Bundle) {
        binding.downloadButton.setOnClickListener {
            val file = downloadedFile

            when {
                file != null -> requestInstallOrPermission(file)

                downloadJob?.isActive != true -> startDownload(
                    url = args.getString(Constants.GITHUB_DOWNLOAD_URL).orEmpty(),
                    version = args.getString(Constants.GITHUB_VERSION).orEmpty()
                )
            }
        }
    }

    private fun startDownload(
        url: String,
        version: String,
    ) {
        setDownloadingState(true)

        downloadJob = viewLifecycleOwner.lifecycleScope.launch {
            ApkDownloader.download(
                context = requireContext().applicationContext,
                url = url,
                fileName = "update-$version.apk",
                onProgress = { percent ->
                    binding.downloadProgress.progress = percent
                },
                onComplete = { file ->
                    downloadedFile = file
                    setDownloadingState(false)
                    requestInstallOrPermission(file)
                },
                onError = {
                    setDownloadingState(false)
                })
        }
    }

    private fun setDownloadingState(isDownloading: Boolean) {
        binding.downloadProgress.isVisible = isDownloading
        binding.downloadButton.isEnabled = !isDownloading
        binding.githubButton.isEnabled = !isDownloading
    }

    private fun requestInstallOrPermission(file: File) {
        val appContext = requireContext().applicationContext

        if (appContext.packageManager.canRequestPackageInstalls()) {
            ApkDownloader.installApk(appContext, file)
        } else {
            installPermissionLauncher.launch(
                Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = "package:${appContext.packageName}".toUri()
                })
        }
    }

    private fun openUrl(url: String) {
        runCatching {
            startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        }
    }

    override fun onDestroyView() {
        downloadJob?.cancel()
        super.onDestroyView()
    }
}