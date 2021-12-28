package com.zzt.coroutinessample.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.coroutinessample.databinding.ActivityMainBinding
import com.zzt.coroutinessample.util.CoroutinePollUtil
import com.zzt.entity.StartActivityDao
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    val mainScope = MainScope()

    private lateinit var binder: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        var listData: MutableList<StartActivityDao> = mutableListOf()
        listData.add(StartActivityDao("跳转到Flow", "", "9"))
        listData.add(StartActivityDao("Flow 使用", "", FlowUseActivity::class.java))
        listData.add(StartActivityDao("验证 kotlin coroutine 循环器", "", EmptyActivity::class.java))
        listData.add(StartActivityDao("验证 Java coroutine 循环器", "", EmptyActivityJava::class.java))
        listData.add(StartActivityDao("阻塞线程", "", "0"))
        listData.add(StartActivityDao("协程统计 measureTimeMillis", "", "1"))
        listData.add(StartActivityDao("协程的线程调度", "", "2"))
        listData.add(StartActivityDao("协程重复 repeat", "activity定义协程作用域 MainScope", "3"))
        //  lauch 是非阻塞的 而 runBlocking 是阻塞的
        listData.add(StartActivityDao("lauch 非阻塞协程", "", "4"))
        listData.add(StartActivityDao("runBlocking 阻塞协程", "", "5"))
        listData.add(StartActivityDao("withContext 串行 可返回结果协程", "", "6"))
        listData.add(StartActivityDao("async 并行 可返回结果协程", "", "7"))
        listData.add(StartActivityDao("阻塞协程", "", "8"))
        listData.add(StartActivityDao("启动轮询", "", "10"))
        listData.add(StartActivityDao("结束轮询", "", "11"))
        listData.add(StartActivityDao("高阶函数", "", "12"))



        StartActivityRecyclerAdapter.setAdapterData(
            binder.rvList,
            RecyclerView.VERTICAL,
            listData
        ) { itemView, position, data ->
            if (data is StartActivityDao) {
                when (data.arouter) {
                    "0" -> threadV1()
                    "1" -> threadV2()
                    "2" -> threadV3()
                    "3" -> threadV4()
                    "4" -> threadV5()
                    "5" -> threadV6()
                    "6" -> threadV7()
                    "7" -> threadV8()
                    "8" -> threadV9()
                    "9" -> startActivity(Intent(this@MainActivity, FlowActivity::class.java))
                    "10" -> CoroutinePollUtil.startPoll(5000)
                    "11" -> CoroutinePollUtil.cancelPoll()
                    "12" -> funKotlin12()
                }
            }
        }
    }

    fun Collection<Int>.foldInt(
        initial: Int,
        combine: (acc: Int, nextElement: Int) -> Int
    ): Int {
        var accumulator: Int = initial
        for (element: Int in this) {
            accumulator = combine(accumulator, element)
        }
        return accumulator
    }

    fun <T, R> Collection<T>.fold(
        initial: R,
        combine: (acc: R, nextElement: T) -> R
    ): R {
        var accumulator: R = initial
        for (element: T in this) {
            accumulator = combine(accumulator, element)
            Log.d(TAG, ">>>>>>>>>>> accumulator:${accumulator}")
        }
        return accumulator
    }


    private fun funKotlin12() {
        val time = measureTimeMillis {
            val items = listOf(1, 2, 3, 4, 5)
            val foldResult = items.fold(0, { acc: Int, i: Int ->
                Log.d(TAG, "acc = $acc, i = $i, ")
                val result = acc + i
                Log.d(TAG, "result = $result")
                result
            })
            Log.d(TAG, "结果： $foldResult")

            // lambda 表达式的参数类型是可选的，如果能够推断出来的话：
            val joinedToString = items.fold("Elements:", { acc, i -> "$acc $i" })
            Log.d(TAG, "结果： $joinedToString")

            // 函数引用也可以用于高阶函数调用：
//            val product = items.fold(1, Int::times)
            val product = items.fold(1, { a: Int, b: Int ->
                val c = a.times(b)
                Log.d(TAG, "计算： $a $b $c ")
                c
            })
            Log.d(TAG, "结果： $product")
        }
        Log.d(TAG, "耗时： $time ")
    }

    fun threadV8() {

        CoroutineScope(Dispatchers.Main).launch {
            val time1 = System.currentTimeMillis()

            val task1 = withContext(Dispatchers.IO) {
                delay(2000)
                Log.e(TAG, "1.执行task1.... [当前线程为：${Thread.currentThread().name}]")
                "one"  //返回结果赋值给task1
            }

            val task2 = withContext(Dispatchers.IO) {
                delay(1000)
                Log.e(TAG, "2.执行task2.... [当前线程为：${Thread.currentThread().name}]")
                "two"  //返回结果赋值给task2
            }

            Log.e(
                TAG,
                "task1 = $task1  , task2 = $task2 , 耗时 ${System.currentTimeMillis() - time1} ms  [当前线程为：${Thread.currentThread().name}]"
            )
        }
    }

    fun threadV9() {
        CoroutineScope(Dispatchers.Main).launch {
            val time1 = System.currentTimeMillis()
            val task1 = async(Dispatchers.IO) {
                delay(2000)
                Log.e(TAG, "1.执行task1.... [当前线程为：${Thread.currentThread().name}]")
                "one"  //返回结果赋值给task1
            }

            val task2 = async(Dispatchers.IO) {
                delay(1000)
                Log.e(TAG, "2.执行task2.... [当前线程为：${Thread.currentThread().name}]")
                "two"  //返回结果赋值给task2
            }

            Log.e(
                TAG,
                "task1 = ${task1.await()}  , task2 = ${task2.await()} , 耗时 ${System.currentTimeMillis() - time1} ms  [当前线程为：${Thread.currentThread().name}]"
            )
        }
    }

    fun threadV7() {
        // 使用协程
        runBlocking {
            Log.e(TAG, "threadV7: start")
            // 挂起当前上下文而非阻塞1000ms
            delay(1000L)
            Log.e(TAG, "threadV7: end")
        }
        // 验证阻塞结果
        Log.e(TAG, "threadV7: result [当前线程为：${Thread.currentThread().name}]")
    }

    fun threadV6() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.e(TAG, "threadV6: start")
            // 挂起当前上下文而非阻塞1000ms
            delay(1000L)
            Log.e(TAG, "threadV6: end")
            Log.e("TAG", " [当前线程为：${Thread.currentThread().name}]")
        }
        Log.e("TAG", "threadV6: result [当前线程为：${Thread.currentThread().name}]")
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

    fun main() = runBlocking<Unit> { // 开始执行主协程
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
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