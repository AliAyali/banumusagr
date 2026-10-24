package com.aliayali.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReportRoute(
    reportId: Long? = null,
    onReportSubmitted: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(reportId) {
        if (reportId != null) {
            viewModel.loadReportForEditing(reportId)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is ReportUiState.Form && uiState.isSent) {
            onReportSubmitted()
        }
    }

    ReportScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToProfile = onNavigateToProfile
    )
}