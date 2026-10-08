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
        totalWorkHours: String
    ): ResponseModel {

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
            $problems

            📌 برنامه کاری بعدی:
            $tomorrowPlan

            ⏱ جمع ساعات کاری:
            $totalWorkHours
        """.trimIndent()

        return RetrofitService.apiService.sendReport(
            to = TO,
            text = message
        )
    }
}