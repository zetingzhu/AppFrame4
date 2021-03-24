package com.zzt.pluginapp;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class PlugInActivity extends BaseActivity {

    @Override
    public void attach(Activity proxyActivity) {
        super.attach(proxyActivity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug_in);

        findViewById(R.id.mBtnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(that, SecondActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.mRegiestBroadCast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intent = new IntentFilter();
                intent.addAction("com.plugin.app.receive");
                //调用register方法 肯定要调用宿主的 所以重写baseactivity
                registerReceiver(new MyReceive(), intent);
            }
        });

        findViewById(R.id.mSendBroadCast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.plugin.app.receive");
                sendBroadcast(intent);
            }
        });
    }
}