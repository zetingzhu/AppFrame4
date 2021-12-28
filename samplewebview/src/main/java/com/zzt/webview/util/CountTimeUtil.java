package com.zzt.webview.util;

import android.util.Log;

/**
 * @author: zeting
 * @date: 2021/12/13
 */
public class CountTimeUtil {
    private static final String TAG = "webView_" + CountTimeUtil.class.getSimpleName();

    public interface CallBack {
        //执行回调操作的方法
        void doSometing();
    }

    public static void countTime(String type, CallBack callBack) {
        long startTime = System.currentTimeMillis(); //起始时间
        callBack.doSometing(); ///进行回调操作
        long endTime = System.currentTimeMillis(); //结束时间
        Log.i(TAG, String.format("方法使用 类型：%s 时间: %d ms", type, endTime - startTime)); //打印使用时间
    }
}
