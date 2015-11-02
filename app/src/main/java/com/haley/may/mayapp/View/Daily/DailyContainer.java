package com.haley.may.mayapp.View.Daily;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.View.Base.BaseContainer;
import com.haley.may.mayapp.View.Base.MayListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lenovo on 2015/10/23.
 */
public class DailyContainer extends BaseContainer {

    private DailyModel dailyModel = null;

    private List<View> views = new ArrayList<View>();

    public DailyContainer(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_daily, this);

        dailyModel = new DailyModel();

        this.initModeAndDate();

        this.initViewPager();
    }

    public void initViewPager(){
        final ViewPager viewPager = (ViewPager)this.findViewById(R.id.viewPager);

        this.views.add(new MayListView(this.getContext()));
        this.views.add(new DailyClassView(this.getContext()));

        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object)   {
                Log.i("AboutUsPageAdapter", "-->>destroyItem");
                container.removeView(views.get(position));//删除页卡
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.i("AboutUsPageAdapter", "-->>instantiateItem");
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

    public void initModeAndDate(){

        final Spinner spinnerMode = (Spinner)this.findViewById(R.id.spinnerMode);
        final Spinner spinnerDate = (Spinner)this.findViewById(R.id.spinnerDate);

        //赋值adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_dropdown_item,this.dailyModel.getDailyModeDateSelector().getModes());
        spinnerMode.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_dropdown_item,this.dailyModel.getDailyModeDateSelector().getModeDatesString());
        spinnerDate.setAdapter(adapter);

        //
        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("","select-->>"+ position);
                dailyModel.getDailyModeDateSelector().setSelectedMode(position);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,dailyModel.getDailyModeDateSelector().getModeDatesString());
                spinnerDate.setAdapter(null);
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

                //ListView listView = (ListView)findViewById(R.id.listView);
                ListView listView = (ListView)views.get(0);
                listView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return dailyModel.getDailyInfoList().size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return dailyModel.getDailyInfoList().get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {

                            convertView = new DailyInfoPanel(DailyContainer.this.getContext());
                            ((DailyInfoPanel) convertView).setModel(dailyModel.getDailyInfoList().get(position), (MayListView) parent);
                        }
                        return convertView;
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
