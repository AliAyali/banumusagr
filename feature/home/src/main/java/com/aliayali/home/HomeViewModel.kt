package com.aliayali.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliayali.common.NetworkChecker
import com.aliayali.datastore.AppPreferences
import com.aliayali.domain.EmployeeUseCases
import com.aliayali.domain.ObserveWorkTimeCountersUseCase
import com.aliayali.domain.WorkReportUseCases
import com.aliayali.home.components.ReportMessageFormatter
import com.aliayali.home.mapper.asHomeUiModel
import com.aliayali.home.model.HomeUiData
import com.aliayali.model.WorkReport
import com.aliayali.network.BuildConfig
import com.aliayali.network.ReportNetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val workReportUseCases: WorkReportUseCases,
    observeWorkTimeCountersUseCase: ObserveWorkTimeCountersUseCase,
    private val reportNetworkDataSource: ReportNetworkDataSource,
    private val employeeUseCases: EmployeeUseCases,
    private val appPreferences: AppPreferences,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
        private const val REPORT_SEND_TAG = "ReportSend"
        private const val REPORT_DELETE_TAG = "ReportDelete"
    }

    private val _uiState =
        MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    private val _sendingReportIds =
        MutableStateFlow<Set<Long>>(emptySet())

    val sendingReportIds: StateFlow<Set<Long>> =
        _sendingReportIds.asStateFlow()

    private val _initialHoursSaved =
        MutableStateFlow<Boolean?>(null)

    val initialHoursSaved: StateFlow<Boolean?> =
        _initialHoursSaved.asStateFlow()

    private val homeDataFlow = combine(
        workReportUseCases.getAllReports(),
        observeWorkTimeCountersUseCase()
    ) { reports, stats ->

        val latestReportIds = reports
            .groupBy { normalizeReportType(it.reportType) }
            .values
            .mapNotNull { group ->
                group.maxWithOrNull(
                    compareBy<WorkReport> { it.createdAtMillis }
                        .thenBy { it.id }
                )?.id
            }
            .toSet()

        val homeReports = reports.map { report ->
            report.asHomeUiModel().copy(
                isLatestOfType = report.id in latestReportIds
            )
        }

        HomeUiData(
            reports = homeReports,
            dailyWorkMinutes = stats.dailyMinutes,
            weeklyWorkMinutes = stats.weeklyMinutes,
            monthlyWorkMinutes = stats.monthlyMinutes,
            totalWorkMinutes = stats.totalMinutes
        )
    }

    init {
        observeHomeData()
        observeInitialHoursSaved()
    }

    private fun observeInitialHoursSaved() {
        viewModelScope.launch {
            appPreferences.initialWorkHours
                .catch { throwable ->
                    if (throwable is CancellationException) {
                        throw throwable
                    }

                    android.util.Log.e(
                        TAG,
                        "دریافت وضعیت ساعات اولیه ناموفق بود",
                        throwable
                    )

                    _initialHoursSaved.value = false
                }
                .collect { hours ->
                    _initialHoursSaved.value = hours.isSaved
                }
        }
    }

    private fun normalizeReportType(type: String): String {
        return when (type.trim().uppercase()) {
            "DAILY", "روزانه", "گزارش روزانه" -> "DAILY"
            "WEEKLY", "هفتگی", "گزارش هفتگی" -> "WEEKLY"
            "MONTHLY", "ماهانه", "گزارش ماهانه" -> "MONTHLY"
            else -> type.trim().uppercase()
        }
    }

    private fun observeHomeData() {
        viewModelScope.launch {
            homeDataFlow
                .onStart {
                    _uiState.value = HomeUiState.Loading
                }
                .catch { throwable ->
                    if (throwable is CancellationException) {
                        throw throwable
                    }

                    android.util.Log.e(
                        TAG,
                        "دریافت اطلاعات داشبورد ناموفق بود",
                        throwable
                    )

                    val currentState = _uiState.value

                    if (currentState is HomeUiState.Success) {
                        _uiState.value =
                            currentState.copy(isRefreshing = false)
                    } else {
                        _uiState.value = HomeUiState.Error(
                            message = throwable.message
                                ?: "دریافت اطلاعات داشبورد ناموفق بود."
                        )
                    }
                }
                .collect { homeUiData ->
                    _uiState.value = HomeUiState.Success(
                        homeUiData = homeUiData
                    )
                }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Refresh -> refresh()
            is HomeEvent.SendReport -> sendReport(event.reportId)
            is HomeEvent.DeleteReport -> deleteReport(event.reportId)
        }
    }

    private fun getNotificationTokens(): List<String> {
        return listOf(
            BuildConfig.NOTIFICATOR_TOKEN_1,
            BuildConfig.NOTIFICATOR_TOKEN_2
        ).filter { it.isNotBlank() }
    }

    private suspend fun sendToBothTokens(
        message: String
    ): List<Boolean> {
        val tokens = getNotificationTokens()

        require(tokens.size == 2) {
            "هر دو توکن ارسال اعلان باید در تنظیمات Release تعریف شده باشند."
        }

        return coroutineScope {
            tokens.mapIndexed { index, token ->
                async {
                    try {
                        val response = reportNetworkDataSource.sendReport(
                            to = token,
                            text = message
                        )

                        android.util.Log.d(
                            "NotificationSend",
                            "نتیجه مقصد ${index + 1}: ok=${response.ok}"
                        )

                        response.ok
                    } catch (exception: CancellationException) {
                        throw exception
                    } catch (exception: Exception) {
                        android.util.Log.e(
                            "NotificationSend",
                            "ارسال اعلان به مقصد ${index + 1} ناموفق بود.",
                            exception
                        )
                        false
                    }
                }
            }.awaitAll()
        }
    }

    private fun sendReport(reportId: Long) {
        if (reportId in _sendingReportIds.value) return

        _sendingReportIds.value += reportId

        viewModelScope.launch {
            try {
                // دریافت اطلاعات لازم از Room
                val employee = employeeUseCases
                    .observeEmployee()
                    .first()
                    ?: throw IllegalStateException(
                        "اطلاعات کارمند ثبت نشده است."
                    )

                val report = workReportUseCases
                    .getReportById(reportId)
                    .first()
                    ?: throw IllegalStateException(
                        "گزارش موردنظر پیدا نشد."
                    )

                // آماده‌سازی متن اعلان
                val message = ReportMessageFormatter.format(
                    report = report,
                    firstName = employee.firstName,
                    lastName = employee.lastName,
                    activityUnit = employee.department,
                    userId = employee.id.toLong()
                )

                // ابتدا وضعیت گزارش را در Room به‌روزرسانی کن.
                // این عملیات به اینترنت وابسته نیست.
                workReportUseCases.markReportAsSent(reportId)

                android.util.Log.d(
                    REPORT_SEND_TAG,
                    "وضعیت گزارش $reportId در Room به‌روزرسانی شد."
                )

                // ارسال اعلان، مستقل از عملیات Room
                val notificationResults = try {
                    if (!hasInternetConnection()) {
                        null
                    } else {
                        sendToBothTokens(message)
                    }
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    android.util.Log.e(
                        REPORT_SEND_TAG,
                        "وضعیت Room تغییر کرد، اما ارسال اعلان خطا داشت.",
                        exception
                    )
                    null
                }

                when {
                    notificationResults == null -> {
                        showToast(
                            "وضعیت گزارش ذخیره شد، اما اعلان ارسال نشد."
                        )
                    }

                    notificationResults.all { it } -> {
                        showToast(
                            "وضعیت گزارش ذخیره شد و اعلان به هر دو مقصد ارسال شد."
                        )
                    }

                    else -> {
                        val successCount =
                            notificationResults.count { it }

                        showToast(
                            "وضعیت گزارش ذخیره شد؛ اعلان به " +
                                    "$successCount از 2 مقصد ارسال شد."
                        )
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                android.util.Log.e(
                    REPORT_SEND_TAG,
                    "به‌روزرسانی گزارش $reportId ناموفق بود.",
                    exception
                )

                showToast(
                    exception.message
                        ?: "به‌روزرسانی گزارش در پایگاه داده ناموفق بود."
                )
            } finally {
                _sendingReportIds.value -= reportId
            }
        }
    }

    private fun deleteReport(reportId: Long) {
        viewModelScope.launch {
            try {
                // ابتدا اطلاعات گزارش را از Room بخوان.
                val report = workReportUseCases
                    .getReportById(reportId)
                    .first()

                if (report == null) {
                    showToast("گزارش موردنظر پیدا نشد.")
                    return@launch
                }

                val employee = employeeUseCases
                    .observeEmployee()
                    .first()

                if (employee == null) {
                    showToast("اطلاعات کاربر پیدا نشد.")
                    return@launch
                }

                // قبل از حذف، متن اعلان را آماده کن.
                val message = buildString {
                    appendLine("🗑️ گزارش حذف شد")
                    appendLine("━━━━━━━━━━━━━━━━━━")
                    appendLine(
                        "👤 نام و نام خانوادگی: " +
                                "${employee.firstName} ${employee.lastName}"
                    )
                    appendLine("🏢 واحد فعالیت: ${employee.department}")
                    appendLine("🆔 شناسه کاربر: ${employee.id}")
                    appendLine("🆔 شناسه گزارش: ${report.id}")
                    appendLine("📅 تاریخ گزارش: ${report.reportDate}")
                    appendLine("📋 نوع گزارش: ${report.reportType}")
                    appendLine()
                    appendLine("این گزارش از برنامه حذف شد.")
                    appendLine()
                    append(
                        "#user_${employee.id} " +
                                "#report_${report.id} #deleted"
                    )
                }

                // حذف محلی از Room؛ قبل از ارسال اعلان
                workReportUseCases.deleteReport(reportId)

                android.util.Log.d(
                    REPORT_DELETE_TAG,
                    "گزارش $reportId از Room حذف شد."
                )

                // ارسال اعلان دیگر شرط حذف گزارش نیست.
                val notificationResults = try {
                    if (!hasInternetConnection()) {
                        null
                    } else {
                        sendToBothTokens(message)
                    }
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    android.util.Log.e(
                        REPORT_DELETE_TAG,
                        "گزارف حذف شد، اما ارسال اعلان خطا داشت.",
                        exception
                    )
                    null
                }

                when {
                    notificationResults == null -> {
                        showToast(
                            "گزارش از برنامه حذف شد، اما اعلان ارسال نشد."
                        )
                    }

                    notificationResults.all { it } -> {
                        showToast(
                            "گزارش حذف شد و اعلان به هر دو مقصد ارسال شد."
                        )
                    }

                    else -> {
                        val successCount =
                            notificationResults.count { it }

                        showToast(
                            "گزارش حذف شد؛ اعلان به " +
                                    "$successCount از 2 مقصد ارسال شد."
                        )
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                android.util.Log.e(
                    REPORT_DELETE_TAG,
                    "حذف گزارش $reportId ناموفق بود.",
                    exception
                )

                showToast(
                    exception.message
                        ?: "حذف گزارش از پایگاه داده ناموفق بود."
                )
            }
        }
    }

    private fun refresh() {
        val currentState = _uiState.value

        if (
            currentState is HomeUiState.Success &&
            currentState.isRefreshing
        ) {
            return
        }

        if (currentState is HomeUiState.Success) {
            _uiState.value = currentState.copy(isRefreshing = true)
        }

        viewModelScope.launch {
            try {
                val freshData = homeDataFlow.first()

                _uiState.value = HomeUiState.Success(
                    homeUiData = freshData,
                    isRefreshing = false
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Exception) {
                android.util.Log.e(
                    TAG,
                    "به‌روزرسانی داشبورد ناموفق بود",
                    throwable
                )

                val latestState = _uiState.value

                if (latestState is HomeUiState.Success) {
                    _uiState.value =
                        latestState.copy(isRefreshing = false)
                } else {
                    _uiState.value = HomeUiState.Error(
                        message = throwable.message
                            ?: "به‌روزرسانی اطلاعات ناموفق بود."
                    )
                }
            }
        }
    }

    private suspend fun hasInternetConnection(): Boolean {
        return withContext(Dispatchers.IO) {
            NetworkChecker.isInternetAvailable(context)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}