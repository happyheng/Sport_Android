package com.happyheng.sport_android.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happyheng.sport_android.model.entity.BaseResult;
import com.happyheng.sport_android.model.network.listener.OnRequestListener;

/**
 * 上传用户运动信息的Request
 * Created by liuheng on 16/7/7.
 */
public class RecordSportMessageRequest extends BaseRequest{

    public static final String REQUEST_PATH = "Record";
    //需要上传的运动信息
    public SportMessage mMessage;

    public void setMessage(SportMessage message){
        this.mMessage = message;
    }

    @Override
    protected OnRequestListener<String> getRequestListener() {
        return new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (mRecordListener != null){
                    BaseResult result = JSONObject.parseObject(s,BaseResult.class);
                    if (result.getResult() == REQUEST_SUCCESS){
                        mRecordListener.onSuccess();
                    } else {
                        mRecordListener.onFail();
                    }
                }
            }

            @Override
            public void onFail() {
                if (mRecordListener != null){
                    mRecordListener.onFail();
                }
            }
        };
    }

    @Override
    protected String getRequestPath() {
        return REQUEST_PATH;
    }

    @Override
    protected String getRequestJsonString() {
        if (mMessage!=null){
            return JSON.toJSONString(mMessage);
        } else {
            return null;
        }
    }

    /**
     * 封装了上传至服务器运动信息的类
     */
    public class SportMessage{
        public int id;
        public float posx;
        public float posy;
        public String location;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getPosx() {
            return posx;
        }

        public void setPosx(float posx) {
            this.posx = posx;
        }

        public float getPosy() {
            return posy;
        }

        public void setPosy(float posy) {
            this.posy = posy;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

    public interface OnRecordListener{
        void onSuccess();
        void onFail();
    }

    private OnRecordListener mRecordListener;

    public void setRecordListener(OnRecordListener listener){
        this.mRecordListener = listener;
    }

}
