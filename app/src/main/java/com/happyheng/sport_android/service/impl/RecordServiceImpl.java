package com.happyheng.sport_android.service.impl;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.happyheng.sport_android.model.BaseRequest;
import com.happyheng.sport_android.model.SportIdRequest;
import com.happyheng.sport_android.service.RecordService;
import com.orhanobut.logger.Logger;

/**
 * 记录运动信息的实现类
 * Created by liuheng on 16/7/5.
 */
public class RecordServiceImpl implements RecordService {

    private  static final int SPORT_DEFAULT_ID = -1;

    private Context mContext;
    private int mSportId = SPORT_DEFAULT_ID;
    private BaseRequest mSportIdRequest;

    //获取SportId的回调
    private SportIdRequest.OnSportIdListener mGetSportIdListener = new SportIdListener();

    public RecordServiceImpl(Context context) {
        this.mContext = context;
        mSportIdRequest = new SportIdRequest(mGetSportIdListener);
    }

    @Override
    public void recordSport(LatLng latLng, String location) {
        if (mSportId == SPORT_DEFAULT_ID){
            mSportIdRequest.doRequest();
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
