package com.aliayali.history

import com.aliayali.history.model.HistoryFilter
import com.aliayali.history.model.HistoryUiModel

sealed interface HistoryUiState {

    data object Loading : HistoryUiState

    data class Success(
        val reports: List<HistoryUiModel> = emptyList(),
        val selectedFilter: HistoryFilter = HistoryFilter.ALL,
        val isRefreshing: Boolean = false
    ) : HistoryUiState

    data class Error(
        val message: String
    ) : HistoryUiState
}
