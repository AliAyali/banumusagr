package com.aliayali.report

sealed interface ReportEvent {

    data class FirstNameChanged(val value: String) : ReportEvent
    data class LastNameChanged(val value: String) : ReportEvent
    data class DepartmentChanged(val value: String) : ReportEvent

    data class ReportTypeChanged(val value: String) : ReportEvent
    data class DayChanged(val value: String) : ReportEvent
    data class MonthChanged(val value: String) : ReportEvent
    data class YearChanged(val value: String) : ReportEvent

    data class EntryTimeChanged(val value: String) : ReportEvent
    data class ExitTimeChanged(val value: String) : ReportEvent

    data class ReportTextChanged(val value: String) : ReportEvent
    data class ProblemsChanged(val value: String) : ReportEvent
    data class TomorrowPlanChanged(val value: String) : ReportEvent

    data object Submit : ReportEvent
    data object DismissErrors : ReportEvent
    data object CancelEditing : ReportEvent
}