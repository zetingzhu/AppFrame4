package com.zzt.coroutinessample.util

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * @author: zeting
 * @date: 2021/11/9
 * 看看协程能不能实现轮询
 */
class LoopCoroutineUtil {
    val TAG = LoopCoroutineUtil::class.java.simpleName
    private var mCountdownJob: Job? = null

    // 开始轮询
    fun startLoop(
        timeMillis: Long,
        doInBackground: () -> Any?,
        onPostExecute: (Any?) -> Unit,
        lifecycle: CoroutineScope
    ) {
        mCountdownJob = coroutinesCyclic(timeMillis, lifecycle, doInBackground, onPostExecute)
    }

    /**
     * 取消轮询
     */
    fun cancelLoop() {
        mCountdownJob?.cancel()
    }

    /**
     * 循环的协程
     */
    private fun coroutinesCyclic(
        timeMillis: Long,
        scope: CoroutineScope,
        doInBackground: () -> Any?,
        onPostExecute: (Any?) -> Unit,
    ): Job? {
        return flow {
            while (true) {
                try {
                    emit(doInBackground.invoke())
//                    Log.i(TAG, ">>>>>  循环线程 ${Thread.currentThread().name}  ${timeMillis}")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(TAG, ">>>>> 异步操作异常 ${Thread.currentThread().name}")
                }
                delay(timeMillis)
            }
        }.flowOn(Dispatchers.IO)
            .onEach {
                try {
                    //                Log.i(TAG, ">>>>> 结果执行线程  ${Thread.currentThread().name}")
                    onPostExecute.invoke(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(TAG, ">>>>> 同步操作异常 ${Thread.currentThread().name}")
                }
            }
            .flowOn(Dispatchers.Main)
            .onStart {
//                Log.i(TAG, ">>>>>  开始 ${Thread.currentThread().name}   ")
            }
            .catch { ex ->
//                Log.i(TAG, ">>>>>定时内部异常 ${Thread.currentThread().name}   ")
                ex.printStackTrace()
            }.onCompletion {
//                Log.i(TAG, ">>>>> 定时结束了 ${Thread.currentThread().name}   ")
            }
            .launchIn(scope)
    }
}