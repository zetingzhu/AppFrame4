package com.zzt.staggeredgridsample;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

/**
 * @author: zeting
 * @date: 2021/5/6
 */
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyViewHolder> {

    List<ViewEntity> mList;

    public ViewAdapter(List<ViewEntity> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = new TextView(parent.getContext());
        switch (viewType) {
            case 0:
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                view.setHeight(300);
                break;
            case 1:
            case 11:
                view.setHeight(150);
                break;
            case 4:
                view.setHeight(600);
                break;
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView tv = (TextView) holder.itemView;
        int itemViewType = holder.getItemViewType();
        if (itemViewType == 0 || itemViewType == 3 ||
                itemViewType == 5 || itemViewType == 7 ||
                itemViewType == 8 || itemViewType == 9) {
            tv.setText(mList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull TextView itemView) {
            super(itemView);
            itemView.setBackgroundColor(randomColor());
        }
    }

    public int randomColor() {
        Random random = new Random();
        return Color.argb(128, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

}
