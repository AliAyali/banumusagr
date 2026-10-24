package com.aliayali.domain

import com.aliayali.domain.repository.WorkTimeCountersRepository
import jakarta.inject.Inject

class AddTotalWorkMinutesUseCase @Inject constructor(
    private val repository: WorkTimeCountersRepository
) {
    suspend operator fun invoke(minutes: Int) {
        repository.addTotalMinutes(minutes)
    }
}