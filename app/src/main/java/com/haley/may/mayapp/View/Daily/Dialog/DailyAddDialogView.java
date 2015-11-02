package com.haley.may.mayapp.View.Daily.Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.haley.may.mayapp.R;

/**
 * Created by lenovo on 2015/10/15.
 */
public class DailyAddDialogView extends LinearLayout {

    public DailyAddDialogView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.dialog_dailyadd, null);

    }
}
