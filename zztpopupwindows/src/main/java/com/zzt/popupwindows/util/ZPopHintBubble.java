package com.zzt.popupwindows.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.zzt.popupwindows.R;
import com.zzt.popupwindows.library.ZDirection;
import com.zzt.popupwindows.library.ZNormalPopup;


/**
 * @author: zeting
 * @date: 2021/8/9
 * 自定义的提示语气泡，黑色那种半透明长气泡
 */
public class ZPopHintBubble extends ZNormalPopup {
    /**
     * @param context
     * @param content            直接传入展示文案
     * @param preferredDirection
     */
    public ZPopHintBubble(Context context, String content, @ZDirection int preferredDirection) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initView(context, content, preferredDirection);
    }

    public void initView(Context context, String content, @ZDirection int preferredDirection) {
        TextView addView = new TextView(context);
        int topPadding = context.getResources().getDimensionPixelOffset(R.dimen.margin_12dp);
        int leftPadding = context.getResources().getDimensionPixelOffset(R.dimen.margin_8dp);

        addView.setPadding(leftPadding, topPadding, leftPadding, topPadding);
        if (content != null && !content.isEmpty()) {
            addView.setText(content);
        } else {
            addView.setText("改版");
        }
        addView.setTextSize(14F);
        addView.setTextColor(Color.parseColor("#ffffff"));
        view(addView);
        bgColor(Color.parseColor("#b2172346"));
        radius(context.getResources().getDimensionPixelOffset(R.dimen.margin_3dp));
        arrow(true);
        arrowSize(context.getResources().getDimensionPixelOffset(R.dimen.margin_10dp), context.getResources().getDimensionPixelOffset(R.dimen.margin_8dp));
        edgeProtection(context.getResources().getDimensionPixelOffset(R.dimen.margin_16dp));
        preferredDirection(preferredDirection);
    }

}
