package com.zzt.systemcalendatsample;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author: zeting
 * @date: 2020/6/29
 * https://developer.android.google.cn/guide/topics/providers/calendar-provider
 * 日历操作类
 * 使用以下Uri时，Android版本>=14;
 */
public class CalendarsResolver {
    private static final String TAG = CalendarsResolver.class.getSimpleName();
    private ContentResolver resolver;

    private Uri calendarsUri = Calendars.CONTENT_URI;
    private Uri eventsUri = Events.CONTENT_URI;
    private Uri remindersUri = Reminders.CONTENT_URI;

    // 日历账户
    private static String CALENDARS_ACCOUNT_NAME = "xtrend_speed";
    private static String CALENDARS_ACCOUNT_TYPE = "xtrend_speed_type";
    private static String CALENDARS_NAME = "xtrend_speed";
    private static String CALENDARS_DISPLAY_NAME = "xtrend_speed";

    /**
     * 事件表
     */
    public static final String[] EVENTS_COLUMNS = new String[]{
            Events._ID,
            Events.CALENDAR_ID,
            Events.TITLE,
            Events.DESCRIPTION,
            Events.EVENT_LOCATION,
            Events.DTSTART,
            Events.DTEND,
            Events.EVENT_TIMEZONE,
            Events.HAS_ALARM,
            Events.ALL_DAY,
            Events.AVAILABILITY,
            Events.ACCESS_LEVEL,
            Events.STATUS,
    };
    /**
     * 提醒表
     */
    public static final String[] REMINDERS_COLUMNS = new String[]{
            Reminders._ID,
            Reminders.EVENT_ID,
            Reminders.MINUTES,
            Reminders.METHOD,
    };

