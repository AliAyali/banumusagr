package com.aliayali.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileRoute(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        uiState = uiState,
        onBackClick = onBackClick,

        onFirstNameChange = viewModel::updateFirstName,
        onLastNameChange = viewModel::updateLastName,
        onActiveUnitChange = viewModel::updateActiveUnit,
        onSaveClick = viewModel::saveProfile,
        onThemeSelected = viewModel::selectTheme,

        onInitialTotalHoursChange =
            viewModel::updateInitialTotalHours,
        onInitialTotalMinutesChange =
            viewModel::updateInitialTotalMinutes,
        onInitialMonthlyHoursChange =
            viewModel::updateInitialMonthlyHours,
        onInitialMonthlyMinutesChange =
            viewModel::updateInitialMonthlyMinutes,
        onSaveInitialHours =
            viewModel::saveInitialHours
    )
}