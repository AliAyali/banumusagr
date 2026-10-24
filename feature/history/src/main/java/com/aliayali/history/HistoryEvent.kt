package com.aliayali.history

import com.aliayali.history.model.HistoryFilter

sealed interface HistoryEvent {
    data class FilterChanged(
        val filter: HistoryFilter
    ) : HistoryEvent

    data object Retry : HistoryEvent
}
