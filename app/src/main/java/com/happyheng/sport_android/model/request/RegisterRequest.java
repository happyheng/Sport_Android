package com.happyheng.sport_android.model.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happyheng.sport_android.model.network.listener.OnRequestListener;

/**
 * 注册请求的类
 * Created by liuheng on 16/6/25.
 */
public class RegisterRequest extends BaseRequest {

    private String mUserName, mPassWord, mNickName;
    private onRegisterListener mListener;

    public static final String REQUEST_PATH = "Register";

    //重复用户名
    public static final int REQUEST_REPEAT_USERNAME = 1;
    //重复昵称
    public static final int REQUEST_REPEAT_NICKNAME = 2;

    public RegisterRequest(String userName, String passWord, String nickName, onRegisterListener listener) {
        this.mUserName = userName;
        this.mPassWord = passWord;
        this.mNickName = nickName;
        this.mListener = listener;
    }

    @Override
    protected OnRequestListener<String> getRequestListener() {
        return new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {
                JSONObject resultJson = JSON.parseObject(s);
                int code = resultJson.getInteger(RESULT_KEY);

                //根据不同的code，回调不同的接口的方法
                if (code == REQUEST_SUCCESS) {
                    mListener.onSuccess();
                } else if (code == REQUEST_REPEAT_USERNAME) {
                    mListener.onRepeatUserName();
                } else if (code == REQUEST_REPEAT_NICKNAME) {
                    mListener.onRepeatNickName();
                } else {
                    mListener.onFail();
                }
            }

            @Override
            public void onFail() {
                mListener.onFail();
            }
        };
    }

    @Override
    protected String getRequestPath() {
        return REQUEST_PATH;
    }

    @Override
    protected String getRequestJsonString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uname", mUserName);
        jsonObject.put("upwd", mPassWord);
        jsonObject.put("nkname", mNickName);
        return jsonObject.toJSONString();
    }

    public interface onRegisterListener {
        void onSuccess();

        void onRepeatUserName();

        void onRepeatNickName();

        void onFail();
    }
}
