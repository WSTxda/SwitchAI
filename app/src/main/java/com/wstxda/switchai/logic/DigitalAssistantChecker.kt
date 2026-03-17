package com.wstxda.switchai.logic

import android.app.role.RoleManager
import android.content.Context
import android.os.Build
import com.wstxda.switchai.utils.Constants

object DigitalAssistantChecker {

    fun isSetupDone(context: Context): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(RoleManager::class.java)
                ?.isRoleHeld(RoleManager.ROLE_ASSISTANT) == true
        } else {
            PreferenceHelper(context).getBoolean(Constants.IS_ASSIST_SETUP_DONE, false)
        }
}