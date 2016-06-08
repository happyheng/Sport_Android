package com.happyheng.sport_android.model.network.listener;

/**
 * 异步请求的接口
 * Created by liuheng on 2016/6/7.
 */
public interface OnRequestListener<T>{
    void onSuccess(T t);
    void onFail();
}
