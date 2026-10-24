package com.aliayali.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.aliayali.profile.components.ProfileField
import com.aliayali.profile.components.ThemeSelector
import com.aliayali.profile.model.ProfileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onBackClick: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onActiveUnitChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onThemeSelected: (ProfileTheme) -> Unit,
    onInitialTotalHoursChange: (String) -> Unit,
    onInitialTotalMinutesChange: (String) -> Unit,
    onInitialMonthlyHoursChange: (String) -> Unit,
    onInitialMonthlyMinutesChange: (String) -> Unit,
    onSaveInitialHours: () -> Unit
) {
    val fieldsEnabled = !uiState.profileSaved

    var showInitialHoursWarning by remember {
        mutableStateOf(false)
    }

    val totalHours = uiState.initialTotalHours.toIntOrNull()
    val totalMinutes = uiState.initialTotalMinutes.toIntOrNull()
    val monthlyHours = uiState.initialMonthlyHours.toIntOrNull()
    val monthlyMinutes = uiState.initialMonthlyMinutes.toIntOrNull()

    val inputsFilled =
        totalHours != null &&
                totalMinutes != null &&
                monthlyHours != null &&
                monthlyMinutes != null

    val minutesValid =
        totalMinutes != null && totalMinutes in 0..59 &&
                monthlyMinutes != null && monthlyMinutes in 0..59

    val hoursValid =
        totalHours != null && totalHours >= 0 &&
                monthlyHours != null && monthlyHours >= 0

    val monthlyNotGreaterThanTotal =
        if (inputsFilled && minutesValid && hoursValid) {
            monthlyHours * 60 + monthlyMinutes <=
                    totalHours * 60 + totalMinutes
        } else {
            false
        }

    val initialHoursValid =
        inputsFilled &&
                minutesValid &&
                hoursValid &&
                monthlyNotGreaterThanTotal

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "پروفایل من",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "بازگشت"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme
                                    .onPrimaryContainer.copy(alpha = 0.10f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "پروفایل",
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .size(32.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Spacer(Modifier.size(16.dp))

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = listOf(
                                        uiState.firstName,
                                        uiState.lastName
                                    ).filter { it.isNotBlank() }
                                        .joinToString(" ")
                                        .ifBlank { "پروفایل کاربری" },
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                Text(
                                    text = uiState.activeUnit.ifBlank {
                                        "واحد فعالیت ثبت نشده"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "اطلاعات فردی",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = if (fieldsEnabled) {
                                    "مشخصات خود را وارد کنید."
                                } else {
                                    "اطلاعات ذخیره شده و امکان ویرایش غیرفعال است."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            ProfileField(
                                label = "نام",
                                value = uiState.firstName,
                                onValueChange = onFirstNameChange,
                                placeholder = "نام خود را وارد کنید",
                                enabled = fieldsEnabled && !uiState.isSaving
                            )

                            ProfileField(
                                label = "نام خانوادگی",
                                value = uiState.lastName,
                                onValueChange = onLastNameChange,
                                placeholder = "نام خانوادگی خود را وارد کنید",
                                enabled = fieldsEnabled && !uiState.isSaving
                            )

                            ProfileField(
                                label = "واحد فعالیت فعلی",
                                value = uiState.activeUnit,
                                onValueChange = onActiveUnitChange,
                                placeholder = "واحد فعالیت را وارد کنید",
                                enabled = fieldsEnabled && !uiState.isSaving
                            )

                            Button(
                                onClick = onSaveClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                enabled = !uiState.isSaving && fieldsEnabled
                            ) {
                                if (uiState.isSaving) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(21.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text(
                                        text = if (uiState.saveSuccess) {
                                            "ذخیره شد"
                                        } else {
                                            "ذخیره تغییرات"
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            if (uiState.saveSuccess) {
                                SuccessMessage(
                                    text = "اطلاعات با موفقیت ذخیره شد."
                                )
                            }

                            uiState.errorMessage?.let { message ->
                                Text(
                                    text = message,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "ثبت ساعات کاری اولیه",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            if (!uiState.initialHoursSaved) {
                                Text(
                                    text = "اگر قبل از استفاده از برنامه سابقه کاری دارید، آن را در این بخش ثبت کنید.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Text(
                                    text = "⚠️ این اطلاعات فقط یک بار قابل ثبت هستند و پس از ثبت نهایی قابل ویرایش نیستند.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "مجموع کل سابقه کاری",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "تمام ساعت‌های کاری از ابتدای سابقه تا امروز، شامل ماه جاری.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                ProfileField(
                                    label = "کل ساعت‌های کاری",
                                    value = uiState.initialTotalHours,
                                    onValueChange = onInitialTotalHoursChange,
                                    placeholder = "مثلاً ۱۲۵",
                                    enabled = !uiState.isSavingInitialHours
                                )

                                ProfileField(
                                    label = "دقیقه‌های سابقه کل",
                                    value = uiState.initialTotalMinutes,
                                    onValueChange = onInitialTotalMinutesChange,
                                    placeholder = "۰ تا ۵۹",
                                    enabled = !uiState.isSavingInitialHours
                                )

                                Text(
                                    text = "سابقه کاری ماه جاری",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "فقط سابقه کاری از ابتدای ماه جاری تا زمان ثبت اولیه؛ این مقدار بخشی از مجموع کل است.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                ProfileField(
                                    label = "ساعت‌های کاری ماه جاری",
                                    value = uiState.initialMonthlyHours,
                                    onValueChange = onInitialMonthlyHoursChange,
                                    placeholder = "مثلاً ۱۸",
                                    enabled = !uiState.isSavingInitialHours
                                )

                                ProfileField(
                                    label = "دقیقه‌های ماه جاری",
                                    value = uiState.initialMonthlyMinutes,
                                    onValueChange = onInitialMonthlyMinutesChange,
                                    placeholder = "۰ تا ۵۹",
                                    enabled = !uiState.isSavingInitialHours
                                )

                                Text(
                                    text = "دقیقه می‌تواند از ۰ تا ۵۹ باشد. برای هر فیلد مقدار موردنظر را وارد کنید.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (inputsFilled && !minutesValid) {
                                    Text(
                                        text = "مقدار دقیقه در هر دو بخش باید بین ۰ تا ۵۹ باشد.",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                if (
                                    inputsFilled &&
                                    minutesValid &&
                                    hoursValid &&
                                    !monthlyNotGreaterThanTotal
                                ) {
                                    Text(
                                        text = "سابقه ماه جاری نمی‌تواند از مجموع سابقه کل بیشتر باشد.",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                if (inputsFilled && !hoursValid) {
                                    Text(
                                        text = "ساعت‌ها نمی‌توانند منفی باشند.",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                Button(
                                    onClick = {
                                        showInitialHoursWarning = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    enabled = !uiState.isSavingInitialHours &&
                                            initialHoursValid
                                ) {
                                    Text(
                                        text = if (uiState.isSavingInitialHours) {
                                            "در حال ذخیره..."
                                        } else {
                                            "بررسی و ثبت نهایی"
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                uiState.errorMessage?.let { message ->
                                    Text(
                                        text = message,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            } else {
                                SuccessMessage(
                                    text = "ساعات کاری اولیه ثبت شده‌اند."
                                )

                                Text(
                                    text = "ثبت مجدد یا تغییر ساعات اولیه از این بخش امکان‌پذیر نیست.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )

                                Text(
                                    text = "مجموع کل سابقه کاری: ${uiState.initialTotalHours} ساعت و ${uiState.initialTotalMinutes} دقیقه",
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    text = "سابقه ماه جاری هنگام ثبت: ${uiState.initialMonthlyHours} ساعت و ${uiState.initialMonthlyMinutes} دقیقه",
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                if (uiState.initialHoursMonth.isNotBlank()) {
                                    Text(
                                        text = "ماه ثبت اولیه: ${uiState.initialHoursMonth}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        ThemeSelector(
                            selectedTheme = uiState.selectedTheme,
                            onThemeSelected = onThemeSelected,
                            modifier = Modifier.padding(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (showInitialHoursWarning && !uiState.initialHoursSaved) {
        CompositionLocalProvider(
            LocalLayoutDirection provides LayoutDirection.Rtl
        ) {
            AlertDialog(
                onDismissRequest = {
                    showInitialHoursWarning = false
                },
                title = {
                    Text(
                        text = "⚠️ هشدار ثبت یک‌باره",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "آیا از مقادیر واردشده مطمئن هستید؟\n\n" +
                                "با انتخاب «بله، ثبت نهایی»، این مقادیر ذخیره می‌شوند " +
                                "و امکان ویرایش یا ثبت مجدد وجود نخواهد داشت.\n\n" +
                                "لطفاً ساعت و دقیقه هر دو بخش را دوباره بررسی کنید.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                },
                confirmButton = {
                    TextButton(
                        enabled = !uiState.isSavingInitialHours &&
                                initialHoursValid,
                        onClick = {
                            onSaveInitialHours()
                            showInitialHoursWarning = false
                        }
                    ) {
                        Text(
                            text = "بله، ثبت نهایی",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showInitialHoursWarning = false
                        }
                    ) {
                        Text("بررسی مجدد")
                    }
                }
            )
        }
    }
}

@Composable
private fun SuccessMessage(
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}