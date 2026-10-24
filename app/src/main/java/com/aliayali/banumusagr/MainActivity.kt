package com.aliayali.banumusagr

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.aliayali.banumusagr.ui.BanumusaApp
import com.aliayali.banumusagr.ui.rememberDaricAppState
import com.example.designsystem.theme.BanumusaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val selectedTheme by
            themeViewModel.selectedTheme.collectAsState()

            BanumusaTheme(appTheme = selectedTheme) {
                val appState = rememberDaricAppState()
                BanumusaApp(appState = appState)
            }
        }
    }
}