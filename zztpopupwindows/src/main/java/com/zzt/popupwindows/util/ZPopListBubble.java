package com.zzt.popupwindows.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zzt.popupwindows.R;
import com.zzt.popupwindows.library.ZDirection;
import com.zzt.popupwindows.library.ZNormalPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zeting
 * @date: 2021/11/19
 * 带有一个list 礼包的气泡
 */
public class ZPopListBubble extends ZNormalPopup {
    // 展示内容
    private List<String> listContent = new ArrayList<>();
    // 默认选中
    private int selectPos = 0;
    // 列表背景色
    private @DrawableRes
    int textBG = 0;
    // 列表颜色
    private int textColor = Color.parseColor("#252C58");
    // 列表字体大小
    private int textSize = 14;
    // 设置距离边缘
    private int padding = 20;
    // 监听
    private OnItemClickListener itemClickListener;

    public ZPopListBubble(Context context, List<String> list, int selectPos, @DrawableRes int textBG, int textColor, int textSize, int padding, OnItemClickListener listener) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.listContent = list;
        this.selectPos = selectPos;
        this.textBG = textBG;
        this.textColor = textColor;
        this.textSize = textSize;
        this.padding = padding;
        this.itemClickListener = listener;
        initView(context, list, ZDirection.DIRECTION_BOTTOM);
    }

    public void initView(Context context, List<String> list, @ZDirection int preferredDirection) {
        RecyclerView recyclerView = new RecyclerView(context);

        // 设置列表默认为线性列表
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        MyTextListAdapter textListAdapter = new MyTextListAdapter();
        recyclerView.setAdapter(textListAdapter);

        view(recyclerView);
        bgColor(Color.parseColor("#b2172346"));
        radius(context.getResources().getDimensionPixelOffset(R.dimen.margin_3dp));
        arrow(false);
        arrowSize(context.getResources().getDimensionPixelOffset(R.dimen.margin_10dp), context.getResources().getDimensionPixelOffset(R.dimen.margin_8dp));
        edgeProtection(context.getResources().getDimensionPixelOffset(R.dimen.margin_16dp));
        preferredDirection(preferredDirection);
    }

    class MyTextListAdapter extends RecyclerView.Adapter<MyTextViewHolder> {
        @NonNull
        @Override
        public MyTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setId(android.R.id.text1);
            textView.setTextColor(textColor);
            if (textBG == 0) {
                textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                textView.setBackgroundResource(textBG);
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(padding, padding, padding, padding);
            return new MyTextViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyTextViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();
            String value = listContent.get(position);
            holder.itemText.setText(value);
            if (selectPos == position) {
                holder.itemText.setSelected(true);
            }
            holder.itemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(position, value);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listContent.size();
        }
    }

    class MyTextViewHolder extends RecyclerView.ViewHolder {
        // textview Id
        int resId = android.R.id.text1;
        // 展示的文本
        TextView itemText;

        public void setResId(int resId) {
            itemText = itemView.findViewById(resId);
        }

        public MyTextViewHolder(@NonNull View itemView) {
            super(itemView);
            setResId(resId);
        }
    }

    /**
     * 设置item监听
     */
    public interface OnItemClickListener {
        void onItemClick(int position, String value);
    }
}
