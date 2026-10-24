package com.aliayali.datastore

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.YearMonth

private val Context.dataStore: DataStore<Preferences> by
preferencesDataStore(name = "app_preferences")

data class InitialWorkHours(
    val totalHours: Int = 0,
    val monthlyHours: Int = 0,
    val month: String = "",
    val isSaved: Boolean = false
)

class AppPreferences(context: Context) {

    private val appContext = context.applicationContext

    private val themeKey =
        stringPreferencesKey("selected_theme")

    private val initialTotalHoursKey =
        intPreferencesKey("initial_total_hours")

    private val initialMonthlyHoursKey =
        intPreferencesKey("initial_monthly_hours")

    private val initialHoursMonthKey =
        stringPreferencesKey("initial_hours_month")

    private val initialHoursSavedKey =
        booleanPreferencesKey("initial_hours_saved")

    val selectedTheme: Flow<String> =
        appContext.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[themeKey] ?: DEFAULT_THEME
            }

    val initialWorkHours: Flow<InitialWorkHours> =
        appContext.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                InitialWorkHours(
                    totalHours = preferences[initialTotalHoursKey] ?: 0,
                    monthlyHours = preferences[initialMonthlyHoursKey] ?: 0,
                    month = preferences[initialHoursMonthKey] ?: "",
                    isSaved = preferences[initialHoursSavedKey] ?: false
                )
            }

    suspend fun saveTheme(themeName: String) {
        appContext.dataStore.edit { preferences ->
            preferences[themeKey] = themeName
        }
    }

    /**
     * ساعات اولیه فقط یک بار قابل ثبت هستند.
     * نتیجه true یعنی ثبت انجام شد.
     * نتیجه false یعنی قبلاً ثبت شده‌اند.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveInitialWorkHours(
        totalHours: Int,
        monthlyHours: Int
    ): Boolean {
        require(totalHours >= 0) {
            "ساعت کل نمی‌تواند منفی باشد."
        }
        require(monthlyHours >= 0) {
            "ساعت ماهانه نمی‌تواند منفی باشد."
        }

        var saved = false

        appContext.dataStore.edit { preferences ->
            val alreadySaved =
                preferences[initialHoursSavedKey] ?: false

            if (!alreadySaved) {
                preferences[initialTotalHoursKey] = totalHours
                preferences[initialMonthlyHoursKey] = monthlyHours
                preferences[initialHoursMonthKey] =
                    YearMonth.now().toString()
                preferences[initialHoursSavedKey] = true

                saved = true
            }
        }

        return saved
    }

    companion object {
        const val DEFAULT_THEME = "Default"
    }
}