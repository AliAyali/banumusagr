package com.aliayali.banumusagr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.aliayali.banumusagr.navigation.TOP_LEVEL_NAV_ITEMS
import com.example.designsystem.theme.BanumusaTheme
import com.aliayali.home.navigation.homeEntry
import com.aliayali.navigation.Navigator
import com.aliayali.navigation.toEntries
import com.aliayali.report.navigation.reportEntry
import com.example.designsystem.components.BanumusaNavigationSuiteScaffold

@Composable
fun BanumusaApp(
    appState: BanumusaAppState,
    modifier: Modifier = Modifier,
) {
    BanumusaTheme {
        val navigator = remember(appState.navigationState) {
            Navigator(appState.navigationState)
        }

        BanumusaNavigationSuiteScaffold(
            navigationSuiteItems = {
                TOP_LEVEL_NAV_ITEMS.forEach { (navKey, navItem) ->
                    val selected =
                        navKey == appState.navigationState.currentTopLevelKey

                    item(
                        selected = selected,
                        onClick = {
                            navigator.navigate(navKey)
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) {
                                    navItem.selectedIcon
                                } else {
                                    navItem.unselectedIcon
                                },
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(stringResource(navItem.iconTextId))
                        },
                    )
                }
            },
        ) {
            Scaffold(
                modifier = modifier,
                contentWindowInsets = WindowInsets.safeDrawing,
                containerColor = MaterialTheme.colorScheme.background,
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding),
                ) {
                    val entryProvider = entryProvider {
                        homeEntry(navigator)
                        reportEntry(navigator)
                    }

                    NavDisplay(
                        entries = appState.navigationState.toEntries(entryProvider),
                        onBack = {
                            navigator.goBack()
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}