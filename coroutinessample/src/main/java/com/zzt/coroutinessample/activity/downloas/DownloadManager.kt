package com.zzt.coroutinessample.activity.downloas

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import java.io.IOException


/**
 * @author: zeting
 * @date: 2021/11/12
 *
 */

//object DownloadManager {
//   public fun download(url: String, file: File): Flow<DownloadStatus> {
//        return flow {
//            val request = Request.Builder().url(url).get().build();
//            val response = OkHttpClient.Builder().build().newCall(request).execute()
//            if (response.isSuccessful) {
//                response.body()!!.let { body ->
//                    //文件大小
//                    val totalLength = body.contentLength().toDouble()
//                    //写文件
//                    file.outputStream().run {
//                        val input = body.byteStream()
//                        input.copyTo(this) { currentLength ->
//                            //当前下载进度
//                            val process = currentLength / totalLength * 100
//                            emit(DownloadStatus.Progress(process.toInt()))
//                        }
//                    }
//
//                    emit(DownloadStatus.Done(file))
//                }
//            } else {
//                throw IOException(response.toString())
//            }
//        }.catch {
//            file.delete()
//            emit(DownloadStatus.Err(it))
//        }.flowOn(Dispatchers.IO)
//    }
//}