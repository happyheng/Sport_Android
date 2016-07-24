package com.happyheng.sport_android.activity;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.happyheng.sport_android.R;
import com.happyheng.sport_android.service.LocationService;
import com.happyheng.sport_android.servicecode.RecordService;
import com.happyheng.sport_android.servicecode.impl.RecordServiceImpl;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 地图的Activity
 * Created by liuheng on 16/6/26.
 */
public class MapActivity extends BaseActivity{

    //显示的百度MapView
    private MapView mMapView = null;
    private BaiduMap mMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMap = mMapView.getMap();

        startLocationService();
    }

    //启动定位的Service
    private void startLocationService(){
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }


    //获取经纬度后回调的方法
    public void onReceiveLocation(BDLocation bdLocation) {
        //先暂时获得经纬度信息，并将其记录在List中
        Logger.d("纬度信息为" + bdLocation.getLatitude()+"\n经度信息为" + bdLocation.getLongitude());

        //进行定位
        location(bdLocation);

        //绘制位置
        //drawLocation()
    }

    //根据定位信息进行定位，即移动地图至当前点
    private void location(BDLocation bdLocation) {

        //可以用下面的方法对在得到经纬度信息后在百度地图中进行显示
        BaiduMap mMap = mMapView.getMap();

        // 开启定位图层
        mMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100)
                .latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude())
                .build();

        //设置定位的配置信息
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        mMap.setMyLocationConfigeration(configuration);
        // 设置定位数据
        mMap.setMyLocationData(locData);
    }

    //根据当前的左边画出点数和直线
//    private void drawLocation() {
//
//        //1、画出当前的点数
//        LatLng nowLatLng = mSportLatLngs.get(mSportLatLngs.size()-1);
//        OverlayOptions dotOptions = new DotOptions().center(nowLatLng);
//        mMap.addOverlay(dotOptions);
//
//        //2、如果不为1，那么还要画一个指向之前的横线
//        if (mSportLatLngs.size() != 1){
//            LatLng beforeLatLng = mSportLatLngs.get(mSportLatLngs.size()-2);
//            List<LatLng> linePoints = new ArrayList<>();
//            linePoints.add(beforeLatLng);
//            linePoints.add(nowLatLng);
//            OverlayOptions lineOptions = new PolylineOptions().points(linePoints);
//            mMap.addOverlay(lineOptions);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }


}
