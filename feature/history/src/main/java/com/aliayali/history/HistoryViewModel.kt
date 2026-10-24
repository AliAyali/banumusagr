package com.aliayali.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliayali.domain.WorkReportUseCases
import com.aliayali.history.model.HistoryFilter
import com.aliayali.history.model.HistoryUiModel
import com.aliayali.model.WorkReport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val workReportUseCases: WorkReportUseCases
) : ViewModel() {

    private val selectedFilter =
        MutableStateFlow(HistoryFilter.ALL)

    private val retryTrigger =
        MutableStateFlow(0)

    private val reportsFlow: Flow<Result<List<WorkReport>>> =
        retryTrigger.flatMapLatest {
            workReportUseCases.getAllReports()
                .map<List<WorkReport>, Result<List<WorkReport>>> { reports ->
                    Result.success(reports)
                }
                .catch { throwable ->
                    emit(Result.failure(throwable))
                }
        }

    val uiState: StateFlow<HistoryUiState> =
        combine(
            reportsFlow,
            selectedFilter
        ) { result, filter ->

            val error = result.exceptionOrNull()

            if (error != null) {
                HistoryUiState.Error(
                    message = error.message
                        ?: "دریافت گزارش‌ها با خطا مواجه شد."
                )
            } else {
                val reports = result.getOrNull().orEmpty()

                val sortedReports = reports.sortedWith(
                    compareByDescending<WorkReport> {
                        it.createdAtMillis
                    }.thenByDescending {
                        it.id
                    }
                )

                val filteredReports = when (filter) {
                    HistoryFilter.ALL -> sortedReports

                    HistoryFilter.DAILY ->
                        sortedReports.filter {
                            it.reportType == "روزانه"
                        }

                    HistoryFilter.WEEKLY ->
                        sortedReports.filter {
                            it.reportType == "هفتگی"
                        }

                    HistoryFilter.MONTHLY ->
                        sortedReports.filter {
                            it.reportType == "ماهانه"
                        }
                }

                HistoryUiState.Success(
                    reports = filteredReports.map { report ->
                        report.toHistoryUiModel()
                    },
                    selectedFilter = filter
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState.Loading
        )

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.FilterChanged -> {
                selectedFilter.value = event.filter
            }

            HistoryEvent.Retry -> {
                retryTrigger.value++
            }
        }
    }
}

private fun WorkReport.toHistoryUiModel(): HistoryUiModel {
    return HistoryUiModel(
        id = id,
        reportType = reportType,
        reportDate = reportDate,
        reportText = reportText,
        problems = problems,
        tomorrowPlan = tomorrowPlan,
        workDurationMinutes = workDurationMinutes,
        isSent = isSent,
        createdAtMillis = createdAtMillis
    )
}