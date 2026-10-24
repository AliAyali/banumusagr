package com.aliayali.report.mapper

import com.aliayali.report.model.ReportFormData

fun ReportFormData.normalized(): ReportFormData {
    return copy(
        firstName = firstName.normalizeDigits().trim(),
        lastName = lastName.normalizeDigits().trim(),
        department = department.trim(),
        day = day.normalizeDigits().trim(),
        month = month.normalizeDigits().trim(),
        year = year.normalizeDigits().trim(),
        entryTime = entryTime.normalizeDigits().trim(),
        exitTime = exitTime.normalizeDigits().trim(),
        reportText = reportText.trim(),
        problems = problems.trim(),
        tomorrowPlan = tomorrowPlan.trim()
    )
}

private fun String.normalizeDigits(): String {
    val persianDigits = "۰۱۲۳۴۵۶۷۸۹"
    val arabicDigits = "٠١٢٣٤٥٦٧٨٩"

    return map { char ->
        when (char) {
            in persianDigits -> {
                ('0'.code + persianDigits.indexOf(char)).toChar()
            }

            in arabicDigits -> {
                ('0'.code + arabicDigits.indexOf(char)).toChar()
            }

            else -> char
        }
    }.joinToString("")
}