package com.haley.may.mayapp.View.Daily;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.View.Base.MayListView;
import com.haley.may.mayapp.View.Daily.DailyInfoPanel;

/**
 * Created by lenovo on 2015/11/9.
 */
public class DailyInfoView extends LinearLayout {

    private DailyModel mModel = null;

    public DailyInfoView(Context context,DailyModel model){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_dailyinfo, this);

        mModel = model;

        this.initAdapter();


    }

    public void initAdapter(){
        ListView listView = (ListView)this.findViewById(R.id.listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mModel.getDailyInfoList().size();
            }

            @Override
            public Object getItem(int position) {
                return mModel.getDailyInfoList().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                //if (convertView == null) {
                {
                    convertView = new DailyInfoPanel(getContext());
                }
                ((DailyInfoPanel) convertView).setModel(mModel.getDailyInfoList().get(position), (MayListView) parent);
                return convertView;
            }
        });
    }

    private void initButton(){

    }


}
