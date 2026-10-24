package com.aliayali.home

sealed interface HomeEvent {
    data object Refresh : HomeEvent

    data class SendReport(val reportId: Long) : HomeEvent

    data class DeleteReport(val reportId: Long) : HomeEvent
}