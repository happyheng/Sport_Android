package com.happyheng.sport_android.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happyheng.sport_android.model.network.HttpClient;
import com.happyheng.sport_android.model.network.listener.OnRequestListener;

import org.json.JSONException;

/**
 * 所有网络访问的请求封装类，会把每一次请求都做好封装
 * Created by liuheng on 16/6/8.
 */
public class LoginRequest {

    public static final int REQUEST_SUCCESS = 0; //成功

    public static final int REQUEST_WRONG_USERNAME = 1; //没有相应用户名
    public static final int REQUEST_WRONG_PASSWORD = 2; //密码错误

    public static final int REQUEST_OTHER_EXCEPTION = 100; //服务器错误，请求错误等等非逻辑错误，都是这个

    public static void onLogin(String userName, String passWord, final OnLoginListener listener) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uname", userName);
            jsonObject.put("upwd", passWord);

            HttpClient.doAsyncPost("Login", jsonObject.toString(), new OnRequestListener<String>() {
                @Override
                public void onSuccess(String s) {

                    JSONObject resultJson = JSON.parseObject(s);
                    int code = resultJson.getInteger("result");
                    if (code == REQUEST_SUCCESS) {
                        String token = resultJson.getJSONObject("data").getString("token");
                        listener.onSuccess(token);
                    } else {
                        listener.onFail(code);
                    }
                }

                @Override
                public void onFail() {
                    listener.onFail(REQUEST_OTHER_EXCEPTION);
                }
            });


        } catch (Exception e) {
            listener.onFail(REQUEST_OTHER_EXCEPTION);
        }
    }

    public interface OnLoginListener {
        void onSuccess(String token);

        void onFail(int code);
    }
}
