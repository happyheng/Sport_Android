package com.happyheng.sport_android.model.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happyheng.sport_android.model.User;
import com.happyheng.sport_android.model.network.listener.OnRequestListener;

/**
 * 得到运动id的Request
 * Created by liuheng on 16/7/5.
 */
public class SportIdRequest extends BaseRequest{


    private OnSportIdListener mSportIdListener;

    public SportIdRequest(OnSportIdListener sportIdListener){
        this.mSportIdListener = sportIdListener;
    }


    @Override
    protected OnRequestListener<String> getRequestListener() {
        return new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (mSportIdListener != null) {
                    JSONObject resultJson = JSON.parseObject(s);
                    int code = resultJson.getIntValue(RESULT_KEY);

                    if (code == REQUEST_SUCCESS) {
                        mSportIdListener.onSuccess(resultJson.getIntValue("id"));
                    }else {
                        mSportIdListener.onFail();
                    }
                }
            }

            @Override
            public void onFail() {
                if (mSportIdListener != null) {
                    mSportIdListener.onFail();
                }
            }
        };
    }

    @Override
    protected String getRequestPath() {
        return "SportId";
    }

    @Override
    protected String getRequestJsonString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ukey", User.getUser().getUserToken());
        return jsonObject.toString();
    }

    public interface OnSportIdListener {
        void onSuccess(int sportId);
        void onFail();
    }
}
