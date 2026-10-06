package com.wstxda.switchai.data

data class ShizukuSetupState(
    val access: ShizukuState? = null,
    val permissionPending: Boolean = false,
    val launching: Boolean = false,
)