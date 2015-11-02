package com.haley.may.mayapp.Model.Daily;

import com.haley.may.mayapp.SQL.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/10/15.
 */
public class DailyInfo {

    //region variable
    private String id;
    private String date;
    private String other;
    private List<DailyInfoLabel> dailyInfoLabelList = new ArrayList<DailyInfoLabel>();
    //endregion

    //region structure
    public DailyInfo(String id,String date,String other){
        this.setId(id);
        this.setDate(date);
        this.setOther(other);
    }
    //endregion

    //region get set
    public void setId(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setOther(String other) {
        this.other = other == null ? "" : other;

        //更新标签数据
        this.dailyInfoLabelList.clear();
        if (this.other.equals("") == false) {
            String[] strings = this.other.split("；");
            for (String string : strings) {
                this.dailyInfoLabelList.add(new DailyInfoLabel(string.split("=")[0], string.split("=")[1]));
            }
        }
    }

    public String getOther() {
        return other;
    }

    public List<DailyInfoLabel> getDailyInfoLabelList() {
        return dailyInfoLabelList;
    }

    public String getCount(){
        float count = 0;
        for (DailyInfoLabel label : this.dailyInfoLabelList){
            count += Float.parseFloat(label.value);
        }

        return Float.toString(count);
    }
    //endregion

    //region function
    public DailyInfoLabel addLabel(String label,String value){

        if (label.equals("") || value.equals(""))
            return null;

        DailyInfoLabel dailyInfoLabel = new DailyInfoLabel(label,value);
        this.dailyInfoLabelList.add(dailyInfoLabel);

        this.updateOther();

        SQLiteHelper.getInstance().UpdateDaily(this.id, "other", this.other);
        DailyModel.getLabelCollection().updateLabel(label,true);

        return dailyInfoLabel;
    }

    public void removeLabel(DailyInfoLabel label){
        if (this.dailyInfoLabelList.contains(label)){
            this.dailyInfoLabelList.remove(label);

            this.updateOther();

            SQLiteHelper.getInstance().UpdateDaily(this.id, "other", this.other) ;
            DailyModel.getLabelCollection().updateLabel(label.label, false);

        }
    }

    private void updateOther(){
        //更新other
        this.other = "";
        for (DailyInfoLabel l : this.dailyInfoLabelList){
            this.other += l.toString() + "；";
        }
        if (other.equals("") == false)
            this.other = this.other.substring(0,this.other.length()-1);
    }
    //endregion

    //region inner class
    public class DailyInfoLabel{
        private String label;
        private String value;

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }

        public DailyInfoLabel(String label,String value){
            this.label = label;
            this.value = value;
        }

        @Override
        public String toString() {
            return label + "=" + value;
        }
    }
    //endregion

    //region OnDailyInfoUpdateListen
//    public interface OnDailyInfoUpdateListen{
//        void onDailyInfoUpdate( );
//    }
//
//    private OnDailyInfoUpdateListen onDailyInfoUpdateListen = null;
//
//    public void setOnDailyInfoUpdateListen(OnDailyInfoUpdateListen onDailyInfoUpdateListen) {
//        this.onDailyInfoUpdateListen = onDailyInfoUpdateListen;
//    }
//
//    public void invokeOnDailyInfoUpdateListen(){
//        if (this.onDailyInfoUpdateListen != null){
//            this.onDailyInfoUpdateListen.onDailyInfoUpdate();
//        }
//    }
    //endregion
}
