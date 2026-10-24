package com.aliayali.profile

import com.aliayali.profile.model.ProfileTheme

data class ProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val activeUnit: String = "",

    val profileSaved: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,

    val selectedTheme: ProfileTheme = ProfileTheme.Default,
    val errorMessage: String? = null,
    val initialHoursSaveSuccess: Boolean = false,

    val initialTotalHours: String = "",
    val initialTotalMinutes: String = "",
    val initialMonthlyHours: String = "",
    val initialMonthlyMinutes: String = "",
    val initialHoursMonth: String = "",
    val initialHoursSaved: Boolean = false,
    val isSavingInitialHours: Boolean = false,

    )