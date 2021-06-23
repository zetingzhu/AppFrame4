package com.zzt.systemcalendatsample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {
    var TAG = "CalendarActivity"

    companion object {
        var CALENDAR_REQUEST_CODE_ADD: Int = 1000
        var CALENDAR_REQUEST_CODE_UPDATE: Int = 1001
    }

    fun testTime() {
        var sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("utc"));
        var str = 1619697600000
        var utcTime = sdf.format(Date(str))

        Log.i(TAG, "日历 当前时间：${formatYMDHM(this@CalendarActivity, System.currentTimeMillis())}  UCT时间：${utcTime}")
    }

    fun formatYMDHM(context: Context?, time: Long): String? {
        val formatSource: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatSource.format(Date(time))
    }

    // 日历操作
    var calendarUtil: CalendarsDataBaseUtil? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        calendarUtil = CalendarsDataBaseUtil(this)
        testTime()


        findViewById<View>(R.id.tv_v1).setOnClickListener {

            if (hasCalendarPermission()) {

                /*************测试数据**************/

                calendarUtil?.queryCalendar()
                /*************测试数据**************/
            } else {
                var rqCode: Int = CALENDAR_REQUEST_CODE_UPDATE
                requestCalendarPermission(rqCode)
            }
        }
        findViewById<View>(R.id.tv_v2).setOnClickListener {

            if (hasCalendarPermission()) {

                /*************测试数据**************/
                calendarUtil?.deleteCalRemEvent()
                /*************测试数据**************/
            } else {
                var rqCode: Int = CALENDAR_REQUEST_CODE_UPDATE
                requestCalendarPermission(rqCode)
            }
        }
    }

    fun hasCalendarPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * 检查并且申请日历权限
     */
    private fun requestCalendarPermission(code: Int) {

        requestPermissions(arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR), code)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CALENDAR_REQUEST_CODE_ADD || requestCode == CALENDAR_REQUEST_CODE_UPDATE) {
            Log.d(TAG, "日历权限申请 收到回调")
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 申请权限 成功
                Log.d(TAG, "日历权限申请 成功")
            } else {
                // 申请权限 失败
                Log.d(TAG, "日历权限申请 失败")
            }
        }
    }
}