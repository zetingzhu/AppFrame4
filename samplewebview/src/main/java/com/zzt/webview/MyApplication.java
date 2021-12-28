package com.zzt.webview;

import android.app.Application;

import java.io.File;

import ren.yale.android.cachewebviewlib.ResourceInterceptor;
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptor;
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptorInst;
import ren.yale.android.cachewebviewlib.config.CacheExtensionConfig;

/**
 * @author: zeting
 * @date: 2021/12/28
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initCacheWebView();
    }

    /**
     * 初始化浏览器缓存内核
     */
    private void initCacheWebView() {
        WebViewCacheInterceptor.Builder builder = new WebViewCacheInterceptor.Builder(this);

        //设置okhttp缓存路径，默认getCacheDir，名称CacheWebViewCache
        builder.setCachePath(new File(this.getCacheDir(), "cache_path_name"))
                .setDynamicCachePath(new File(this.getCacheDir(), "dynamic_webview_cache"))
                .setCacheSize(1024 * 1024 * 100)//设置缓存大小，默认100M
                .setConnectTimeoutSecond(20)//设置http请求链接超时，默认20秒
                .setReadTimeoutSecond(20);//设置http请求链接读取超时，默认20秒

        CacheExtensionConfig extension = new CacheExtensionConfig();
        extension.addExtension("json").removeExtension("swf");

        builder.setCacheExtensionConfig(extension);
        //builder.setAssetsDir("static");
        //builder.isAssetsSuffixMod(true);
        builder.setDebug(true);

        builder.setResourceInterceptor(new ResourceInterceptor() {
            @Override
            public boolean interceptor(String url) {
                return true;
            }
        });

        WebViewCacheInterceptorInst.getInstance().init(builder);

    }
}
