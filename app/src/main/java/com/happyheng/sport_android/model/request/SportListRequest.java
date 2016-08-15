package com.happyheng.sport_android.model.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happyheng.sport_android.model.entity.SportListResult;
import com.happyheng.sport_android.model.network.listener.OnRequestListener;

import java.util.List;

/**
 * 请求Sport信息的Request
 * Created by liuheng on 16/8/15.
 */
public class SportListRequest extends BaseRequest {

    private String mUserKey;

    public SportListRequest(String userKey) {
        this.mUserKey = userKey;
    }

    @Override
    protected OnRequestListener<String> getRequestListener() {
        return new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (listener!=null){
                    SportListResult result = JSON.parseObject(s,SportListResult.class);

                    if (result != null && result.getResult() != REQUEST_OTHER_EXCEPTION){
                        listener.onSuccess(result.getData());
                    } else {
                        listener.onFail();
                    }
                }
            }

            @Override
            public void onFail() {
                if (listener!=null){
                    listener.onFail();
                }
            }
        };
    }

    @Override
    protected String getRequestPath() {
        return "SportList";
    }

    @Override
    protected String getRequestJsonString() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userkey", mUserKey);
        return jsonObject.toString();
    }

    public interface ListListener{
        void onSuccess(List<Integer> list);
        void onFail();
    }

    private ListListener listener;

    public void setListListener(ListListener listener){
        this.listener = listener;
    }
}
