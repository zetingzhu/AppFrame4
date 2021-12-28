package com.zzt.coroutinessample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zzt.coroutinessample.R;
import com.zzt.coroutinessample.util.LoopCoroutineUtil;
import com.zzt.coroutinessample.util.LoopCoroutineVM;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class EmptyActivityJava extends AppCompatActivity {
    private static final String TAG = EmptyActivityJava.class.getSimpleName();
    Button btn_start, btn_stop, btn_start2, btn_stop2, btn_stop3;

    LoopCoroutineVM loopVM;

    LoopCoroutineUtil loopCoroutineUtil;
    LoopCoroutineUtil loopV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        initView();
    }

    private void initView() {
//        loopVM = new ViewModelProvider(this).get(LoopCoroutineVM.class);
//        loopVM.startLoop(4000, new LoopCoroutineVM.LoopListener() {
//            @Override
//            public void onPostExecute(@Nullable Object any) {
//
//            }
//
//            @Nullable
//            @Override
//            public Object doInBackground() {
//                return null;
//            }
//        });

        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopCoroutineUtil = new LoopCoroutineUtil();
                loopCoroutineUtil.startLoopJava(2000, new Function0<Object>() {
                    @Override
                    public Object invoke() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "第一个按钮循环：" + System.currentTimeMillis();
                    }
                }, new Function1<Object, Unit>() {
                    @Override
                    public Unit invoke(Object o) {
                        Log.w(TAG, ">> 一 " + o);
                        return null;
                    }
                }, getLifecycle());
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopCoroutineUtil.cancelLoopJava();
            }
        });
        btn_start2 = findViewById(R.id.btn_start2);
        btn_stop2 = findViewById(R.id.btn_stop2);
        btn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopV2 = new LoopCoroutineUtil();
                loopV2.startLoopJava(3000, new Function0<Object>() {
                    @Override
                    public Object invoke() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "第二个按钮循环：" + System.currentTimeMillis();
                    }
                }, new Function1<Object, Unit>() {
                    @Override
                    public Unit invoke(Object o) {
                        Log.w(TAG, ">>  二  " + o);
                        return null;
                    }
                }, getLifecycle());
            }
        });
        btn_stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopV2.cancelLoopJava();
            }
        });
        btn_stop3 = findViewById(R.id.btn_stop3);
        btn_stop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}