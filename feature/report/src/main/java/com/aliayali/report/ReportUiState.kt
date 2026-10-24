package com.aliayali.report

import com.aliayali.report.model.ReportFormData

sealed interface ReportUiState {

    data object Loading : ReportUiState

    data class Form(
        val data: ReportFormData = ReportFormData(),

        val isProfileComplete: Boolean = false,

        val editingReportId: Long? = null,
        val isSending: Boolean = false,
        val isSent: Boolean = false,

        val validationError: String? = null,
        val sendError: String? = null,

        val dayError: String? = null,
        val monthError: String? = null,
        val yearError: String? = null,

        val entryTimeError: String? = null,
        val exitTimeError: String? = null
    ) : ReportUiState

    data class Error(
        val message: String
    ) : ReportUiState
}