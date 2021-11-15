package com.zzt.coroutinessample.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

/**
 * @author: zeting
 * @date: 2021/11/15
 * 设置 循环
 */
class LoopCoroutineVM : ViewModel() {

    val loopCoroutineUtil: LoopCoroutineUtil by lazy {
        LoopCoroutineUtil()
    }

    fun startLoop( timeMillis: Long,listener: LoopListener) {
        loopCoroutineUtil.startLoop(
            timeMillis,
            doInBackground = {
                listener.doInBackground()
            },
            onPostExecute = {
                listener.onPostExecute(it)
            },
            viewModelScope
        )
    }

    fun cancelLoop() {
        loopCoroutineUtil.cancelLoop()
    }

    interface LoopListener {
        fun doInBackground(): Any?
        fun onPostExecute(any: Any?)
    }
}


