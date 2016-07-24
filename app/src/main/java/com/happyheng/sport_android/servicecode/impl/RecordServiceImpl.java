package com.happyheng.sport_android.servicecode.impl;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.happyheng.sport_android.model.request.RecordSportMessageRequest;
import com.happyheng.sport_android.model.request.SportIdRequest;
import com.happyheng.sport_android.servicecode.RecordService;
import com.orhanobut.logger.Logger;

/**
 * 记录运动信息的实现类
 * Created by liuheng on 16/7/5.
 */
public class RecordServiceImpl implements RecordService {

    private  static final int SPORT_DEFAULT_ID = -1;

    private Context mContext;
    private int mSportId = SPORT_DEFAULT_ID;
    //分别是记录SportId的Request，和记录Sport信息的Request
    private SportIdRequest mSportIdRequest;
    private RecordSportMessageRequest mSportMessageRecordRequest;

    //获取SportId的回调
    private SportIdRequest.OnSportIdListener mGetSportIdListener = new SportIdListener();

    public RecordServiceImpl(Context context) {
        this.mContext = context;
        mSportIdRequest = new SportIdRequest(mGetSportIdListener);
        mSportMessageRecordRequest = new RecordSportMessageRequest();
    }

    @Override
    public void recordSport(LatLng latLng, String location) {
        //如果sportId尚未获取的话，那么就继续获取
        if (mSportId == SPORT_DEFAULT_ID) {
            mSportIdRequest.doRequest();
        }
        else {
            //如果获取成功，那么
            RecordSportMessageRequest.SportMessage  message = new RecordSportMessageRequest.SportMessage(mSportId,latLng.longitude,latLng.latitude,location);
            mSportMessageRecordRequest.setMessage(message);
            mSportMessageRecordRequest.doRequest();
        }
    }

    private class  SportIdListener implements SportIdRequest.OnSportIdListener{

        @Override
        public void onSuccess(int sportId) {
            Logger.d("获取的sportId为"+sportId);
            mSportId = sportId;
        }

        @Override
        public void onFail() {

        }
    }
}
