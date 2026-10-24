package com.aliayali.history.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aliayali.history.HistoryRoute
import com.aliayali.navigation.Navigator

fun EntryProviderScope<NavKey>.historyEntry(
    navigator: Navigator,
) {
    entry<HistoryNavKey> {
        HistoryRoute()
    }
}