package com.aliayali.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TotalHoursCard(
    dailyMinutes: Int,
    weeklyMinutes: Int,
    monthlyMinutes: Int,
    totalMinutes: Int,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val onSurface = MaterialTheme.colorScheme.onSurface
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val safeTotal = totalMinutes.coerceAtLeast(0)

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // عنوان بخش
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "آمار کارکرد",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = onSurface
                    )

                    Text(
                        text = "خلاصه زمان کاری ثبت‌شده",
                        style = MaterialTheme.typography.bodySmall,
                        color = muted
                    )
                }
            }

            // مجموع کل کارکرد
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = primary
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.onPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = primary,
                            modifier = Modifier.size(27.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "مجموع کل کارکرد",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${safeTotal / 60}",
                                fontSize = 34.sp,
                                lineHeight = 38.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                text = "ساعت",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(bottom = 5.dp)
                            )

                            Text(
                                text = "${safeTotal % 60} دقیقه",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }
                }
            }

            // سه آمار در یک ردیف
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WorkStatItem(
                    title = "روزانه",
                    minutes = dailyMinutes,
                    icon = Icons.Default.Today,
                    primary = primary,
                    muted = muted,
                    onSurface = onSurface,
                    modifier = Modifier.weight(1f)
                )

                WorkStatItem(
                    title = "هفتگی",
                    minutes = weeklyMinutes,
                    icon = Icons.Default.DateRange,
                    primary = primary,
                    muted = muted,
                    onSurface = onSurface,
                    modifier = Modifier.weight(1f)
                )

                WorkStatItem(
                    title = "ماهانه",
                    minutes = monthlyMinutes,
                    icon = Icons.Default.CalendarMonth,
                    primary = primary,
                    muted = muted,
                    onSurface = onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
private fun WorkStatItem(
    title: String,
    minutes: Int,
    icon: ImageVector,
    primary: Color,
    muted: Color,
    onSurface: Color,
    modifier: Modifier = Modifier
) {
    val safeMinutes = minutes.coerceAtLeast(0)
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // آیکون سمت راست
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = primary,
                    modifier = Modifier.size(19.dp)
                )
            }

            // عنوان و مدت کارکرد کنار آیکون
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = onPrimary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "${safeMinutes / 60}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = onPrimary,
                        maxLines = 1
                    )

                    Text(
                        text = "ساعت",
                        style = MaterialTheme.typography.labelSmall,
                        color = onPrimary
                    )
                }

                Text(
                    text = "${safeMinutes % 60} دقیقه",
                    style = MaterialTheme.typography.labelSmall,
                    color = onPrimary,
                    maxLines = 1
                )
            }
        }
    }
}