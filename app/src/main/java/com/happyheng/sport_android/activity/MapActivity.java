package com.happyheng.sport_android.activity;

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
import com.happyheng.sport_android.service.RecordService;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 地图的Activity
 * Created by liuheng on 16/6/26.
 */
public class MapActivity extends BaseActivity implements BDLocationListener {

    //定位的时间间隔，单位是毫秒
    private static final int LOCATION_SPAN = 10 * 1000;

    //显示的百度MapView
    private MapView mMapView = null;
    private BaiduMap mMap = null;

    //百度地图中定位的类
    public LocationClient mLocationClient = null;
    //记录着运动中移动的坐标位置
    private List<LatLng> mSportLatLngs = new LinkedList<>();
    //记录运动信息的Service
    private RecordService mRecordService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMap = mMapView.getMap();

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //给定位类加入自定义的配置
        initLocationOption();
        //注册监听函数
        mLocationClient.registerLocationListener(this);

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
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
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

        //进行定位
        location(bdLocation);

        //绘制位置
        drawLocation();

        //将运动信息上传至服务器
        recordLocation(locationValue,bdLocation.getLocationDescribe());
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
    private void drawLocation() {

        //1、画出当前的点数
        LatLng nowLatLng = mSportLatLngs.get(mSportLatLngs.size()-1);
        OverlayOptions dotOptions = new DotOptions().center(nowLatLng);
        mMap.addOverlay(dotOptions);

        //2、如果不为1，那么还要画一个指向之前的横线
        if (mSportLatLngs.size() != 1){
            LatLng beforeLatLng = mSportLatLngs.get(mSportLatLngs.size()-2);
            List<LatLng> linePoints = new ArrayList<>();
            linePoints.add(beforeLatLng);
            linePoints.add(nowLatLng);
            OverlayOptions lineOptions = new PolylineOptions().points(linePoints);
            mMap.addOverlay(lineOptions);
        }
    }

    private void recordLocation(LatLng latLng, String location) {
        if (mRecordService != null) {
            mRecordService.recordSport(latLng, location);
        }
    }

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
