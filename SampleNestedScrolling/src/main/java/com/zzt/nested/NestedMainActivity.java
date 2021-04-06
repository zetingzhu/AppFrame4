package com.zzt.nested;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.zzt.adapter.StartActivityRecyclerAdapter;
import com.zzt.entity.StartActivityDao;
import com.zzt.nested.samplev2.NestedScrolling2DemoActivity;

import java.util.ArrayList;
import java.util.List;

public class NestedMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_main);

        RecyclerView rv_main = findViewById(R.id.rv_main);
        List<StartActivityDao> mList = new ArrayList<>();
        mList.add(new StartActivityDao("Nested Scrooling 使用 1", "", R.layout.activity_nested_v1));
        mList.add(new StartActivityDao("Nested Scrooling 使用 2", "", R.layout.activity_nested_v2));
        mList.add(new StartActivityDao("Nested Scrooling 使用 3", "", R.layout.activity_nested_v3));
        mList.add(new StartActivityDao("Nested Scrooling 滑动渐变", "", NestedScrolling2DemoActivity.class));
        mList.add(new StartActivityDao("Nested Scrooling 使用 4", "这里集成了上下联动，上面头部悬浮，并且头部可以固定吸顶", R.layout.activity_nested_v4));
        mList.add(new StartActivityDao("Nested Scrooling 使用 5", "测试这个头部滑动什么意思", R.layout.activity_nested_v5));
        mList.add(new StartActivityDao("Nested Scrooling 使用 6", "测试这个头部滑动什么意思", R.layout.activity_nested_v6));
        mList.add(new StartActivityDao("Nested Scrooling 使用 7", "测试这个头部滑动什么意思", R.layout.activity_nested_v7));
        mList.add(new StartActivityDao("Nested Scrooling 使用 8", "测试这个头部滑动什么意思", R.layout.activity_nested_v8));

        StartActivityRecyclerAdapter.setAdapterData(rv_main, RecyclerView.VERTICAL, mList, (position, data) -> {

        });

    }
}