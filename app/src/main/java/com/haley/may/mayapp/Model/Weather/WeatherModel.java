package com.haley.may.mayapp.Model.Weather;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by haley on 2015/11/27.
 */
public class WeatherModel {

    //region variable
    private String city;
    private String date;
    private String week;
    private String tempNow;
    private String sunRise;
    private String sunSet;
    private String updateTime;
    private List<WeatherItem> itemList = new ArrayList<WeatherItem>();

    private boolean isInited = false;
    //endregion

    //region structure
    public WeatherModel() {

    }
    //endregion

    //region set get
    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }

    public String getWeek() {
        return week;
    }

    public String getTempNow() {
        return tempNow;
    }

    public String getSunRise() {
        return sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public List<WeatherItem> getItemList() {
        return itemList;
    }
    //endregion

    //region function
    public void init(String xml)
    {
        date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date(System.currentTimeMillis()));
        week = new SimpleDateFormat("EEEE").format(new Date(System.currentTimeMillis()));

        String tag = "";

        WeatherItem item = null;
        try {
            XmlPullParser parser = Xml.newPullParser();//得到Pull解析器
            parser.setInput(new StringReader(xml));//设置下输入
            int eventType = parser.getEventType();//得到第一个事件类型
            while (eventType != XmlPullParser.END_DOCUMENT) {//如果事件类型不是文档结束的话则不断处理事件
                switch (eventType) {
                    case (XmlPullParser.START_DOCUMENT)://如果是文档开始事件
                        break;
                    case (XmlPullParser.START_TAG)://如果遇到标签开始
                        if (parser.getName().equals("city"))
                            this.city = parser.nextText();
                        if (parser.getName().equals("wendu"))
                            this.tempNow = parser.nextText() + "℃";
                        if (parser.getName().equals("sunrise_1"))
                            this.sunRise = parser.nextText();
                        if (parser.getName().equals("sunset_1"))
                            this.sunSet = parser.nextText();
                        if (parser.getName().equals("updatetime"))
                            this.updateTime = parser.nextText();

                        //以下为一步步获取信息
                        if (parser.getName().equals("forecast"))
                            tag = parser.getName();

                        if (parser.getName().equals("weather") && tag.equals("forecast"))
                            item = new WeatherItem();

                        if (parser.getName().equals("date") && item != null) {
                            String str = parser.nextText();
                            item.date = new SimpleDateFormat("yyyy年MM月").format(new Date(System.currentTimeMillis())) + str.split("日")[0] + "日";
                            item.week = str.split("日")[1];
                        }

                        if (parser.getName().equals("low") && item != null)
                            item.minTemp = parser.nextText().split(" ")[1];

                        if (parser.getName().equals("high") && item != null)
                            item.maxTemp = parser.nextText().split(" ")[1];

                        if (parser.getName().equals("type") && item != null) {
                            if (item.type_day == null)
                                item.type_day = parser.nextText();
                            else
                                item.type_night = parser.nextText();
                        }
                        //Log.i("WeatherInfo","-->>" + parser.getName());
                        break;
                    case (XmlPullParser.END_TAG)://如果遇到标签结束
                        if (parser.getName().equals("weather") && item != null) {
                            this.itemList.add(item);
                            item = null;
                        }
                        if (parser.getName().equals("forecast"))
                            tag = "";
                        break;
                }
                eventType = parser.next();//进入下一个事件处理
            }
            this.isInited = true;
        } catch (Exception ce) {
            this.isInited = false;
            Log.i("WeatherInfo", "初始化错误-->>" + ce.toString());
        }
    }

    public String getNowType(){
        try {
            long now = System.currentTimeMillis();
            long sunRise = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(new SimpleDateFormat("yyyy-MM-dd ").format(new Date(System.currentTimeMillis())) + this.sunRise).getTime();
            long sunSet = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(new SimpleDateFormat("yyyy-MM-dd ").format(new Date(System.currentTimeMillis())) + this.sunSet).getTime();

            if (now < sunSet)
                return this.itemList.get(0).type_day;
            else
                return this.itemList.get(0).type_night;
        }
        catch (Exception ce) {
            return "";
        }
    }
    //endregion

    //region class:WeatherItem
    public class WeatherItem{
        private String minTemp;
        private String maxTemp;
        private String date;
        private String week;
        private String type_day;
        private String type_night;
    }
    //endregion
}
