package com.zzt.coroutinessample.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.zzt.coroutinessample.databinding.ActivityEmptyBinding
import com.zzt.coroutinessample.util.LoopCoroutineUtil
import com.zzt.coroutinessample.util.LoopCoroutineVM
import com.zzt.coroutinessample.MyViewModel


class EmptyActivity : AppCompatActivity() {
    val TAG = EmptyActivity::class.java.simpleName
    lateinit var binding: ActivityEmptyBinding

    var coroutinePollUtilV2: LoopCoroutineUtil? = null
    var loopCUtil: LoopCoroutineUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmptyBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initView()
    }


    var loopVM: LoopCoroutineVM? = null
    var count: Int = 0
    private fun initView() {
        binding.btnStart.setOnClickListener {
            coroutinePollUtilV2 = LoopCoroutineUtil()
            coroutinePollUtilV2?.startLoop(
                4000,
                {
                    Thread.sleep(1000)
                    "第一个按钮循环：" + System.currentTimeMillis()
                },
                { o: Any? ->
                    Log.w(TAG, ">> 一 $o")
                },
                lifecycle = lifecycleScope
            )
        }

        loopVM = ViewModelProvider(this).get(LoopCoroutineVM::class.java)
        binding.btnStop.setOnClickListener {
            coroutinePollUtilV2?.cancelLoop()
        }




        binding.btnStart2.setOnClickListener {
            count = 0
            loopCUtil = LoopCoroutineUtil()
            loopCUtil?.startLoop(2000, {
                Thread.sleep(1000)

                count++


                if (count in 5..7) {
                    throw RuntimeException("这里是运行异常了")
                }

                "第二个按钮循环：" + count + " >> " + System.currentTimeMillis()
            }, {
                Log.e(TAG, "》》二 $it")
            }, lifecycleScope)
        }
        binding.btnStop2.setOnClickListener {
            loopCUtil?.cancelLoop()
        }

    }
}