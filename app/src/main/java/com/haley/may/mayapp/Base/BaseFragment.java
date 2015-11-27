package com.haley.may.mayapp.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haley.may.mayapp.Style.IStyleChangedEvent;
import com.haley.may.mayapp.Style.StyleManager;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by haley on 2015/11/26.
 */
public class BaseFragment extends Fragment implements IStyleChangedEvent {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //为eventBus注册
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        //在创建时为设置style
        this.setStyle(StyleManager.getInstance().getStyle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //在创建时为设置style
        this.setStyle(StyleManager.getInstance().getStyle());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Subscribe
    @Override
    public void onEventMainThread(StyleManager.StyleEventInfo styleEventInfo) {
        this.setStyle(styleEventInfo);
    }

    protected void setStyle(StyleManager.StyleEventInfo styleEventInfo)
    {
        if (this.getView() != null)
            this.getView().setBackgroundDrawable(getResources().getDrawable(styleEventInfo.getBackGround()));
        if(  getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(styleEventInfo.getBackGround()));
    }
}
