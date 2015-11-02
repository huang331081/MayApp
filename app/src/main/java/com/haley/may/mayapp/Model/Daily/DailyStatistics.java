package com.haley.may.mayapp.Model.Daily;

import com.haley.may.mayapp.SQL.SQLiteHelper;

import java.util.List;

/**
 * Created by lenovo on 2015/10/28.
 */
public class DailyStatistics {

    private int user_id;
    private float threeMeals = 0;
    private float others = 0;

    public DailyStatistics(int user_id){
        this.user_id = user_id;

    }

    public float getThreeMeals() {
        return threeMeals;
    }

    public float getOthers() {
        return others;
    }

    public float getAll(){
        return threeMeals + others;
    }

    public void init(List<DailyInfo> infos){
        this.threeMeals = 0;
        this.others = 0;

        for (DailyInfo info : infos){
            if (info.getOther().equals(""))
                continue;

            String[] strings = info.getOther().split(";");
            for (String string : strings){
                String[] labels = string.split("=");
                if (labels[0].equals("早饭") || labels[0].equals("中饭") || labels[0].equals("晚饭"))
                    this.threeMeals += Float.parseFloat(labels[1]);
                else
                    this.others += Float.parseFloat(labels[1]);
            }
        }
    }
}
