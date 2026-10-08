package com.aliayali.banumusagr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliayali.banumusagr.R
import com.aliayali.banumusagr.components.DateSection
import com.aliayali.banumusagr.components.EmployeeInformation
import com.aliayali.banumusagr.components.Header
import com.aliayali.banumusagr.components.ProblemsCard
import com.aliayali.banumusagr.components.ReportCard
import com.aliayali.banumusagr.components.ReportTypeSelector
import com.aliayali.banumusagr.components.SentMessage
import com.aliayali.banumusagr.components.TomorrowPlanCard
import com.aliayali.banumusagr.components.TotalWorkHoursCard
import com.aliayali.banumusagr.network.ReportSender
import com.aliayali.banumusagr.utils.getPersianToday
import kotlinx.coroutines.launch


@Composable
fun BanumusaApp() {

    val today = remember {
        getPersianToday()
    }

    val scope = rememberCoroutineScope()

    var isSending by remember {
        mutableStateOf(false)
    }

    var sendError by remember {
        mutableStateOf(false)
    }

    var totalWorkHours by remember {
        mutableStateOf("")
    }

    var employeeName by remember {
        mutableStateOf("")
    }

    var employeeDepartment by remember {
        mutableStateOf("")
    }

    val dailyReport = stringResource(
        R.string.report_daily
    )

    var selectedReportType by remember {
        mutableStateOf(dailyReport)
    }

    var reportText by remember {
        mutableStateOf("")
    }

    var problems by remember {
        mutableStateOf("")
    }

    var tomorrowPlan by remember {
        mutableStateOf("")
    }

    var isSent by remember {
        mutableStateOf(false)
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Top
        ) {

            Header()


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            ReportTypeSelector(
                selectedType = selectedReportType,

                onTypeSelected = { selectedType ->

                    selectedReportType = selectedType

                    isSent = false
                    sendError = false
                }
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            EmployeeInformation(

                fullName = employeeName,

                onFullNameChange = {
                    employeeName = it
                    isSent = false
                    sendError = false
                },

                department = employeeDepartment,

                onDepartmentChange = {
                    employeeDepartment = it
                    isSent = false
                    sendError = false
                }
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            DateSection()


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            ReportCard(

                reportText = reportText,

                onReportTextChange = { text ->

                    reportText = text

                    isSent = false
                    sendError = false
                }
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            ProblemsCard(

                problems = problems,

                onProblemsChange = { text ->

                    problems = text

                    isSent = false
                    sendError = false
                }
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            TomorrowPlanCard(

                text = tomorrowPlan,

                onTextChange = { text ->

                    tomorrowPlan = text

                    isSent = false
                    sendError = false
                }
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            TotalWorkHoursCard(

                totalHours = totalWorkHours,

                onTotalHoursChange = {

                    totalWorkHours = it

                    isSent = false
                    sendError = false
                }
            )


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            Button(

                onClick = {

                    if (isSending) {
                        return@Button
                    }

                    isSending = true
                    isSent = false
                    sendError = false


                    scope.launch {

                        try {

                            val response = ReportSender.sendReport(

                                reportType = selectedReportType,

                                employeeName = employeeName,

                                department = employeeDepartment,

                                date = today,

                                reportText = reportText,

                                problems = problems,

                                tomorrowPlan = tomorrowPlan,

                                totalWorkHours = totalWorkHours
                            )


                            if (response.ok) {

                                isSent = true

                                employeeName = ""
                                employeeDepartment = ""
                                reportText = ""
                                problems = ""
                                tomorrowPlan = ""
                                totalWorkHours = ""

                            } else {
                                sendError = true
                            }


                        } catch (e: Exception) {

                            sendError = true

                        } finally {

                            isSending = false
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                enabled = !isSending,

                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {

                if (isSending) {

                    CircularProgressIndicator(

                        modifier = Modifier.size(22.dp),

                        color = MaterialTheme.colorScheme.onPrimary,

                        strokeWidth = 2.dp
                    )
                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.sending_report
                        ),
                        fontSize = 16.sp
                    )

                } else {

                    Text(
                        text = stringResource(
                            R.string.send_report
                        ),
                        fontSize = 16.sp
                    )
                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null
                    )
                }
            }

            if (isSent) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                SentMessage()
            }


            if (sendError) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = stringResource(
                        R.string.connection_error_message
                    ),

                    color = MaterialTheme.colorScheme.error,

                    fontSize = 14.sp
                )
            }
        }
    }
}