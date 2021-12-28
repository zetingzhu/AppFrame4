package com.zzt.webview;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ViewGroup;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.zzt.webview.b.MWebView;

/**
 * @author: zeting
 * @date: 2021/12/13
 */
public class MyWebObserver implements LifecycleObserver {
    private static final String TAG = "webView_" + MyWebObserver.class.getSimpleName();
    private MWebView mWebView;

    public MyWebObserver(MWebView mWebView) {
        this.mWebView = mWebView;
    }

    public void setWebView(MWebView mWebView) {
        this.mWebView = mWebView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        Log.d(TAG, "生命周期检测 >>>>>> ON_START");
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        Log.d(TAG, "生命周期检测 >>>>>> ON_RESUME");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Log.d(TAG, "生命周期检测 >>>>>> ON_PAUSE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        Log.d(TAG, "生命周期检测 >>>>>> ON_STOP");
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(false);
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        Log.d(TAG, "生命周期检测 >>>>>> ON_DESTROY");
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            Log.d(TAG, "结束的时候父类：" + parent);
            if (parent != null) {
                parent.removeAllViews();
            }
        }
    }


}
