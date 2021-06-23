package com.zzt.staggeredgridsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActivityStaggeredGridLayout extends AppCompatActivity {

    RecyclerView rv_staggered;
    ViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_grid_layout);
        rv_staggered = findViewById(R.id.rv_staggered);

        Random random = new Random();
        List<ViewEntity> mItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mItems.add(new ViewEntity("item:" + i, i % 12));
        }
        mAdapter = new ViewAdapter(mItems);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_staggered.setLayoutManager(manager);
        rv_staggered.setAdapter(mAdapter);
    }
}