package com.zzt.systemcalendatsample

import java.io.Serializable

/**
 * @author: zeting
 * @date: 2020/6/22
 */

/**
 * 签到界面数据
 */
data class BindSingingObj(
        // 奖励金
        var creditAmount: String,
        // 连续签到天数
        var continuousDay: Int,
        // 签到日期的数据
        var modelList: MutableList<BindSingingItemObj>? = null
) : Serializable  {
    companion object {
        // 当天签到状态   0 未签到 ,1 已签到, 2 申请补签，3 多日断签
        var STATUS_SIGNING_TODAY_NOT = 0
        var STATUS_SIGNING_TODAY_SIGNED = 1
        var STATUS_SIGNING_TODAY_MISSING = 2
        var STATUS_SIGNING_TODAY_MULTIDAY = 3
    }

    // 签到状态
    var status: Int = STATUS_SIGNING_TODAY_NOT
        set(value) {
            field = value
        }

    // 日历权限
    var calendarPermissions: Boolean = false
        set(value) {
            field = value
        }

}

/**
 * 绑定签到日期的item数据
 */
data class BindSingingItemObj(
        var sort: Long,//排序
        var day: String, // 天
        var credit: String,  //奖励
        var type: Int,// 奖励方式  0普通奖励，1银宝箱,2金宝箱
        var status: Int//签到状态 0 未签到 1已签到 2 漏签,

) : Serializable {
    companion object {
        //   奖励方式 0普通奖励，1银宝箱,2金宝箱
        val TYPE_SIGN_SILVER = 1
        val TYPE_SIGN_GOLD = 2
        val TYPE_SIGN_DEFAULT = 0

        // 签到状态 签到状态 0 未签到 1已签到 2 漏签
        var STATUS_SIGNING_SUCCESS = 1
        var STATUS_SIGNING_FAIL = 2
        var STATUS_SIGNING_NOT = 0
    }

}


/**
 * 签到成功数据
 */
data class SignSuccessObj(
        var todayCredit: String,//        今日领取金额
        var todayType: Int,//        今日领取类型 0普通奖励，1银宝箱 ，2金宝箱
        var tomorrowCredit: String,//明日可领取
        var tomorrowType: Int //.明日可领取奖励类型 0普通奖励，1银宝箱 ，2金宝箱
) : Serializable {
    companion object {

    }
}

/**
 * 老用户补签数据
 */
data class QueForOldObj(
        var protocol: String, // 协议
        var type: Int // 弹框类型 0充值 1交易
) : Serializable

/**
 * 新用户补签问题
 */
data class QueForNewObj(
        var answer: String,// 答案1
        var no: Int,// 答案编号
        var status: Int// 状态 0正确,1错误
) : Serializable


/**
 * 用户补签数据
 */
data class SigningMissingObj(
        var status: Int,//补签状态
        var sort: Int,//题号
        var question: String,//题目
        var modelList: MutableList<QueForNewObj>,//问题答案
        var frameList: MutableList<QueForOldObj>//弹框
) : Serializable {
    companion object {
        /*
          NEW_USER(0, "从未现⾦充值过：新⽤户"),
          OLD_USER(1, "现⾦充值且现⾦交易过&&新手问题全部回答完：⽼⽤户"),
          NEVER_CASH_TRAN_USER(2, "现⾦充值但从未现⾦交易过的用户"),
          SIGN_UP_IN_ADVANCE(3, "提前完成补签任务"),
        */
        //补签状态
        val STATUS_SIGNING_MISSING_NEW_USER = 0
        val STATUS_SIGNING_MISSING_OLD_USER = 1
        val STATUS_SIGNING_MISSING_NEVER_CASH_TRAN_USER = 2
        val STATUS_SIGNING_MISSING_SIGN_UP_IN_ADVANCE = 3

        // 答案状态 0正确,1错误
        val STATUS_ANSWER_CORRECT = 0
        val STATUS_ANSWER_ERROR = 1

        //弹框类型 0充值 1交易
        val TYPE_ITEM_DEPOSIT = 0
        val TYPE_ITEM_TRADE = 1

    }
}

/**
 * 日历提醒
 */
data class RemindersObj(
        var reminderDate: Long,//提醒时间
        var sendDate: Long,//发送时间
        var title: String,//标题
        var content: String,//内容
        var id: Long //日历事件ID
) : Serializable

