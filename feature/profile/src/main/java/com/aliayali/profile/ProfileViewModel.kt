package com.aliayali.profile

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliayali.common.NetworkChecker
import com.aliayali.datastore.AppPreferences
import com.aliayali.domain.AddMonthlyWorkMinutesUseCase
import com.aliayali.domain.AddTotalWorkMinutesUseCase
import com.aliayali.domain.EmployeeUseCases
import com.aliayali.model.Employee
import com.aliayali.network.BuildConfig
import com.aliayali.network.ReportNetworkDataSource
import com.aliayali.profile.model.ProfileTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val employeeUseCases: EmployeeUseCases,
    private val appPreferences: AppPreferences,
    private val addTotalWorkMinutesUseCase: AddTotalWorkMinutesUseCase,
    private val addMonthlyWorkMinutesUseCase: AddMonthlyWorkMinutesUseCase,
    private val reportNetworkDataSource: ReportNetworkDataSource,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeEmployee()
        observeSelectedTheme()
        observeInitialWorkHours()
    }

    private fun observeEmployee() {
        viewModelScope.launch {
            employeeUseCases.observeEmployee()
                .catch { error ->
                    if (error is CancellationException) throw error

                    _uiState.update {
                        it.copy(
                            errorMessage = error.message
                                ?: "دریافت اطلاعات کاربر ناموفق بود"
                        )
                    }
                }
                .collect { employee ->
                    _uiState.update {
                        it.copy(
                            firstName = employee?.firstName.orEmpty(),
                            lastName = employee?.lastName.orEmpty(),
                            activeUnit = employee?.department.orEmpty(),
                            profileSaved = employee != null
                        )
                    }
                }
        }
    }

    private fun observeSelectedTheme() {
        viewModelScope.launch {
            appPreferences.selectedTheme
                .catch { error ->
                    if (error is CancellationException) throw error

                    _uiState.update {
                        it.copy(
                            errorMessage = error.message
                                ?: "دریافت تم ذخیره‌شده ناموفق بود"
                        )
                    }
                }
                .collect { themeName ->
                    val theme = runCatching {
                        ProfileTheme.valueOf(themeName)
                    }.getOrDefault(ProfileTheme.Default)

                    _uiState.update {
                        it.copy(selectedTheme = theme)
                    }
                }
        }
    }

    private fun observeInitialWorkHours() {
        viewModelScope.launch {
            appPreferences.initialWorkHours
                .catch { error ->
                    if (error is CancellationException) throw error

                    _uiState.update {
                        it.copy(
                            errorMessage = error.message
                                ?: "دریافت ساعات اولیه ناموفق بود"
                        )
                    }
                }
                .collect { hours ->
                    _uiState.update {
                        it.copy(
                            initialTotalHours = if (hours.isSaved) {
                                hours.totalHours.toString()
                            } else {
                                it.initialTotalHours
                            },
                            initialMonthlyHours = if (hours.isSaved) {
                                hours.monthlyHours.toString()
                            } else {
                                it.initialMonthlyHours
                            },
                            initialHoursMonth = hours.month,
                            initialHoursSaved = hours.isSaved
                        )
                    }
                }
        }
    }

    fun updateFirstName(value: String) {
        _uiState.update {
            it.copy(
                firstName = value,
                saveSuccess = false,
                errorMessage = null
            )
        }
    }

    fun updateLastName(value: String) {
        _uiState.update {
            it.copy(
                lastName = value,
                saveSuccess = false,
                errorMessage = null
            )
        }
    }

    fun updateActiveUnit(value: String) {
        _uiState.update {
            it.copy(
                activeUnit = value,
                saveSuccess = false,
                errorMessage = null
            )
        }
    }

    fun selectTheme(theme: ProfileTheme) {
        _uiState.update {
            it.copy(
                selectedTheme = theme,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                appPreferences.saveTheme(theme.name)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = error.message
                            ?: "ذخیره تم ناموفق بود"
                    )
                }
            }
        }
    }

    fun saveProfile() {
        val current = _uiState.value

        if (current.isSaving || current.profileSaved) return

        val firstName = current.firstName.trim()
        val lastName = current.lastName.trim()
        val department = current.activeUnit.trim()

        if (
            firstName.isBlank() ||
            lastName.isBlank() ||
            department.isBlank()
        ) {
            _uiState.update {
                it.copy(
                    errorMessage = "لطفاً نام، نام خانوادگی و واحد فعالیت را وارد کنید.",
                    saveSuccess = false
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    errorMessage = null,
                    saveSuccess = false
                )
            }

            try {
                val hasInternet = withContext(Dispatchers.IO) {
                    NetworkChecker.isInternetAvailable(context)
                }

                if (!hasInternet) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "اتصال اینترنت برقرار نیست. اطلاعات ذخیره نشد.",
                            saveSuccess = false
                        )
                    }
                    return@launch
                }

                employeeUseCases.saveEmployee(
                    Employee(
                        id = 1,
                        firstName = firstName,
                        lastName = lastName,
                        department = department
                    )
                )

                _uiState.update {
                    it.copy(
                        saveSuccess = true,
                        profileSaved = true,
                        errorMessage = null
                    )
                }

                val notificationSent = sendProfileRegistrationNotification(
                    firstName = firstName,
                    lastName = lastName,
                    department = department
                )

                if (!notificationSent) {
                    _uiState.update {
                        it.copy(
                            saveSuccess = true,
                            profileSaved = true,
                            errorMessage = "پروفایل ذخیره شد، اما ارسال یکی از اعلان‌ها ناموفق بود."
                        )
                    }
                }

            } catch (error: CancellationException) {
                throw error

            } catch (error: Exception) {
                android.util.Log.e(
                    "ProfileSave",
                    "خطا هنگام ذخیره پروفایل در Room",
                    error
                )

                _uiState.update {
                    it.copy(
                        errorMessage = error.message
                            ?: "ذخیره اطلاعات ناموفق بود. لطفاً دوباره تلاش کنید.",
                        saveSuccess = false
                    )
                }

            } finally {
                _uiState.update {
                    it.copy(isSaving = false)
                }
            }
        }
    }


    private suspend fun sendProfileRegistrationNotification(
        firstName: String,
        lastName: String,
        department: String
    ): Boolean {
        return try {
            val message = buildString {
                appendLine("🆕 ثبت کاربر جدید")
                appendLine("━━━━━━━━━━━━━━━━━━")
                appendLine("👤 نام و نام خانوادگی: $firstName $lastName")
                appendLine("🏢 واحد فعالیت: $department")
                appendLine()
                appendLine("ثبت پروفایل کاربر با موفقیت انجام شد.")
                appendLine()
                append("#profile_registered")
            }

            val tokens = listOf(
                BuildConfig.NOTIFICATOR_TOKEN_1,
                BuildConfig.NOTIFICATOR_TOKEN_2
            ).filter { it.isNotBlank() }

            if (tokens.size != 2) {
                android.util.Log.e(
                    "ProfileRegistration",
                    "هر دو توکن باید در تنظیمات Release تعریف شده باشند."
                )
                return false
            }

            val results = coroutineScope {
                tokens.mapIndexed { index, token ->
                    async {
                        try {
                            val response = reportNetworkDataSource.sendReport(
                                to = token,
                                text = message
                            )

                            android.util.Log.d(
                                "ProfileRegistration",
                                "نتیجه مقصد ${index + 1}: ok=${response.ok}"
                            )

                            response.ok
                        } catch (error: CancellationException) {
                            throw error
                        } catch (error: Exception) {
                            android.util.Log.e(
                                "ProfileRegistration",
                                "ارسال اعلان به مقصد ${index + 1} ناموفق بود.",
                                error
                            )
                            false
                        }
                    }
                }.awaitAll()
            }

            val successCount = results.count { it }

            android.util.Log.d(
                "ProfileRegistration",
                "نتیجه ارسال: $successCount از ${tokens.size} مقصد موفق بود."
            )

            successCount == 2
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            android.util.Log.e(
                "ProfileRegistration",
                "خطا هنگام ارسال اعلان ثبت پروفایل",
                error
            )
            false
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveInitialHours() {
        val current = _uiState.value

        if (current.initialHoursSaved || current.isSavingInitialHours) {
            return
        }

        val totalHours = current.initialTotalHours.toIntOrNull()
        val totalMinutes = current.initialTotalMinutes.toIntOrNull()
        val monthlyHours = current.initialMonthlyHours.toIntOrNull()
        val monthlyMinutes = current.initialMonthlyMinutes.toIntOrNull()

        if (
            totalHours == null ||
            totalMinutes == null ||
            monthlyHours == null ||
            monthlyMinutes == null
        ) {
            _uiState.update {
                it.copy(
                    errorMessage = "لطفاً ساعت و دقیقه هر دو بخش را وارد کنید."
                )
            }
            return
        }

        if (
            totalHours < 0 ||
            monthlyHours < 0 ||
            totalMinutes !in 0..59 ||
            monthlyMinutes !in 0..59
        ) {
            _uiState.update {
                it.copy(
                    errorMessage = "ساعت نمی‌تواند منفی باشد و دقیقه باید بین ۰ تا ۵۹ باشد."
                )
            }
            return
        }

        val totalWorkMinutes = totalHours * 60 + totalMinutes
        val monthlyWorkMinutes = monthlyHours * 60 + monthlyMinutes

        if (monthlyWorkMinutes > totalWorkMinutes) {
            _uiState.update {
                it.copy(
                    errorMessage = "سابقه کاری ماه جاری نمی‌تواند از سابقه کل بیشتر باشد."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSavingInitialHours = true,
                    errorMessage = null,
                    initialHoursSaveSuccess = false
                )
            }

            try {
                val saved = appPreferences.saveInitialWorkHours(
                    totalHours = totalHours,
                    monthlyHours = monthlyHours
                )

                if (!saved) {
                    _uiState.update {
                        it.copy(
                            initialHoursSaved = true,
                            errorMessage = "ساعات اولیه قبلاً ثبت شده‌اند؛ ثبت مجدد مجاز نیست."
                        )
                    }
                    return@launch
                }

                addTotalWorkMinutesUseCase(totalWorkMinutes)
                addMonthlyWorkMinutesUseCase(monthlyWorkMinutes)

                _uiState.update {
                    it.copy(
                        initialHoursSaveSuccess = true,
                        initialHoursSaved = true,
                        initialHoursMonth = YearMonth.now().toString()
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = error.message
                            ?: "ذخیره ساعات اولیه ناموفق بود."
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(isSavingInitialHours = false)
                }
            }
        }
    }

    fun updateInitialTotalHours(value: String) {
        _uiState.update {
            it.copy(initialTotalHours = value.filter(Char::isDigit))
        }
    }

    fun updateInitialTotalMinutes(value: String) {
        _uiState.update {
            it.copy(initialTotalMinutes = value.filter(Char::isDigit))
        }
    }

    fun updateInitialMonthlyHours(value: String) {
        _uiState.update {
            it.copy(initialMonthlyHours = value.filter(Char::isDigit))
        }
    }

    fun updateInitialMonthlyMinutes(value: String) {
        _uiState.update {
            it.copy(initialMonthlyMinutes = value.filter(Char::isDigit))
        }
    }
}