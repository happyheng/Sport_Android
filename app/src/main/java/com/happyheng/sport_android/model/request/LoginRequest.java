package com.happyheng.sport_android.model.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happyheng.sport_android.model.network.listener.OnRequestListener;

/**
 * 登陆请求的类
 * Created by liuheng on 16/6/8.
 */
public class LoginRequest extends BaseRequest{

    public static final int REQUEST_WRONG_USERNAME = 1; //没有相应用户名
    public static final int REQUEST_WRONG_PASSWORD = 2; //密码错误

    public static final String REQUEST_PATH = "Login";

    //登陆的用户名和密码
    private String userName,password;
    private OnLoginListener listener;

    public LoginRequest(String userName, String password, OnLoginListener listener) {
        this.userName = userName;
        this.password = password;
        this.listener = listener;
    }

    @Override
    protected String getRequestPath() {
        return REQUEST_PATH;
    }

    @Override
    protected String getRequestJsonString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uname", userName);
        jsonObject.put("upwd", password);
        return jsonObject.toString();
    }

    @Override
    protected OnRequestListener<String> getRequestListener() {
        return new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {

                if (listener != null){
                    JSONObject resultJson = JSON.parseObject(s);
                    int code = resultJson.getInteger(RESULT_KEY);
                    if (code == REQUEST_SUCCESS) {
                        String token = resultJson.getJSONObject("data").getString("token");
                        listener.onSuccess(token);
                    } else {
                        listener.onFail(code);
                    }
                }

            }

            @Override
            public void onFail() {
                if (listener!=null){
                    listener.onFail(REQUEST_OTHER_EXCEPTION);
                }
            }
        };
    }

    public interface OnLoginListener {
        void onSuccess(String token);

        void onFail(int code);
    }
}
