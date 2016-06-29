package com.happyheng.sport_android.model.network;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.happyheng.sport_android.model.BaseRequest;
import com.happyheng.sport_android.model.network.listener.OnRequestListener;

/**
 * 记录运动信息的Request
 * Created by liuheng on 16/6/29.
 */
public class RecordRequest extends BaseRequest {

    public static final String REQUEST_PATH = "Record";

    private String mUserKey;
    private int mSportId;
    private float mPosX, mPosY;
    private long mTime;
    private String mLocation;

    //包含userKey的构造方式
    public RecordRequest(String userKey, float posX, float poxY, long time, String location) {
        this.mUserKey = userKey;
        this.mPosX = posX;
        this.mPosY = poxY;
        this.mTime = time;
        this.mLocation = location;
    }

    public RecordRequest(int sportId, float posX, float poxY, long time, String location) {
        this.mSportId = sportId;
        this.mPosX = posX;
        this.mPosY = poxY;
        this.mTime = time;
        this.mLocation = location;
    }

    @Override
    protected OnRequestListener<String> getRequestListener() {
        return null;
    }

    @Override
    protected String getRequestPath() {
        return REQUEST_PATH;
    }

    @Override
    protected JSONObject getRequestJson() {
        JSONObject jsonObject = new JSONObject();
        if (!TextUtils.isEmpty(mUserKey)) {
            jsonObject.put("ukey",mUserKey);
        } else {
            jsonObject.put("id",mSportId);
        }
        jsonObject.put("posx",mPosX);
        jsonObject.put("posy",mPosY);
        jsonObject.put("time",mTime);
        jsonObject.put("location",mLocation);

        return jsonObject;
    }
}
