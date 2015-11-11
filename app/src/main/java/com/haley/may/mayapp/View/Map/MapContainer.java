package com.haley.may.mayapp.View.Map;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.haley.may.mayapp.View.Base.BaseContainer;

/**
 * Created by lenovo on 2015/11/4.
 */
public class MapContainer extends BaseContainer {
    public MapContainer(Context context) {
        super(context);

        final MapView mapView = new MapView(context);
        this.addView(mapView);



        BDLocationListener listener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.i("MapContainer", "-->>" + bdLocation.getAddrStr());
                mapView.getMap().setMyLocationEnabled(true);
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        .direction(100)
                        .latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude())
                        .build();
                mapView.getMap().setMyLocationData(myLocationData);
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mapView.getMap().animateMapStatus(mapStatusUpdate);
            }
        };
        LocationClient locationClient = new LocationClient(context.getApplicationContext());
        locationClient.registerLocationListener(listener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

        locationClient.setLocOption(option);
        locationClient.start();
    }
}
