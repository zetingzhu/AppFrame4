package com.zzt.coroutinessample.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.coroutinessample.databinding.ActivityFlowBinding
import com.zzt.coroutinessample.modle.MyWeather
import com.zzt.coroutinessample.net.serviceapi.WeatherApi
import com.zzt.entity.StartActivityDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.measureTimeMillis

/**
 * Flow 使用
 */
class FlowActivity : AppCompatActivity() {
    val TAG = FlowActivity::class.java.simpleName
    lateinit var binding: ActivityFlowBinding
    private var daoList = mutableListOf<StartActivityDao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData() {
        daoList.add(StartActivityDao("Flow 发送数据流", "", "1"))
        daoList.add(StartActivityDao("Flow 捕获异常", "", "2"))
        daoList.add(StartActivityDao("Flow 切换线程", "", "3"))
        daoList.add(StartActivityDao("天气网络请求LiveData", "", "4"))
        daoList.add(StartActivityDao("天气网络请求Call", "", "5"))
        daoList.add(StartActivityDao("Flow 和 Sequence", "", "6"))
        daoList.add(
            StartActivityDao(
                "Flow 背压 buffer",
                "没有固定大小，可以无限制添加数据，不会抛出 MissingBackpressureException 异常，但会导致 OOM",
                "7"
            )
        )
        daoList.add(StartActivityDao("Flow 背压 conflate", "如果缓存池满了，会丢掉将要放入缓存池中的数据", "8"))
        daoList.add(StartActivityDao("Flow retry 操作符", "重试操作符", "9"))
        daoList.add(StartActivityDao("Flow retryWhen 操作符", "重试", "10"))
        daoList.add(StartActivityDao("Flow retryWhen 操作符", "重试", "11"))
        daoList.add(StartActivityDao("Flow map 操作符", "变换操作符", "12"))

        StartActivityRecyclerAdapter.setAdapterData(
            binding.rvList,
            RecyclerView.VERTICAL,
            daoList
        ) { itemView, position, data ->
            if (data is StartActivityDao) {
                when (data.arouter) {
                    "1" -> flowTest1()
                    "2" -> flowTest2()
                    "3" -> flowTest3()
                    "4" -> flowTest4()
                    "5" -> flowTest5()
                    "6" -> flowTest6()
                    "7" -> flowTest7()
                    "8" -> flowTest8()
                    "9" -> flowTest9()
                    "10" -> flowTest10()
                    "12" -> flowTest12()
                }
            }
        }
    }

    private fun flowTest12() {
        flow {
            List(5) {
                emit(it)

            }
        }.map {
            it * 2
        }.onStart {

        }
            .onEach {

            }
            .onCompletion {

            }

    }

    private fun flowTest10() {


    }

    private fun flowTest9() {
        lifecycleScope.launch {
            (1..5).asFlow().onEach {
                if (it == 3) throw RuntimeException("Error on $it")
            }.retry(2) {

                if (it is RuntimeException) {
                    return@retry true
                }
                false
            }
                .onEach { println("Emitting $it") }
                .catch { it.printStackTrace() }
                .collect()
        }
    }

    private fun flowTest8() {
        lifecycleScope.launch {
            val time = measureTimeMillis {
                (1..5)
                    .asFlow()
                    .onStart { start = currTime() }
                    .onEach {
                        delay(100)
                        println("Emit $it (${currTime() - start}ms) ")
                    }
                    .conflate()
                    .collect {
                        println("Collect $it starts (${currTime() - start}ms) ")
                        delay(500)
                        println("Collect $it ends (${currTime() - start}ms) ")
                    }
            }

            println("Cost $time ms")
        }
    }

    fun currTime() = System.currentTimeMillis()

    var start: Long = 0
    private fun flowTest7() {
        lifecycleScope.launch {
            val time = measureTimeMillis {
                (1..5)
                    .asFlow()
                    .onStart {
                        start = currTime()
                    }
                    .onEach {
                        delay(100)
                        println("Emit $it (${currTime() - start}ms) ")
                    }
                    .buffer()
                    .collect {
                        println("Collect $it starts (${currTime() - start}ms) ")
                        delay(500)
                        println("Collect $it ends (${currTime() - start}ms) ")
                    }
            }

            println("Cost $time ms")
        }

    }

    private fun flowTest6() {
        runBlocking {

            launch {
                for (k in 1..5) {
                    delay(100)
                    println("I'm blocked $k")
                }
            }

            sequence {
                for (i in 1..5) {
                    Thread.sleep(100)
                    yield(i)
                }
            }.forEach { println(it) }

            println("Done")
        }

    }

    private fun flowTest5() {
        val weatherByAddressCall = WeatherApi.getApi().getWeatherByAddressCall("上海")
        weatherByAddressCall.enqueue(object : Callback<MyWeather> {
            override fun onResponse(call: Call<MyWeather>, response: Response<MyWeather>) {
                val body = response.body()
                Log.d(TAG, ">>>>> Call  $body \n $call")
            }

            override fun onFailure(call: Call<MyWeather>, t: Throwable) {
                Log.d(TAG, ">>>>> Call  error:$t \n $call")
            }
        })
    }

    private fun flowTest4() {
        val weatherByAddress = WeatherApi.getApi().getWeatherByAddress("北京")
        weatherByAddress.observe(this, Observer {
            Log.d(TAG, ">>>>> $it ")
        })
    }

    private fun flowTest3() {
        lifecycleScope.launch {
            count().flowOn(Dispatchers.IO) // 指定数据流产生运行线程
                .map {
                    Log.w("Coroutine", "$it          --=  map on ${Thread.currentThread().name}")
                    if (it > 15) {
                        throw NumberFormatException()
                    }
                    "I am $it"
                }.flowOn(Dispatchers.IO)           // 指定map中间action运行线程
                .catch { ex ->
                    Log.e("Coroutine", "catch on ${Thread.currentThread().name}")
                    emit("error")
                }.collect {
                    Log.i("Coroutine", it + " - collect on ${Thread.currentThread().name}")
                    binding.tvResult.text = it
                }
        }
    }

    private fun count(): Flow<Int> = flow {
        var x = 0
        while (true) {
            if (x > 20) {
                break
            }
            delay(500)
            Log.d("Coroutine", "                         emit on ${Thread.currentThread().name}")
            emit(x)
            x = x.plus(1)
        }
    }

    private fun flowTest2() {
        lifecycleScope.launch {
            count()
                .map {
                    "this is $it"
                }.map {
                    " 这个地方在转一遍 $it "
                }.catch { ex ->
                    ex.printStackTrace()
                    Log.d("Coroutine", ex.toString())
                    emit("-1")
                }.collect {
                    Log.d("Coroutine", it)
                }
        }
    }

    private fun flowTest1() {
        lifecycleScope.launch {
            flow {
                for (i in 1..5) {
                    delay(100)
                    emit(i)
                }
            }.collect {
                println(it)
            }
        }
    }


}