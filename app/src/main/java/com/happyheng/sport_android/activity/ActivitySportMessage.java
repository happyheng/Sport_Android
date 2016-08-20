package com.happyheng.sport_android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.happyheng.sport_android.R;
import com.happyheng.sport_android.model.User;
import com.happyheng.sport_android.model.entity.Location;
import com.happyheng.sport_android.model.request.BaseRequest;
import com.happyheng.sport_android.model.request.SportMessageRequest;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示运动轨迹的Activity
 * Created by liuheng on 16/8/16.
 */
public class ActivitySportMessage extends BaseActivity implements SportMessageRequest.OnSportMessageListener{

    public static final String TAG_SPORT_ID = "sport_id";

    //显示的百度MapView
    private MapView mMapView = null;
    private BaiduMap mMap = null;

    private int mSportId;
    private SportMessageRequest request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_message);

        initView();
        getData();
    }

    private void initView() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMap = mMapView.getMap();
    }

    private void getData(){
        mSportId = getIntent().getIntExtra(TAG_SPORT_ID,0);
        request = new SportMessageRequest(User.getUser().getUserToken(),String.valueOf(mSportId));
        request.setMessageListener(this);
        request.doRequest();
    }

    @Override
    public void onSuccess(List<Location> data) {

        if (!data.isEmpty()){
            Location firstLocation = data.get(0);

            //1、定位至第一个点，然后缩小地图范围
            //此为设置缩放比例为15级
            mMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(18).build()));
            LatLng firstLatLng = new LatLng(firstLocation.getPosy(),firstLocation.getPosx());
            location(firstLatLng);

            //2、绘制运动信息
            List<LatLng> latLngList = new ArrayList<>();
            List<OverlayOptions> optionsList = new ArrayList<>();
            for (Location location : data){
                LatLng latLng = new LatLng(location.getPosy(),location.getPosx());
                latLngList.add(latLng);

                OverlayOptions options = new DotOptions().center(latLng);
                optionsList.add(options);
            }

            //画点
            mMap.addOverlays(optionsList);

            //画折线
            if(latLngList.size() != 1){
                OverlayOptions lineOptions = new PolylineOptions().points(latLngList);
                mMap.addOverlay(lineOptions);
            }
        }
    }


    //根据定位信息进行定位，即移动地图至当前点
    private void location(LatLng bdLocation) {

        //可以用下面的方法对在得到经纬度信息后在百度地图中进行显示
        BaiduMap mMap = mMapView.getMap();

        // 开启定位图层
        mMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100)
                .latitude(bdLocation.latitude)
                .longitude(bdLocation.longitude)
                .build();

        //设置定位的配置信息
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        mMap.setMyLocationConfigeration(configuration);
        // 设置定位数据
        mMap.setMyLocationData(locData);
    }

    @Override
    public void onFail() {
        Logger.d("请求失败");
    }
}
