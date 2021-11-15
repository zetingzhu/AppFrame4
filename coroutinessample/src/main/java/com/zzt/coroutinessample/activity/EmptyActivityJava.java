package com.zzt.coroutinessample.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zzt.coroutinessample.R;
import com.zzt.coroutinessample.util.LoopCoroutineVM;

public class EmptyActivityJava extends AppCompatActivity {

    Button btn_start;

    LoopCoroutineVM loopVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        initView();
    }

    private void initView() {
        loopVM = new ViewModelProvider(this).get(LoopCoroutineVM.class);
        loopVM.startLoop(4000, new LoopCoroutineVM.LoopListener() {
            @Override
            public void onPostExecute(@Nullable Object any) {

            }

            @Nullable
            @Override
            public Object doInBackground() {
                return null;
            }
        });

        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}