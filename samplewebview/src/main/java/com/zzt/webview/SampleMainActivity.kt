package com.zzt.webview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.zzt.webview.util.DataUtil
import java.util.*

class SampleMainActivity : AppCompatActivity() {
    val TAG = "webView_" + SampleMainActivity::class.java.simpleName

    companion object {
        var startV0 = 0L
        var startV2 = 0L
        var startV1 = 0L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_main)
        initView()
    }

    private fun initView() {
//        findViewById<LinearLayout>(R.id.ll_web_view_parent)?.apply {
//            CountTimeUtil.countTime("默认启动") {
//                var htmlUrl =
//                    "http://test-static.daily-fx.net/product/app-xsxt/index.html?html=QA_on_deposit&type=4&t=1639379927364&deviceId=2505d0b8-952b-47cb-9f77-ac806cd20cf2&sourceId=10&device=1&v=1.4.5.75&language=zh-CN&market=debug&exchangeId=7&tradeToken=78dad4da-abda-47aa-b1f8-7adec163cf48&timeZoneOffset=28800&remoteLoginTips=1&sensorsId=&uuid=6564DE7F17D33FCE&deviceType=1&useNewVersion=1&auth=658e11b3132eec780c03c9fcf54b3caa"
//
//
//                Debug.startMethodTracing()
//                Debug.startNativeTracing()
//
//                this.removeAllViews()
//                val vebView = MyWebViewUtil.getInstance().getVebView(this@SampleMainActivity)
//                this.addView(vebView)
//                vebView.loadUrl(htmlUrl)
//            }
//        }

        findViewById<Button>(R.id.btn_v2).setOnClickListener {
            startV2 = System.currentTimeMillis()
            Log.w(TAG, "启动时间 V2 <<< ：" + DataUtil.getTime(startV2))
            startActivity(Intent(this@SampleMainActivity, SkipActivityV2::class.java))
        }
        findViewById<Button>(R.id.btn_v0).setOnClickListener {
            startV0 = System.currentTimeMillis()
            Log.d(TAG, "启动时间 V0 <<< ：" + DataUtil.getTime(startV0))
            startActivity(Intent(this@SampleMainActivity, SkipActivityV0::class.java))
        }
        findViewById<Button>(R.id.btn_v1).setOnClickListener {
            startV1 = System.currentTimeMillis()
            Log.i(TAG, "启动时间 V1 <<< ：" + DataUtil.getTime(startV1))
            startActivity(Intent(this@SampleMainActivity, SkipActivityV1::class.java))
        }
    }


}