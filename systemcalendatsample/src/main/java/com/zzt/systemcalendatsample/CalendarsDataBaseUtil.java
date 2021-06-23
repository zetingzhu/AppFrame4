package com.zzt.systemcalendatsample;

import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author: zeting
 * @date: 2020/6/30
 * 日历数据库操作工具类
 */
public class CalendarsDataBaseUtil {
    private static final String TAG = CalendarsDataBaseUtil.class.getSimpleName();
    // 固定日历关联id为1，添加数据到默认用户下
    public Context mContext;

    public CalendarsDataBaseUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 批量插入事件
     */
    public void addAllListEvent(List<RemindersObj> remindersObjList) {
        try {
            CalendarsResolver calendar = new CalendarsResolver(mContext.getContentResolver());
            long calendarId = calendar.queryCalendarsId();
            if (calendarId < 0) {
                Log.d(TAG, "日历没有用户，不能保存数据");
                return;
            }
            // 批量保存的事件
            List<Map<String, Object>> mListMap = new ArrayList<>();
            for (RemindersObj remindersObj : remindersObjList) {
                mListMap.add(obtainListEvents(String.valueOf(calendarId), remindersObj));
            }

            Map<String, Object> addUriEvents = calendar.insertEvents(mListMap);
            Log.d(TAG, "日历信息 - 批量插入日历结果:" + addUriEvents.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "日历信息 - 批量插入日历结果:" + e.getMessage());
        }
    }

    /**
     * 添加财经日历提醒
     *
     * @param date
     */
    public void addCalRemEvent(SaveCalRemObj date) {
        try {
            CalendarsResolver calendar = new CalendarsResolver(mContext.getContentResolver());
            long calendarId = calendar.queryCalendarsId();
            if (calendarId < 0) {
                Log.d(TAG, "日历没有用户，不能保存数据");
                return;
            }

            // 批量保存的事件
            List<Map<String, Object>> mListMap = new ArrayList<>();
            mListMap.add(obtainListEvents(String.valueOf(calendarId), date));

            Map<String, Object> addUriEvents = calendar.insertEvents(mListMap);
            Log.d(TAG, "日历信息 - 批量插入日历结果:" + addUriEvents.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "日历信息 - 批量插入日历结果:" + e.getMessage());
        }
    }

    /**
     * 创建保存的日历数据
     * 两个时间，提醒时间+发送时间 发送时间在后 ， 闹钟只有一个，就是提醒时间的附近时间响。时间段是提醒时间->发送时间的时间段
     *
     * @return
     */
    private Map<String, Object> obtainListEvents(String calendarId, RemindersObj remindersObjList) {
        // 添加事件
        String startData = DateUtil.formatYMDHMS(mContext, remindersObjList.getReminderDate());
        String endData = DateUtil.formatYMDHMS(mContext, remindersObjList.getSendDate());
        Map<String, Object> eventMap = obtainEvent(calendarId, startData, endData, remindersObjList.getTitle(), remindersObjList.getContent());
        // 批量提醒集合
        List<Map<String, String>> eventsReminder = new ArrayList<>();
        // 在默认添加一个准时提醒
        Map<String, String> reminderMap2 = obtainReminder(null, 0);
        eventsReminder.add(reminderMap2);
        eventMap.put("reminders", eventsReminder);
        return eventMap;
    }

    private Map<String, Object> obtainListEvents(String calendarId, SaveCalRemObj remindersObjList) {
        Log.d(TAG, "日历信息 - 添加事件日历calendarId:" + calendarId);
        // 添加事件
        String startData = DateUtil.formatYMDHMS(mContext, remindersObjList.getStartDate());
        String endData = DateUtil.formatYMDHMS(mContext, remindersObjList.getEndDate());
        Map<String, Object> eventMap = obtainEvent(calendarId, startData, endData, remindersObjList.getTitle(), remindersObjList.getDescription());
        // 批量提醒集合
        List<Map<String, String>> eventsReminder = new ArrayList<>();
        // 在默认添加一个准时提醒
        Map<String, String> reminderMap2 = obtainReminder(null, remindersObjList.getRemindersMinutes());
        eventsReminder.add(reminderMap2);
        eventMap.put("reminders", eventsReminder);
        return eventMap;
    }


