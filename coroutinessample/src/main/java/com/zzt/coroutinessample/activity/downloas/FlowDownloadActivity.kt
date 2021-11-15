package com.zzt.coroutinessample.activity.downloas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zzt.coroutinessample.R
import com.zzt.coroutinessample.databinding.ActivityFlowDownloadBinding

class FlowDownloadActivity : AppCompatActivity() {
    lateinit var bindin: ActivityFlowDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindin = ActivityFlowDownloadBinding.inflate(layoutInflater)
        setContentView(bindin.root)


//        val downloadViewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory(application)
//        ).get(DownloadViewModel::class.java)
//        fragmentFlowDownBinding.downloadViewModel = downloadViewModel
//        fragmentFlowDownBinding.lifecycleOwner = this;


    }
}