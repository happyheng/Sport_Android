package com.happyheng.sport_android.model.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happyheng.sport_android.model.entity.Location;
import com.happyheng.sport_android.model.entity.SportMessageResult;
import com.happyheng.sport_android.model.network.listener.OnRequestListener;

import java.util.List;

/**
 * 请求详细运动数据的Request
 * Created by liuheng on 16/8/16.
 */
public class SportMessageRequest extends BaseRequest {


    private String userKey, sportId;

    public SportMessageRequest(String userKey, String sportId) {
        this.userKey = userKey;
        this.sportId = sportId;
    }

    @Override
    protected OnRequestListener<String> getRequestListener() {
        return new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {

                if (messageListener != null) {

                    SportMessageResult result = JSON.parseObject(s, SportMessageResult.class);
                    if (result != null && result.getResult() != REQUEST_OTHER_EXCEPTION) {
                        messageListener.onSuccess(result.getData());
                    } else {
                        messageListener.onFail();
                    }
                }

            }

            @Override
            public void onFail() {
                if (messageListener != null) {
                    messageListener.onFail();
                }
            }
        };
    }

    public interface OnSportMessageListener {
        void onSuccess(List<Location> data);

        void onFail();
    }

    private OnSportMessageListener messageListener;

    public void setMessageListener(OnSportMessageListener listener) {
        this.messageListener = listener;
    }

    @Override
    protected String getRequestPath() {
        return "SportMessage";
    }

    @Override
    protected String getRequestJsonString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userkey", userKey);
        jsonObject.put("sportId", sportId);
        return jsonObject.toString();
    }
}
