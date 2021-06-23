package com.zzt.systemcalendatsample;

public class StringUtil {

    /**
     * If a string is not null and an empty string, it's not a null string.
     *
     * @param str String
     * @return boolean
     */
    public static boolean isNotNull(String str) {
        return str != null && str.trim().length() > 0;
    }



}