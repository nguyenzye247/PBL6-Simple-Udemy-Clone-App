package com.pbl.mobile.util

import android.webkit.WebView
import android.webkit.WebViewClient

class AppWebViewClients(
    private val onFinishedLoading: () -> Unit
) : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        onFinishedLoading.invoke()
        super.onPageFinished(view, url)
    }
}
