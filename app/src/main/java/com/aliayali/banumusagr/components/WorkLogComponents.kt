package com.aliayali.banumusagr.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliayali.banumusagr.R
import com.aliayali.banumusagr.utils.getPersianToday


@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.worklog_description),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Right
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Surface(
            modifier = Modifier.size(52.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun ReportTypeSelector(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    val daily = stringResource(R.string.report_daily)
    val weekly = stringResource(R.string.report_weekly)
    val monthly = stringResource(R.string.report_monthly)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = stringResource(R.string.report_type),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReportTypeButton(
                    text = monthly,
                    selected = selectedType == monthly,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTypeSelected(monthly)
                    }
                )
                ReportTypeButton(
                    text = weekly,
                    selected = selectedType == weekly,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTypeSelected(weekly)
                    }
                )
                ReportTypeButton(
                    text = daily,
                    selected = selectedType == daily,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTypeSelected(daily)
                    }
                )
            }
        }
    }
}


@Composable
private fun ReportTypeButton(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                },
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmployeeInformation(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    department: String,
    onDepartmentChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {

            Text(
                text = stringResource(R.string.employee_information),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )
            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.full_name),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.full_name_hint),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = department,
                onValueChange = onDepartmentChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.department),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.department_hint),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                )
            )
        }
    }
}

@Composable
fun DateSection() {

    val today = remember {
        getPersianToday()
    }

    val parts = today.split(" ")

    val day = parts.getOrNull(0) ?: ""
    val month = parts.getOrNull(1) ?: ""
    val year = parts.getOrNull(2) ?: ""

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = stringResource(R.string.report_date),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Right
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = year,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = month,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = day,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
fun TotalWorkHoursCard(
    totalHours: String,
    onTotalHoursChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {

            Text(
                text = stringResource(R.string.total_work_hours),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = totalHours,
                onValueChange = onTotalHoursChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.total_work_hours_hint),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                )
            )
        }
    }
}

@Composable
fun ReportCard(
    reportText: String,
    onReportTextChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            SectionTitle(
                icon = Icons.Default.Description,
                title = stringResource(R.string.activity_report)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = reportText,
                onValueChange = onReportTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.activity_report_hint),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                shape = RoundedCornerShape(14.dp),
                maxLines = 7,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                )
            )
        }
    }
}

@Composable
fun ProblemsCard(
    problems: String,
    onProblemsChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = stringResource(R.string.problems_and_obstacles),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = problems,
                onValueChange = onProblemsChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.problems_hint),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                shape = RoundedCornerShape(14.dp),
                maxLines = 5,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                )
            )
        }
    }
}

@Composable
fun TomorrowPlanCard(
    text: String,
    onTextChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = stringResource(R.string.next_work_plan),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.next_work_plan_hint),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                },
                shape = RoundedCornerShape(14.dp),
                maxLines = 5,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                )
            )
        }
    }
}

@Composable
private fun SectionTitle(
    icon: ImageVector,
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Right
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun SentMessage() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.report_sent_successfully),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}