package com.zzt.webview.b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.zzt.webview.MyWebObserver;

/**
 * @author: zeting
 * @date: 2021/12/13
 */
public class MWebView extends WebView {
    private static final String TAG = "webView_" + MWebView.class.getSimpleName();

    private ProgressBar mProgressBar;

    public MWebView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public MWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        initWebSettings();
        initinject();
        addProgressView();
    }


    private void initinject() {
//         addJavascriptInterface(new AAA(this), "aaa");
    }

    private void initWebSettings() {
        WebSettings webSettings = getSettings();
        if (webSettings == null) return;
        //设置字体缩放倍数，默认100
        webSettings.setTextZoom(100);
        // 支持 Js 使用,只需设置支持JS就自动打开 IndexedDB 存储机制
        webSettings.setJavaScriptEnabled(true);
        //  支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 开启DOM缓存
        webSettings.setDomStorageEnabled(false);
        // 支持自动加载图片
        webSettings.setLoadsImagesAutomatically(hasKitkat());
        // 设置 WebView 的缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        // 设置 UserAgent 属性
        webSettings.setUserAgentString("A");
        //设置http和https资源可以混合使用，解决问题，https的地址中包含了http的视频流，导致5.0以上的手机不能播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 允许加载本地 html 文件/false
        webSettings.setAllowFileAccess(true);
        // 允许通过 file url 加载的 Javascript 读取其他的本地文件,Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        webSettings.setAllowFileAccessFromFileURLs(false);
        // 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源，
        // Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        // 如果此设置是允许，则 setAllowFileAccessFromFileURLs 不起做用
        webSettings.setAllowUniversalAccessFromFileURLs(true);
    }

    public void addWebClient() {
        setWebViewClient(new SafeWebViewClient());
        setWebChromeClient(new SafeWebChromeClient());
    }

    public void removeWebClient() {
        setWebViewClient(null);
        setWebChromeClient(null);
    }

    public class SafeWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        /**
         * 当WebView得页面Scale值发生改变时回调
         */
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        /**
         * 是否在 WebView 内加载页面
         *
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        /**
         * WebView 开始加载页面时回调，一次Frame加载对应一次回调
         *
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        /**
         * WebView 完成加载页面时回调，一次Frame加载对应一次回调
         *
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "--- onPageFinished --- ");
        }

        /**
         * WebView 加载页面资源时会回调，每一个资源产生的一次网络加载，除非本地有当前 url 对应有缓存，否则就会加载。
         *
         * @param view WebView
         * @param url  url
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        /**
         * WebView 可以拦截某一次的 request 来返回我们自己加载的数据，这个方法在后面缓存会有很大作用。
         *
         * @param view    WebView
         * @param request 当前产生 request 请求
         * @return WebResourceResponse
         */
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        /**
         * WebView 访问 url 出错
         *
         * @param view
         * @param request
         * @param error
         */
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        /**
         * WebView ssl 访问证书出错，handler.cancel()取消加载，handler.proceed()对然错误也继续加载
         *
         * @param view
         * @param handler
         * @param error
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }
    }

    public class SafeWebChromeClient extends WebChromeClient {

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }

        /**
         * 当前 WebView 加载网页进度
         *
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(TAG, "------onProgressChanged-----" + newProgress);
//            private AtomicBoolean mIsLoadFinish = new AtomicBoolean(false);
//            if (!mIsLoadFinish.compareAndSet(false, true)) {
//                return;
//            }
            if (newProgress < 100) {
                showLoadProgress(newProgress);
            } else {
                hideLoadProgress();
            }
        }

        /**
         * Js 中调用 alert() 函数，产生的对话框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        /**
         * 处理 Js 中的 Confirm 对话框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        /**
         * 处理 JS 中的 Prompt对话框
         *
         * @param view
         * @param url
         * @param message
         * @param defaultValue
         * @param result
         * @return
         */
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        /**
         * 接收web页面的icon
         *
         * @param view
         * @param icon
         */
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        /**
         * 接收web页面的 Title
         *
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

    }

    private static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public void setLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(new MyWebObserver(this));
    }

    private void addProgressView() {
        this.mProgressBar = new ProgressBar(this.getContext(), null, android.R.attr.progressBarStyleHorizontal);
        this.mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10, 0, 0));
        int progressBarColor = Color.parseColor("#55A8FD");

        ClipDrawable d = new ClipDrawable(new ColorDrawable(progressBarColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        this.mProgressBar.setProgressDrawable(d);
        this.mProgressBar.setVisibility(GONE);
        this.addView(this.mProgressBar);
    }

    public void showLoadProgress(int progress) {
        if (null != this.mProgressBar) {
            if (this.mProgressBar.getVisibility() == GONE) {
                this.mProgressBar.setVisibility(VISIBLE);
            }

            this.mProgressBar.setProgress(progress);
        }
    }

    public void hideLoadProgress() {
        if (null != this.mProgressBar) {
            this.mProgressBar.setVisibility(GONE);
        }

    }
}
