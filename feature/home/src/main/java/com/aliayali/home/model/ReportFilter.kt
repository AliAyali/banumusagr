package com.aliayali.home.model

enum class ReportFilter(val title: String) {
    ALL("همه"),
    DAILY("روزانه"),
    WEEKLY("هفتگی"),
    MONTHLY("ماهانه");

    fun matches(reportType: String): Boolean {
        if (this == ALL) return true

        val normalizedType = reportType.trim().uppercase()

        return when (this) {
            ALL -> true
            DAILY -> normalizedType in setOf("DAILY", "روزانه")
            WEEKLY -> normalizedType in setOf("WEEKLY", "هفتگی")
            MONTHLY -> normalizedType in setOf("MONTHLY", "ماهانه")
        }
    }
}