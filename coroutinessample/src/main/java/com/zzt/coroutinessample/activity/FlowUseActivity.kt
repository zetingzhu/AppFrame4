package com.zzt.coroutinessample.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.coroutinessample.databinding.ActivityFlowBinding
import com.zzt.coroutinessample.util.LoopCoroutineUtil
import com.zzt.entity.StartActivityDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * We weren’t able to create the release for you. Make sure you have a valid tag.
 *
 * 修改列表点击区域
 */
class FlowUseActivity : AppCompatActivity() {
    val TAG = FlowUseActivity::class.java.simpleName
    lateinit var binding: ActivityFlowBinding
    private var daoList = mutableListOf<StartActivityDao>()
    private var mCountdownJob: Job? = null
    private var pollUtilV2: LoopCoroutineUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData() {
        daoList.add(StartActivityDao("开始 倒计时", " ", "1"))
        daoList.add(StartActivityDao("结束 倒计时", " ", "2"))
        daoList.add(StartActivityDao("开启异步定时器", " ", "3"))
        daoList.add(StartActivityDao("关闭异步定时器", " ", "4"))
        daoList.add(StartActivityDao("启动一个空页面，再来打开一个定时器",  "",))
        StartActivityRecyclerAdapter.setAdapterData(
            binding.rvList,
            RecyclerView.VERTICAL,
            daoList
        ) { itemView, position, data ->
            if (data is StartActivityDao) {
                when (data.arouter) {
                    "1" -> flowTest1()
                    "2" -> mCountdownJob?.cancel()
                    "3" -> flowTest3()
                    "4" -> pollUtilV2?.cancelLoop()
                }
            }
        }
    }

    private fun flowTest3() {
        pollUtilV2 = LoopCoroutineUtil()
        pollUtilV2?.startLoop( 3500, {
            Thread.sleep(1000)

            "时间：" + System.currentTimeMillis()
        }, {
            Toast.makeText(this@FlowUseActivity, "" + it, Toast.LENGTH_SHORT).show()
        }, lifecycleScope)

    }

    private fun flowTest1() {
        mCountdownJob = countDownCoroutines(5000, lifecycleScope,
            onTick = { second ->
                Log.d(TAG, " 计时 ${second}s ")
            }, onStart = {
                // countdown start
                Log.d(TAG, " 计时开始 ")
            }, onFinish = {
                // countdown finished, reset state
                Log.d(TAG, " 计时结束 ")
            })
    }

    fun countDownCoroutines(
        total: Int,
        scope: CoroutineScope,
        onTick: (Int) -> Unit,
        onStart: (() -> Unit)? = null,
        onFinish: (() -> Unit)? = null,
    ): Job {
        var jobbb = scope.launch {
            val flow = flow {
                for (i in total downTo 0) {
                    emit(i)
                    delay(1000)
                }
            }
            flow.flowOn(Dispatchers.IO)
                .onEach {
                    Log.d(TAG, ">>> onEach $it")
                    onTick.invoke(it)
                }
                .flowOn(Dispatchers.Main)
                .onStart {
                    Log.d(TAG, ">>> onStart ${this}")
                    onStart?.invoke()
                }
                .onCompletion {
                    Log.d(TAG, ">>> onCompletion -1- ${this}")
                }
                .onCompletion { cause ->
                    Log.d(TAG, ">>> onCompletion -2- ${cause}")
                    onFinish?.invoke()
                }
                .collect {
                    Log.d(TAG, ">>> collect ${this}")
                }
        }
        return jobbb


//        return flow {
//            for (i in total downTo 0) {
//                emit(i)
//                delay(1000)
//            }
//        }.flowOn(Dispatchers.IO)
//            .onStart { onStart?.invoke() }
//            .onCompletion { onFinish?.invoke() }
//            .onEach { onTick.invoke(it) }
//            .launchIn(scope)
    }
}

