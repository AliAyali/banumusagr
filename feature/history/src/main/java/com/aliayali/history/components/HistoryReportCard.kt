package com.aliayali.history.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aliayali.history.model.HistoryUiModel

@Composable
fun HistoryReportCard(
    report: HistoryUiModel,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable(report.id) {
        mutableStateOf(false)
    }

    val colors = MaterialTheme.colorScheme
    val duration = report.workDurationMinutes.coerceAtLeast(0)
    val hours = duration / 60
    val minutes = duration % 60

    val reportIcon = when (report.reportType) {
        "روزانه" -> Icons.Default.Today
        "هفتگی" -> Icons.Default.DateRange
        "ماهانه" -> Icons.Default.CalendarMonth
        else -> Icons.Default.Description
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (expanded) {
                colors.primary.copy(alpha = 0.45f)
            } else {
                colors.outlineVariant.copy(alpha = 0.65f)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colors.primaryContainer
                ) {
                    Icon(
                        imageVector = reportIcon,
                        contentDescription = report.reportType,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(23.dp),
                        tint = colors.onPrimaryContainer
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "گزارش ${report.reportType}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )

                    Text(
                        text = "تاریخ: ${report.reportDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )

                    Text(
                        text = report.reportText
                            .replace("\n", " ")
                            .trim()
                            .ifBlank { "بدون توضیحات" },
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                        maxLines = if (expanded) 4 else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = if (expanded) {
                        "بستن جزئیات"
                    } else {
                        "نمایش جزئیات"
                    },
                    modifier = Modifier
                        .size(26.dp)
                        .rotate(if (expanded) 180f else 0f),
                    tint = colors.onSurfaceVariant
                )
            }

            // مدت کار و وضعیت ارسال
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colors.surfaceVariant.copy(alpha = 0.55f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 14.dp,
                            vertical = 13.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = colors.primaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(18.dp),
                            tint = colors.onPrimaryContainer
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "مدت کار",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onSurfaceVariant
                        )

                        Text(
                            text = "$hours ساعت و $minutes دقیقه",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onSurface
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (report.isSent) {
                            colors.primaryContainer
                        } else {
                            colors.errorContainer
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 7.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if (report.isSent) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = colors.onPrimaryContainer
                                )
                            }

                            Text(
                                text = if (report.isSent) {
                                    "ارسال شده"
                                } else {
                                    "ارسال نشده"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = if (report.isSent) {
                                    colors.onPrimaryContainer
                                } else {
                                    colors.onErrorContainer
                                }
                            )
                        }
                    }
                }
            }

            // جزئیات گزارش
            if (expanded) {
                HorizontalDivider(
                    color = colors.outlineVariant
                )

                ReportDetail(
                    title = "گزارش فعالیت",
                    value = report.reportText
                )

                ReportDetail(
                    title = "مشکلات و موانع",
                    value = report.problems
                )

                ReportDetail(
                    title = "برنامه کاری بعدی",
                    value = report.tomorrowPlan
                )
            }
        }
    }
}

@Composable
private fun ReportDetail(
    title: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = value.ifBlank {
                "موردی ثبت نشده است"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}