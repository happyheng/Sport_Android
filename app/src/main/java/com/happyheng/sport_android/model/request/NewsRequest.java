package com.happyheng.sport_android.model.request;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happyheng.sport_android.model.News;
import com.happyheng.sport_android.model.entity.NewsResult;
import com.happyheng.sport_android.model.network.listener.OnRequestListener;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * '发现频道'的请求类
 * Created by liuheng on 16/7/20.
 */
public class NewsRequest extends BaseRequest {

    public static final String REQUEST_PATH = "News";
    public static final int DEFAULT_VALUE = -1;

    private int mBeginPosition = DEFAULT_VALUE, mId = DEFAULT_VALUE, mCount;

    public void setBeginCount(int beginPosition, int count) {
        this.mBeginPosition = beginPosition;
        this.mCount = count;
    }

    public void setIdCount(int id, int count) {
        this.mId = id;
        this.mCount = count;
    }


    /**
     * 通过获取新闻数据源的方法
     * @return
     */
    public List<News> getNewsItem() {

        //进行同步请求
        String resultJson = doSyncPost();

        if (!TextUtils.isEmpty(resultJson)){
            NewsResult result = JSON.parseObject(resultJson, NewsResult.class);
            if (result != null && result.getCode() == REQUEST_SUCCESS) {
                return result.getData();
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    @Override
    protected OnRequestListener<String> getRequestListener() {
        return new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {

                if (mNewsListener != null) {
                    NewsResult result = JSON.parseObject(s, NewsResult.class);

                    if (result != null && result.getCode() == REQUEST_SUCCESS) {
                        mNewsListener.onSuccess(result.getData());
                    } else {
                        mNewsListener.onFail();
                    }
                }

            }

            @Override
            public void onFail() {
                if (mNewsListener != null) {
                    mNewsListener.onFail();
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

        JSONObject jsonObject = new JSONObject();
        if (mId == DEFAULT_VALUE) {
            jsonObject.put("begin", mBeginPosition);
        } else {
            jsonObject.put("id", mId);
        }
        jsonObject.put("count", mCount);

        return jsonObject.toString();
    }

    public interface OnNewsListener {
        void onSuccess(List<News> list);

        void onFail();
    }

    private OnNewsListener mNewsListener;

    public void setNewsListener(OnNewsListener newsListener) {
        this.mNewsListener = newsListener;
    }
}
