//package com.zzt.coroutinessample.net
//
//import android.util.Log
//import okhttp3.logging.HttpLoggingInterceptor.setLevel
//import okhttp3.OkHttpClient.Builder.addInterceptor
//import okhttp3.OkHttpClient.Builder.addNetworkInterceptor
//import okhttp3.OkHttpClient.Builder.connectTimeout
//import okhttp3.OkHttpClient.Builder.readTimeout
//import okhttp3.OkHttpClient.Builder.build
//import retrofit2.Retrofit
//import com.zzt.coroutinessample.net.ApiRetrofitKotlinUtil
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import com.facebook.stetho.okhttp3.StethoInterceptor
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
//import com.zzt.coroutinessample.net.factory.LiveDataCallAdapterFactory
//import java.lang.Exception
//import java.util.concurrent.TimeUnit
//
///**
// * @author: zeting
// * @date: 2020/12/7
// * Retrofit 工具封装
// */
//class ApiRetrofitKotlinUtil {
//    fun <T> getApiService(ShowUrl: String?, service: Class<T>?): T {
//        val retrofit = getRetrofit(ShowUrl)
//        return retrofit.create(service)
//    }
//
//    companion object {
//        private val TAG = ApiRetrofitKotlinUtil::class.java.simpleName
//        private var apiRetrofit: ApiRetrofitKotlinUtil? = null
//        private var client: OkHttpClient? = null
//        private var retrofit: Retrofit? = null
//        val instance: ApiRetrofitKotlinUtil?
//            get() {
//                if (apiRetrofit == null) {
//                    synchronized(Any::class.java) {
//                        if (apiRetrofit == null) {
//                            apiRetrofit = ApiRetrofitKotlinUtil()
//                        }
//                    }
//                }
//                return apiRetrofit
//            }//添加log拦截器
//
//        @get:Synchronized
//        private val okHttpClient:
//
//        //包含header、body数据
//                OkHttpClient
//            private get() {
//
//                val interceptor = HttpLoggingInterceptor(object : Logger() {
//                    override fun log(message: String) {
//                        try {
//                            Log.i(TAG, message)
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                            Log.e(TAG, message)
//                        }
//                    }
//                })
//
//                //包含header、body数据
//                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//                return Builder() //添加log拦截器
//                    .addInterceptor(interceptor) // FaceBook 网络调试器，可在Chrome调试网络请求，查看SharePreferences,数据库等
//                    .addNetworkInterceptor(StethoInterceptor())
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .readTimeout(10, TimeUnit.SECONDS)
//                    .build().also { client = it }
//            }
//
//        @Synchronized
//        fun getRetrofit(baseUrl: String?): Retrofit {
//
//
//
//            return Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(LiveDataCallAdapterFactory())
//                .client(okHttpClient)
//                .build().also { retrofit = it }
//        }
//    }
//}