package com.aliayali.report.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aliayali.navigation.Navigator

fun EntryProviderScope<NavKey>.reportEntry(navigator: Navigator) {
    entry<ReportNavKey> {
    }
}