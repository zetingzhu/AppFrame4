package com.zzt.webview

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatSpinner
import com.zzt.webview.b.MWebView
import com.zzt.webview.util.DataUtil
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptorInst
import kotlin.text.equals as equals1

/**
 * 正常布局添加 VebView
 */
class SkipActivityV1 : AppCompatActivity() {
    val TAG = "webView_" + SkipActivityV1::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skip_v0)

        initView()
    }

    var start = 0L

    var mWebView: WebView? = null
    var lastUrl: String? = null

    override fun onResume() {
        super.onResume()
        start = System.currentTimeMillis()
        log("------ onResume -- $start ")
        Looper.myQueue().addIdleHandler {
            val currentTimeMillis = System.currentTimeMillis()
            log(">>>>>> $currentTimeMillis" + DataUtil.getTime(currentTimeMillis))
            log("启动时间:" + (currentTimeMillis - start))
            log("整个耗时:" + (currentTimeMillis - SampleMainActivity.Companion.startV1))
            false
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val currentTimeMillis = System.currentTimeMillis()
        log("------ onWindowFocusChanged --")
        log(">>>>> 启动完成状态 ：$hasFocus 时间：${DataUtil.getTime(currentTimeMillis)}")
    }


    private fun initView() {
        var dataList = mutableListOf<String>()

        var time = System.currentTimeMillis()
        var htmlUrl =
            "http://static.xtrendspeed.com/product/aboutus/index.html?t=$time&deviceId=2505d0b8-952b-47cb-9f77-ac806cd20cf2&sourceId=10&device=1&v=1.4.5.76&language=zh-CN&market=debug&exchangeId=7&tradeToken=26591a65-094a-45f2-b845-9a2824d0b635&timeZoneOffset=28800&remoteLoginTips=1&sensorsId=&uuid=B2B398E121ED391C&deviceType=1&useNewVersion=1&auth=cb1da820d02c263d933ac674239d7eeb";

//            "http://test-static.daily-fx.net/product/app-xsxt/index.html?html=QA_on_deposit&type=4&t=1639379927364&deviceId=2505d0b8-952b-47cb-9f77-ac806cd20cf2&sourceId=10&device=1&v=1.4.5.75&language=zh-CN&market=debug&exchangeId=7&tradeToken=78dad4da-abda-47aa-b1f8-7adec163cf48&timeZoneOffset=28800&remoteLoginTips=1&sensorsId=&uuid=6564DE7F17D33FCE&deviceType=1&useNewVersion=1&auth=658e11b3132eec780c03c9fcf54b3caa"

//        htmlUrl = "http://www.baidu.com"

        dataList.add(htmlUrl)
        dataList.add("http://www.baidu.com")
        dataList.add("https://www.zhihu.com/question/385397882/answer/1594966805")
        dataList.add("https://juejin.cn/post/6876011410061852680")

        findViewById<AppCompatSpinner>(R.id.spinner).also {
            val adapter: ArrayAdapter<String> =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, dataList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            it.adapter = adapter
            it.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val getUrl = dataList.get(p2)
                    if (lastUrl != null && lastUrl != getUrl) {
                        start = System.currentTimeMillis()
                    }
                    WebViewCacheInterceptorInst.getInstance().loadUrl(mWebView, getUrl)
                    log("选择加载: $getUrl")
                    lastUrl = getUrl
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }



        mWebView = findViewById<WebView>(R.id.wb_skip_v0).also { mWebView ->
            val webSettings: WebSettings = mWebView.settings

            webSettings.javaScriptEnabled = true

            webSettings.domStorageEnabled = true
            webSettings.allowFileAccess = true
            webSettings.useWideViewPort = true
            webSettings.loadWithOverviewMode = true
            webSettings.setSupportZoom(true)
            webSettings.builtInZoomControls = false
            webSettings.displayZoomControls = false
            webSettings.defaultTextEncodingName = "UTF-8"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            }

            // 设置缓存模式
//            webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

            //使用自定义的WebViewClient
            mWebView.webViewClient = object : WebViewClient() {

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest
                ): Boolean {
                    val url = request.url.toString()
                    if (url.endsWith(".pdf") || url.contains(".pdf")) {

                        return true
                    }
                    WebViewCacheInterceptorInst.getInstance()
                        .loadUrl(mWebView, url)
                    return true
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    WebViewCacheInterceptorInst.getInstance().loadUrl(mWebView, url)
                    return true
                }

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Nullable
                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    return WebViewCacheInterceptorInst.getInstance().interceptRequest(request)
                }

                @Nullable
                override fun shouldInterceptRequest(
                    view: WebView?,
                    url: String?
                ): WebResourceResponse? {
                    return WebViewCacheInterceptorInst.getInstance().interceptRequest(url)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    log("--- onPageStarted --- $url ")
                    super.onPageStarted(view, url, favicon)
                    log("加载开始:" + (System.currentTimeMillis() - start))
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    log("--- onPageFinished ---  $url ")
                    super.onPageFinished(view, url)
                    log("加载完成:" + (System.currentTimeMillis() - start))
                }

            }

            mWebView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    log("--- onProgressChanged ---  $newProgress ")
                    super.onProgressChanged(view, newProgress)
                }
            }

//            WebViewCacheInterceptorInst.getInstance().loadUrl(mWebView, htmlUrl)
//            log("开始加载:" + (System.currentTimeMillis() - start))
        }
    }


    fun log(str: String) {
        Log.i(TAG, str)
    }
}