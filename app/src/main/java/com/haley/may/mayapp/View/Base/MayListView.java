package com.haley.may.mayapp.View.Base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.haley.may.mayapp.System.Public;

/**
 * Created by lenovo on 2015/10/14.
 */
public class MayListView extends ListView {

    public MayListView(Context context) {
        super(context);
    }

    public MayListView(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (Public.isChildCapture() == false)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
    }
}
