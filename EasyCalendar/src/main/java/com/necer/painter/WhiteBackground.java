package com.necer.painter;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import org.joda.time.LocalDate;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by necer on 2020/3/27.
 */
public class WhiteBackground implements CalendarBackground {

    @ColorInt
    int drawColor = Color.WHITE;

    public void setDrawColor(@ColorInt int color) {
        this.drawColor = color;
    }


    @Override
    public Drawable getBackgroundDrawable(LocalDate localDate, int totalDistance, int currentDistance) {
        return new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                canvas.drawColor(drawColor);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @SuppressLint("WrongConstant")
            @Override
            public int getOpacity() {
                return 0;
            }
        };
    }
}
