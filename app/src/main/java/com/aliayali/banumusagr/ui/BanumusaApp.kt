package com.aliayali.banumusagr.ui

import android.annotation.SuppressLint
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.aliayali.banumusagr.components.ErrorScreen
import com.aliayali.banumusagr.components.LoadingScreen

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BanumusaApp() {

    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var webView: WebView? by remember { mutableStateOf(null) }

    val backDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            backDispatcher?.onBackPressed()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (!hasError) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->

                    WebView(context).apply {

                        webViewClient = object : WebViewClient() {

                            override fun onPageFinished(
                                view: WebView?,
                                url: String?
                            ) {
                                isLoading = false
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                if (request?.isForMainFrame == true) {
                                    isLoading = false
                                    hasError = true
                                    view?.stopLoading()
                                }
                            }
                        }

                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        webView = this

                        loadUrl("https://banumusagr.ir/")
                    }
                }
            )
        }

        if (isLoading && !hasError) {
            LoadingScreen()
        }

        if (hasError) {
            ErrorScreen(
                onRetry = {
                    hasError = false
                    isLoading = true
                    webView?.reload()
                }
            )
        }
    }
}