package com.zzt.webview

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zzt.webview.util.DataUtil

/**
 * 正常布局添加 VebView
 */
class SkipActivityV0 : AppCompatActivity() {
    val TAG = "webView_" + SkipActivityV0::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skip_v0)

        initView()
    }

    override fun onResume() {
        super.onResume()
        log( "------ onResume --")
        val start = System.currentTimeMillis()
        Looper.myQueue().addIdleHandler {
            val currentTimeMillis = System.currentTimeMillis()
            log( ">>>>>> $currentTimeMillis" + DataUtil.getTime(currentTimeMillis))
            log( "启动时间:" + (currentTimeMillis - start))
            log( "整个耗时:" + (currentTimeMillis - SampleMainActivity.Companion.startV0))
            false
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val currentTimeMillis = System.currentTimeMillis()
        log( "------ onWindowFocusChanged --")
        log( ">>>>> 启动完成状态 ：$hasFocus 时间：${DataUtil.getTime(currentTimeMillis)}")
    }


    private fun initView() {
        var time = System.currentTimeMillis()
        var htmlUrl =
            "http://static.xtrendspeed.com/product/aboutus/index.html?t=$time&deviceId=2505d0b8-952b-47cb-9f77-ac806cd20cf2&sourceId=10&device=1&v=1.4.5.76&language=zh-CN&market=debug&exchangeId=7&tradeToken=26591a65-094a-45f2-b845-9a2824d0b635&timeZoneOffset=28800&remoteLoginTips=1&sensorsId=&uuid=B2B398E121ED391C&deviceType=1&useNewVersion=1&auth=cb1da820d02c263d933ac674239d7eeb";

//            "http://test-static.daily-fx.net/product/app-xsxt/index.html?html=QA_on_deposit&type=4&t=1639379927364&deviceId=2505d0b8-952b-47cb-9f77-ac806cd20cf2&sourceId=10&device=1&v=1.4.5.75&language=zh-CN&market=debug&exchangeId=7&tradeToken=78dad4da-abda-47aa-b1f8-7adec163cf48&timeZoneOffset=28800&remoteLoginTips=1&sensorsId=&uuid=6564DE7F17D33FCE&deviceType=1&useNewVersion=1&auth=658e11b3132eec780c03c9fcf54b3caa"

//        htmlUrl = "http://www.baidu.com"
        findViewById<WebView>(R.id.wb_skip_v0).apply {
            // 开启 localStorage
            getSettings().setDomStorageEnabled(false)
            // 设置支持javascript
            getSettings().setJavaScriptEnabled(true)
            // 设置缓存模式
            getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
            //使用自定义的WebViewClient
            setWebViewClient(object : WebViewClient() {
                //覆盖shouldOverrideUrlLoading 方法
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    log( "--- onPageStarted --- $url ")
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    log( "--- onPageFinished ---  $url ")
                    super.onPageFinished(view, url)
                }

            })
            loadUrl(htmlUrl)

        }
    }

    fun log(str: String) {
        Log.d(TAG, str)
    }
}