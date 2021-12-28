package com.zzt.webview.b;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.lifecycle.Lifecycle;

import java.lang.ref.WeakReference;

/**
 * @author: zeting
 * @date: 2021/12/13
 */
public class MyWebViewUtil {
    private static final String TAG = "webView_" + MyWebViewUtil.class.getSimpleName();
    private static volatile MyWebViewUtil instance;

    public MyWebViewUtil() {
    }

    public static MyWebViewUtil getInstance() {
        if (instance == null) {
            synchronized (MyWebViewUtil.class) {
                if (instance == null) {
                    instance = new MyWebViewUtil();
                }
            }
        }
        return instance;
    }

    private MWebView webView;

    private WeakReference<Context> weakReference;

    public MWebView getVebView(Context context, Lifecycle lifecycle) {
        if (webView == null || weakReference == null || weakReference.get() == null) {
            Log.e(TAG, "---创建view");
            weakReference = new WeakReference<>(context);
            webView = new MWebView(weakReference.get());
            webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
//            webView.setLifecycle(lifecycle);
        }
        webView.addWebClient();

        Log.e(TAG, "---得到view");
        return webView;
    }


    public void recycleWebView() {
        if (webView == null) {
            return;
        }
        ViewGroup viewParent = (ViewGroup) webView.getParent();
        if (viewParent != null) {
            viewParent.removeView(webView);
        }
        webView.removeWebClient();
    }
}
