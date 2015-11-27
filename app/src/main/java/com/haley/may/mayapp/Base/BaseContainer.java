package com.haley.may.mayapp.Base;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;

import com.haley.may.mayapp.Interface.ICustomMenu;

/**
 * Created by lenovo on 2015/10/23.
 */
public class BaseContainer extends LinearLayout implements ICustomMenu {


    public BaseContainer(Context context) {
        super(context);
        //this.setPadding(0, Public.dip2px(context,80),0,0);

    }

    @Override
    public void initMenu(MenuInflater inflater, Menu menu) {
        return;
    }
}
