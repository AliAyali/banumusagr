package com.aliayali.domain

import com.aliayali.domain.repository.WorkTimeCountersRepository
import javax.inject.Inject

class ObserveWorkTimeCountersUseCase @Inject constructor(
    private val repository: WorkTimeCountersRepository
) {
    operator fun invoke() = repository.observeCounters()
}