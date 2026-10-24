package com.aliayali.home.mapper

import com.aliayali.home.model.HomeReportUiModel
import com.aliayali.home.model.HomeUiData
import com.aliayali.model.WorkReport

fun WorkReport.asHomeUiModel(): HomeReportUiModel {
    val typeTitle = when (reportType.trim().uppercase()) {
        "DAILY", "روزانه" -> "گزارش روزانه"
        "WEEKLY", "هفتگی" -> "گزارش هفتگی"
        "MONTHLY", "ماهانه" -> "گزارش ماهانه"
        else -> "گزارش کاری"
    }

    return HomeReportUiModel(
        id = id,
        reportTypeTitle = typeTitle,
        reportDate = reportDate,
        reportText = reportText,
        problems = problems,
        tomorrowPlan = tomorrowPlan,
        workDurationMinutes = workDurationMinutes,
        reportType = reportType,
        isSent = isSent,
        isLatestOfType = false
    )
}

fun List<WorkReport>.asHomeUiData(
    totalWorkMinutes: Int
): HomeUiData {
    return HomeUiData(
        reports = map { it.asHomeUiModel() },
        totalWorkMinutes = totalWorkMinutes
    )
}