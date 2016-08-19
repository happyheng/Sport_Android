package com.happyheng.sport_android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.happyheng.sport_android.model.event.LocationEvent;
import com.happyheng.sport_android.servicecode.RecordService;
import com.happyheng.sport_android.servicecode.impl.RecordServiceImpl;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

/**
 * 定位的Service类，用户在运动时此服务会在后台进行定位。
 * Created by liuheng on 16/7/24.
 */
public class LocationService extends Service implements BDLocationListener {

    //定位的时间间隔，单位是毫秒
    private static final int LOCATION_SPAN = 10 * 1000;

    //百度地图中定位的类
    public LocationClient mLocationClient = null;
    //记录着运动中移动的坐标位置
    private List<LatLng> mSportLatLngs = new LinkedList<>();

    //记录运动信息的Service
    private RecordService mRecordService = null;

    @Override
    public void onCreate() {

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //给定位类加入自定义的配置
        initLocationOption();
        //注册监听函数
        mLocationClient.registerLocationListener(this);

        //初始化信息记录类
        mRecordService = new RecordServiceImpl(this);

        //开始监听
        mLocationClient.start();
    }

    //初始化定位的配置
    private void initLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(LOCATION_SPAN);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    //获取经纬度后回调的方法
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //先暂时获得经纬度信息，并将其记录在List中
        Logger.d("纬度信息为" + bdLocation.getLatitude()+"\n经度信息为" + bdLocation.getLongitude());
        LatLng locationValue = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        mSportLatLngs.add(locationValue);

        //将运动信息上传至服务器
        recordLocation(locationValue,bdLocation.getLocationDescribe());

        //定位成功，发送定位Event通知
        EventBus.getDefault().post(new LocationEvent(locationValue));
    }


    private void recordLocation(LatLng latLng, String location) {
        if (mRecordService != null) {
            mRecordService.recordSport(latLng, location);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(this);
        Logger.d("onDestroy");
    }
}
