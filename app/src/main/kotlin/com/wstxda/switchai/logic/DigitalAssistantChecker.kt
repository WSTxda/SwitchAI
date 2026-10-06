package com.wstxda.switchai.logic

import android.app.role.RoleManager
import android.content.Context
import android.os.Build

internal fun Context.isAssistantSetupDone(savedSetup: Boolean): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSystemService(RoleManager::class.java)?.isRoleHeld(RoleManager.ROLE_ASSISTANT) == true
    } else savedSetup