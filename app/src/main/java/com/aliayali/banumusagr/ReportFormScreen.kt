//package com.aliayali.banumusagr
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.aliayali.banumusagr.components.DateSection
//import com.aliayali.banumusagr.components.EmployeeInformation
//import com.aliayali.banumusagr.components.Header
//import com.aliayali.banumusagr.components.ProblemsCard
//import com.aliayali.banumusagr.components.ReportCard
//import com.aliayali.banumusagr.components.ReportTypeSelector
//import com.aliayali.banumusagr.components.SentMessage
//import com.aliayali.banumusagr.components.TomorrowPlanCard
//import com.aliayali.banumusagr.components.TotalWorkHoursCard
//import com.aliayali.banumusagr.database.ReportDatabase
//import com.aliayali.banumusagr.database.repository.EmployeeRepositoryImpl
//import com.aliayali.banumusagr.database.repository.WorkReportRepositoryImpl
//import com.aliayali.banumusagr.domain.model.Employee
//import com.aliayali.banumusagr.domain.model.ReportType
//import com.aliayali.banumusagr.domain.model.WorkReport
//import com.aliayali.banumusagr.network.ReportSender
//import com.aliayali.banumusagr.utils.getPersianToday
//import kotlinx.coroutines.CancellationException
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//@Composable
//fun ReportFormScreen(
//    onReportSubmitted: () -> Unit
//) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//
//    val database = remember {
//        ReportDatabase.getDatabase(context)
//    }
//
//    val employeeRepository = remember {
//        EmployeeRepositoryImpl(database.employeeDao())
//    }
//
//    val workReportRepository = remember {
//        WorkReportRepositoryImpl(database.workReportDao())
//    }
//
//    val today = remember {
//        getPersianToday()
//    }
//
//    val dailyReport = stringResource(R.string.report_daily)
//    val weeklyReport = stringResource(R.string.report_weekly)
//    val monthlyReport = stringResource(R.string.report_monthly)
//
//    var isSending by remember { mutableStateOf(false) }
//    var sendError by remember { mutableStateOf(false) }
//    var validationError by remember { mutableStateOf(false) }
//
//    var hoursError by remember { mutableStateOf<String?>(null) }
//    var minutesError by remember { mutableStateOf<String?>(null) }
//
//    var employeeName by remember { mutableStateOf("") }
//    var employeeDepartment by remember { mutableStateOf("") }
//    var selectedReportType by remember { mutableStateOf(dailyReport) }
//
//    var workHours by remember { mutableStateOf("") }
//    var workMinutes by remember { mutableStateOf("") }
//
//    var reportText by remember { mutableStateOf("") }
//    var problems by remember { mutableStateOf("") }
//    var tomorrowPlan by remember { mutableStateOf("") }
//    var isSent by remember { mutableStateOf(false) }
//
//    val isDailyReport = selectedReportType == dailyReport
//
//    LaunchedEffect(employeeRepository) {
//        try {
//            val employee = withContext(Dispatchers.IO) {
//                employeeRepository.observeEmployee().first()
//            }
//
//            if (employee != null) {
//                employeeName = listOf(
//                    employee.firstName,
//                    employee.lastName
//                )
//                    .filter { it.isNotBlank() }
//                    .joinToString(" ")
//
//                employeeDepartment = employee.department
//            }
//        } catch (_: Exception) {
//            employeeName = ""
//            employeeDepartment = ""
//        }
//    }
//
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(top = 30.dp),
//        color = MaterialThemecolorScheme.background
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(horizontal = 16.dp, vertical = 12.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(14.dp)
//        ) {
//            Header()
//
//            DateSection()
//
//            EmployeeInformation(
//                fullName = employeeName,
//                onFullNameChange = {
//                    employeeName = it
//                    validationError = false
//                    sendError = false
//                },
//                department = employeeDepartment,
//                onDepartmentChange = {
//                    employeeDepartment = it
//                    validationError = false
//                    sendError = false
//                }
//            )
//
//            ReportTypeSelector(
//                selectedType = selectedReportType,
//                onTypeSelected = {
//                    selectedReportType = it
//                    hoursError = null
//                    minutesError = null
//                    validationError = false
//                    sendError = false
//                    isSent = false
//                }
//            )
//
//            if (isDailyReport) {
//                TotalWorkHoursCard(
//                    hours = workHours,
//                    onHoursChange = {
//                        workHours = it
//                        hoursError = null
//
//                        if (
//                            normalizeDigits(it).toIntOrNull() != 24 ||
//                            normalizeDigits(workMinutes).toIntOrNull() == 0
//                        ) {
//                            if (hoursError?.contains("مجموع زمان") == true) {
//                                hoursError = null
//                            }
//                        }
//
//                        validationError = false
//                    },
//                    minutes = workMinutes,
//                    onMinutesChange = {
//                        workMinutes = it
//                        minutesError = null
//
//                        if (hoursError?.contains("مجموع زمان") == true) {
//                            hoursError = null
//                        }
//
//                        validationError = false
//                    },
//                    hoursError = hoursError,
//                    minutesError = minutesError
//                )
//            }
//
//            ReportCard(
//                reportText = reportText,
//                onReportTextChange = {
//                    reportText = it
//                    validationError = false
//                    sendError = false
//                }
//            )
//
//            ProblemsCard(
//                problems = problems,
//                onProblemsChange = {
//                    problems = it
//                }
//            )
//
//            TomorrowPlanCard(
//                text = tomorrowPlan,
//                onTextChange = {
//                    tomorrowPlan = it
//                }
//            )
//
//            if (validationError) {
//                Text(
//                    text = if (isDailyReport) {
//                        "لطفاً نام و نام خانوادگی، واحد سازمانی و متن گزارش را تکمیل کنید"
//                    } else {
//                        "لطفاً نام و نام خانوادگی، واحد سازمانی و متن گزارش را تکمیل کنید"
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    color = MaterialTheme.colorScheme.error,
//                    textAlign = TextAlign.End,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//
//            if (sendError) {
//                Text(
//                    text = "ارسال گزارش انجام نشد، اتصال اینترنت را بررسی کنید و دوباره تلاش کنید",
//                    modifier = Modifier.fillMaxWidth(),
//                    color = MaterialTheme.colorScheme.error,
//                    textAlign = TextAlign.End,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//
//            if (isSent) {
//                SentMessage()
//            }
//
//            Button(
//                onClick = {
//                    if (isSending) {
//                        return@Button
//                    }
//
//                    validationError = false
//                    sendError = false
//                    hoursError = null
//                    minutesError = null
//                    isSent = false
//
//                    var hours = 0
//                    var minutes = 0
//
//                    if (isDailyReport) {
//                        val normalizedHours = normalizeDigits(workHours)
//                        val normalizedMinutes = normalizeDigits(workMinutes)
//
//                        val parsedHours = normalizedHours.toIntOrNull()
//                        val parsedMinutes = normalizedMinutes.toIntOrNull()
//
//                        hoursError = when {
//                            normalizedHours.isBlank() ->
//                                "ساعت کاری را وارد کنید"
//
//                            parsedHours == null ->
//                                "ساعت باید یک عدد معتبر باشد"
//
//                            parsedHours !in 0..24 ->
//                                "ساعت باید بین صفر تا ۲۴ باشد"
//
//                            else -> null
//                        }
//
//                        minutesError = when {
//                            normalizedMinutes.isBlank() ->
//                                "دقیقه کاری را وارد کنید"
//
//                            parsedMinutes == null ->
//                                "دقیقه باید یک عدد معتبر باشد"
//
//                            parsedMinutes !in 0..59 ->
//                                "دقیقه باید بین صفر تا ۵۹ باشد"
//
//                            else -> null
//                        }
//
//                        if (hoursError != null || minutesError != null) {
//                            return@Button
//                        }
//
//                        val validHours = parsedHours ?: return@Button
//                        val validMinutes = parsedMinutes ?: return@Button
//
//                        if (validHours == 24 && validMinutes > 0) {
//                            hoursError = "مجموع زمان نباید بیشتر از ۲۴ ساعت باشد"
//                            return@Button
//                        }
//
//                        hours = validHours
//                        minutes = validMinutes
//                    }
//
//                    val nameParts = employeeName.trim()
//                        .split(Regex("\\s+"))
//                        .filter { it.isNotBlank() }
//
//                    if (
//                        nameParts.size < 2 ||
//                        employeeDepartment.isBlank() ||
//                        reportText.isBlank()
//                    ) {
//                        validationError = true
//                        return@Button
//                    }
//
//                    val firstName = nameParts.first()
//                    val lastName = nameParts.drop(1).joinToString(" ")
//
//                    val reportType = when (selectedReportType) {
//                        dailyReport -> ReportType.DAILY
//                        weeklyReport -> ReportType.WEEKLY
//                        monthlyReport -> ReportType.MONTHLY
//                        else -> ReportType.DAILY
//                    }
//
//                    val durationMinutes = hours * 60 + minutes
//
//                    val todayWorkHours = if (isDailyReport) {
//                        "$hours ساعت و $minutes دقیقه"
//                    } else {
//                        "برای گزارش $selectedReportType ثبت نمی‌شود"
//                    }
//
//                    val report = WorkReport(
//                        reportType = reportType,
//                        reportDate = today,
//                        reportText = reportText.trim(),
//                        problems = problems.trim(),
//                        tomorrowPlan = tomorrowPlan.trim(),
//                        workDurationMinutes = durationMinutes
//                    )
//
//                    scope.launch {
//                        isSending = true
//
//                        try {
//                            withContext(Dispatchers.IO) {
//                                employeeRepository.saveEmployee(
//                                    Employee(
//                                        firstName = firstName,
//                                        lastName = lastName,
//                                        department = employeeDepartment.trim()
//                                    )
//                                )
//                            }
//
//                            withContext(Dispatchers.IO) {
//                                workReportRepository.insertReport(report)
//                            }
//
//                            val totalWorkHours = if (
//                                selectedReportType == weeklyReport ||
//                                selectedReportType == monthlyReport
//                            ) {
//                                withContext(Dispatchers.IO) {
//                                    val totalMinutes = workReportRepository
//                                        .observeTotalWorkMinutes()
//                                        .first()
//
//                                    "${totalMinutes / 60} ساعت و ${totalMinutes % 60} دقیقه"
//                                }
//                            } else {
//                                null
//                            }
//
//                            val response = withContext(Dispatchers.IO) {
//                                ReportSender.sendReport(
//                                    reportType = selectedReportType,
//                                    employeeName = employeeName.trim(),
//                                    department = employeeDepartment.trim(),
//                                    date = today,
//                                    reportText = reportText.trim(),
//                                    problems = problems.trim(),
//                                    tomorrowPlan = tomorrowPlan.trim(),
//                                    todayWorkHours = todayWorkHours,
//                                    totalWorkHours = totalWorkHours
//                                )
//                            }
//
//                            if (response.ok) {
//                                isSent = true
//                                sendError = false
//                                onReportSubmitted()
//                            } else {
//                                sendError = true
//                            }
//                        } catch (exception: CancellationException) {
//                            throw exception
//                        } catch (_: Exception) {
//                            sendError = true
//                        } finally {
//                            isSending = false
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp),
//                enabled = !isSending,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                )
//            ) {
//                if (isSending) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(18.dp),
//                        color = MaterialTheme.colorScheme.onPrimary,
//                        strokeWidth = 2.dp
//                    )
//                } else {
//                    Text(
//                        text = "ثبت و ارسال گزارش",
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//    }
//}
//
//private fun normalizeDigits(value: String): String {
//    return value.trim().map { char ->
//        when (char) {
//            in '۰'..'۹' -> (char.code - '۰'.code + '0'.code).toChar()
//            in '٠'..'٩' -> (char.code - '٠'.code + '0'.code).toChar()
//            else -> char
//        }
//    }.joinToString("")
//}