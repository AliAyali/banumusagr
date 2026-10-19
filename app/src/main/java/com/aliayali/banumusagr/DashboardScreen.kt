package com.aliayali.banumusagr

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.aliayali.banumusagr.database.ReportDatabase
import com.aliayali.banumusagr.database.repository.WorkReportRepositoryImpl
import com.aliayali.banumusagr.domain.model.ReportType
import com.aliayali.banumusagr.domain.model.WorkReport

private enum class ReportFilter(val title: String) {
    ALL("همه"),
    DAILY("روزانه"),
    WEEKLY("هفتگی"),
    MONTHLY("ماهانه")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddReportClick: () -> Unit
) {
    val context = LocalContext.current

    val database = remember {
        ReportDatabase.getDatabase(context)
    }

    val reportRepository = remember {
        WorkReportRepositoryImpl(database.workReportDao())
    }

    val reports by reportRepository
        .observeAllReports()
        .collectAsState(initial = emptyList())

    val totalWorkMinutes by reportRepository
        .observeTotalWorkMinutes()
        .collectAsState(initial = 0)

    var selectedFilter by rememberSaveable {
        mutableStateOf(ReportFilter.ALL.name)
    }

    val currentFilter = ReportFilter.valueOf(selectedFilter)

    val filteredReports = remember(reports, currentFilter) {
        val selectedType = when (currentFilter) {
            ReportFilter.ALL -> null
            ReportFilter.DAILY -> ReportType.DAILY
            ReportFilter.WEEKLY -> ReportType.WEEKLY
            ReportFilter.MONTHLY -> ReportType.MONTHLY
        }

        reports
            .filter { selectedType == null || it.reportType == selectedType }
            .sortedByDescending { it.reportDate }
    }

    val groupedReports = remember(filteredReports) {
        filteredReports.groupBy { it.reportDate }
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddReportClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "ثبت گزارش جدید",
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                item {
                    DashboardHeader()
                }

                item {
                    TotalHoursCard(
                        totalMinutes = totalWorkMinutes
                    )
                }

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "تاریخچه گزارش‌ها",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Text(
                                    text = "گزارش‌های ثبت‌شده شما",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = filteredReports.size.toString(),
                                    modifier = Modifier.padding(
                                        horizontal = 13.dp,
                                        vertical = 7.dp
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            ReportFilter.entries.forEach { filter ->
                                FilterChip(
                                    selected = currentFilter == filter,
                                    onClick = {
                                        selectedFilter = filter.name
                                    },
                                    label = {
                                        Text(
                                            text = filter.title,
                                            maxLines = 1,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(13.dp),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = currentFilter == filter,
                                        borderColor = MaterialTheme.colorScheme.outlineVariant,
                                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                                        borderWidth = 1.dp,
                                        selectedBorderWidth = 1.dp
                                    ),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }

                if (filteredReports.isEmpty()) {
                    item {
                        EmptyReportsCard()
                    }
                } else {
                    groupedReports.forEach { (date, dateReports) ->
                        item(key = "date_$date") {
                            DateGroupHeader(date = date)
                        }

                        items(
                            items = dateReports,
                            key = { report -> report.id }
                        ) { report ->
                            ReportHistoryCard(report = report)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = "داشبورد من",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "گزارش‌ها و ساعات کاری خود را مدیریت کنید.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.padding(top = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Surface(
                modifier = Modifier.size(width = 28.dp, height = 4.dp),
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {}

            Surface(
                modifier = Modifier.size(width = 9.dp, height = 4.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                shape = CircleShape
            ) {}
        }
    }
}

@Composable
private fun TotalHoursCard(
    totalMinutes: Int
) {
    val safeMinutes = totalMinutes.coerceAtLeast(0)
    val hours = safeMinutes / 60
    val minutes = safeMinutes % 60

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(17.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.14f)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(26.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "مجموع ساعات کاری",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "کل زمان ثبت‌شده در برنامه",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.78f)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = hours.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Column(
                    modifier = Modifier.padding(bottom = 9.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "ساعت",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "$minutes دقیقه",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.22f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.EventNote,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )

                Text(
                    text = "بر اساس گزارش‌های ذخیره‌شده",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun DateGroupHeader(
    date: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 1.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(17.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Text(
            text = date,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun ReportHistoryCard(
    report: WorkReport
) {
    var expanded by rememberSaveable(report.id) {
        mutableStateOf(false)
    }

    val reportTitle = when (report.reportType) {
        ReportType.DAILY -> "گزارش روزانه"
        ReportType.WEEKLY -> "گزارش هفتگی"
        ReportType.MONTHLY -> "گزارش ماهانه"
    }

    val durationMinutes = report.workDurationMinutes.coerceAtLeast(0)
    val hours = durationMinutes / 60
    val minutes = durationMinutes % 60

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable {
                expanded = !expanded
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (expanded) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(23.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = reportTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = report.reportDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "بستن گزارش" else "مشاهده گزارش",
                    modifier = Modifier
                        .size(25.dp)
                        .rotate(if (expanded) 180f else 0f),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (
                report.reportType == ReportType.DAILY &&
                durationMinutes > 0
            ) {
                Surface(
                    shape = RoundedCornerShape(11.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.07f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 11.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "مدت کار",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = buildString {
                                if (hours > 0) append("$hours ساعت")
                                if (hours > 0 && minutes > 0) append(" و ")
                                if (minutes > 0) append("$minutes دقیقه")
                            },
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (expanded) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                ReportDetailSection(
                    title = "شرح گزارش",
                    content = report.reportText.ifBlank { "بدون توضیحات" }
                )

                if (report.problems.isNotBlank()) {
                    ReportDetailSection(
                        title = "مشکلات و موانع",
                        content = report.problems,
                        highlighted = true
                    )
                }

                if (report.tomorrowPlan.isNotBlank()) {
                    ReportDetailSection(
                        title = "برنامه بعدی",
                        content = report.tomorrowPlan
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportDetailSection(
    title: String,
    content: String,
    highlighted: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (highlighted) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.primary
            }
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = if (highlighted) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ) {
            Text(
                text = content,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun EmptyReportsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.EventNote,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(18.dp)
                        .size(34.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = "هنوز گزارشی ثبت نشده",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = "با ثبت اولین گزارش، تاریخچه و ساعات کاری‌ات اینجا نمایش داده می‌شود.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}