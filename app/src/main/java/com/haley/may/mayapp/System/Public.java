package com.haley.may.mayapp.System;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 2015/10/21.
 */
public class Public {

    private static boolean isChildCapture = false;//子控件获得焦点的控制变量，用于控制父控件不处罚onTouch

    public static void setIsChildCapture(boolean isChildCapture) {
        Public.isChildCapture = isChildCapture;
    }

    public static boolean isChildCapture() {
        return isChildCapture;
    }

    public static String getDayOfWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekInt = calendar.get(Calendar.DAY_OF_WEEK);
        if (weekInt == 1)
            return "日";
        else if (weekInt == 2)
            return "一";
        else if (weekInt == 3)
            return "二";
        else if (weekInt == 4)
            return "三";
        else if (weekInt == 5)
            return "四";
        else if (weekInt == 6)
            return "五";
        else if (weekInt == 7)
            return "六";

        return "";
    }

    /**
     * 判断手机网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager == null)
                return false;
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
