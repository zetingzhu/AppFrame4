package com.zzt.webview.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: zeting
 * @date: 2021/12/13
 */
object DataUtil {

    fun getTime(date: Long): String {
        var format = SimpleDateFormat("yyyy.MM.dd.a.HH.mm.ss.SSS", Locale.CHINA);
        val date = Date(date)
        return format.format(date)
    }
}