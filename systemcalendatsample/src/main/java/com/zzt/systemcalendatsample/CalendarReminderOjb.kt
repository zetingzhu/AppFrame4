package com.zzt.systemcalendatsample

import java.io.Serializable

/**
 * @author: zeting
 * @date: 2021/4/20
 */

/**
 * 保存到日历数据库的提醒
 */
data class SaveCalRemObj(
        var startDate: Long,// 开始时间
        var endDate: Long, // 结束时间
        var title: String, // 标题
        var description: String,// 描述
        var remindersMinutes: Int// 提醒时间
) : Serializable {
    var calendarId: String? = ""  // 日历账号id
    override fun toString(): String {
        return "SaveCalRemObj(startDate=$startDate, endDate=$endDate, title='$title', description='$description', remindersMinutes=$remindersMinutes, calendarId=$calendarId)"
    }
}

/**
 * 日历提醒数据类型
 */
data class ReminderTypeObj(
        var index: Int,// id
        var reminderValue: String, //提醒文字
        var reminderTime: Long, //提醒时间
        var remindType: Int,//发送给服务器的时间类型
        var remindersMinutes: Int //保存本地日历的提前时间
) : Serializable


/**
 * 接口成功操作日历
 */
data class optCalendarDataBase(
        val opt: Int,
        val obj: SaveCalRemObj,
        var position: Int,
        var remindType: Int
) : Serializable {
    companion object {
        val OPT_ADD = 1
        val OPT_UPDATA = 2
        val OPT_DELETE = 3
    }
}


