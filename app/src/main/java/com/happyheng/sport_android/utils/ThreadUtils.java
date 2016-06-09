package com.happyheng.sport_android.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 线程的工具类
 * Created by liuheng on 16/6/9.
 */
public class ThreadUtils {

    /**
     * 在子线程中执行
     * @param runnable
     */
    public static void runOnNewThread(Runnable runnable){
        new Thread(runnable).start();
    }

    /**
     * 在主线程中执行
     * @param runnable
     */
    public static void runOnMainThread(Runnable runnable){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }


}
