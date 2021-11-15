package com.zzt.lv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private CustomViewModel mViewModel;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mViewModel = ViewModelProviders.of(this).get(CustomViewModel.class);
        mViewModel = new ViewModelProvider(this).get(CustomViewModel.class);
        final MutableLiveData<Integer> liveData = mViewModel.getLiveData();
//        liveData.observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                Log.e(TAG, "参数返回： " + integer);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MutableLiveData<Integer> liveData = mViewModel.getLiveData();
                liveData.observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        Log.e(TAG, "参数返回： " + integer);
                    }
                });
            }
        }, 5000);
    }

}