package com.aliayali.report.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PersianMonths = listOf(
    "فروردین", "اردیبهشت", "خرداد", "تیر",
    "مرداد", "شهریور", "مهر", "آبان",
    "آذر", "دی", "بهمن", "اسفند"
)

@Composable
fun ReportHeader() {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(colors.primaryContainer)
            .padding(22.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(15.dp),
                color = colors.primary,
                contentColor = colors.onPrimary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "✦",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "گزارش کار",
                    color = colors.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "گزارش فعالیت‌های سازمانی",
                    color = colors.onPrimaryContainer.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ReportCard(
    title: String,
    subtitle: String,
    icon: String,
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(colors.surface)
            .border(1.dp, colors.outlineVariant, RoundedCornerShape(22.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = colors.secondaryContainer,
                contentColor = colors.onSecondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(icon, style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    color = colors.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    subtitle,
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider(color = colors.outlineVariant)
        content()
    }
}

@Composable
fun ReportFieldLabel(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun ReportInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(14.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        ReportFieldLabel(label)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(colors.surface)
                .border(
                    1.dp,
                    if (error != null) colors.error else colors.outline,
                    shape
                )
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = colors.onSurface
                ),
                cursorBrush = SolidColor(colors.primary),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                minLines = minLines,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        if (error != null) {
            Text(
                error,
                color = colors.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun ReportTypeSelector(
    selected: String,
    enabled: Boolean = true,
    onSelected: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val types = listOf("روزانه", "هفتگی", "ماهانه")

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            types.forEach { type ->
                val selectedNow = selected == type

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            enabled = enabled
                        ) {
                            onSelected(type)
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = when {
                        !enabled && selectedNow ->
                            colors.primary.copy(alpha = 0.55f)

                        !enabled ->
                            colors.surfaceVariant.copy(alpha = 0.5f)

                        selectedNow ->
                            colors.primary

                        else ->
                            colors.surface
                    },
                    contentColor = when {
                        !enabled && selectedNow ->
                            colors.onPrimary.copy(alpha = 0.8f)

                        !enabled ->
                            colors.onSurfaceVariant.copy(alpha = 0.65f)

                        selectedNow ->
                            colors.onPrimary

                        else ->
                            colors.onSurface
                    },
                    border = BorderStroke(
                        1.dp,
                        when {
                            !enabled ->
                                colors.outlineVariant.copy(alpha = 0.5f)

                            selectedNow ->
                                colors.primary

                            else ->
                                colors.outlineVariant
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(
                            horizontal = 4.dp,
                            vertical = 13.dp
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = type,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        if (!enabled) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        colors.surfaceVariant.copy(alpha = 0.45f)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 8.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔒",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "نوع گزارش هنگام ویرایش قابل تغییر نیست.",
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


/**
 * انتخاب ساعت به‌صورت کشویی درون صفحه؛ بدون Dialog و Popup.
 * مقدار خروجی با فرمت HH:mm است.
 */
@Composable
fun ReportTimeField(
    label: String,
    value: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    val colors = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    val parts = value.split(":")
    val currentHour = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 8
    val currentMinute = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0

    var selectedHour by remember(value) { mutableIntStateOf(currentHour) }
    var selectedMinute by remember(value) { mutableIntStateOf(currentMinute) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        ReportFieldLabel(label)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(14.dp),
            color = colors.surface,
            contentColor = colors.onSurface,
            border = BorderStroke(
                1.dp,
                if (error != null) colors.error else colors.outlineVariant
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("◷", color = colors.primary, fontSize = 23.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    value.ifBlank { "--:--" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Text(if (expanded) "⌃" else "⌄", color = colors.onSurfaceVariant)
            }
        }

        if (expanded) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = colors.surface,
                border = BorderStroke(1.dp, colors.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "انتخاب ساعت و دقیقه",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("ساعت", style = MaterialTheme.typography.labelMedium)

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(144.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                (0..23).forEach { hour ->
                                    TimeOption(
                                        text = "%02d".format(hour),
                                        selected = selectedHour == hour,
                                        onClick = { selectedHour = hour }
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("دقیقه", style = MaterialTheme.typography.labelMedium)

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(144.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                (0..59 step 5).forEach { minute ->
                                    TimeOption(
                                        text = "%02d".format(minute),
                                        selected = selectedMinute == minute,
                                        onClick = { selectedMinute = minute }
                                    )
                                }
                            }
                        }
                    }



                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp),
                            contentPadding = PaddingValues(8.dp),
                            onClick = {
                                expanded = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "بستن",
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp),
                            contentPadding = PaddingValues(8.dp),
                            onClick = {
                                onSelected(
                                    "%02d:%02d".format(selectedHour, selectedMinute)
                                )
                                expanded = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "ثبت ساعت",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                }
            }
        }

        if (error != null) {
            Text(error, color = colors.error, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun TimeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (selected) colors.primaryContainer else colors.surface
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = if (selected) colors.onPrimaryContainer else colors.onSurface,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

/**
 * انتخاب تاریخ شمسی با سه فهرست داخل صفحه.
 * قالب ورودی و خروجی: yyyy/MM/dd با ارقام انگلیسی، مانند 1405/07/29.
 *
 * این کامپوننت تقویم را به‌صورت محلی انتخاب می‌کند؛ تبدیل تاریخ میلادی
 * به شمسی یا اعتبارسنجی تعداد روزهای هر ماه را انجام نمی‌دهد.
 */
@Composable
fun ReportDateField(
    label: String,
    value: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    val colors = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    val parts = value.replace('-', '/').split("/")
    val initialYear = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(1300, 1500) ?: 1405
    val initialMonth = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(1, 12) ?: 1
    val initialDay = parts.getOrNull(2)?.toIntOrNull()?.coerceIn(1, 31) ?: 1

    var year by remember(value) { mutableIntStateOf(initialYear) }
    var month by remember(value) { mutableIntStateOf(initialMonth) }
    var day by remember(value) { mutableIntStateOf(initialDay) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        ReportFieldLabel(label)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(14.dp),
            color = colors.surface,
            border = BorderStroke(
                1.dp,
                if (error != null) colors.error else colors.outlineVariant
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("▦", color = colors.primary, fontSize = 20.sp)
                Spacer(Modifier.width(10.dp))
                Text(
                    value.ifBlank { "انتخاب تاریخ" },
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (value.isBlank()) colors.onSurfaceVariant else colors.onSurface
                )
                Text(if (expanded) "⌃" else "⌄", color = colors.onSurfaceVariant)
            }
        }

        if (expanded) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = colors.surface,
                border = BorderStroke(1.dp, colors.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "انتخاب تاریخ شمسی",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DateNumberColumn(
                            title = "روز",
                            values = (1..31).toList(),
                            selected = day,
                            modifier = Modifier.weight(1f),
                            format = { it.toString().padStart(2, '0') },
                            onSelect = { day = it }
                        )

                        DateNumberColumn(
                            title = "ماه",
                            values = (1..12).toList(),
                            selected = month,
                            modifier = Modifier.weight(1f),
                            format = { PersianMonths[it - 1] },
                            onSelect = { month = it }
                        )

                        DateNumberColumn(
                            title = "سال",
                            values = (1405..1410).toList(),
                            selected = year,
                            modifier = Modifier.weight(1f),
                            format = { it.toString() },
                            onSelect = { year = it }
                        )
                    }

                    Text(
                        "%04d/%02d/%02d".format(year, month, day),
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { expanded = false }
                        ) {
                            Text("بستن")
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onSelected("%04d/%02d/%02d".format(year, month, day))
                                expanded = false
                            }
                        ) {
                            Text("ثبت تاریخ")
                        }
                    }
                }
            }
        }

        if (error != null) {
            Text(error, color = colors.error, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DateNumberColumn(
    title: String,
    values: List<Int>,
    selected: Int,
    modifier: Modifier = Modifier,
    format: (Int) -> String,
    onSelect: (Int) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            values.forEach { item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSelect(item) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (selected == item) {
                        colors.primaryContainer
                    } else {
                        colors.surface
                    }
                ) {
                    Text(
                        text = format(item),
                        modifier = Modifier.padding(vertical = 9.dp, horizontal = 3.dp),
                        color = if (selected == item) {
                            colors.onPrimaryContainer
                        } else {
                            colors.onSurface
                        },
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected == item) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ReportMessage(message: String, isError: Boolean) {
    val colors = MaterialTheme.colorScheme
    val messageColor = if (isError) colors.error else colors.primary
    val messageBackground =
        if (isError) colors.errorContainer else colors.secondaryContainer
    val messageContentColor =
        if (isError) colors.onErrorContainer else colors.onSecondaryContainer

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = messageBackground,
        contentColor = messageContentColor,
        border = BorderStroke(1.dp, messageColor.copy(alpha = 0.25f))
    ) {
        Text(
            message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}