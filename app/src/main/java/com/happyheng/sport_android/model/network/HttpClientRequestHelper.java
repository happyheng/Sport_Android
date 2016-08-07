package com.happyheng.sport_android.model.network;

import com.happyheng.sport_android.model.entity.PostRequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 获取HttpClient中Request请求对象的帮助类
 * Created by liuheng on 16/6/11.
 */
public class HttpClientRequestHelper {

    public static Request getRequest(String url, PostRequestBody[] bodys, okhttp3.CacheControl cacheControl) throws UnsupportedEncodingException {

        FormBody.Builder builder = new FormBody.Builder();
        for (PostRequestBody body : bodys) {
            //请求的value要经过编码
            //String encoderRequestString = URLEncoder.encode(body.value, "UTF8");
            builder.add(body.name, body.value);
        }

        RequestBody requestBody = builder.build();

        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .cacheControl(cacheControl)
                .build();
    }


}
