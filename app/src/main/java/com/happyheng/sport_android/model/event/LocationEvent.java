package com.happyheng.sport_android.model.event;

import com.baidu.mapapi.model.LatLng;

/**
 * 定位的Event
 * Created by liuheng on 16/7/24.
 */
public class LocationEvent {

    private LatLng location;

    public LocationEvent(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }

}
