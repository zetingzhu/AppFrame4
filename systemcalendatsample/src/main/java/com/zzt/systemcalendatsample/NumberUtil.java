package com.zzt.systemcalendatsample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fangzhu on 2015/2/6.
 */
public class NumberUtil {

    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNormalNumber(String str) {
        if (str == null)
            return false;

        Pattern pattern = Pattern.compile("^[-+]?[0-9]+(\\.[0-9]+)?$");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        }
        return false;
    }

}
