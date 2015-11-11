package com.haley.may.mayapp;

import android.app.Application;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by lenovo on 2015/11/4.
 */
public class MayApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }
}
