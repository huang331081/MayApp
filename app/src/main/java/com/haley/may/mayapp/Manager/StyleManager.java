package com.haley.may.mayapp.Manager;

import android.os.Bundle;

import com.haley.may.mayapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 2015/10/23.
 */
public class StyleManager {

    private List<String> styles = new ArrayList<String>();
    private HashMap<String,StyleInfo> styleHashMap = new HashMap<String,StyleInfo>();

    private String previousStyleName = "default";
    private String styleName = "default";

    private static StyleManager instance = new StyleManager();

    public static StyleManager getInstance(){
        return instance;
    }

    private StyleManager(){

        previousStyleName = "default";
        styleName = "default";

        this.styles.add("default");
        this.styleHashMap.put("default", new StyleInfo(R.color.DefaultBackColor));

        this.styles.add("weatherbad");
        this.styleHashMap.put("weatherbad", new StyleInfo(R.color.WeatherBadBackColor));

        this.styles.add("weathergood");
        this.styleHashMap.put("weathergood", new StyleInfo(R.color.WeatherGoodBackColor));
    }

    public String getStyleName() {
        return styleName;
    }

    public StyleInfo getStyle(){
        return styleHashMap.get(styleName);
    }

    public StyleInfo getPreviousStyle(){
        return styleHashMap.get(previousStyleName);
    }

    public void setStyle(String styleName){
        if (this.styles.contains(styleName) && this.styleName != styleName){
            this.previousStyleName = this.styleName;
            this.styleName = styleName;

            MayFragmentManager.getInstance().setStyle();
        }
    }

    public class StyleInfo{
        private int backGround;

        public StyleInfo(int backGround){
            this.backGround = backGround;
        }

        public int getBackGround() {
            return backGround;
        }
    }
}