    /**
     * 构造方法
     *
     * @param resolver
     */
    public CalendarsResolver(ContentResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * 判断是否为空
     *
     * @param obj
     * @return
     */
    public boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof Map) {
            if (((Map) obj).isEmpty()) {
                return true;
            }
        } else if (obj instanceof String) {
            if (((String) obj).trim().length() == 0) {
                return true;
            }
            if (((String) obj).trim().equalsIgnoreCase("null")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为数字
     *
     * @param obj
     * @return
     */
    public boolean isNumber(String obj) {
        return NumberUtil.isNormalNumber(obj);
    }

    /**
     * 日期转换成字符串
     *
     * @param calendar
     * @return
     */
    private Object getFormatCld(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    /**
     * 将日期转换成 Calendar
     *
     * @param endDate
     * @return
     * @throws ParseException
     */
    private Calendar parseStrToCld(String endDate) {
        Date date = null;
        SimpleDateFormat format = null;
        Calendar calendar = Calendar.getInstance();
        try {
            if (endDate != null) {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = format.parse(endDate);
                calendar.setTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * 更新日程事件
     *
     * @param param
     * @return
     */
    public Map<String, String> updateEvent(Map<String, String> param) {
        Map<String, String> result = new HashMap<String, String>();
        if (isEmpty(param)) {
            result.put("result", "0");
            result.put("obj", "更新参数为空！");
            return null;
        }
        String id = param.get("id");
        if (!isExistEvent(Long.parseLong(id))) {
            result.put("result", "0");
            result.put("obj", "事件id不能为空！");
            return result;
        }
        String calendarId = param.get("calendarId");
        String title = param.get("title");
        String description = param.get("description");
        String location = param.get("location");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String status = param.get("status");
        String timeZone = param.get("timeZone");
        String hasAlarm = param.get("hasAlarm");
        String allDay = param.get("allDay");
        String availability = param.get("availability");
        String accessLevel = param.get("accessLevel");
        if (!isNumber(calendarId) && isEmpty(title) && isEmpty(description) && isEmpty(location)
                && isEmpty(startDate) && isEmpty(endDate)) {
            result.put("result", "0");
            result.put("obj", "事件更新的信息不能都为空！");
            return result;
        }
        ContentValues values = new ContentValues();
        if (isNumber(calendarId)) {
            values.put(Events.CALENDAR_ID, calendarId);
        }
        if (!isEmpty(title)) {
            values.put(Events.TITLE, title);
        }
        if (!isEmpty(description)) {
            values.put(Events.DESCRIPTION, description);
        }
        if (!isEmpty(location)) {
            values.put(Events.EVENT_LOCATION, location);
        }
        //计算开始、结束时间，全部用Date也可以。
        Calendar startCld = Calendar.getInstance();
        Calendar endCld = Calendar.getInstance();
        //如果是全天事件的话，取开始时间的那一整天
        if ((isNumber(allDay) && Integer.parseInt(allDay) == 1) && !isEmpty(startDate)) {
            Calendar cld = Calendar.getInstance();
            cld = parseStrToCld(startDate);
            //开始时间
            startCld.set(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH), cld.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            //结束时间
            endCld.set(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH), cld.get(Calendar.DAY_OF_MONTH), 24, 0, 0);
            values.put(Events.ALL_DAY, 0);
            values.put(Events.DTSTART, startCld.getTimeInMillis());
            values.put(Events.DTEND, endCld.getTimeInMillis());
        } else {
            //开始时间
            startCld = parseStrToCld(startDate);
            //结束时间
            endCld = parseStrToCld(endDate);
            if (!isEmpty(startDate)) {
                values.put(Events.DTSTART, startCld.getTimeInMillis());
            }
            if (!isEmpty(endDate)) {
                values.put(Events.DTEND, endCld.getTimeInMillis());
            }
        }
        if (!isEmpty(timeZone)) {
            values.put(Events.EVENT_TIMEZONE, timeZone);
        }
        if (isNumber(hasAlarm)) {
            values.put(Events.HAS_ALARM, hasAlarm);
        }
        if (isNumber(availability)) {
            values.put(Events.AVAILABILITY, availability);
        }
        if (isNumber(accessLevel)) {
            values.put(Events.ACCESS_LEVEL, accessLevel);
        }
        try {
            int n = resolver.update(eventsUri, values, Events._ID + "=" + id, null);
            result.put("result", "1");
            result.put("obj", n + "");
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            result.put("result", "-1");
            result.put("obj", e.getMessage());
        }
        return result;
    }

    /**
     * 查询日程(事件、提醒、参与人)
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryEvents(Map<String, String> param) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        StringBuffer selection = new StringBuffer();
        List<String> selectionArgs = new ArrayList<String>();
        if (!isEmpty(param)) {
            selection.append(" 1=1 ");
            String calendarId = param.get("calendar_id");
            String eventId = param.get("id");
            String title = param.get("title");
            String description = param.get("description");
            String location = param.get("location");
            String startDate = param.get("startDate");
            String endDate = param.get("endDate");
            String status = param.get("status");
            if (isNumber(calendarId)) {
                selection.append(" AND " + Events.CALENDAR_ID + "=? ");
                selectionArgs.add(calendarId);
            }
            if (isNumber(eventId)) {
                selection.append(" AND " + Events._ID + "=? ");
                selectionArgs.add(eventId);
            }
            if (!isEmpty(title)) {
                selection.append(" AND " + Events.TITLE + " LIKE ? ");
                selectionArgs.add("%" + title + "%");
            }
            if (!isEmpty(description)) {
                selection.append(" AND " + Events.DESCRIPTION + " LIKE ? ");
                selectionArgs.add("%" + description + "%");
            }
            if (!isEmpty(location)) {
                selection.append(" AND " + Events.EVENT_LOCATION + " LIKE ? ");
                selectionArgs.add("%" + location + "%");
            }
            if (isNumber(status)) {
                selection.append(" AND " + Events.STATUS + " =? ");
                selectionArgs.add(status);
            }
            if (!isEmpty(startDate)) {
                long startMillis = parseStrToCld(startDate).getTimeInMillis();
                selection.append(" AND " + Events.DTSTART + " >=? ");
                selectionArgs.add(startMillis + "");
            }
            if (!isEmpty(endDate)) {
                long endMillis = parseStrToCld(endDate).getTimeInMillis();
                selection.append(" AND " + Events.DTEND + " <=? ");
                selectionArgs.add(endMillis + "");
            }
        }
        Log.w(TAG, "日历信息 - 查询条件:" + selection.toString() + " ? " + selectionArgs.toString());
//		EVENTS_COLUMNS 换成 null 查询所有字段
        Cursor eventsCursor = resolver.query(
                eventsUri,
                EVENTS_COLUMNS,
                selection.length() == 0 ? null : selection.toString(),
                selectionArgs.size() == 0 ? null : selectionArgs.toArray(new String[]{}),
                null);
        Map<String, Object> event;
        while (eventsCursor.moveToNext()) {
            event = new HashMap<String, Object>();
            //以下字段解释，在添加事件里可查看addEvents()
            String eid = eventsCursor.getString(eventsCursor.getColumnIndex(Events._ID));
            String calendarId = eventsCursor.getString(eventsCursor.getColumnIndex(Events.CALENDAR_ID));
            String title = eventsCursor.getString(eventsCursor.getColumnIndex(Events.TITLE));
            String description = eventsCursor.getString(eventsCursor.getColumnIndex(Events.DESCRIPTION));
            String location = eventsCursor.getString(eventsCursor.getColumnIndex(Events.EVENT_LOCATION));
            long startDate = eventsCursor.getLong(eventsCursor.getColumnIndex(Events.DTSTART));
            long endDate = eventsCursor.getLong(eventsCursor.getColumnIndex(Events.DTEND));
            String timeZone = eventsCursor.getString(eventsCursor.getColumnIndex(Events.EVENT_TIMEZONE));
            String hasAlarm = eventsCursor.getString(eventsCursor.getColumnIndex(Events.HAS_ALARM));
            String allDay = eventsCursor.getString(eventsCursor.getColumnIndex(Events.ALL_DAY));
            String availability = eventsCursor.getString(eventsCursor.getColumnIndex(Events.AVAILABILITY));
            String accessLevel = eventsCursor.getString(eventsCursor.getColumnIndex(Events.ACCESS_LEVEL));
            String status = eventsCursor.getString(eventsCursor.getColumnIndex(Events.STATUS));
            Calendar calendar = Calendar.getInstance();
            event.put("id", eid);
            event.put("calendar_id", calendarId);
            event.put("title", title);
            event.put("description", description);
            event.put("location", location);
            calendar.setTimeInMillis(startDate);
            event.put("startDate", getFormatCld(calendar));
            calendar.setTimeInMillis(endDate);
            event.put("endDate", getFormatCld(calendar));
            event.put("timeZone", timeZone);
            event.put("hasAlarm", hasAlarm);
            event.put("allDay", allDay);
            event.put("availability", availability);
            event.put("accessLevel", accessLevel);
            event.put("status", status);
            //查询提醒
            Cursor remindersCursor = resolver.query(
                    remindersUri,
                    REMINDERS_COLUMNS,
                    Reminders.EVENT_ID + "=?",
                    new String[]{eid},
                    null);
            List<Map<String, Object>> reminders = new ArrayList<Map<String, Object>>();
            while (remindersCursor.moveToNext()) {
                Map<String, Object> reminder = new HashMap<String, Object>();
                String rid = remindersCursor.getString(remindersCursor.getColumnIndex(Reminders._ID));
                String eventId = remindersCursor.getString(remindersCursor.getColumnIndex(Reminders.EVENT_ID));
                String minutes = remindersCursor.getString(remindersCursor.getColumnIndex(Reminders.MINUTES));
                String method = remindersCursor.getString(remindersCursor.getColumnIndex(Reminders.METHOD));
                reminder.put("id", rid);
                reminder.put("event_id", eventId);
                reminder.put("minutes", minutes);
                reminder.put("method", method);
                reminders.add(reminder);
            }
            remindersCursor.close();
            event.put("reminders", reminders);

            result.add(event);
        }
        eventsCursor.close();
        return result;
    }

    /**
     * 批量插入日程
     *
     * @param calendars
     * @return Map<String, Object>
     */
    public Map<String, Object> insertEvents(List<Map<String, Object>> calendars) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (isEmpty(calendars)) {
            result.put("result", "0");
            result.put("obj", "日程信息为空，添加失败！");
            return null;
        }
        List<String> addResult = new ArrayList<String>();
        //插入事件
        Uri eUri = null;
        for (int i = 0; i < calendars.size(); i++) {
            //获得日程
            Map<String, Object> calendar = calendars.get(i);
            try {
                eUri = insertEvent(calendar);
            } catch (Exception e) {
                addResult.add("第" + (i + 1) + "条日程，添加事件失败：" + e.getMessage());
            }
            //如果事件插入成功，则插入提醒和参与者
            if (!isEmpty(eUri)) {
                String eventId = eUri.getLastPathSegment();
                //存入插入事件的结果
                addResult.add(eUri.toString());
                //插入提醒，可以添加多个提醒
                List<Map<String, String>> reminders = (List<Map<String, String>>) calendar.get("reminders");
                if (!isEmpty(reminders)) {
                    for (Map<String, String> reminder : reminders) {
                        reminder.put(Reminders.EVENT_ID, eventId);
                        try {
                            eUri = insertReminder(reminder);
                            //存入插入事件的结果
                            addResult.add(eUri.toString());
                        } catch (Exception e) {
                            Log.i(TAG, e.getMessage());
                        }
                    }
                }
            }
        }
        result.put("result", "1");
        result.put("obj", addResult);
        return result;
    }


    /**
     * 插入日程事件
     *
     * @param events
     * @return Uri
     */
    public Uri insertEvent(Map<String, Object> events) throws Exception {
        if (isEmpty(events)) {
            return null;
        }
        try {
            ContentValues eventVal = new ContentValues();
            //---------------------------Event表的数据------------------------------------
            //插入一条事件，必须满足，有日历id、开始时间、结束时间、和标题或者内容，自定义的，不然插入没有意义
            String calendarId = (String) events.get(Events.CALENDAR_ID);//日历id
            String startDate = (String) events.get(Events.DTSTART);//开始时间:格式=yyyy-MM-dd HH:mm:ss
            String endDate = (String) events.get(Events.DTEND);//结束时间:格式=yyyy-MM-dd HH:mm:ss
            String title = (String) events.get(Events.TITLE);//日程标题
            String description = (String) events.get(Events.DESCRIPTION);//日程内容
            if (!isNumber(calendarId) || isEmpty(startDate) || isEmpty(endDate) || (isEmpty(title))) {
                return null;
            }
            String location = (String) events.get(Events.EVENT_LOCATION);//地点
            String timeZone = (String) events.get(Events.EVENT_TIMEZONE);//时区：TimeZone.getAvailableIDs()
            String hasAlarm = (String) events.get(Events.HAS_ALARM);//是否事件触发报警:0=false, 1=true
            String allDay = (String) events.get(Events.ALL_DAY);//是否全天事件：0=false, 1=true
            //		String eventStatus	= (String) calendar.get("eventStatus");//事件状态:暂定(0)，确认(1)或取消(2)
            String availability = (String) events.get(Events.AVAILABILITY);//我的状态:0=忙碌，1=有空
            String accessLevel = (String) events.get(Events.ACCESS_LEVEL);//访问权限：默认=0，机密=1，私有=2，公共=3
            eventVal.put(Events.CALENDAR_ID, calendarId);
            eventVal.put(Events.TITLE, title);
            eventVal.put(Events.DESCRIPTION, description);
            eventVal.put(Events.STATUS, 1);
            // 必须设置，默认为手机时区 TimeZone.getAvailableIDs() 获取时区列表
            eventVal.put(Events.EVENT_TIMEZONE, isEmpty(timeZone) ? TimeZone.getDefault().getID() : timeZone);
            eventVal.put(Events.HAS_ALARM, (isNumber(hasAlarm) ? Integer.parseInt(hasAlarm) : 0));
            //计算开始、结束时间，全部用Date也可以。
            Calendar cld = Calendar.getInstance();
            Calendar startCld = Calendar.getInstance();
            Calendar endCld = Calendar.getInstance();
            cld = parseStrToCld(startDate);
            //如果地址不为空插入地址
            if (!isEmpty(location)) {
                eventVal.put(Events.EVENT_LOCATION, location);
            }
            //如果是全天事件的话，取开始时间的那一整天
            if (isNumber(allDay) && Integer.parseInt(allDay) == 1) {
                //开始时间
                startCld.set(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH), cld.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                //结束时间
                endCld.set(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH), cld.get(Calendar.DAY_OF_MONTH), 24, 0, 0);
                eventVal.put(Events.ALL_DAY, 1);
                eventVal.put(Events.DTSTART, startCld.getTimeInMillis());
                eventVal.put(Events.DTEND, endCld.getTimeInMillis());
            } else {
                //开始时间
                startCld = parseStrToCld(startDate);
                //结束时间
                endCld = parseStrToCld(endDate);
                eventVal.put(Events.ALL_DAY, 0);
                eventVal.put(Events.DTSTART, startCld.getTimeInMillis());
                eventVal.put(Events.DTEND, endCld.getTimeInMillis());
            }
            //设置我的状态
            if (isNumber(availability) && Integer.parseInt(availability) == 0) {
                eventVal.put(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
            } else {
                eventVal.put(Events.AVAILABILITY, Events.AVAILABILITY_FREE);
            }
            //设置隐私
            if (isNumber(accessLevel) && Integer.parseInt(accessLevel) == 1) {
                eventVal.put(Events.ACCESS_LEVEL, Events.ACCESS_CONFIDENTIAL);
            } else if (isNumber(accessLevel) && Integer.parseInt(accessLevel) == 2) {
                eventVal.put(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
            } else if (isNumber(accessLevel) && Integer.parseInt(accessLevel) == 3) {
                eventVal.put(Events.ACCESS_LEVEL, Events.ACCESS_PUBLIC);
            } else {
                eventVal.put(Events.ACCESS_LEVEL, Events.ACCESS_DEFAULT);
            }
            return resolver.insert(eventsUri, eventVal);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 删除event表里数据
     *
     * @return
     */
    public int deleteEvents(List<String> ids, String calendarId, boolean delAll) {
        String selection = null;
        if (delAll) {
            selection = Events._ID + " > 0";
        } else if (isNumber(calendarId)) {
            selection = Events.CALENDAR_ID + "=" + calendarId;
        } else if (isEmpty(ids)) {
            Log.e(TAG, "日历 日程删除参数为空");
            return -1;
        } else {
            String where = "";
            for (String id : ids) {
                if (isNumber(id)) {
                    where += id + ",";
                }
            }
            selection = Events._ID + " in(" + where.substring(0, where.length() - 1) + ")";
        }
        try {
            Log.i(TAG, "日历 日程删除参数信息：" + selection);
            return resolver.delete(
                    eventsUri,
                    selection,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 删除日历
     *
     * @param ids
     * @return
     */
    public int deleteEvent(List<String> ids) {
        String selection = null;
        if (isEmpty(ids)) {
            Log.e(TAG, "日历 日程删除参数为空");
            return -1;
        } else {
            String where = "";
            for (String id : ids) {
                if (isNumber(id)) {
                    where += id + ",";
                }
            }
            selection = Events._ID + " in(" + where.substring(0, where.length() - 1) + ")";
        }
        try {
            Log.i(TAG, "日历 日程删除参数信息：" + selection);
            return resolver.delete(
                    eventsUri,
                    selection,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int deleteEvent(String ids, String cid) {
        if (isEmpty(ids)) {
            Log.e(TAG, "日历 日程删除参数为空");
            return -1;
        }
        try {
            Log.i(TAG, "日历 日程删除参数信息：" + ids);
            return resolver.delete(
                    eventsUri,
                    Events.CALENDAR_ID + "=" + cid + " AND " + Events._ID + "=?",
                    new String[]{ids});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 查询event是否存在
     *
     * @param id
     * @return
     */
    public boolean isExistEvent(long id) {
        Cursor cursor = resolver.query(
                eventsUri,
                new String[]{Events._ID},
                Events._ID + "=" + id,
                null,
                null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    /**
     * 插入日程提醒，如果参数ops不为空，则不执行插入。
     *
     * @param reminders
     * @return Uri
     */
    public Uri insertReminder(Map<String, String> reminders) throws Exception {
        //---------------------------Reminders表的数据------------------------------------
        //插入提醒，可以添加多个提醒
        if (!isEmpty(reminders)) {
            try {
                String eventId = reminders.get("event_id");//外键事件id
                //如果时间id为空，不添加提醒
                if (!isExistEvent(Long.parseLong(eventId))) {
                    return null;
                }
                String minutes = reminders.get("minutes");//提醒在事件前几分钟后发出
                String method = reminders.get("method");//提醒方法:METHOD_DEFAULT:0,*_ALERT:1,*_EMAIL:2,*_SMS:3
                //提醒方法
                int methodType = Reminders.METHOD_DEFAULT;
                if (method.equals("1")) {
                    methodType = Reminders.METHOD_ALERT;
                } else if (method.equals("2")) {
                    methodType = Reminders.METHOD_EMAIL;
                } else if (method.equals("3")) {
                    methodType = Reminders.METHOD_SMS;
                }
                //提醒时间
                int m = isNumber(minutes) ? Integer.parseInt(minutes) : 0;
                ContentValues alarmVal = new ContentValues();
                alarmVal.put(Reminders.EVENT_ID, eventId);
                alarmVal.put(Reminders.MINUTES, m);//提醒在事件前多少分钟后发出
                alarmVal.put(Reminders.METHOD, methodType);
                Uri uri = resolver.insert(remindersUri, alarmVal);
                return uri;
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }

    /**
     * 更新日程提醒
     *
     * @param param
     * @return Uri
     * @throws Exception
     */
    public int updateReminder(Map<String, String> param) {
        if (isEmpty(param)) {
            Log.e(TAG, "日历 提醒更新参数为空！");
            return -1;
        }
        String id = param.get(Reminders._ID);
        if (!isExistReminder(Long.parseLong(id))) {
            Log.e(TAG, "日历 提醒id无效，id不存在！");
            return -1;
        }
        String mimutes = param.get(Reminders.MINUTES);//提醒在事件前几分钟后发出
        String method = param.get("method");//提醒方法:METHOD_DEFAULT:0,*_ALERT:1,*_EMAIL:2,*_SMS:3
        if (isEmpty(mimutes)) {
            Log.e(TAG, "日历 提醒更新的信息不能都为空！");
            return -1;
        }
        ContentValues reminderVal = new ContentValues();
        if (!isEmpty(mimutes)) {
            //提醒时间
            int m = isNumber(mimutes) ? Integer.parseInt(mimutes) : 0;
            reminderVal.put(Reminders.MINUTES, m);//提醒在事件前多少分钟后发出
        }
        if (!isEmpty(method)) {
            //提醒方法
            int methodType = Reminders.METHOD_DEFAULT;
            if (method.equals("1")) {
                methodType = Reminders.METHOD_ALERT;
            } else if (method.equals("2")) {
                methodType = Reminders.METHOD_EMAIL;
            } else if (method.equals("3")) {
                methodType = Reminders.METHOD_SMS;
            }
            reminderVal.put(Reminders.METHOD, methodType);
        }
        try {
            return resolver.update(remindersUri, reminderVal, Reminders._ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 删除日历提醒
     *
     * @param param
     * @return
     */
    public int deleteReminder(Map<String, String> param) {
        if (isEmpty(param)) {
            Log.e(TAG, "日历 提醒更新参数为空！");
            return -1;
        }
        String id = param.get(Reminders._ID);
        if (!isExistReminder(Long.parseLong(id))) {
            Log.e(TAG, "日历 提醒id无效，id不存在！");
            return -1;
        }
        try {
            return resolver.delete(remindersUri, Reminders._ID + "=?", new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 查询Reminder是否存在
     *
     * @param id
     * @return
     */
    public boolean isExistReminder(long id) {
        Cursor cursor = resolver.query(
                remindersUri,
                new String[]{Reminders._ID},
                Reminders._ID + "=" + id,
                null,
                null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }


    /**
     * 根据账户查询账户日历
     */
    public long queryCalendarsId() {
        long oldId = checkCalendarAccounts();
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount();
            if (addId >= 0) {
                return checkCalendarAccounts();
            } else {
                return -1;
            }
        }
    }

//    public long checkCalendarAccountsV1() {
//        Cursor cursor = resolver.query(calendarsUri, null, null, null, null);
//        try {
//            if (cursor == null)//查询返回空值
//                return -1;
//            int count = cursor.getCount();
//            if (count > 0) {//存在现有账户，取第一个账户的id返回
//                do {
//                    cursor.getString(cursor.getColumnIndex(Calendars.ACCOUNT_NAME));
//                    //取出数据
//                    Log.d(TAG, "日历信息 - name: " + cursor.getString(cursor.getColumnIndex(Calendars.NAME))
//                            + "\naccount_name: " + cursor.getString(cursor.getColumnIndex(Calendars.ACCOUNT_NAME))
//                            + "\nid: " + cursor.getString(cursor.getColumnIndex(Calendars._ID))
//                            + "\ndis_name: " + cursor.getString(cursor.getColumnIndex(Calendars.CALENDAR_DISPLAY_NAME)));
//                } while (cursor.moveToNext());
//            } else {
//                Log.d(TAG, "日历信息 - 没有日历用户");
//                return -1;
//            }
//        } finally {
//            if (cursor.moveToFirst()) {
//                do {
//                    //取出数据
//                    Log.d(TAG, "日历信息 - name: " + cursor.getString(cursor.getColumnIndex(Calendars.NAME))
//                            + "\naccount_name: " + cursor.getString(cursor.getColumnIndex(Calendars.ACCOUNT_NAME))
//                            + "\nid: " + cursor.getString(cursor.getColumnIndex(Calendars._ID))
//                            + "\ndis_name: " + cursor.getString(cursor.getColumnIndex(Calendars.CALENDAR_DISPLAY_NAME)));
//                } while (cursor.moveToNext());
//            }
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }


    /**
     * 检测用户账号
     *
     * @return
     */
    public long checkCalendarAccounts() {
        Cursor cursor = resolver.query(calendarsUri, null, null, null, null);
        try {
            if (cursor == null)//查询返回空值
                return -1;
            int count = cursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                cursor.moveToFirst();
                Log.d(TAG, "name: " + cursor.getString(cursor.getColumnIndex(Calendars.NAME)));
                Log.d(TAG, "account_name: " + cursor.getString(cursor.getColumnIndex(Calendars.ACCOUNT_NAME)));
                Log.d(TAG, "id: " + cursor.getString(cursor.getColumnIndex(Calendars._ID)));
                Log.d(TAG, "dis_name: " + cursor.getString(cursor.getColumnIndex(Calendars.CALENDAR_DISPLAY_NAME)));
                return cursor.getLong(cursor.getColumnIndex(Calendars._ID));
            } else {
                Log.d(TAG, "日历信息 - 没有日历用户");
                return -1;
            }
        } finally {
            if (cursor.moveToFirst()) {
                do {
                    //取出数据
                    Log.d(TAG, "日历信息 - name: " + cursor.getString(cursor.getColumnIndex(Calendars.NAME))
                            + "\naccount_name: " + cursor.getString(cursor.getColumnIndex(Calendars.ACCOUNT_NAME))
                            + "\nid: " + cursor.getString(cursor.getColumnIndex(Calendars._ID))
                            + "\ndis_name: " + cursor.getString(cursor.getColumnIndex(Calendars.CALENDAR_DISPLAY_NAME)));
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 获取账户uri
     *
     * @param account
     * @param accountType
     * @return
     */
    public Uri asSyncAdapter(String account, String accountType) {
        return calendarsUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(Calendars.ACCOUNT_TYPE, accountType)
                .build();
    }

    /**
     * 添加一个日历账户
     *
     * @return
     */
    public long addCalendarAccount() {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(Calendars.NAME, CALENDARS_NAME);

        value.put(Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(Calendars.VISIBLE, 1);
        value.put(Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
        value.put(Calendars.SYNC_EVENTS, 1);
        value.put(Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = asSyncAdapter(CALENDARS_ACCOUNT_NAME, CALENDARS_ACCOUNT_TYPE);

        Uri result = resolver.insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 删除日历账户
     *
     * @return
     */
    public void deleteCalendarAccount() {
        Cursor cursor = resolver.query(calendarsUri, null, Calendars.ACCOUNT_NAME + " = ?", new String[]{CALENDARS_ACCOUNT_NAME}, null);
        long accountId = -1;
        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                accountId = cursor.getLong(cursor.getColumnIndex(Calendars._ID));
            }
        }
        Uri calendarUri = asSyncAdapter(CALENDARS_ACCOUNT_NAME, CALENDARS_ACCOUNT_TYPE);
        int del = resolver.delete(calendarUri, Calendars._ID + " = ?", new String[]{"" + accountId});
        Log.d(TAG, "日历信息 -  删除日历信息: " + del);
    }


    /**
     * 删除event表里数据
     */
    public Map<String, String> delEvents(List<String> ids, String calendarId, boolean delAll) {
        Map<String, String> result = new HashMap<String, String>();

        String selection = null;

        if (delAll) {
            selection = Events._ID + " > 0";
        } else if (NumberUtil.isNormalNumber(calendarId)) {
            selection = Events.CALENDAR_ID + "=" + calendarId;
        } else if (ids.isEmpty()) {
            result.put("result", "0");
            result.put("obj", "要删除日程事件的id为空！");
            return result;
        } else {
            String where = "";
            for (String id : ids) {
                if (NumberUtil.isNormalNumber(id)) {
                    where += id + ",";
                }
            }
            selection = Events._ID + " in(" + where.substring(0, where.length() - 1) + ")";
        }

        try {
            Log.i(TAG, "====：" + selection);
            int n = resolver.delete(
                    eventsUri,
                    selection,
                    null);

            result.put("result", "1");
            result.put("obj", n + "");

        } catch (Exception e) {
            result.put("result", "-1");
            result.put("obj", "删除错误：" + e.toString());
        }
        return result;
    }

}
