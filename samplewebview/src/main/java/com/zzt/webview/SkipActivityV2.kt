package com.zzt.webview

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.zzt.webview.b.MyWebViewUtil
import com.zzt.webview.util.CountTimeUtil
import com.zzt.webview.util.DataUtil

/**
 * 动态添加 VebView
 *
 */
class SkipActivityV2 : AppCompatActivity() {
    val TAG = "webView_" + SkipActivityV2::class.java.simpleName
    var ll_content_root: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skip_v2)
        initView()
    }

    override fun onResume() {
        super.onResume()
        log("------ onResume --")
        val start = System.currentTimeMillis()
        Looper.myQueue().addIdleHandler {
            val currentTimeMillis = System.currentTimeMillis()
            Log.w(TAG, ">>>>>> $currentTimeMillis" + DataUtil.getTime(currentTimeMillis))
            Log.w(TAG, "启动时间:" + (currentTimeMillis - start))
            Log.w(TAG, "整个耗时:" + (currentTimeMillis - SampleMainActivity.Companion.startV2))
            false
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val currentTimeMillis = System.currentTimeMillis()
        Log.w(TAG, "------ onWindowFocusChanged --")
        Log.w(TAG, ">>>>> 启动完成状态 ：$hasFocus 时间：${DataUtil.getTime(currentTimeMillis)}")
    }

    private fun initView() {
        log("------ 1 --")
        ll_content_root = findViewById<LinearLayout>(R.id.ll_content_root)
        ll_content_root?.apply {
            CountTimeUtil.countTime("启动第二个") {
                var time = System.currentTimeMillis()
                var htmlUrl =
                    "http://static.xtrendspeed.com/product/aboutus/index.html?t=$time&deviceId=2505d0b8-952b-47cb-9f77-ac806cd20cf2&sourceId=10&device=1&v=1.4.5.76&language=zh-CN&market=debug&exchangeId=7&tradeToken=26591a65-094a-45f2-b845-9a2824d0b635&timeZoneOffset=28800&remoteLoginTips=1&sensorsId=&uuid=B2B398E121ED391C&deviceType=1&useNewVersion=1&auth=cb1da820d02c263d933ac674239d7eeb";
//                    "http://test-static.daily-fx.net/product/app-xsxt/index.html?html=QA_on_deposit&type=4&t=1639379927364&deviceId=2505d0b8-952b-47cb-9f77-ac806cd20cf2&sourceId=10&device=1&v=1.4.5.75&language=zh-CN&market=debug&exchangeId=7&tradeToken=78dad4da-abda-47aa-b1f8-7adec163cf48&timeZoneOffset=28800&remoteLoginTips=1&sensorsId=&uuid=6564DE7F17D33FCE&deviceType=1&useNewVersion=1&auth=658e11b3132eec780c03c9fcf54b3caa"
                this.removeAllViews()
                val webView = MyWebViewUtil.getInstance().getVebView(this@SkipActivityV2, lifecycle)
                log("这里说明父布局没有被清空:" + webView.parent)
                if (webView.parent == null) {
                    this.addView(webView)
                }
                webView.loadUrl(htmlUrl)
            }
        }
        log("------ 2 --")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("------ onDestroy --")
//        ll_content_root?.removeAllViews()
        MyWebViewUtil.getInstance().recycleWebView()
    }


    fun log(str: String) {
        Log.d(TAG, str)
    }
}