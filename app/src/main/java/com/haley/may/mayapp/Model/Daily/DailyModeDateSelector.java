package com.haley.may.mayapp.Model.Daily;

import android.provider.ContactsContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2015/10/26.
 */
public class DailyModeDateSelector {
    //region variable
    private String[] modes = {"Week","Month","All"};
    private Date startDate,endDate;
    private List<ModeDate> modeDates = new ArrayList<ModeDate>();

    private final long dayMilSecond = 86400000;

    private int selectedMode = -1;
    private int selectedDate = 0;
    //endregion

    //region structure
    public DailyModeDateSelector(Date startDate,Date endDate){

        this.startDate = new Date(startDate.getTime());
        this.endDate = new Date(endDate.getTime());
//        this.setSelectedMode(0);
    }
    //endregion

    //region functon
    public String[] getModes() {
        return modes;
    }

    public List<ModeDate> getModeDates() {
        return modeDates;
    }

    public String[] getModeDatesString(){
        String[] temp = new String[this.modeDates.size()];
        int index = 0;

        for (ModeDate modedate : this.modeDates){
            temp[index] = modedate.toString();
            index++;
        }
        return temp;
    }

    public int getSelectedMode() {
        return selectedMode;
    }

    public int getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedMode(int selectedMode) {
        if (selectedMode == this.selectedMode)
            return;

        this.selectedMode = selectedMode;
        this.modeDates = this.getModeDates(this.selectedMode);

        Collections.reverse(this.modeDates);
    }

    public void setSelectedDate(int selectedDate) {
        this.selectedDate = selectedDate;

        //暂无用
        this.invokeOnDateChanged();
    }

    public List<ModeDate> getModeDates(int mode){
        List<ModeDate> modeDates = new ArrayList<ModeDate>();

        if (this.modes[mode].equals("Week")){
            for (Date weekStart = new Date(startDate.getTime()) ; weekStart.getTime() < this.endDate.getTime();) {
                Date weekEnd = new Date(weekStart.getTime() + dayMilSecond * (7 - getDayOfWeek(weekStart)));

                modeDates.add(new ModeDate("Week",weekStart,weekEnd.getTime() <= this.endDate.getTime() ?  weekEnd : this.endDate));

                weekStart.setTime(weekEnd.getTime() + dayMilSecond);
            }
        }
        else
        if (this.modes[mode].equals("Month")){
            for (Date monthStart = new Date(startDate.getTime()) ; monthStart.getTime() < this.endDate.getTime();) {
                Date monthEnd = new Date(monthStart.getTime() + dayMilSecond * (getMonthDays(monthStart) - getDayOfMonth(monthStart)));

                modeDates.add(new ModeDate("Month",monthStart,monthEnd.getTime() <= this.endDate.getTime() ? monthEnd : this.endDate));

                monthStart.setTime(monthEnd.getTime() + dayMilSecond);
            }
        }
        if (this.modes[mode].equals("All")){
            modeDates.add(new ModeDate("All",this.startDate,this.endDate));
        }

        return modeDates;
    }

    private int getDayOfWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekInt = calendar.get(Calendar.DAY_OF_WEEK);
        if (weekInt == 1)
            weekInt = 7;
        else
            weekInt--;

        return weekInt;
    }

    private int getDayOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthInt =calendar.get(Calendar.DAY_OF_MONTH);

        return monthInt;
    }

    private int getMonthDays(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE,-1);
        return calendar.get(Calendar.DATE);
    }
    //endregion

    //region OnInitDailyInfoListen
    public interface OnDateChangedListener{
        void onDateChanged();
    }

    private OnDateChangedListener onDateChangedListener = null;

    public void setOnInitDailyInfoListen(OnDateChangedListener onDateChangedListener) {
        this.onDateChangedListener = onDateChangedListener;
    }

    private void invokeOnDateChanged(){
        if (this.onDateChangedListener != null)
            this.onDateChangedListener.onDateChanged();
    }
    //endregion

    //region class:ModeDate
    /**
     * ModeDate类,存放类型下起止日期
     */
    public class ModeDate{
        private String mode;
        private Date startDate,endDate;

        public ModeDate(String mode,Date startDate,Date endDate){
            this.mode = mode;
            this.startDate = new Date(startDate.getTime());
            this.endDate = new Date(endDate.getTime());

            //Log.i("ModeDate","-->>"+this.toString());
        }

        public boolean isContain(long date){
            if (startDate.getTime() <= date && endDate.getTime() >= date)
                return true;
            else
                return false;
        }

        public String getStartDateString() {
            return new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(this.startDate);
        }

        public String getEndDateString() {
            return new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(this.endDate);
        }

        @Override
        public String toString() {
            return new SimpleDateFormat("yyyy/MM/dd").format(this.startDate) + " : " + new SimpleDateFormat("yyyy/MM/dd").format(this.endDate);
        }
    }
    //endregion
}
