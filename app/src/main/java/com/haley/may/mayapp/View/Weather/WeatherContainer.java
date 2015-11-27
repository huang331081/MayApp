package com.haley.may.mayapp.View.Weather;

import android.content.Context;
import android.view.LayoutInflater;

import com.haley.may.mayapp.R;
import com.haley.may.mayapp.Base.BaseContainer;

/**
 * Created by lenovo on 2015/10/23.
 */
public class WeatherContainer extends BaseContainer{
    public WeatherContainer(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_weather, this);
    }
}
