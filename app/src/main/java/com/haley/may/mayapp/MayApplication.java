package com.haley.may.mayapp;

import android.app.Application;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;

import de.greenrobot.event.EventBus;

/**
 * Created by lenovo on 2015/11/4.
 */
public class MayApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //百度地图初始化
        SDKInitializer.initialize(this);


    }
}
