package com.zzt.webview.a

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.MutableContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.*
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.FORCE_DARK_OFF
import androidx.webkit.WebSettingsCompat.FORCE_DARK_ON
import androidx.webkit.WebViewFeature
import com.zzt.webview.a.InjectUtils.injectVConsoleJs
import java.util.*

/**
 * @author: zeting
 * @date: 2021/12/27
 *
 */
class WebViewHelper private constructor(parent: ViewGroup) {

    companion object {
        fun with(parent: ViewGroup): WebViewHelper {
            return WebViewHelper(parent)
        }
    }

    private val webView = WebViewManager.obtain(parent.context)

    private var onPageChangedListener: OnPageChangedListener? = null

    private var injectState = false
    private var injectVConsole = false
    private var originalUrl = "about:blank"

    init {
        parent.addView(webView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val uri = request?.url
                if (view != null && uri != null && !("http" == uri.scheme || "https" == uri.scheme)) {
                    try {
                        view.context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return true
                }
                return false
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (view != null && request != null) {
                    when {
                        canAssetsResource(request) -> {
                            return assetsResourceRequest(view.context, request)
                        }
                        canCacheResource(request) -> {
                            return cacheResourceRequest(view.context, request)
                        }
                    }
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                onPageChangedListener?.onPageStarted(view, url, favicon)
                injectState = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                onPageChangedListener?.onPageFinished(view, url)
                injectState = false
            }
        }
        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                onPageChangedListener?.onProgressChanged(view, newProgress)
                if (newProgress > 80 && injectVConsole && !injectState) {
                    view?.apply {
                        context.injectVConsoleJs()?.let {
                            evaluateJavascript(it) {}
                        }
                    }
                    injectState = true
                }
            }
        }
        webView.setDownloadListener { url, _, _, _, _ ->
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                webView.context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val isAppDarkMode = true
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            if (webView is android.webkit.WebView) {
                val forceDarkMode = if (isAppDarkMode) FORCE_DARK_ON else FORCE_DARK_OFF
                WebSettingsCompat.setForceDark(webView.settings, forceDarkMode)
            }
        }
    }

    fun evaluateJavascript(js: String, callback: (String) -> Unit): WebViewHelper {
        webView.evaluateJavascript(js, callback)
        return this
    }

    fun injectVConsole(inject: Boolean): WebViewHelper {
        injectVConsole = inject
        return this
    }

    fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener?): WebViewHelper {
        this.onPageChangedListener = onPageChangedListener
        return this
    }

    fun canGoBack(): Boolean {
        val canBack = webView.canGoBack()
        if (canBack) webView.goBack()
        val backForwardList = webView.copyBackForwardList()
        val currentIndex = backForwardList.currentIndex
        if (currentIndex == 0) {
            val currentUrl = backForwardList.currentItem?.url
            val currentHost = Uri.parse(currentUrl).host
            //栈底不是链接则直接返回
            if (currentHost.isNullOrBlank()) return false
            //栈底链接不是原始链接则直接返回
            if (originalUrl != currentUrl) return false
        }
        return canBack
    }

    fun canGoForward(): Boolean {
        val canForward = webView.canGoForward()
        if (canForward) webView.goForward()
        return canForward
    }

    fun loadUrl(url: String) {
        webView.loadUrl(url)
        originalUrl = url
    }

    fun reload() {
        webView.reload()
    }

    fun onResume() {
        webView.onResume()
    }

    fun onPause() {
        webView.onPause()
    }

    fun onDestroyView() {
        WebViewManager.recycle(webView)
    }

    private fun canAssetsResource(webRequest: WebResourceRequest): Boolean {
        val url = webRequest.url.toString()
        return url.startsWith("file:///android_asset/")
    }

    private fun canCacheResource(webRequest: WebResourceRequest): Boolean {
        val url = webRequest.url.toString()
        val extension = getExtensionFromUrl(url)
        return extension == "ico" || extension == "bmp" || extension == "gif"
                || extension == "jpeg" || extension == "jpg" || extension == "png"
                || extension == "svg" || extension == "webp" || extension == "css"
                || extension == "js" || extension == "json" || extension == "eot"
                || extension == "otf" || extension == "ttf" || extension == "woff"
    }

    private fun assetsResourceRequest(
        context: Context,
        webRequest: WebResourceRequest
    ): WebResourceResponse? {

        return null
    }

    private fun cacheResourceRequest(
        context: Context,
        webRequest: WebResourceRequest
    ): WebResourceResponse? {

        return null
    }

    private fun getExtensionFromUrl(url: String): String {
        try {
            if (url.isNotBlank() && url != "null") {
                val extension = url
                    .substringBeforeLast('#') // Strip the fragment.
                    .substringBeforeLast('?') // Strip the query.
                    .substringAfterLast('/') // Get the last path segment.
                    .substringAfterLast('.', missingDelimiterValue = "") // Get the file extension.
                return MimeTypeMap.getFileExtensionFromUrl(extension)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getMimeTypeFromUrl(url: String): String {
        try {
            val extension = getExtensionFromUrl(url)
            if (extension.isNotBlank() && extension != "null") {
                if (extension == "json") {
                    return "application/json"
                }
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "*/*"
    }

    interface OnPageChangedListener {
        fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?)
        fun onPageFinished(view: WebView?, url: String?)
        fun onProgressChanged(view: WebView?, newProgress: Int)
    }
}

@SuppressLint("SetJavaScriptEnabled")
class WebViewManager private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: WebViewManager? = null

        private fun instance() = INSTANCE ?: synchronized(this) {
            INSTANCE ?: WebViewManager().also { INSTANCE = it }
        }

        fun prepare(context: Context) {
            instance().prepare(context)
        }

        fun obtain(context: Context): WebView {
            return instance().obtain(context)
        }

        fun recycle(webView: WebView) {
            instance().recycle(webView)
        }

        fun destroy() {
            instance().destroy()
        }
    }

    private val webViewCache: MutableList<WebView> = ArrayList(1)

    private fun create(context: Context): WebView {
        val webView = WebView(context)
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.overScrollMode = WebView.OVER_SCROLL_NEVER
        val webSetting = webView.settings
        webSetting.allowFileAccess = true
        webSetting.setAppCacheEnabled(true)
        webSetting.cacheMode = WebSettings.LOAD_DEFAULT
        webSetting.domStorageEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.javaScriptEnabled = true
        webSetting.loadWithOverviewMode = true
        webSetting.setSupportZoom(true)
        webSetting.displayZoomControls = false
        webSetting.useWideViewPort = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        }

        return webView
    }

    fun prepare(context: Context) {
        if (webViewCache.isEmpty()) {
            Looper.myQueue().addIdleHandler {
                webViewCache.add(create(MutableContextWrapper(context)))
                false
            }
        }
    }

    fun obtain(context: Context): WebView {
        if (webViewCache.isEmpty()) {
            webViewCache.add(create(MutableContextWrapper(context)))
        }
        val webView = webViewCache.removeFirst()
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        webView.clearHistory()
        webView.resumeTimers()
        return webView
    }

    fun recycle(webView: WebView) {
        try {
            webView.stopLoading()
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            webView.pauseTimers()
            webView.webChromeClient = null
            webView.webViewClient = WebViewClient()
            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (!webViewCache.contains(webView)) {
                webViewCache.add(webView)
            }
        }
    }

    fun destroy() {
        try {
            webViewCache.forEach {
                it.removeAllViews()
                it.destroy()
                webViewCache.remove(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}