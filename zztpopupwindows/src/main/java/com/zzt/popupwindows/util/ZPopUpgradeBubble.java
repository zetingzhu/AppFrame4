package com.zzt.popupwindows.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.zzt.popupwindows.R;
import com.zzt.popupwindows.library.ZNormalPopup;


/**
 * @author: zeting
 * @date: 2021/7/9
 * 自定义的升级气泡,黄色那种圆角升级
 */
public class ZPopUpgradeBubble extends ZNormalPopup {

    public ZPopUpgradeBubble(Context context, String content) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initView(context, content);
    }

    public void initView(Context context, String content) {
        TextView addView = new TextView(context);
        int topPadding = context.getResources().getDimensionPixelOffset(R.dimen.margin_5dp);
        int leftPadding = context.getResources().getDimensionPixelOffset(R.dimen.margin_12dp);

        addView.setPadding(leftPadding, topPadding, leftPadding, topPadding);
        if (content != null && !content.isEmpty()) {
            addView.setText(content);
        } else {
            addView.setText("改版");
        }
        addView.setTextSize(12F);
        addView.setTextColor(Color.parseColor("#ffffff"));
        addView.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.icon_lamp_bulb), null, null, null);
        addView.setCompoundDrawablePadding(context.getResources().getDimensionPixelOffset(R.dimen.margin_4dp));
        view(addView);
        // 渐变颜色
        int[] arrayOf = new int[]{Color.parseColor("#FFD333"),
                Color.parseColor("#FF8702")};
        bgColor(Color.parseColor("#FFB01C"));
        bgColorGradient(arrayOf);
        borderColor(Color.parseColor("#FF8802"));
        borderWidth(2);
        radius(context.getResources().getDimensionPixelOffset(R.dimen.margin_16dp));
        arrow(true);
        arrowSize(context.getResources().getDimensionPixelOffset(R.dimen.margin_10dp),
                context.getResources().getDimensionPixelOffset(R.dimen.margin_6dp));
        edgeProtection(context.getResources().getDimensionPixelOffset(R.dimen.margin_16dp));
    }
}
