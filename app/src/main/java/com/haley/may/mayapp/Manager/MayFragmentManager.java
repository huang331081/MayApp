package com.haley.may.mayapp.Manager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haley.may.mayapp.Interface.IStyleChange;
import com.haley.may.mayapp.View.Daily.DailyContainer;
import com.haley.may.mayapp.View.Record.RecordContainer;
import com.haley.may.mayapp.View.Weather.WeatherContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haley on 2015/10/22.
 */
public class MayFragmentManager {
    private static String TAG = "MayFragmentManager";

    private List<String> viewTitles = new ArrayList<String>();
    private List<View> views = new ArrayList<View>();

    private static MayFragmentManager instance = new MayFragmentManager();

    public static MayFragmentManager getInstance(){
        return instance;
    }

    private MayFragmentManager() {
        viewTitles.add("天气");
        viewTitles.add("账单");
        viewTitles.add("记录");

        for (String string : viewTitles){
            views.add(null);
        }
    }

    public List<String> getViewTitles() {
        return viewTitles;
    }

    public View getView(int position, LayoutInflater inflater,ViewGroup container){
        position--;

        if (views.size() < position)
            return null;

        Log.i(TAG,"getView-->>" + this.viewTitles.get(position));
        switch (position){
            case 0:
                if (views.get(position) == null) {
                    views.set(position, this.createWeahterView(inflater,container));
                    ((IStyleChange) this.views.get(position)).setStyle(StyleManager.getInstance().getPreviousStyle(),StyleManager.getInstance().getStyle());
                }
                return views.get(position);
            case 1:
                if (views.get(position) == null) {
                    views.set(position,this.createDailyView(inflater,container));
                    ((IStyleChange) this.views.get(position)).setStyle(StyleManager.getInstance().getPreviousStyle(),StyleManager.getInstance().getStyle());
                }
                return views.get(position);
            case 2:
                if (views.get(position) == null){
                    views.set(position,this.createRecordView(inflater,container));
                    ((IStyleChange) this.views.get(position)).setStyle(StyleManager.getInstance().getPreviousStyle(), StyleManager.getInstance().getStyle());
                }
                return views.get(position);
        }
        return null;
    }

    public void setStyle(){
        for (int i=0 ;i<this.views.size();i++)
            if (this.views.get(0) != null)
                ((IStyleChange) this.views.get(0)).setStyle(StyleManager.getInstance().getPreviousStyle(), StyleManager.getInstance().getStyle());
    }

    private View createWeahterView(final LayoutInflater inflater,ViewGroup container){
        //inflater.inflate(R.layout.activity_main_test,container,false)
        return new WeatherContainer(inflater.getContext());
    }

    private View createDailyView(final LayoutInflater inflater,ViewGroup container){
        return new DailyContainer(inflater.getContext());
    }

    private View createRecordView(final LayoutInflater inflater,ViewGroup container){
        return new RecordContainer(inflater.getContext());
    }
}
