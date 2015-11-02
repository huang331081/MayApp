package com.haley.may.mayapp.Model.Daily;

import com.haley.may.mayapp.SQL.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2015/10/8.
 */
public class DailyModel {

    //region variable
    private int userID = 1;
    private String userName = "haley";
    private List<DailyInfo> dailyInfoList = new ArrayList<DailyInfo>();
    private DailyModeDateSelector dailyModeDateSelector;

    private DailyStatistics dailyStatistics;

    private static LabelCollection labelCollection = null;
    //endregion

    //region structure
    public  DailyModel(){

        SQLiteHelper.getInstance().addDaily(this.userID, new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(new Date(System.currentTimeMillis())));

        labelCollection = new LabelCollection(this.userID);
        this.dailyModeDateSelector = new DailyModeDateSelector(SQLiteHelper.getInstance().getStartDaily(this.userID),new Date(System.currentTimeMillis()));


        //this.init();
    }
    //endregion

    //region set get
    public String getUserName(){
        return userName;
    }

    public List<DailyInfo> getDailyInfoList(){
        return dailyInfoList;
    }

    public DailyModeDateSelector getDailyModeDateSelector() {
        return dailyModeDateSelector;
    }

    public static LabelCollection getLabelCollection() {
        return labelCollection;
    }
    //endregion

    //region function
    public void init(){
        this.dailyInfoList.clear();
        SQLiteHelper.getInstance().getDaily(this.userID,this.dailyModeDateSelector.getModeDates().get(this.dailyModeDateSelector.getSelectedDate()).getStartDateString(),this.dailyModeDateSelector.getModeDates().get(this.dailyModeDateSelector.getSelectedDate()).getEndDateString(),this);
    }

    public void AddDailyInfo(String id,String date,String breakfast,String lunch,String dinner,String other) {
        DailyInfo info = new DailyInfo(id, date, other);
        this.dailyInfoList.add(info);
    }
    //endregion

    //region OnInitDailyInfoListen
    public interface OnInitDailyInfoListen{
        public void onInitDailyInfo();
    }

    private OnInitDailyInfoListen onInitDailyInfoListen = null;

    public void setOnInitDailyInfoListen(OnInitDailyInfoListen onInitDailyInfoListen) {
        this.onInitDailyInfoListen = onInitDailyInfoListen;
    }

    public void invokeOnInitDailyInfoListen(){
        if (this.onInitDailyInfoListen != null)
            this.onInitDailyInfoListen.onInitDailyInfo();
    }
    //endregion
}

