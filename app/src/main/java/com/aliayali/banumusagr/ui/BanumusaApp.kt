package com.aliayali.banumusagr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.aliayali.history.navigation.historyEntry
import com.aliayali.home.navigation.homeEntry
import com.aliayali.navigation.Navigator
import com.aliayali.navigation.toEntries
import com.aliayali.profile.navigation.profileEntry
import com.aliayali.report.navigation.reportEntry


@Composable
fun BanumusaApp(
    appState: BanumusaAppState,
    modifier: Modifier = Modifier,
) {
    val navigator = remember(appState.navigationState) {
        Navigator(appState.navigationState)
    }

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
                historyEntry(navigator)
                profileEntry(navigator)
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