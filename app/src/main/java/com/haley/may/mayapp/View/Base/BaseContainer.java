package com.haley.may.mayapp.View.Base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;

import com.haley.may.mayapp.Interface.ICustomMenu;
import com.haley.may.mayapp.Interface.IStyleChange;
import com.haley.may.mayapp.Manager.StyleManager;
import com.haley.may.mayapp.System.Public;

/**
 * Created by lenovo on 2015/10/23.
 */
public class BaseContainer extends LinearLayout implements IStyleChange,ICustomMenu {


    public BaseContainer(Context context) {
        super(context);
        //this.setPadding(0, Public.dip2px(context,80),0,0);

    }

    @Override
    public void setStyle(StyleManager.StyleInfo previousStyle,StyleManager.StyleInfo style) {
        this.setBackgroundColor(this.getContext().getResources().getColor(style.getBackGround()));
    }


    @Override
    public void initMenu(MenuInflater inflater, Menu menu) {
        return;
    }
}