    /**
     * 添加提醒事件
     *
     * @return
     */
    private Map<String, String> obtainReminder(String eventId, int minutes) {
        Map<String, String> reminderMap = new HashMap<>();
        if (StringUtil.isNotNull(eventId)) {
            reminderMap.put(CalendarContract.Reminders.EVENT_ID, eventId);
        }
        reminderMap.put(CalendarContract.Reminders.MINUTES, String.valueOf(minutes));
        reminderMap.put(CalendarContract.Reminders.METHOD, "1");
        return reminderMap;
    }

    /**
     * 组成基本的日历信息
     *
     * @return
     */
    private Map<String, Object> obtainEvent(String calendarId, String startDate, String endDate, String title, String description) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        eventMap.put(CalendarContract.Events.DTSTART, startDate);//开始时间:格式=yyyy-MM-dd HH:mm:ss
        eventMap.put(CalendarContract.Events.DTEND, endDate);//结束时间:格式=yyyy-MM-dd HH:mm:ss
        eventMap.put(CalendarContract.Events.TITLE, title);
        eventMap.put(CalendarContract.Events.DESCRIPTION, description);
        return eventMap;
    }

    public void addEvent() {
        try {
            Random random = new Random();
            CalendarsResolver calendar = new CalendarsResolver(mContext.getContentResolver());
            // 添加事件
            Map<String, Object> eventMap = obtainEvent("2", "2020-07-02 08:08:08", "2020-07-02 20:08:08", "这个是标题:" + random.nextInt(100), "这个是内容:" + random.nextInt(100));
            Uri addUriEvent = calendar.insertEvent(eventMap);
            Log.d(TAG, "日历信息 - 插入日历结果:" + addUriEvent.getLastPathSegment() + "\n" + addUriEvent.toString());

            // 添加提醒
            Map<String, String> reminderMap = obtainReminder(addUriEvent.getLastPathSegment(), random.nextInt(100));
            Uri addUriReminder = calendar.insertReminder(reminderMap);
            Log.d(TAG, "日历信息 - 插入提醒结果:" + addUriReminder.getLastPathSegment() + "\n" + addUriReminder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "日历信息 - 插入日历结果:" + e.getMessage());
        }
    }

    /**
     * 查询已经保存的日程信息
     */
    public void queryCalendar() {
        CalendarsResolver calendar = new CalendarsResolver(mContext.getContentResolver());
        long calendarId = calendar.queryCalendarsId();
        if (calendarId < 0) {
            Log.d(TAG, "日历没有用户，不能保存数据");
            return;
        }
        // 查询日程
        Map<String, String> param = new HashMap<>();
        param.put(CalendarContract.Events.CALENDAR_ID, "" + calendarId);
//        param.put(CalendarContract.Events.TITLE, "英国核心CPI");
        List<Map<String, Object>> eventsList = calendar.queryEvents(param);
        Gson gson = new Gson();
        Log.w(TAG, "日历信息 - 查询日程信息:" + gson.toJson(eventsList));


//        for (Map<String, Object> stringObjectMap : eventsList) {
//            List<Map<String, String>> reminders = (List<Map<String, String>>) stringObjectMap.get("reminders");
//            for (Map<String, String> reminder : reminders) {
//                String rmId = reminder.get("id");
//                Map<String, String> remPparam = new HashMap<>();
//                remPparam.put(CalendarContract.Reminders._ID, "" + rmId);
//                remPparam.put(CalendarContract.Reminders.MINUTES, "9");
//                int i = calendar.updateReminder(remPparam);
//                Log.w(TAG, "日历信息 - 修改提醒:" + i);
//            }
//        }
    }

    /**
     * 删除所有日历提醒
     */
    public void deleteCalRemEvent() {
        CalendarsResolver calendar = new CalendarsResolver(mContext.getContentResolver());
        long calendarId = calendar.queryCalendarsId();
        if (calendarId < 0) {
            Log.d(TAG, "日历没有用户，不能保存数据");
            return;
        }
        // 查询日程
        Map<String, String> param = new HashMap<>();
        param.put(CalendarContract.Events.CALENDAR_ID, "" + calendarId);
        List<Map<String, Object>> eventsList = calendar.queryEvents(param);
        List<String> eventIds = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : eventsList) {
//            List<Map<String, String>> reminders = (List<Map<String, String>>) stringObjectMap.get("reminders");
//            for (Map<String, String> reminder : reminders) {
//                String rmId = reminder.get("id");
//                Map<String, String> remPparam = new HashMap<>();
//                remPparam.put(CalendarContract.Reminders._ID, "" + rmId);
//                int i = calendar.deleteReminder(remPparam);
//                Log.w(TAG, "日历信息 - 删除提醒:" + i);
//            }
            String eventId = (String) stringObjectMap.get("id");
            if (eventId != null && !eventId.isEmpty()) {
                eventIds.add(eventId);
            }
        }

