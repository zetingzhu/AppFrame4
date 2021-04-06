package com.zzt.nested.samplev1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.NestedScrollingParent3;
import androidx.recyclerview.widget.RecyclerView;

import com.zzt.nested.R;

public class NestedActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    NestedScrollingParent3 nestedScrollingParent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_v3);
    }

}