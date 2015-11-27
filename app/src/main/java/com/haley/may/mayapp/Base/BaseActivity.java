package com.haley.may.mayapp.Base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.haley.may.mayapp.R;
import com.haley.may.mayapp.Style.IStyleChangedEvent;
import com.haley.may.mayapp.Style.StyleManager;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by haley on 2015/11/26.
 */
public class BaseActivity extends AppCompatActivity implements IStyleChangedEvent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//activity最上面背景可占用，但是系统的时间、电量等信息仍在
        super.onCreate(savedInstanceState);

        //为eventBus注册
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        //在创建时为设置style
        this.setStyle(StyleManager.getInstance().getStyle());
    }

    @Subscribe
    @Override
    public void onEventMainThread(StyleManager.StyleEventInfo styleEventInfo) {
        this.setStyle(styleEventInfo);
    }

    protected void setStyle(StyleManager.StyleEventInfo styleEventInfo)
    {
        getWindow().setBackgroundDrawableResource(styleEventInfo.getBackGround());
        if(this.getSupportActionBar() != null)
            this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(styleEventInfo.getBackGround()));
    }
}
