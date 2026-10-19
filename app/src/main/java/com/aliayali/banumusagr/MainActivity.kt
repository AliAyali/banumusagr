package com.aliayali.banumusagr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.aliayali.banumusagr.ui.BanumusaApp
import com.aliayali.banumusagr.ui.rememberDaricAppState
import com.example.designsystem.theme.BanumusaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            BanumusaTheme {
                CompositionLocalProvider {
                    val appState = rememberDaricAppState()

                    BanumusaApp(
                        appState = appState
                    )
                }
            }
        }
    }
}