package com.zzt.appframe4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class ActivityV3 extends AppCompatActivity {
    private static final String TAG = ActivityV3.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v3);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String abc = extras.getString("abc");
            Log.e(TAG, "这个获取信息 Bundle：" + abc);

        }

        String abc1 = getIntent().getStringExtra("abc");
        Log.e(TAG, "这个获取信息 getStringExtra：" + abc1);
    }
}