package com.haley.may.mayapp.Model.Record;

import android.util.Log;

import com.haley.may.mayapp.SQL.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haley on 2015/10/28.
 */
public class RecordModel {

    private int userID = 1;
    private List<RecordInfo> recordInfos = new ArrayList<RecordInfo>();

    public RecordModel(){
        this.initRecord();
    }

    public List<RecordInfo> getRecordInfos() {
        return recordInfos;
    }

    public void addRecord(String date,String title,String text){
        SQLiteHelper.getInstance().addRecord(this.userID,date,title,text);
    }

    private void initRecord(){
        for (String[] strings : SQLiteHelper.getInstance().getRecord(this.userID)) {
            Log.i("RecordModel","-->>"+strings[0]+ strings[1]+strings[2]);
            this.recordInfos.add(new RecordInfo(strings[0], strings[1], strings[2]));
        }
    }

    public class RecordInfo{
        private String date;
        private String title;
        private String text;

        public RecordInfo(String date,String title,String text){
            this.date = date;
            this.title = title;
            this.text = text;
        }

        public String getDate() {
            return date;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return date + "\n" + title;
        }
    }
}
