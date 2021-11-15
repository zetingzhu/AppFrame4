package com.zzt.coroutinessample.activity.downloas

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author: zeting
 * @date: 2021/11/12
 *
 */


//class DownloadViewModel(val context: Application) : AndroidViewModel(context) {
//    private var progressData = MutableLiveData<Int>()
//    val progress = progressData
//
//    private val url: String = "http://10.254.219.178:8080/test.rar"
//
//    fun downloadClick(v: View) {
//        viewModelScope.launch {
//            progressData.value = 0
//            val file = File(context.getExternalFilesDir(null), "test.rar")
//            DownloadManager.download(url, file).collect {
//                when (it) {
//                    is DownloadStatus.Progress -> {
//                        Log.i("progress", "progress: $it.progress")
//                        progressData.value = it.progress
//                    }
//                    is DownloadStatus.Done -> {
//                        progressData.value = 100
//                        Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show()
//                    }
//                    is DownloadStatus.Err ->
//                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//}