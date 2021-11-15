package com.zzt.coroutinessample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * @author: zeting
 * @date: 2021/4/14
 *
 */
class MyViewModel : ViewModel() {


    fun testVm2(){

    }



    fun abc() {
        // 在 ViewModel 中启动新的协程
        viewModelScope.launch {

        }
    }

// 方案 1: 取消之前的任务

// 对于排序和过滤的情况，新请求进来，取消上一个，这样的方案是很适合的。

    var controlledRunner = ControlledRunner<List<ProductListing>>()

//    suspend fun loadSortedProductsV1(ascending: Boolean): List<ProductListing> {
        // 在开启新的排序之前，先取消上一个排序任务
//        return controlledRunner.cancelPreviousThenRun {
//            mutableListOf<ProductListing>()
//        }
//    }

    // 方案 2: 使用互斥锁
// 注意: 这个方法对于排序或者是过滤来说并不是一个很好的解决方案，但是它对于解决网络请求引起的并发问题非常适合。
    val singleRunner = SingleRunner()
    suspend fun loadSortedProductsV2(ascending: Boolean): List<ProductListing> {
        // 开始新的任务之前，等待之前的排序任务完成
        return singleRunner.afterPrevious {
            List<ProductListing>(2) {
                ProductListing()
            }
        }
    }

    // 方案 3: 复用前一个任务
    suspend fun fetchProductsFromBackendV3(): List<ProductListing> {
        // 如果已经有一个正在运行的请求，那么就返回它。如果没有的话，开启一个新的请求。
        return controlledRunner.joinPreviousOrRun {
            mutableListOf<ProductListing>()
        }
    }
}