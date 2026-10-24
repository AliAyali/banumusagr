package com.aliayali.report.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aliayali.navigation.Navigator
import com.aliayali.profile.navigation.ProfileNavKey
import com.aliayali.report.ReportRoute

fun EntryProviderScope<NavKey>.reportEntry(navigator: Navigator) {
    entry<ReportNavKey> { key ->
        ReportRoute(
            reportId = key.reportId,
            onReportSubmitted = {
                navigator.goBack()
            },
            onNavigateToProfile = {
                navigator.navigate(ProfileNavKey)
            }
        )
    }
}