package com.haley.may.mayapp.View.Base;

import android.content.Context;
import android.widget.LinearLayout;

import com.haley.may.mayapp.Interface.IStyleChange;
import com.haley.may.mayapp.Manager.StyleManager;
import com.haley.may.mayapp.System.Public;

/**
 * Created by lenovo on 2015/10/23.
 */
public class BaseContainer extends LinearLayout implements IStyleChange {


    public BaseContainer(Context context) {
        super(context);
        this.setPadding(0, Public.dip2px(context,25),0,0);
    }

    @Override
    public void setStyle(StyleManager.StyleInfo previousStyle,StyleManager.StyleInfo style) {
        this.setBackgroundColor(this.getContext().getResources().getColor(style.getBackGround()));
    }
}
