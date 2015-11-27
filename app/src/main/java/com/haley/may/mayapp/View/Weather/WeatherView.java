package com.haley.may.mayapp.View.Weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import com.haley.may.mayapp.Style.StyleManager;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.System.Public;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by lenovo on 2015/10/16.
 */
public class WeatherView extends View {

    private WeatherInfo weatherInfo = null;

    public WeatherView(Context context) {
        super(context);

        try {
           this.init();
        }
        catch (Exception ce){
            Log.i("WeatherView","初始化出错-->>" + ce.toString());
        }
    }

    public WeatherView(Context context,AttributeSet attributeSet){
        super(context, attributeSet);

        try {
            this.init();
        }
        catch (Exception ce){
            Log.i("WeatherView","初始化出错-->>" + ce.toString());
        }
    }

    public void init() throws MalformedURLException {

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {

                Log.i("WeatherDownLoadTask","-->>doInBackground");
                if (!Public.isNetworkConnected((Context) params[1])) {
                    Log.i("", "-->>无网络连接");
                    return null;
                }

                try {
                    String data = "";
                    HttpURLConnection urlConnection = (HttpURLConnection)((URL)params[0]).openConnection();

                    urlConnection.setDoInput(true);
                    urlConnection.setRequestMethod("GET");

                    InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader buffer = new BufferedReader(in);
                    String line = null;

                    while ((line = buffer.readLine()) != null){
                        data += line;
                    }
                    in.close();
                    urlConnection.disconnect();

                    return data;

                }
                catch (IOException e) {
                    Log.i("WeatherDownLoadTask","error-->>"+e.toString());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Object o) {

                super.onPostExecute(o);
                setData((String)o);
            }
        };
        task.execute(new URL("http://wthrcdn.etouch.cn/WeatherApi?citykey=101210101"),this.getContext());
    }

    public void setData(String data){

        this.weatherInfo = new WeatherInfo(data);
        if (this.weatherInfo.getNowType().equals("多云") || this.weatherInfo.getNowType().equals("晴"))
            StyleManager.getInstance().setStyle("weathergood");
        else
            StyleManager.getInstance().setStyle("weatherbad");

        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float dis = 20,top = 0;
        float dateSize = 50;
        float citySize = 80,cityTop = top+20;
        float tempSize = 350,tempTop = cityTop + citySize + 50;
        float typeTempSize = 60,typeTempTop = tempTop + tempSize + 150;
        float sunSize = 50,sunTop = typeTempTop + typeTempSize + 150;

        String city = this.weatherInfo != null && this.weatherInfo.city != null ? this.weatherInfo.city : "";
        String date_y = this.weatherInfo != null && this.weatherInfo.date != null ? this.weatherInfo.date : "";
        String week = this.weatherInfo != null && this.weatherInfo.week != null ? this.weatherInfo.week : "";
        String temp_now = this.weatherInfo != null && this.weatherInfo.tempNow != null ? this.weatherInfo.tempNow : "";
        String type_and_temp = this.weatherInfo != null && this.weatherInfo.itemList.size() > 0 ? this.weatherInfo.getNowType() + "  " + this.weatherInfo.itemList.get(0).minTemp + "-" + this.weatherInfo.itemList.get(0).maxTemp: "";
        String sunRise = this.weatherInfo != null && this.weatherInfo.sunRise != null ? "日出:" + this.weatherInfo.sunRise : "";
        String sunSet = this.weatherInfo != null && this.weatherInfo.sunSet != null ? "日落:" + this.weatherInfo.sunSet : "";



        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        paint.setTextSize(50);
        canvas.drawText(date_y, dis, dateSize+top, paint);canvas.drawText(week, dis, dateSize * 2 +top, paint);

        paint.setTextSize(citySize);
        canvas.drawText(city, (this.getWidth() - paint.measureText(city)) - dis, cityTop + citySize, paint);

        paint.setTextSize(tempSize);
        canvas.drawText(temp_now, (this.getWidth() - paint.measureText(temp_now)) / 2, tempSize + tempTop, paint);

        paint.setTextSize(typeTempSize);
        canvas.drawText(type_and_temp, (this.getWidth() - paint.measureText(type_and_temp)) / 2, typeTempSize + typeTempTop, paint);

        paint.setTextSize(sunSize);
        canvas.drawText(sunRise, dis,  sunSize + sunTop, paint);canvas.drawText(sunSet, (this.getWidth() - paint.measureText(sunSet)) -dis, sunSize + sunTop, paint);


        //Log.i("WeatherView", "onDraw-->>");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));

        //Log.i("WeatherView", "onMeasure-->>");
    }

    private int measureWidth(int widthMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        int result = 0;

        //match_parent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        //wrap_content
        else if (specMode == MeasureSpec.AT_MOST) {
            result = 50;
        }
        //未知
        else if (specMode == MeasureSpec.UNSPECIFIED) {
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec){
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        int result = 0;

        //match_parent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        //wrap
        else if (specMode == MeasureSpec.AT_MOST) {
            result = 1000;
        }
        //未知
        else if (specMode == MeasureSpec.UNSPECIFIED) {
            result = specSize;
        }
        return result;
    }

    public class WeatherInfo{
        private String city;
        private String date;
        private String week;
        private String tempNow;
        private String sunRise;
        private String sunSet;
        private String updateTime;
        private List<WeatherItem> itemList = new ArrayList<WeatherItem>();

        public WeatherInfo(String data){


            date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date(System.currentTimeMillis()));
            week = new SimpleDateFormat("EEEE").format(new Date(System.currentTimeMillis()));

            String tag = "";

            WeatherItem item = null;
            try {
                XmlPullParser parser = Xml.newPullParser();//得到Pull解析器
                parser.setInput(new StringReader(data));//设置下输入
                int eventType = parser.getEventType();//得到第一个事件类型
                while (eventType != XmlPullParser.END_DOCUMENT){//如果事件类型不是文档结束的话则不断处理事件
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

                            if (parser.getName().equals("type") && item != null){
                                if (item.type_day == null)
                                    item.type_day = parser.nextText();
                                else
                                    item.type_night = parser.nextText();
                            }
                            //Log.i("WeatherInfo","-->>" + parser.getName());
                            break;
                        case (XmlPullParser.END_TAG)://如果遇到标签结束
                            if (parser.getName().equals("weather") && item != null){
                                this.itemList.add(item);
                                item = null;
                            }
                            if (parser.getName().equals("forecast"))
                                tag = "";
                            break;
                    }
                    eventType=parser.next();//进入下一个事件处理
                }
            }
            catch (Exception ce){
                Log.i("WeatherInfo","初始化错误-->>" + ce.toString());
            }


            //json解析
//            try {
//                JSONObject jsonObject = new JSONObject(data);
//                if (jsonObject.has("weatherinfo")) {
//
//                    JSONObject weatherinfo = jsonObject.getJSONObject("weatherinfo");
//
//                    this.city = weatherinfo.getString("city");
//                    this.date_y = weatherinfo.getString("date_y");
//                    this.week = weatherinfo.getString("week");
//                    for (int i = 1; i <= 6; i++) {
//                        this.tempList.add(weatherinfo.getString("temp" + i));
//                        this.weatherList.add(weatherinfo.getString("weather" + i));
//                    }
//                }
//            }
//            catch (Exception ce){
//
//            }
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

        public class WeatherItem{
            private String minTemp;
            private String maxTemp;
            private String date;
            private String week;
            private String type_day;
            private String type_night;
        }

    }
}
