package com.aliayali.home.components

import com.aliayali.model.WorkReport

object ReportMessageFormatter {

    fun format(
        report: WorkReport,
        firstName: String,
        lastName: String,
        activityUnit: String,
        userId: Long
    ): String = buildString {
        appendLine("📋 گزارش ${report.reportType}")

        if (report.isEdited) {
            appendLine("✏️ این گزارش ویرایش شده است")
        }

        appendLine("━━━━━━━━━━━━━━━━━━")
        appendLine("👤 نام و نام خانوادگی: $firstName $lastName")
        appendLine("🏢 واحد فعالیت: $activityUnit")
        appendLine("📅 تاریخ گزارش: ${report.reportDate}")
        appendLine("🆔 شناسه گزارش: ${report.id}")
        appendLine("👥 شناسه کاربر: $userId")

        val hours = report.workDurationMinutes / 60
        val minutes = report.workDurationMinutes % 60

        appendLine("⏱ مدت کار: $hours ساعت و $minutes دقیقه")
        appendLine()

        appendLine("📝 شرح وظایف:")
        appendLine(report.reportText.ifBlank { "ثبت نشده است." })
        appendLine()

        appendLine("⚠️ مشکلات و موانع:")
        appendLine(report.problems.ifBlank { "موردی ثبت نشده است." })
        appendLine()

        appendLine("📌 برنامه فردا:")
        appendLine(report.tomorrowPlan.ifBlank { "ثبت نشده است." })
        appendLine()

        appendLine("━━━━━━━━━━━━━━━━━━")
        val userTag = "${firstName}_${lastName}"
            .trim()
            .replace(Regex("\\s+"), "_")

        append("#$userTag #report_${report.id}")

        if (report.isEdited) {
            append(" #edited")
        }
    }
}