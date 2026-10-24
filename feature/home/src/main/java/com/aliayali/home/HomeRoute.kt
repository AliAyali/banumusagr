package com.aliayali.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    onAddReportClick: () -> Unit,
    onViewAllReportsClick: () -> Unit,
    onEditReport: (Long) -> Unit,
    onProfileClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val initialHoursSaved by viewModel.initialHoursSaved
        .collectAsStateWithLifecycle()

    val sendingReportIds by viewModel.sendingReportIds
        .collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        initialHoursSaved = initialHoursSaved,
        sendingReportIds = sendingReportIds,
        onRefresh = {
            viewModel.onEvent(HomeEvent.Refresh)
        },
        onAddReportClick = onAddReportClick,
        onViewAllReportsClick = onViewAllReportsClick,
        onSendReport = { reportId ->
            viewModel.onEvent(HomeEvent.SendReport(reportId))
        },
        onEditReport = onEditReport,
        onDeleteReport = { reportId ->
            viewModel.onEvent(HomeEvent.DeleteReport(reportId))
        },
        onProfileClick = onProfileClick
    )
}