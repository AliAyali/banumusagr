package com.aliayali.report.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ReportNavKey(
    val reportId: Long? = null
) : NavKey