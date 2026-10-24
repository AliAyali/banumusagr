package com.aliayali.home.model

data class HomeUiData(
    val reports: List<HomeReportUiModel> = emptyList(),

    val dailyWorkMinutes: Int = 0,
    val weeklyWorkMinutes: Int = 0,
    val monthlyWorkMinutes: Int = 0,
    val totalWorkMinutes: Int = 0
)