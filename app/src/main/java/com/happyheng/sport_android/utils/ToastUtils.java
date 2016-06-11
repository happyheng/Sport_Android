package com.happyheng.sport_android.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司的辅助类
 * Created by liuheng on 16/6/8.
 */
public class ToastUtils {

    public static void showToast(Context context, String content) {
        showToast(context, content, false);
    }

    public static void showToast(Context context, String content, boolean isLongToast) {
        int length;
        if (isLongToast) {
            length = Toast.LENGTH_LONG;
        } else {
            length = Toast.LENGTH_SHORT;
        }

        Toast.makeText(context, content, length).show();
    }

}
