package com.haley.may.mayapp.Style;

import com.haley.may.mayapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by haley on 2015/11/23.
 */
public class StyleManager {

    private List<String> styles = new ArrayList<String>();//style的名称集合
    private HashMap<String,StyleEventInfo> styleHashMap = new HashMap<String,StyleEventInfo>();//style名称集合对应的style类的hashmap
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
        this.styleHashMap.put("default", new StyleEventInfo(R.color.DefaultBackColor));

        this.styles.add("weatherbad");
        this.styleHashMap.put("weatherbad", new StyleEventInfo(R.color.WeatherBadBackColor));

        this.styles.add("weathergood");
        this.styleHashMap.put("weathergood", new StyleEventInfo(R.color.WeatherGoodBackColor));
    }

    public String getStyleName() {
        return styleName;
    }

    public StyleEventInfo getStyle(){
        return styleHashMap.get(styleName);
    }

    public StyleEventInfo getPreviousStyle(){
        return styleHashMap.get(previousStyleName);
    }

    public void setStyle(String styleName){
        if (this.styles.contains(styleName) && this.styleName != styleName) {
            this.previousStyleName = this.styleName;
            this.styleName = styleName;
            EventBus.getDefault().post(this.getStyle());
        }
    }

    //region calss:StyleEventInfo
    public class StyleEventInfo {
        private int backGround;

        public StyleEventInfo(int backGround){
            this.backGround = backGround;
        }

        public int getBackGround() {
            return backGround;
        }
    }
    //endregion
}
