package com.aliayali.banumusagr.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.aliayali.banumusagr.navigation.TOP_LEVEL_NAV_ITEMS
import com.aliayali.home.navigation.HomeNavKey
import com.aliayali.navigation.NavigationState
import com.aliayali.navigation.rememberNavigationState

class BanumusaAppState(
    val navigationState: NavigationState,
)

@Composable
fun rememberDaricAppState(): BanumusaAppState {

    val navigationState = rememberNavigationState(
        startKey = HomeNavKey,
        topLevelKeys = TOP_LEVEL_NAV_ITEMS.keys,
    )

    return remember {
        BanumusaAppState(
            navigationState = navigationState,
        )
    }
}