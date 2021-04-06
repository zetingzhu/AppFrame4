package com.zzt.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.necer.calendar.MonthCalendar;
import com.necer.calendar.WeekCalendar;
import com.necer.painter.CalendarBackground;
import com.necer.painter.WhiteBackground;

import org.joda.time.LocalDate;

public class CalendarViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        MonthCalendar monthCalendar = findViewById(R.id.mc);
        WhiteBackground mwb = new WhiteBackground();
        mwb.setDrawColor(Color.parseColor("#EBEBEB"));
        monthCalendar.setCalendarBackground(mwb);
        WeekCalendar weekCalendar = findViewById(R.id.wc);
        WhiteBackground wwb = new WhiteBackground();
        wwb.setDrawColor(Color.parseColor("#BDBDBD"));
        weekCalendar.setCalendarBackground(wwb);
    }
}