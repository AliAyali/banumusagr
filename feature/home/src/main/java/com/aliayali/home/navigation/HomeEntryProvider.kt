package com.aliayali.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aliayali.history.navigation.HistoryNavKey
import com.aliayali.home.HomeRoute
import com.aliayali.navigation.Navigator
import com.aliayali.profile.navigation.ProfileNavKey
import com.aliayali.report.navigation.ReportNavKey

fun EntryProviderScope<NavKey>.homeEntry(
    navigator: Navigator
) {
    entry<HomeNavKey> {
        HomeRoute(
            onAddReportClick = {
                navigator.navigate(ReportNavKey())
            },
            onViewAllReportsClick = {
                navigator.navigate(HistoryNavKey)
            },
            onEditReport = { reportId ->
                navigator.navigate(
                    ReportNavKey(reportId = reportId)
                )
            },
            onProfileClick = { navigator.navigate(ProfileNavKey) },
        )
    }
}