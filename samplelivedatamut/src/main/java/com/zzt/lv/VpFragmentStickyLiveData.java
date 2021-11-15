package com.zzt.lv;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


/**
 * @author: zeting
 * @date: 2021/6/28
 * 解决viewpager 中嵌套多个fragment中的livedata 粘粘发送数据问题
 */
public class VpFragmentStickyLiveData<T> extends MutableLiveData<T> {

    /**
     * 拦截是否能够正常接收数据
     */
    boolean getChanged = false;

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        getChanged = false;
        super.observe(owner, new CustomObserver<T>(observer));
    }

    @Override
    public void setValue(T value) {
        getChanged = true;
        super.setValue(value);
    }

    @Override
    public void postValue(T value) {
        getChanged = true;
        super.postValue(value);
    }

    class CustomObserver<T> implements Observer<T> {
        private Observer<? super T> mObserver;

        public CustomObserver(Observer<? super T> observer) {
            mObserver = observer;
        }

        @Override
        public void onChanged(T t) {
            Log.v("HomeMarketProductFragment", "刷新列表数据   这里设置了几次");
            if (getChanged) {
                mObserver.onChanged(t);
            }
        }
    }
}