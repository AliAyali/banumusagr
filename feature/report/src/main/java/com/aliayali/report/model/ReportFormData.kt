package com.aliayali.report.model

data class ReportFormData(
    val firstName: String = "",
    val lastName: String = "",
    val department: String = "",
    val reportType: String = "روزانه",

    val day: String = "",
    val month: String = "",
    val year: String = "",

    val entryTime: String = "",
    val exitTime: String = "",

    val reportText: String = "",
    val problems: String = "",
    val tomorrowPlan: String = ""
) {
    val workDurationMinutes: Int?
        get() {
            val entry = entryTime.toMinutes()
            val exit = exitTime.toMinutes()

            if (entry == null || exit == null || exit <= entry) {
                return null
            }

            return exit - entry
        }

    private fun String.toMinutes(): Int? {
        val parts = split(":")
        if (parts.size != 2) return null

        val hours = parts[0].toIntOrNull() ?: return null
        val minutes = parts[1].toIntOrNull() ?: return null

        if (hours !in 0..23 || minutes !in 0..59) return null

        return hours * 60 + minutes
    }
}