package com.wstxda.switchai.logic

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.data.ShizukuLaunchResult
import com.wstxda.switchai.service.IShizukuService
import com.wstxda.switchai.service.ShizukuService
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku

object ShizukuLauncher {

    private data class ServiceBinding(
        val args: Shizuku.UserServiceArgs,
        val connection: ServiceConnection,
        val service: IShizukuService,
    )

    suspend fun launch(context: Context, component: ComponentName): ShizukuLaunchResult {
        val binding = try {
            bind(context)
        } catch (error: CancellationException) {
            throw error
        } catch (throwable: Throwable) {
            return if (throwable.isPermissionFailure()) {
                ShizukuLaunchResult.PERMISSION_REQUIRED
            } else {
                ShizukuLaunchResult.UNAVAILABLE
            }
        }

        return try {
            withContext(Dispatchers.IO) {
                try {
                    if (binding.service.launchActivity(
                            component.packageName,
                            component.className,
                        )
                    ) {
                        ShizukuLaunchResult.SUCCESS
                    } else {
                        ShizukuLaunchResult.TARGET_FAILED
                    }
                } catch (error: CancellationException) {
                    throw error
                } catch (throwable: Throwable) {
                    if (throwable.isPermissionFailure()) {
                        ShizukuLaunchResult.PERMISSION_REQUIRED
                    } else {
                        ShizukuLaunchResult.UNAVAILABLE
                    }
                }
            }
        } finally {
            runCatching {
                Shizuku.unbindUserService(binding.args, binding.connection, true)
            }
        }
    }

    private suspend fun bind(context: Context): ServiceBinding =
        suspendCancellableCoroutine { continuation ->
            val args = Shizuku.UserServiceArgs(ComponentName(context, ShizukuService::class.java))
                .daemon(false).processNameSuffix(Constants.SHIZUKU_SERVICE_PROCESS)
                .tag(Constants.SHIZUKU_SERVICE_TAG).version(Constants.SHIZUKU_SERVICE_VERSION)

            val completed = AtomicBoolean(false)
            lateinit var connection: ServiceConnection

            fun complete(block: () -> Unit) {
                if (completed.compareAndSet(false, true)) block()
            }

            connection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, service: IBinder) {
                    if (completed.compareAndSet(false, true)) {
                        continuation.resume(
                            ServiceBinding(
                                args,
                                this,
                                IShizukuService.Stub.asInterface(service),
                            ),
                            onCancellation = { _, _, _ ->
                                runCatching {
                                    Shizuku.unbindUserService(args, connection, true)
                                }
                            },
                        )
                    } else if (!continuation.isActive) {
                        runCatching {
                            Shizuku.unbindUserService(args, this, true)
                        }
                    }
                }

                override fun onServiceDisconnected(name: ComponentName) {
                    complete {
                        continuation.resumeWithException(
                            IllegalStateException("Shizuku user service disconnected")
                        )
                    }
                }
            }

            continuation.invokeOnCancellation {
                completed.set(true)
                runCatching {
                    Shizuku.unbindUserService(args, connection, true)
                }
            }

            try {
                Shizuku.bindUserService(args, connection)
            } catch (throwable: Throwable) {
                complete {
                    continuation.resumeWithException(throwable)
                }
            }
        }

    private fun Throwable.isPermissionFailure(): Boolean =
        generateSequence(this) { it.cause }.any { it is SecurityException }
}