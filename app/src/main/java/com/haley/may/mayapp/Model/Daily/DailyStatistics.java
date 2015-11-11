package com.haley.may.mayapp.Model.Daily;

import android.util.Log;

import com.haley.may.mayapp.SQL.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2015/10/28.
 */
public class DailyStatistics {

    private List<CostInfo> weekCost = new ArrayList<CostInfo>();
    private List<CostInfo> monthCost = new ArrayList<CostInfo>();

    public DailyStatistics(int userid,List<DailyModeDateSelector.ModeDate> weekModeDates,List<DailyModeDateSelector.ModeDate> monthModeDates){

        for (DailyModeDateSelector.ModeDate week : weekModeDates){
            this.weekCost.add(new CostInfo(userid,week.getStartDateString(),week.getEndDateString()));
        }

        for (DailyModeDateSelector.ModeDate month : monthModeDates){
            this.monthCost.add(new CostInfo(userid,month.getStartDateString(),month.getEndDateString()));
        }
    }

    public List<CostInfo> getWeekCost() {
        return weekCost;
    }

    public List<CostInfo> getMonthCost() {
        return monthCost;
    }

    public class CostInfo{
        private String startDate;
        private String endDate;
        private float value = 0;
        private List<Float> values = new ArrayList<Float>();

        public CostInfo(int userid,String start,String end) {
            this.startDate = start;
            this.endDate = end;
            this.value = 0;

            List<String[]> infos = SQLiteHelper.getInstance().getDaily(userid, startDate, endDate);
            for (String[] info : infos) {
                if (info[5] != null && info[5].equals("") == false ) {
                    String[] strings = info[5].split("；");
                    float f = 0;
                    for (String value : strings) {
                        f +=Float.parseFloat(value.split("=")[1]);
                    }
                    values.add(f);
                    value += f;
                }
                else
                    values.add(0.0f);
            }
        }

        public float getValue() {
            return value;
        }

        public List<Float> getValues() {
            return values;
        }

        public String getWeekString(){
            return this.startDate.split(" ")[0].substring(5)+ "\r\n"+this.endDate.split(" ")[0].substring(5);
        }

        public String getMonthString(){
            return this.startDate.split(" ")[0].substring(0,7);
        }
    }
}
