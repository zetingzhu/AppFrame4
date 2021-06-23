package com.zzt.systemcalendatsample;


import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat mFormatYMD = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat mFormatDMY = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat mFormatYMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat mFormatDMYHM = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private static final SimpleDateFormat mFormatYMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat mFormatDMYHMS = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final SimpleDateFormat mFormatYMDHMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
    private static final SimpleDateFormat mFormatDMYHMSS = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SS");
    private static final SimpleDateFormat mFormatMDHM = new SimpleDateFormat("MM-dd HH:mm");
    private static final SimpleDateFormat mFormatMDHM2 = new SimpleDateFormat("MM/dd HH:mm");
    private static final SimpleDateFormat mFormatDMHM = new SimpleDateFormat("dd-MM HH:mm");
    private static final SimpleDateFormat mFormatDMHM2 = new SimpleDateFormat("dd/MM HH:mm");
    private static final SimpleDateFormat mFormatMDHMS = new SimpleDateFormat("MM-dd HH:mm:ss");
    private static final SimpleDateFormat mFormatDMHMS = new SimpleDateFormat("dd-MM HH:mm:ss");
    private static final SimpleDateFormat mFormatMDHMS2 = new SimpleDateFormat("MM.dd HH:mm:ss");
    private static final SimpleDateFormat mFormatDMHMS2 = new SimpleDateFormat("dd.MM HH:mm:ss");
    private static final SimpleDateFormat mFormatYM = new SimpleDateFormat("yyyy-MM");
    private static final SimpleDateFormat mFormatMY = new SimpleDateFormat("MM-yyyy");
    private static final SimpleDateFormat mFormatMD = new SimpleDateFormat("MM-dd");
    private static final SimpleDateFormat mFormatDM = new SimpleDateFormat("dd-MM");
    private static final SimpleDateFormat mFormatHM = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat mFormatHMS2 = new SimpleDateFormat("HH:mm:ss");
    // 意大利时间
    private static final SimpleDateFormat MFORMATITALYDMHMS = new SimpleDateFormat("HH:mm:ss, dd/MM");
    private static SimpleDateFormat formatter;

    /**
     * 星期日  0
     * 星期一  1
     *
     * @param dt
     * @return
     */
    public static int getDayOfWeek(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return w;
    }

    public static String getTime(Date aDate, String dateformat) {
        if (aDate == null)
            return "";
        formatter = new SimpleDateFormat(dateformat);
        return formatter.format(aDate);
    }

    public static boolean equals(Date date1, Date date2) {
        if (date1 == null && date2 == null)
            return true;
        if (date1 == null && date2 != null)
            return false;
        if (date1 != null && date2 == null)
            return false;
        return date1.equals(date2);
    }

    /**
     * @param aDate
     * @return String
     */
    private static final String getDateFormat(Date aDate) {
        if (aDate == null)
            return null;
        SimpleDateFormat formatter
                = new SimpleDateFormat("M/d/yyyy");
        return formatter.format(aDate);
    }

    /**
     * @param date
     * @return String
     */
    public static String NVL(Date date) {
        if (date == null)
            return "";
        else
            return getDateFormat(date);
    }

    public static Date getDate(String strDate, String formatter) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        try {
            return sdf.parse(strDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDate(Date aDate, String formatStr) {
        if (aDate == null)
            return "";
        formatter = new SimpleDateFormat(formatStr);
        return formatter.format(aDate);
    }

    /**
     * 添加一个有毫秒转固定格式时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatDateLong(long time, String format) {
        String targetTime = "";
        try {
            SimpleDateFormat formatSource = new SimpleDateFormat(format);
            Date date = new Date(time);
            targetTime = formatSource.format(date);
        } catch (Exception e) {
            return Long.toString(time);
        }
        return targetTime;
    }

    /**
     * 获取时间戳的时分
     *
     * @param millis
     * @return
     */
    public static String parseMillisSecondToHHmm(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date(millis);
        return formatter.format(date);
    }

    /**
     * 获取时间戳比当前时间多x时x分x秒
     *
     * @param content_createtime
     * @return
     */
    public static String[] formatDateDecentHMS(long content_createtime) {
        String[] date = new String[]{"00", "00", "00"};

        long hl = 60L * 60L;
        long hourDecent = content_createtime / hl;// 时间戳的小时差距

        long minute = 60L;
        long minuteDecent = (content_createtime - (hourDecent * hl)) / minute;// 时间戳的分钟差距

        long secondDecent = content_createtime - (hourDecent * hl) - (minuteDecent * minute);// 时间戳的秒差距

        if (hourDecent > 0) {
            if (hourDecent > 9) {
                date[0] = hourDecent + "";
            } else {
                date[0] = "0" + hourDecent;
            }
        }

        if (minuteDecent > 0) {
            if (minuteDecent > 9) {
                date[1] = minuteDecent + "";
            } else {
                date[1] = "0" + minuteDecent;
            }
        }

        if (secondDecent > 0) {
            if (secondDecent > 9) {
                date[2] = secondDecent + "";
            } else {
                date[2] = "0" + secondDecent;
            }
        }
        return date;
    }

    /**
     * 将时间年月日替换成当天
     *
     * @param millis
     * @return
     */
    public static long changeDateYMD(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ss_old = sdf.format(new Date(millis));
        String ss_current = sdf.format(new Date(System.currentTimeMillis()));

        String ss_new = ss_old.replace(ss_old.substring(0, 10), ss_current.substring(0, 10));
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(ss_new));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }


    /**
     * 字符串时间毫秒 ， 返回对应的时间数据格式
     *
     * @param str
     * @param formatSource
     * @return
     */
    public static String formatYMD(String str, SimpleDateFormat formatSource) {
        try {
            long time = Long.parseLong(str);
            return formatSource.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatYMDHMS(Context context, long time) {
        SimpleDateFormat formatSource;
            formatSource = mFormatDMYHMS;
        return formatSource.format(new Date(time));
    }


}

