package com.happyheng.sport_android.utils;

import android.content.Context;
import android.content.Intent;

/**
 * 简单的跳转Activity的工具类
 * Created by liuheng on 16/6/7.
 */
public class SimpleStartActivityUtils {
    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }
}
