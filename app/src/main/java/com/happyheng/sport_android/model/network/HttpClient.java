package com.happyheng.sport_android.model.network;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.happyheng.sport_android.model.network.listener.OnRequestListener;
import com.happyheng.sport_android.utils.ThreadUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 基于"Okhttp"的网络访问工具类
 * 注意在所有的请求中，都会有请求对应的路径名，和对应的键值对，需要把键值对序列化成json，然后请求时让s对应此json。这样是为了
 * 将数据加密
 * Created by liuheng on 2016/6/7.
 */
public class HttpClient {


    private static final String BASE_URL = "http://192.168.1.15:8080/Sport/";
    private static final int CacheSize = 50 * 1024 * 1024; // 缓存的大小,默认为50 MiB
    private static final String DEFAULT_NULL_RESULT = "";  //默认的空返回值


    private static OkHttpClient mOkHttpClient = null;


    public enum CacheControl {
        //先从缓存中取，如果没有，在从网络上去取
        CacheElseNetwork,
        //只从网络中去取
        Network
    }

    /**
     * 初始化请求工具类的方法
     */
    public static void init(Context context) {
        //写到缓存下的子目录下，方便统一管理
        Cache cache = new Cache(context.getExternalCacheDir(), CacheSize);
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .build();
        }

    }


    /**
     * 同步从网络上获取数据
     *
     * @param path          请求的path
     * @param requestString 此String是请求的数据转换的json字符串
     * @return 返回请求的json字符串，如果失败，返回""
     */
    public static String doSyncPost(String path, String requestString) {

        Request request;
        try {
            request = getRequest(path, requestString, okhttp3.CacheControl.FORCE_NETWORK);
            Response response = mOkHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {

                return response.body().string();
            } else {
                Logger.d("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DEFAULT_NULL_RESULT;
    }


    /**
     * 异步去网络上请求数据的方法
     *
     * @param path            请求的path
     * @param requestString   请求的数据，注意须是json数据生成的String数据
     * @param requestListener 请求回调的接口  两种情况:
     *                        请求完全成功后，才会调用onSuccess()方法，其它调用onFail()方法
     */
    public static void doAsyncPost(String path, final String requestString, final OnRequestListener<String> requestListener) {
        final Request request;
        try {
            request = getRequest(path, requestString, okhttp3.CacheControl.FORCE_NETWORK);

            mOkHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(Call call, final Response response) throws IOException {


                    ThreadUtils.runOnNewThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                if (response.isSuccessful()) {
                                    requestListener.onSuccess(response.body().string());
                                }
                                //在这里失败，即为返回的code不在200-300之间，意思即为服务器出现的错误，这种情况很少出现，但视为网络错误
                                else {
                                    requestListener.onFail();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                requestListener.onFail();
                            }

                        }
                    });


                }

                //一般来说，如果是失败的话，一般都为网络异常，比如手机未联网等
                @Override
                public void onFailure(Call call, IOException e) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            requestListener.onFail();
                        }
                    });
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            requestListener.onFail();

        }

    }


    /**
     * 提供"缓存"的异步方法，具体的缓存方法为:
     *
     * 1、先从本地去取缓存，取到缓存后，先回调成功接口，然后走下一步，如果没有取到缓存，直接走下一步
     * 2、不管取没取到，都会从网络取新的数据，如果失败，回调失败接口。如果成功，回调成功接口。
     */


    /**
     * 根据请求的数据等来获取Okhttp中的Request请求对象
     */
    private static Request getRequest(String path, String requestString, okhttp3.CacheControl cacheControl) throws UnsupportedEncodingException {

        String encoderRequestString = URLEncoder.encode(requestString, "UTF8");

        RequestBody requestBody = new FormBody.Builder()
                .add("s", encoderRequestString)
                .build();

        return new Request.Builder()
                .url(BASE_URL + path)
                .post(requestBody)
                .cacheControl(cacheControl)
                .build();
    }


}
