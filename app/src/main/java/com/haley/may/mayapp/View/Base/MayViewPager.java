package com.haley.may.mayapp.View.Base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.haley.may.mayapp.System.Public;

/**
 * Created by lenovo on 2015/10/28.
 */
public class MayViewPager extends ViewPager {

    public MayViewPager(Context context){
        super(context);
    }

    public MayViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (Public.isChildCapture() == false)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
    }
}
