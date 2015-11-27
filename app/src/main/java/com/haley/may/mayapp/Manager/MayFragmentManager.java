package com.haley.may.mayapp.Manager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haley.may.mayapp.Base.BaseContainer;
import com.haley.may.mayapp.View.Daily.DailyContainer;
import com.haley.may.mayapp.View.Map.MapContainer;
import com.haley.may.mayapp.View.Record.RecordContainer;
import com.haley.may.mayapp.View.Weather.WeatherContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例模式
 * 用于创建、管理fragment的类
 * Created by haley on 2015/10/22.
 */
public class MayFragmentManager {

    //region variable
    private static String TAG = "MayFragmentManager";
    private FragmentInfo showingFragment = null;
    private List<FragmentInfo> fragmentInfos = new ArrayList<FragmentInfo>();
    //endregion

    //region single case
    private static MayFragmentManager instance = new MayFragmentManager();
    public static MayFragmentManager getInstance(){
        return instance;
    }
    private MayFragmentManager() {
        fragmentInfos.add(new FragmentInfo("天气"));
        fragmentInfos.add(new FragmentInfo("账单"));
        fragmentInfos.add(new FragmentInfo("记录"));
        fragmentInfos.add(new FragmentInfo("地图"));
    }
    //endregion


    public FragmentInfo getShowingFragment() {
        return showingFragment;
    }

    public List<String> getTitles(){
        List<String> list = new ArrayList<String>();

        for (FragmentInfo fragmentInfo : this.fragmentInfos){
            list.add(fragmentInfo.title);
        }

        return list;
    }

    /**
     * 初始化所有view
     * @param inflater
     */
    public void initViews(LayoutInflater inflater){
//        for (int i=0; i<this.views.size(); i++){
//            if (this.views.get(i) == null && i != 3){
//                this.getView(i+1,inflater,null);
//            }
//        }
    }

    /**
     * 获取对应fragment的view
     * @param position
     * @param inflater
     * @param container
     * @return
     */
    public View getView(int position, LayoutInflater inflater,ViewGroup container){
        position--;

        if (fragmentInfos.size() < position)
            return null;

        this.showingFragment = fragmentInfos.get(position);

        if (fragmentInfos.get(position).container == null) {
            switch (position) {
                case 0:
                    fragmentInfos.get(position).container = ((BaseContainer) this.createWeatherView(inflater, container));
                    break;
                case 1:
                    fragmentInfos.get(position).container = ((BaseContainer) this.createDailyView(inflater, container));
                    break;
                case 2:
                    fragmentInfos.get(position).container = ((BaseContainer) this.createRecordView(inflater, container));
                    break;
                case 3:
                    fragmentInfos.get(position).container = ((BaseContainer) this.createMapView(inflater, container));
                    break;
            }
        }

        return fragmentInfos.get(position).container;
    }
    //endregion

    //region private function
    private View createWeatherView(final LayoutInflater inflater,ViewGroup container){
        return new WeatherContainer(inflater.getContext());
    }

    private View createDailyView(final LayoutInflater inflater,ViewGroup container){
        return new DailyContainer(inflater.getContext());
    }

    private View createRecordView(final LayoutInflater inflater,ViewGroup container){
        return new RecordContainer(inflater.getContext());
    }

    private View createMapView(final LayoutInflater inflater,ViewGroup container){
        return new MapContainer(inflater.getContext());
    }
    //endregion


    //region class:FragmentInfo
    public class FragmentInfo{
        private String title;
        private BaseContainer container = null;

        public FragmentInfo(String title){
            this.title = title;
        }

        public void initActionBar(MenuInflater inflater,Menu menu){
            this.container.initMenu(inflater,menu);
        }
    }
    //endregion
}
