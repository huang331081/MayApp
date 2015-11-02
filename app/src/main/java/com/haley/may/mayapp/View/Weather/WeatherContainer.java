package com.haley.may.mayapp.View.Weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.haley.may.mayapp.Manager.StyleManager;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.View.Base.BaseContainer;

/**
 * Created by lenovo on 2015/10/23.
 */
public class WeatherContainer extends BaseContainer{


    public WeatherContainer(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_weather, this);
    }


    @Override
    public void setStyle(StyleManager.StyleInfo previousStyle, StyleManager.StyleInfo style) {
        if (!previousStyle.equals(style)) {
            int beginRGB = this.getContext().getResources().getColor(previousStyle.getBackGround());
            final int beginR = beginRGB & 0xff;
            final int beginG = (beginRGB & 0xff00) >> 8;
            final int beginB = (beginRGB & 0xff0000) >> 16;

            //int endRGB = 0xff0080FF;
            int endRGB = this.getContext().getResources().getColor(style.getBackGround());

            final int endR = endRGB & 0xff;
            final int endG = (endRGB & 0xff00) >> 8;
            final int endB = (endRGB & 0xff0000) >> 16;


            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {

                    int tempR = (int) (beginR + (endR - beginR) * interpolatedTime);
                    int tempG = (int) (beginG + (endG - beginG) * interpolatedTime);
                    int tempB = (int) (beginB + (endB - beginB) * interpolatedTime);

                    WeatherContainer.this.setBackgroundColor(tempR | tempG << 8 | tempB << 16 | 0xff000000);
                    WeatherContainer.this.invalidate();

                    super.applyTransformation(interpolatedTime, t);
                }
            };
            animation.setDuration(1000);
            this.startAnimation(animation);
        }

        super.setStyle(previousStyle,style);
    }
}
