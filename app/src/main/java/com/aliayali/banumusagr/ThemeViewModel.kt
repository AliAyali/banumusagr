package com.aliayali.banumusagr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliayali.datastore.AppPreferences
import com.example.designsystem.theme.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    appPreferences: AppPreferences
) : ViewModel() {

    val selectedTheme: StateFlow<AppTheme> =
        appPreferences.selectedTheme
            .map { themeName ->
                AppTheme.entries.firstOrNull {
                    it.name == themeName
                } ?: AppTheme.Default
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = AppTheme.Default
            )
}