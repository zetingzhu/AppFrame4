package com.zzt.webview.a

import android.app.Application
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

/**
 * @author: zeting
 * @date: 2021/12/27
 *
 */
object WebViewInterceptRequestProxy {
    val TAG = "webViewResourceCacheDir"
    private lateinit var application: Application

    private val webViewResourceCacheDir by lazy {
        File(application.cacheDir, "RobustWebView")
    }

    private val okHttpClient by lazy {
//        val build = ChuckerInterceptor.Builder(application)
//            .collector(ChuckerCollector(application))
//            .maxContentLength(250000L)
//            .alwaysReadResponseBody(true)
//            .build()
        OkHttpClient.Builder().cache(Cache(webViewResourceCacheDir, 100L * 1024 * 1024))
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
    }

    fun init(application: Application) {
        this.application = application
    }

    fun shouldInterceptRequest(webResourceRequest: WebResourceRequest?): WebResourceResponse? {
        if (webResourceRequest == null || webResourceRequest.isForMainFrame) {
            return null
        }
        val url = webResourceRequest.url ?: return null
        if (isHttpUrl(url)) {
            return getHttpResource(url.toString(), webResourceRequest)
        }
        return null
    }

    private fun isHttpUrl(url: Uri): Boolean {
        val scheme = url.scheme
        log("url: $url")
        log("scheme: $scheme")
        if (scheme == "http" || scheme == "https") {
            return true
        }
        return false
    }

    private fun getHttpResource(
        url: String,
        webResourceRequest: WebResourceRequest
    ): WebResourceResponse? {
        val method = webResourceRequest.method
        if (method.equals("GET", true)) {
            try {
                val requestBuilder =
                    Request.Builder().url(url).method(webResourceRequest.method, null)
                val requestHeaders = webResourceRequest.requestHeaders
                if (!requestHeaders.isNullOrEmpty()) {
                    var requestHeadersLog = ""
                    requestHeaders.forEach {
                        requestBuilder.addHeader(it.key, it.value)
                        requestHeadersLog = it.key + " : " + it.value + "\n" + requestHeadersLog
                    }
                    log("requestHeaders: $requestHeadersLog")
                }
                val response = okHttpClient.newCall(requestBuilder.build())
                    .execute()
                val body = response.body
                if (body != null) {
                    val mimeType = response.header(
                        "content-type", body.contentType()?.type
                    ).apply {
                        log(this)
                    }
                    val encoding = response.header(
                        "content-encoding",
                        "utf-8"
                    ).apply {
                        log(this)
                    }
                    val responseHeaders = mutableMapOf<String, String>()
                    var responseHeadersLog = ""
                    for (header in response.headers) {
                        responseHeaders[header.first] = header.second
                        responseHeadersLog =
                            header.first + " : " + header.second + "\n" + responseHeadersLog
                    }
                    log("responseHeadersLog: $responseHeadersLog")
                    var message = response.message
                    val code = response.code
                    if (code == 200 && message.isBlank()) {
                        message = "OK"
                    }
                    val resourceResponse =
                        WebResourceResponse(mimeType, encoding, body.byteStream())
                    resourceResponse.responseHeaders = responseHeaders
                    resourceResponse.setStatusCodeAndReasonPhrase(code, message)
                    return resourceResponse
                }
            } catch (e: Throwable) {
                log("Throwable: $e")
            }
        }
        return null
    }

    private fun getAssetsImage(url: String): WebResourceResponse? {
        if (url.contains(".jpg")) {
            try {
                val inputStream = application.assets.open("ic_launcher.webp")
                return WebResourceResponse(
                    "image/webp",
                    "utf-8", inputStream
                )
            } catch (e: Throwable) {
                log("Throwable: $e")
            }
        }
        return null
    }

    private fun log(s: String) {
        Log.d(TAG, s)
    }

    private fun log(s: Any?) {
        Log.d(TAG, "" + s)
    }

}
