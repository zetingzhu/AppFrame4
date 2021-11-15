package com.zzt.coroutinessample.net.serviceapi

import androidx.lifecycle.LiveData
import com.zzt.coroutinessample.modle.MyWeather
import com.zzt.coroutinessample.net.ApiRetrofitUtils
import com.zzt.coroutinessample.net.factory.ApiResponse
import retrofit2.http.*
import retrofit2.Call

/**
 * @author: zeting
 * @date: 2020/12/7
 */
interface WeatherApi {
    companion object {
        const val BASE_SERVER_URL_WEATHER = "https://search.heweather.com/"
        fun getApi(): WeatherApi {
            return ApiRetrofitUtils.getInstance()
                .getApiService(BASE_SERVER_URL_WEATHER, WeatherApi::class.java)
        }
    }

    /**
     * 天气预报
    https://search.heweather.com/find?location=北京&key=4b61a68895b149f1a5ea53fe43782e17
     */
    @GET("find?key=4b61a68895b149f1a5ea53fe43782e17")
    fun getWeatherByAddress(@Query("location") location: String): LiveData<ApiResponse<MyWeather>>


    @GET("find?key=4b61a68895b149f1a5ea53fe43782e17")
    fun getWeatherByAddressCall(@Query("location") location: String): Call<MyWeather>

}