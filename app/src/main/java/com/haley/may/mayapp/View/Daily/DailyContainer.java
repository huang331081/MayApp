package com.haley.may.mayapp.View.Daily;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.haley.may.mayapp.MainActivity;
import com.haley.may.mayapp.View.Daily.Activity.DailyPacketActivity;
import com.haley.may.mayapp.View.Daily.Activity.DailyStatisticsActivity;
import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.Base.BaseContainer;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


/**
 * Created by lenovo on 2015/10/23.
 */
public class DailyContainer extends BaseContainer {

    private DailyModel dailyModel = null;

    private List<View> views = new ArrayList<View>();

    public DailyContainer(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_daily, this);

        EventBus.getDefault().register(this);

        dailyModel = new DailyModel();

        //this.initModeAndDate();

        this.initViewPager();

        this.initFunction();
    }

    @Override
    public void initMenu(MenuInflater inflater, Menu menu) {
        super.initMenu(inflater, menu);

        inflater.inflate(R.menu.daily, menu);

        final Spinner spinnerMode = (Spinner)menu.findItem(R.id.actionbar_daily_mode).getActionView();
        final Spinner spinnerDate = (Spinner) menu.findItem(R.id.actionbar_daily_date).getActionView();

        //赋值adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_dropdown_item,this.dailyModel.getDailyModeDateSelector().getModes());
        spinnerMode.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return dailyModel.getDailyModeDateSelector().getModes().length;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(getContext());
                textView.setText(dailyModel.getDailyModeDateSelector().getModes()[position]);
                textView.setTextSize(15);
                convertView = textView;
                return convertView;
            }
        });

//        adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_dropdown_item,this.dailyModel.getDailyModeDateSelector().getModeDatesString());
//        spinnerDate.setAdapter(adapter);

        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("", "select-->>" + position);
                dailyModel.getDailyModeDateSelector().setSelectedMode(position);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dailyModel.getDailyModeDateSelector().getModeDatesString());
                spinnerDate.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dailyModel.getDailyModeDateSelector().setSelectedDate(position);
                dailyModel.init();

                //dailyview更新显示
                DailyInfoView dailyInfoView = (DailyInfoView) views.get(0);
                dailyInfoView.initAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initViewPager(){
        final ViewPager viewPager = (ViewPager)this.findViewById(R.id.viewPager);

        this.views.add(new DailyInfoView(getContext(), dailyModel));
        //this.views.add(new DailyStatisticsView(getContext(),dailyModel));
       // this.views.add(new DailyPacketView(this.getContext()));

        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));//删除页卡
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position), 0);
                return views.get(position);
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    private void initFunction(){
        findViewById(R.id.layoutStatistics).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //ActivityCompat.startActivity(MainActivity.getInstance(),new Intent(getContext(), StatisticsActivity.class),ActivityOptionsCompat.makeCustomAnimation(getContext(),R.anim.anim_activity_open_enter,R.anim.anim_activity_exit_stop).toBundle());
                getContext().startActivity(new Intent(getContext(), DailyStatisticsActivity.class));
                MainActivity.getInstance().overridePendingTransition(R.anim.anim_activity_open_enter, R.anim.anim_activity_exit_stop);
            }
        });

        findViewById(R.id.layoutPakcet).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), DailyPacketActivity.class));
                MainActivity.getInstance().overridePendingTransition(R.anim.anim_activity_open_enter,R.anim.anim_activity_exit_stop);
            }
        });
    }

    @Subscribe
    public void onEventMainThread(DailyModelRequestEvent event){
        EventBus.getDefault().post(dailyModel);
    }

//    public void initModeAndDate(){
//
//        final Spinner spinnerMode = (Spinner)this.findViewById(R.id.spinnerMode);
//        final Spinner spinnerDate = (Spinner)this.findViewById(R.id.spinnerDate);
//
//        //赋值adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_dropdown_item,this.dailyModel.getDailyModeDateSelector().getModes());
//        spinnerMode.setAdapter(adapter);
//
//        adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_dropdown_item,this.dailyModel.getDailyModeDateSelector().getModeDatesString());
//        spinnerDate.setAdapter(adapter);
//
//        //
//        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("", "select-->>" + position);
//                dailyModel.getDailyModeDateSelector().setSelectedMode(position);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,dailyModel.getDailyModeDateSelector().getModeDatesString());
//                spinnerDate.setAdapter(adapter);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                dailyModel.getDailyModeDateSelector().setSelectedDate(position);
//                dailyModel.init();
//
//                //dailyview更新显示
//                DailyInfoView dailyInfoView = (DailyInfoView)views.get(0);
//                dailyInfoView.initAdapter();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
}
