package com.happyheng.sport_android.model;

import android.content.Context;
import android.text.TextUtils;

import com.happyheng.sport_android.utils.SPUtils;

/**
 * User类封装了用户操作的一系列信息
 * Created by liuheng on 16/6/13.
 */
public class User {

    //储存用户token的key
    private static final String TOKEN_KEY = "token";
    private static User defaultUser;

    private Context mContext;

    /**
     * 初始化单例User对象
     * @param context
     */
    public static void init(Context context){
        if (defaultUser == null){
            defaultUser = new User(context);
        }
    }

    /**
     * 得到单例对象
     * @return
     */
    public static User getUser(){
        return  defaultUser;
    }


    private User(Context context){
        this.mContext = context;
    }

    /**
     * 得到用户是否登录的方法
     * @return
     */
    public boolean isUserLogin(){
        String token = getUserToken();
        return !TextUtils.isEmpty(token);
    }


    /**
     * 设置User的Token
     *
     * @param token
     */
    public void setUserToken(String token) {
        SPUtils.put(mContext, TOKEN_KEY, token);
    }


    /**
     * 得到User的Token
     * @return
     */
    public  String getUserToken(){
        return (String) SPUtils.get(mContext,TOKEN_KEY,"");
    }

}
