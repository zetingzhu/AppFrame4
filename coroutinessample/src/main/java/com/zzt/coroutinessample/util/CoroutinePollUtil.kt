package com.zzt.coroutinessample.util

import android.os.Handler
import android.util.Log
import kotlinx.coroutines.*
import kotlin.math.log

/**
 * @author: zeting
 * @date: 2021/11/9
 * 看看协程能不能实现轮询
 */
object CoroutinePollUtil {
    val TAG = CoroutinePollUtil::class.java.simpleName

    var scope: CoroutineScope? = null

    // 开始轮询
    fun startPoll(timeMillis: Long) {
        cancelPoll()
        scope = CoroutineScope(Dispatchers.Main)
        Log.d(TAG, "轮询时间 启动 ${Thread.currentThread().name}")
        var job1: Job? = scope?.launch() {
            try {
                while (isActive) {
                    Log.e(TAG, "轮询时间 开始 ${Thread.currentThread().name}")
                    withContext(Dispatchers.IO) {
                        Log.e(TAG, "轮询时间 进行 ${Thread.currentThread().name}")
                    }
                    Log.e(TAG, "轮询时间 结果 ${Thread.currentThread().name}")
                    delay(5000)
                }
                Log.e(TAG, "轮询 取消 ${Thread.currentThread().name} ")
            } catch (e: Exception) {
                Log.e(TAG, "轮询 异常 ${Thread.currentThread().name} ")
            }
        }

        var job2: Job? = scope?.launch() {
            try {
                while (isActive) {
                    Log.w(TAG, "轮询时间 开始 ${Thread.currentThread().name}")
                    withContext(Dispatchers.IO) {
                        Log.w(TAG, "轮询时间 进行 ${Thread.currentThread().name}")
                    }
                    Log.w(TAG, "轮询时间 结果 ${Thread.currentThread().name}")
                    delay(9000)
                }
                Log.w(TAG, "轮询 取消 ${Thread.currentThread().name}  ")
            } catch (e: Exception) {
                Log.w(TAG, "轮询 异常 ${Thread.currentThread().name}  ")
            }
        }

        Log.d(TAG, "轮询时间 结束 ${Thread.currentThread().name}  ")

        scope?.launch {
            job1?.key?.let {
                Log.d(TAG, "  $it")
                scope?.coroutineContext?.get(job1.key)
            }

            delay(50 * 1000)
            job2?.cancel(CancellationException("取消 2"))


            delay(30 * 1000)
            job1?.cancel(CancellationException("取消 1"))


        }


    }

    /**
     * 取消轮询
     */
    fun cancelPoll() {
        scope?.cancel()
        scope = null
    }
}