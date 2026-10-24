package com.aliayali.profile.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aliayali.navigation.Navigator
import com.aliayali.profile.ProfileRoute

fun EntryProviderScope<NavKey>.profileEntry(
    navigator: Navigator
) {
    entry<ProfileNavKey> {
        ProfileRoute(
            onBackClick = { navigator.goBack() },
        )
    }
}