package com.eje.sozip.SOZIP.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.eje.sozip.ui.theme.accent
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

class AddressSelectView : ComponentActivity() {
    inner class JavaScriptInterface{
        @JavascriptInterface
        fun processDATA(data : String){
            val extra = Bundle()
            val intent = Intent()

            extra.putString("data", data)
            intent.putExtras(extra)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            webViewClient()
        }
    }

    @Composable
    fun webViewClient(){
        val webViewState = rememberWebViewState(url = "https://sozip-45078.web.app")
        val navigator = rememberWebViewNavigator()

        if(webViewState.isLoading){
            CircularProgressIndicator(modifier = Modifier, color = accent)
        }

        val webClient = remember{
            object : AccompanistWebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.loadUrl("javascript:execPostCode();")
                }
            }
        }

        WebView(state = webViewState,
            onCreated = { webView ->
                with(webView){
                    settings.run {
                        javaScriptEnabled = true
                    }

                    addJavascriptInterface(JavaScriptInterface(), "Android")
                }
            }, client = webClient, navigator = navigator)
    }
}