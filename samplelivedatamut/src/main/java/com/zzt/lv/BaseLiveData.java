package com.zzt.lv;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * @author: zeting
 * @date: 2021/6/28
 */
public class BaseLiveData<T> extends MutableLiveData<T> {
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer, boolean isSticky) {
        if(isSticky){
            super.observe(owner, observer);
        } else {
            super.observe(owner,new CustomObserver<T>(observer));
        }
    }

    @Override
    public void setValue(T value) {

        super.setValue(value);
    }

    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    class CustomObserver<T> implements Observer<T> {
        private Observer<? super T> mObserver;

        public CustomObserver(Observer<? super T> observer) {
            mObserver = observer;
        }

        @Override
        public void onChanged(T t) {
            //此处做拦截操作

            mObserver.onChanged(t);
        }
    }
}