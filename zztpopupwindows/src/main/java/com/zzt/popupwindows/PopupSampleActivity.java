package com.zzt.popupwindows;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PopupSampleActivity extends AppCompatActivity {

    TextView tv_text1, tv_text2, tv_text3, tv_text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_sample);
        initView();
    }

    int padding = 50;
    ZNormalPopup zNormalPopup;

    public void initView() {

        zNormalPopup = new ZNormalPopup(
                this,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        tv_text1 = findViewById(R.id.tv_text1);
        tv_text2 = findViewById(R.id.tv_text2);
        tv_text3 = findViewById(R.id.tv_text3);
        tv_text4 = findViewById(R.id.tv_text4);
        tv_text1.setOnClickListener(v -> {

            //region 这个可以的
            TextView textView = new TextView(PopupSampleActivity.this);


            textView.setPadding(padding, padding, padding, padding);
            textView.setText("通过 dimAmount() 设置背景遮罩");
            textView.setTextColor(
                    getResources().getColor(R.color.salmon)
            );

            zNormalPopup.preferredDirection(ZNormalPopup.DIRECTION_BOTTOM)
                    .view(textView)
                    .bgColor(getResources().getColor(R.color.qmui_config_color_white))
                    .borderColor(getResources().getColor(R.color.magenta))
                    .borderWidth(5)
                    .radius(10)
                    .arrow(true)
                    .arrowSize(30, 30)
                    .offsetYIfBottom(10)
                    .edgeProtection(20)
                    .dimAmount(0.6f)

                    .show(v);
            //endregion

        });
        tv_text2.setOnClickListener(v -> {
            //region 这个可以的
            TextView textView = new TextView(PopupSampleActivity.this);


            textView.setPadding(padding, padding, padding, padding);
            textView.setText("通过 dimAmount() 设置背景遮罩");
            textView.setTextColor(
                    getResources().getColor(R.color.salmon)
            );

            zNormalPopup.preferredDirection(ZNormalPopup.DIRECTION_TOP)
                    .view(textView)
                    .bgColor(getResources().getColor(R.color.qmui_config_color_white))
                    .borderColor(getResources().getColor(R.color.magenta))
                    .borderWidth(5)
                    .radius(10)
                    .arrow(true)
                    .arrowSize(30, 30)
                    .offsetYIfBottom(10)
                    .dimAmount(0.6f)

                    .show(v);
            //endregion
        });
        tv_text3.setOnClickListener(v -> {

            //region 这个可以的
            TextView textView = new TextView(PopupSampleActivity.this);


            textView.setPadding(padding, padding, padding, padding);
            textView.setText("通过 dimAmount() 设置背景遮罩");
            textView.setTextColor(
                    getResources().getColor(R.color.salmon)
            );

            zNormalPopup.preferredDirection(ZNormalPopup.DIRECTION_CENTER_IN_SCREEN)
                    .view(textView)
                    .bgColor(getResources().getColor(R.color.qmui_config_color_white))
                    .borderColor(getResources().getColor(R.color.magenta))
                    .borderWidth(5)
                    .radius(10)
                    .dimAmount(0.6f)
                    .onDismiss(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                        }
                    })
                    .show(v);
            //endregion
        });
    }
}