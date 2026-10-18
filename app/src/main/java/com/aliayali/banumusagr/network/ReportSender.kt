package com.aliayali.banumusagr.network


object ReportSender {

    private const val TO = "3oW9pmF6Z4dfXT3isu7oceaLOxmywkIVrAZawNbe"

    suspend fun sendReport(
        reportType: String,
        employeeName: String,
        department: String,
        date: String,
        reportText: String,
        problems: String,
        tomorrowPlan: String,
        todayWorkHours: String,
        totalWorkHours: String? = null
    ): ResponseModel {

        val totalHoursSection = if (!totalWorkHours.isNullOrBlank()) {
            """
        
        📊 مجموع ساعات کاری ثبت‌شده:
        $totalWorkHours
        """.trimIndent()
        } else {
            ""
        }

        val message = """
        📋 گزارش $reportType

        👤 نام و نام خانوادگی:
        $employeeName

        🏢 واحد:
        $department

        📅 تاریخ:
        $date

        📝 گزارش فعالیت:
        $reportText

        ⚠️ مشکلات و موانع:
        ${problems.ifBlank { "موردی ثبت نشده است" }}

        📌 برنامه کاری بعدی:
        ${tomorrowPlan.ifBlank { "موردی ثبت نشده است" }}

        ⏱ ساعات کاری امروز:
        $todayWorkHours
        $totalHoursSection
    """.trimIndent()

        return RetrofitService.apiService.sendReport(
            to = TO,
            text = message
        )
    }
}