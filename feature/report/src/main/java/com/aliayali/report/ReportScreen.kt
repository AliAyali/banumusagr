package com.aliayali.report

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.aliayali.report.components.ReportCard
import com.aliayali.report.components.ReportDateField
import com.aliayali.report.components.ReportFieldLabel
import com.aliayali.report.components.ReportHeader
import com.aliayali.report.components.ReportInput
import com.aliayali.report.components.ReportMessage
import com.aliayali.report.components.ReportTimeField
import com.aliayali.report.components.ReportTypeSelector

@Composable
fun ReportScreen(
    uiState: ReportUiState,
    onEvent: (ReportEvent) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val colors = MaterialTheme.colorScheme

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colors.background
        ) {
            when (uiState) {

                ReportUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ReportUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.message,
                            color = colors.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                is ReportUiState.Form -> {

                    // اعتبارسنجی تاریخ
                    val year = uiState.data.year.toIntOrNull()
                    val month = uiState.data.month.toIntOrNull()
                    val day = uiState.data.day.toIntOrNull()

                    val isDateValid =
                        year != null &&
                                month != null && month in 1..12 &&
                                day != null && day in 1..31

                    // شرح فعالیت اجباری است
                    val isReportTextValid =
                        uiState.data.reportText.isNotBlank()

                    // فقط گزارش روزانه به ساعت ورود و خروج نیاز دارد
                    val isDailyReport =
                        uiState.data.reportType == "روزانه"

                    val isWorkTimeValid =
                        !isDailyReport ||
                                (
                                        uiState.data.entryTime.isNotBlank() &&
                                                uiState.data.exitTime.isNotBlank()
                                        )

                    val isRequiredFieldsValid =
                        isDateValid &&
                                isReportTextValid &&
                                isWorkTimeValid

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(
                                    radius = if (uiState.isProfileComplete) {
                                        0.dp
                                    } else {
                                        16.dp
                                    }
                                )
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp)
                                .padding(
                                    top = 18.dp,
                                    bottom = 32.dp
                                ),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            ReportHeader()

                            // اطلاعات پرسنلی
                            ReportCard(
                                title = "اطلاعات پرسنلی",
                                subtitle = "مشخصات ثبت‌کننده گزارش",
                                icon = "۱"
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    ProfileInfoItem(
                                        label = "نام",
                                        value = uiState.data.firstName
                                    )

                                    ProfileInfoItem(
                                        label = "نام خانوادگی",
                                        value = uiState.data.lastName
                                    )

                                    ProfileInfoItem(
                                        label = "واحد سازمانی",
                                        value = uiState.data.department
                                    )
                                }
                            }

                            // جزئیات گزارش
                            ReportCard(
                                title = "جزئیات گزارش",
                                subtitle = "نوع گزارش، تاریخ و ساعات کاری",
                                icon = "۲"
                            ) {
                                ReportFieldLabel("نوع گزارش")

                                ReportTypeSelector(
                                    selected = uiState.data.reportType,
                                    enabled = uiState.editingReportId == null,
                                    onSelected = { type ->
                                        onEvent(
                                            ReportEvent.ReportTypeChanged(type)
                                        )
                                    }
                                )

                                ReportDateField(
                                    label = "تاریخ گزارش",
                                    value = "${uiState.data.year}/${uiState.data.month}/${uiState.data.day}",
                                    onSelected = { selectedDate ->
                                        val parts = selectedDate.split("/")

                                        if (parts.size == 3) {
                                            onEvent(
                                                ReportEvent.YearChanged(parts[0])
                                            )
                                            onEvent(
                                                ReportEvent.MonthChanged(parts[1])
                                            )
                                            onEvent(
                                                ReportEvent.DayChanged(parts[2])
                                            )
                                        }
                                    },
                                    error = listOfNotNull(
                                        uiState.dayError,
                                        uiState.monthError,
                                        uiState.yearError
                                    ).joinToString(" ").ifBlank {
                                        null
                                    }
                                )

                                if (isDailyReport) {
                                    ReportFieldLabel("ساعت کاری")

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        ReportTimeField(
                                            label = "ساعت ورود",
                                            value = uiState.data.entryTime,
                                            error = uiState.entryTimeError,
                                            modifier = Modifier.weight(1f),
                                            onSelected = { time ->
                                                onEvent(
                                                    ReportEvent.EntryTimeChanged(time)
                                                )
                                            }
                                        )

                                        ReportTimeField(
                                            label = "ساعت خروج",
                                            value = uiState.data.exitTime,
                                            error = uiState.exitTimeError,
                                            modifier = Modifier.weight(1f),
                                            onSelected = { time ->
                                                onEvent(
                                                    ReportEvent.ExitTimeChanged(time)
                                                )
                                            }
                                        )
                                    }
                                }
                            }

                            // شرح فعالیت‌ها
                            ReportCard(
                                title = "شرح فعالیت‌ها",
                                subtitle = "کارهایی که در این بازه انجام داده‌اید",
                                icon = "۳"
                            ) {
                                ReportInput(
                                    label = "گزارش فعالیت *",
                                    value = uiState.data.reportText,
                                    onValueChange = { text ->
                                        onEvent(
                                            ReportEvent.ReportTextChanged(text)
                                        )
                                    },
                                    placeholder = "فعالیت‌های انجام‌شده را توضیح دهید...",
                                    minLines = 4
                                )
                            }

                            // مشکلات اختیاری
                            ReportCard(
                                title = "چالش‌ها و مشکلات",
                                subtitle = "این قسمت اختیاری است",
                                icon = "۴"
                            ) {
                                ReportInput(
                                    label = "مشکلات",
                                    value = uiState.data.problems,
                                    onValueChange = { text ->
                                        onEvent(
                                            ReportEvent.ProblemsChanged(text)
                                        )
                                    },
                                    placeholder = "اگر مشکلی وجود داشته، اینجا بنویسید...",
                                    minLines = 3
                                )
                            }

                            // برنامه آینده اختیاری
                            ReportCard(
                                title = "برنامه آینده",
                                subtitle = "این قسمت اختیاری است",
                                icon = "۵"
                            ) {
                                ReportInput(
                                    label = "برنامه فردا",
                                    value = uiState.data.tomorrowPlan,
                                    onValueChange = { text ->
                                        onEvent(
                                            ReportEvent.TomorrowPlanChanged(text)
                                        )
                                    },
                                    placeholder = "برنامه‌های روز بعد را وارد کنید...",
                                    minLines = 3
                                )
                            }

                            uiState.validationError?.let { message ->
                                ReportMessage(
                                    message = message,
                                    isError = true
                                )
                            }

                            uiState.sendError?.let { message ->
                                ReportMessage(
                                    message = message,
                                    isError = true
                                )
                            }

                            if (uiState.isSent) {
                                ReportMessage(
                                    message = "گزارش با موفقیت ثبت شد.",
                                    isError = false
                                )
                            }

                            // راهنمای فیلدهای اجباری
                            if (!isRequiredFieldsValid) {
                                Text(
                                    text = if (isDailyReport) {
                                        "برای ثبت گزارش، تاریخ، ساعت ورود، ساعت خروج و شرح فعالیت را وارد کنید."
                                    } else {
                                        "برای ثبت گزارش، تاریخ و شرح فعالیت را وارد کنید."
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    color = colors.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Start
                                )
                            }

                            // دکمه ثبت یا ذخیره تغییرات
                            Button(
                                onClick = {
                                    onEvent(ReportEvent.Submit)
                                },
                                enabled = !uiState.isSending &&
                                        uiState.isProfileComplete &&
                                        isRequiredFieldsValid,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.primary,
                                    contentColor = colors.onPrimary
                                )
                            ) {
                                if (uiState.isSending) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(22.dp),
                                        color = colors.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = if (uiState.editingReportId != null) {
                                            "ذخیره تغییرات"
                                        } else {
                                            "ثبت گزارش"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Text(
                                text = "پیش از ثبت، اطلاعات واردشده را بررسی کنید.",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurfaceVariant
                            )
                        }

                        // جلوگیری از تعامل با فرم در صورت ناقص بودن پروفایل
                        if (!uiState.isProfileComplete) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        colors.background.copy(alpha = 0.82f)
                                    )
                                    .pointerInput(Unit) {
                                        awaitEachGesture {
                                            awaitFirstDown(
                                                requireUnconsumed = false
                                            )

                                            do {
                                                val event = awaitPointerEvent()

                                                event.changes.forEach { change ->
                                                    change.consume()
                                                }
                                            } while (
                                                event.changes.any { it.pressed }
                                            )
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 28.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        text = "پروفایل شما کامل نیست",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        color = colors.onBackground
                                    )

                                    Text(
                                        text = "برای ثبت گزارش، ابتدا نام، نام خانوادگی و واحد سازمانی خود را در پروفایل تکمیل کنید.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Center,
                                        color = colors.onBackground
                                    )

                                    Button(
                                        onClick = onNavigateToProfile,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text(
                                            text = "تکمیل پروفایل",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.5f
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value.ifBlank { "—" },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}