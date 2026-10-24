package com.aliayali.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliayali.domain.EmployeeUseCases
import com.aliayali.domain.ObserveWorkTimeCountersUseCase
import com.aliayali.domain.WorkReportUseCases
import com.aliayali.model.Employee
import com.aliayali.model.WorkReport
import com.aliayali.report.model.ReportFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val workReportUseCases: WorkReportUseCases,
    private val observeWorkTimeCountersUseCase: ObserveWorkTimeCountersUseCase,
    private val employeeUseCases: EmployeeUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReportUiState>(
        ReportUiState.Form(data = getInitialFormData())
    )

    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    private var currentEmployee: Employee? = null

    init {
        observeEmployeeProfile()
    }

    private fun observeEmployeeProfile() {
        viewModelScope.launch {
            employeeUseCases.observeEmployee().collect { employee ->
                currentEmployee = employee

                _uiState.update { state ->
                    if (state is ReportUiState.Form) {
                        state.copy(
                            data = state.data.copy(
                                firstName = employee?.firstName.orEmpty(),
                                lastName = employee?.lastName.orEmpty(),
                                department = employee?.department.orEmpty()
                            ),
                            isProfileComplete =
                                isEmployeeProfileComplete(employee)
                        )
                    } else {
                        state
                    }
                }
            }
        }
    }

    private fun isEmployeeProfileComplete(
        employee: Employee?
    ): Boolean {
        return !employee?.firstName.isNullOrBlank() &&
                !employee?.lastName.isNullOrBlank() &&
                !employee?.department.isNullOrBlank()
    }

    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.ReportTypeChanged -> {
                val current = _uiState.value as? ReportUiState.Form

                if (current?.editingReportId == null) {
                    updateData {
                        copy(reportType = event.value)
                    }
                }
            }

            is ReportEvent.YearChanged ->
                updateData { copy(year = event.value) }

            is ReportEvent.MonthChanged ->
                updateData { copy(month = event.value) }

            is ReportEvent.DayChanged ->
                updateData { copy(day = event.value) }

            is ReportEvent.EntryTimeChanged ->
                updateData { copy(entryTime = event.value) }

            is ReportEvent.ExitTimeChanged ->
                updateData { copy(exitTime = event.value) }

            is ReportEvent.ReportTextChanged ->
                updateData { copy(reportText = event.value) }

            is ReportEvent.ProblemsChanged ->
                updateData { copy(problems = event.value) }

            is ReportEvent.TomorrowPlanChanged ->
                updateData { copy(tomorrowPlan = event.value) }

            ReportEvent.Submit -> validateForm()

            ReportEvent.CancelEditing -> {
                val employee = currentEmployee

                _uiState.value = ReportUiState.Form(
                    data = getInitialFormData().copy(
                        firstName = employee?.firstName.orEmpty(),
                        lastName = employee?.lastName.orEmpty(),
                        department = employee?.department.orEmpty()
                    ),
                    isProfileComplete =
                        isEmployeeProfileComplete(employee)
                )
            }

            else -> Unit
        }
    }

    private fun updateData(
        transform: ReportFormData.() -> ReportFormData
    ) {
        _uiState.update { current ->
            if (current is ReportUiState.Form) {
                current.copy(
                    data = current.data.transform(),
                    validationError = null,
                    sendError = null,
                    dayError = null,
                    monthError = null,
                    yearError = null,
                    entryTimeError = null,
                    exitTimeError = null,
                    isSent = false
                )
            } else {
                current
            }
        }
    }

    fun loadReportForEditing(reportId: Long) {
        viewModelScope.launch {
            _uiState.value = ReportUiState.Loading

            try {
                require(reportId > 0L) {
                    "شناسه گزارش معتبر نیست."
                }

                val report = workReportUseCases
                    .getReportById(reportId)
                    .first()
                    ?: throw IllegalStateException(
                        "گزارش موردنظر پیدا نشد."
                    )

                val dateParts = report.reportDate.split("/")

                require(dateParts.size == 3) {
                    "تاریخ گزارش معتبر نیست."
                }

                val employee = currentEmployee

                val formData = getInitialFormData().copy(
                    firstName = employee?.firstName.orEmpty(),
                    lastName = employee?.lastName.orEmpty(),
                    department = employee?.department.orEmpty(),
                    reportType = report.reportType,
                    year = dateParts[0],
                    month = dateParts[1],
                    day = dateParts[2],
                    entryTime = report.entryTime,
                    exitTime = report.exitTime,
                    reportText = report.reportText,
                    problems = report.problems,
                    tomorrowPlan = report.tomorrowPlan
                )

                _uiState.value = ReportUiState.Form(
                    editingReportId = report.id,
                    data = formData,
                    isProfileComplete =
                        isEmployeeProfileComplete(employee)
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = ReportUiState.Error(
                    e.message
                        ?: "بارگذاری گزارش برای ویرایش ناموفق بود."
                )
            }
        }
    }

    private fun validateForm() {
        val current =
            _uiState.value as? ReportUiState.Form ?: return

        if (!current.isProfileComplete || current.isSending) return

        val data = current.data

        if (data.reportType.isBlank()) {
            _uiState.update {
                (it as? ReportUiState.Form)?.copy(
                    validationError = "نوع گزارش را انتخاب کنید."
                ) ?: it
            }
            return
        }

        val year = data.year.toIntOrNull()
        val month = data.month.toIntOrNull()
        val day = data.day.toIntOrNull()

        if (year == null || year <= 0) {
            setDateError(yearError = "سال را صحیح وارد کنید.")
            return
        }

        if (month == null || month !in 1..12) {
            setDateError(monthError = "ماه باید بین ۱ تا ۱۲ باشد.")
            return
        }

        if (day == null || day !in 1..31) {
            setDateError(dayError = "روز باید بین ۱ تا ۳۱ باشد.")
            return
        }

        var duration = 0

        if (data.reportType == "روزانه") {
            val entryMinutes = parseTimeToMinutes(data.entryTime)
            val exitMinutes = parseTimeToMinutes(data.exitTime)

            if (entryMinutes == null) {
                setDateError(
                    entryTimeError = "ساعت ورود معتبر نیست."
                )
                return
            }

            if (exitMinutes == null) {
                setDateError(
                    exitTimeError = "ساعت خروج معتبر نیست."
                )
                return
            }

            if (exitMinutes <= entryMinutes) {
                setDateError(
                    exitTimeError =
                        "ساعت خروج باید بعد از ساعت ورود باشد."
                )
                return
            }

            duration = exitMinutes - entryMinutes
        }

        saveReport(
            data = data,
            year = year,
            month = month,
            day = day,
            duration = duration,
            editingReportId = current.editingReportId
        )
    }

    private fun setDateError(
        yearError: String? = null,
        monthError: String? = null,
        dayError: String? = null,
        entryTimeError: String? = null,
        exitTimeError: String? = null
    ) {
        _uiState.update { current ->
            if (current is ReportUiState.Form) {
                current.copy(
                    yearError = yearError,
                    monthError = monthError,
                    dayError = dayError,
                    entryTimeError = entryTimeError,
                    exitTimeError = exitTimeError
                )
            } else {
                current
            }
        }
    }

    private fun saveReport(
        data: ReportFormData,
        year: Int,
        month: Int,
        day: Int,
        duration: Int,
        editingReportId: Long?
    ) {
        val current =
            _uiState.value as? ReportUiState.Form ?: return

        if (!current.isProfileComplete || current.isSending) return

        _uiState.value = current.copy(
            isSending = true,
            sendError = null,
            validationError = null,
            isSent = false
        )

        viewModelScope.launch {
            try {
                val existingReport = if (editingReportId != null) {
                    workReportUseCases
                        .getReportById(editingReportId)
                        .first()
                        ?: throw IllegalStateException(
                            "گزارش موردنظر پیدا نشد."
                        )
                } else {
                    null
                }

                val finalType =
                    existingReport?.reportType ?: data.reportType

                val stats = observeWorkTimeCountersUseCase().first()

                val finalDuration = when (finalType) {
                    "روزانه" -> duration

                    "هفتگی" -> {
                        existingReport?.workDurationMinutes
                            ?: stats.weeklyMinutes
                    }

                    "ماهانه" -> {
                        existingReport?.workDurationMinutes
                            ?: stats.monthlyMinutes
                    }

                    else -> duration
                }

                val reportDate = "%04d/%02d/%02d".format(
                    year,
                    month,
                    day
                )

                if (existingReport == null) {
                    val newReport = WorkReport(
                        reportType = finalType,
                        reportDate = reportDate,
                        reportText = data.reportText,
                        problems = data.problems,
                        tomorrowPlan = data.tomorrowPlan,
                        workDurationMinutes = finalDuration,
                        entryTime = data.entryTime,
                        exitTime = data.exitTime
                    )

                    workReportUseCases.saveReport(newReport)
                } else {
                    val updatedReport = existingReport.copy(
                        reportType = existingReport.reportType,
                        reportDate = reportDate,
                        reportText = data.reportText,
                        problems = data.problems,
                        tomorrowPlan = data.tomorrowPlan,
                        workDurationMinutes = finalDuration,
                        entryTime = data.entryTime,
                        exitTime = data.exitTime,
                        isSent = false,
                        sentAtMillis = null,
                        isEdited = true
                    )

                    workReportUseCases.updateReport(updatedReport)
                }

                _uiState.update { state ->
                    if (state is ReportUiState.Form) {
                        state.copy(
                            isSending = false,
                            isSent = true,
                            sendError = null,
                            validationError = null
                        )
                    } else {
                        state
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { state ->
                    if (state is ReportUiState.Form) {
                        state.copy(
                            isSending = false,
                            isSent = false,
                            sendError = e.message
                                ?: "ذخیره گزارش ناموفق بود."
                        )
                    } else {
                        state
                    }
                }
            }
        }
    }

    private fun parseTimeToMinutes(value: String): Int? {
        val parts = value.trim().split(":")
        if (parts.size != 2) return null

        val hour = parts[0].toIntOrNull() ?: return null
        val minute = parts[1].toIntOrNull() ?: return null

        if (hour !in 0..23 || minute !in 0..59) return null

        return hour * 60 + minute
    }

    private fun getInitialFormData(): ReportFormData {
        return ReportFormData(
            firstName = currentEmployee?.firstName.orEmpty(),
            lastName = currentEmployee?.lastName.orEmpty(),
            department = currentEmployee?.department.orEmpty(),
            reportType = "روزانه",
            year = "",
            month = "",
            day = "",
            entryTime = "",
            exitTime = "",
            reportText = "",
            problems = "",
            tomorrowPlan = ""
        )
    }
}