//        for (int i = 0; i < eventsList.size(); i++) {
//            Map<String, Object> stringObjectMap = eventsList.get(i);
//            String evId = (String) stringObjectMap.get("id");
//            int c = calendar.deleteEvent(evId, calendarId + "");
//            Log.w(TAG, "日历信息 - 删除日历:" + c);
//        }

        Map<String, String> map = calendar.delEvents(eventIds, calendarId + "", true);
        Log.w(TAG, "日历信息 - 删除日历:" + map);
    }


    /**
     * 修改本地日历提醒
     *
     * @param date
     */
    public void updateCalRemEvent(SaveCalRemObj date) {
        CalendarsResolver calendar = new CalendarsResolver(mContext.getContentResolver());
        long calendarId = calendar.queryCalendarsId();
        if (calendarId < 0) {
            Log.d(TAG, "日历没有用户，不能保存数据");
            return;
        }
        // 查询日程
        Map<String, String> param = new HashMap<>();
        param.put(CalendarContract.Events.CALENDAR_ID, "" + calendarId);
        param.put(CalendarContract.Events.TITLE, date.getTitle());
        List<Map<String, Object>> eventsList = calendar.queryEvents(param);
        for (Map<String, Object> stringObjectMap : eventsList) {
            List<Map<String, String>> reminders = (List<Map<String, String>>) stringObjectMap.get("reminders");
            if (reminders.size() > 0) {
                Map<String, String> reminder = reminders.get(0);
                String rmId = reminder.get("id");
                Map<String, String> remPparam = new HashMap<>();
                remPparam.put(CalendarContract.Reminders._ID, "" + rmId);
                remPparam.put(CalendarContract.Reminders.MINUTES, "" + date.getRemindersMinutes());
                int i = calendar.updateReminder(remPparam);
                Log.w(TAG, "日历信息 - 修改提醒:" + i);
            }
        }
    }

    /**
     * 删除日历提醒
     *
     * @param date
     */
    public void deleteCalRemEvent(SaveCalRemObj date) {
        CalendarsResolver calendar = new CalendarsResolver(mContext.getContentResolver());
        long calendarId = calendar.queryCalendarsId();
        if (calendarId < 0) {
            Log.d(TAG, "日历没有用户，不能保存数据");
            return;
        }
        // 查询日程
        Map<String, String> param = new HashMap<>();
        param.put(CalendarContract.Events.CALENDAR_ID, "" + calendarId);
        param.put(CalendarContract.Events.TITLE, date.getTitle());
        List<Map<String, Object>> eventsList = calendar.queryEvents(param);
        List<String> eventIds = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : eventsList) {
            List<Map<String, String>> reminders = (List<Map<String, String>>) stringObjectMap.get("reminders");
            for (Map<String, String> reminder : reminders) {
                String rmId = reminder.get("id");
                Map<String, String> remPparam = new HashMap<>();
                remPparam.put(CalendarContract.Reminders._ID, "" + rmId);
                int i = calendar.deleteReminder(remPparam);
                Log.w(TAG, "日历信息 - 删除提醒:" + i);
            }
            String eventId = (String) stringObjectMap.get("id");
            if (eventId != null && !eventId.isEmpty()) {
                eventIds.add(eventId);
            }
        }
        if (!eventIds.isEmpty()) {
            int i = calendar.deleteEvent(eventIds);
            Log.w(TAG, "日历信息 - 删除日历:" + i);
        }
    }

}
