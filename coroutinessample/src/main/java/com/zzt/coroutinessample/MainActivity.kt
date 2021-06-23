package com.zzt.coroutinessample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.entity.StartActivityDao
import com.zzt.views.DefaultDataRecycleView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    val mainScope = MainScope()
    val channel = Channel<Int>()

    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var listData: MutableList<StartActivityDao> = mutableListOf()
        listData.add(0, StartActivityDao("阻塞协程", "", ""))
        listData.add(1, StartActivityDao("阻塞线程", "", ""))
        listData.add(2, StartActivityDao("协程统计 measureTimeMillis", "", ""))
        listData.add(3, StartActivityDao("协程的线程调度", "", ""))
        listData.add(4, StartActivityDao("协程重复 repeat", "activity定义协程作用域 MainScope", ""))

        StartActivityRecyclerAdapter.setAdapterData(rv_list, RecyclerView.VERTICAL, listData) { position, data ->
            when (position) {
                0 -> threadV1()
                1 -> threadV2()
                2 -> threadV3()
                3 -> threadV4()
                4 -> threadV5()
            }
        }

    }

    fun threadV5() {
        // 在示例中启动了 10 个协程，且每个都工作了不同的时长
        repeat(10) { i ->
            mainScope.launch {
                delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间
                println("Coroutine $i is done")
            }
        }
    }

    fun threadV4() {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch(Dispatchers.IO) {
            Log.w(TAG, "显示数据：launch 开始：" + Thread.currentThread().name)
            val text = doThree()
            coroutineScope.launch(Dispatchers.Main) {
                Log.w(TAG, "显示数据 launch ：$text   ：" + Thread.currentThread().name)
            }
            Log.w(TAG, "显示数据：launch 结束：" + Thread.currentThread().name)
        }

        coroutineScope.launch(Dispatchers.Default) {
            Log.i(TAG, "显示数据：launch Default 开始：" + Thread.currentThread().name)
            val text = doThree()
            coroutineScope.launch(Dispatchers.Main) {
                Log.i(TAG, "显示数据 launch Default：$text ：" + Thread.currentThread().name)
            }
            Log.i(TAG, "显示数据：launch Default 结束：" + Thread.currentThread().name)
        }
        coroutineScope.launch(Dispatchers.Unconfined) {
            Log.v(TAG, "显示数据：launch Unconfined 开始：" + Thread.currentThread().name)
            val text = doThree()
            coroutineScope.launch(Dispatchers.Main) {
                Log.v(TAG, "显示数据 launch Unconfined：$text ：" + Thread.currentThread().name)
            }
            Log.v(TAG, "显示数据：launch Unconfined 结束：" + Thread.currentThread().name)
        }
        Log.e(TAG, "显示数据： 结束：" + Thread.currentThread().name)
    }

    fun threadV1() {

        // 使用协程
        runBlocking {
            println("Coroutines: start")
            val jobs = List(100_000) {
                // 创建新的coroutine
                launch {
                    // 挂起当前上下文而非阻塞1000ms
                    delay(1000L)
                    println("." + Thread.currentThread().name)
                }
            }
            jobs.forEach { it.join() }
            println("Coroutines: end")
        }
    }

    fun threadV2() {
        println("No Coroutines: start")
        // 使用阻塞
        val noCoroutinesJobs = List(100_000) {
            // 创建新的线程
            thread {
                // 阻塞
                Thread.sleep(1000L)
                println("." + Thread.currentThread().name)
            }
        }
        noCoroutinesJobs.forEach { it.join() }
        println("No Coroutines: end")
    }


    fun threadV3() = runBlocking<Unit> {
        // 计算总共需要执行多久，measureTimeMillis是kotlin标准库中所提供的方法
        val time = measureTimeMillis {
            val one = async() { doOne() } // 这里将doOne抛到CommonPool中的线程执行，并在结束时将结果带回来。
            val two = async() { doTwo() } // 这里将doTwo抛到CommonPool中的线程执行，并在结束时将结果带回来。
            println("The answer is ${one.await() + two.await()}") // 这里会输出6
        }
        println("${time}ms") // 由于doOne与doTwo在异步执行，因此这里输出大概是700ms


    }

    suspend fun doOne(): Int {
        delay(500L)
        return 1
    }

    suspend fun doTwo(): Int {
        delay(700L)
        return 5
    }

    suspend fun doThree(): String {
        Thread.sleep(3000)
        return "网络数据..."
    }

    fun println(str: String) {
        Log.d(TAG, str)
    }
}