package com.aliayali.home

import ErrorContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliayali.home.components.EmptyReportsCard
import com.aliayali.home.components.HomeHeader
import com.aliayali.home.components.LoadingContent
import com.aliayali.home.components.ReportHistoryCard
import com.aliayali.home.components.ReportsSectionHeader
import com.aliayali.home.components.TotalHoursCard

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    initialHoursSaved: Boolean?,
    sendingReportIds: Set<Long>,
    onRefresh: () -> Unit,
    onAddReportClick: () -> Unit,
    onViewAllReportsClick: () -> Unit,
    onSendReport: (Long) -> Unit,
    onEditReport: (Long) -> Unit,
    onDeleteReport: (Long) -> Unit,
    onProfileClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddReportClick,
                    shape = RoundedCornerShape(18.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "افزودن گزارش",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (uiState) {
                    is HomeUiState.Loading -> {
                        LoadingContent()
                    }

                    is HomeUiState.Error -> {
                        ErrorContent(
                            message = uiState.message,
                            onRetry = onRefresh
                        )
                    }

                    is HomeUiState.Success -> {
                        val homeData = uiState.homeUiData

                        val latestReports = remember(homeData.reports) {
                            homeData.reports.take(5)
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 18.dp,
                                end = 18.dp,
                                top = 16.dp,
                                bottom = 108.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            if (initialHoursSaved == false) {
                                item(key = "initial_hours_warning") {
                                    InitialHoursWarningCard(
                                        onProfileClick = onProfileClick
                                    )
                                }
                            }

                            item(key = "header") {
                                HomeHeader(
                                    onProfileClick = onProfileClick
                                )
                            }

                            item(key = "statistics") {
                                TotalHoursCard(
                                    dailyMinutes = homeData.dailyWorkMinutes,
                                    weeklyMinutes = homeData.weeklyWorkMinutes,
                                    monthlyMinutes = homeData.monthlyWorkMinutes,
                                    totalMinutes = homeData.totalWorkMinutes
                                )
                            }

                            item(key = "reports_header") {
                                ReportsSectionHeader(
                                    onViewAllReportsClick =
                                        onViewAllReportsClick
                                )
                            }

                            if (latestReports.isEmpty()) {
                                item(key = "empty_reports") {
                                    EmptyReportsCard()
                                }
                            } else {
                                items(
                                    items = latestReports,
                                    key = { report -> report.id }
                                ) { report ->
                                    ReportHistoryCard(
                                        report = report,
                                        isLatestOfType = report.isLatestOfType,
                                        snackbarHostState = snackbarHostState,
                                        isSending = report.id in sendingReportIds,
                                        onSend = onSendReport,
                                        onEdit = onEditReport,
                                        onDelete = onDeleteReport
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

@Composable
private fun InitialHoursWarningCard(
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )

                Text(
                    text = "ساعات اولیه ثبت نشده است",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "برای محاسبه صحیح سابقه کاری، ساعت و دقیقه کل و ماه جاری را در صفحه پروفایل وارد کنید.",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 23.sp
            )

            Button(
                onClick = onProfileClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(
                    text = "ثبت ساعات اولیه",
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(18.dp)
                )
            }
        }
    }
}