package com.happyheng.sport_android.activity;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * 项目中所有Activity的基类
 * Created by liuheng on 16/6/26.
 */
public class BaseActivity extends FragmentActivity{

    protected void showToast(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_LONG).show();
    }

}
