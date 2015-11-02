package com.haley.may.mayapp.View.Daily;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by lenovo on 2015/10/28.
 */
public class DailyStatisticsView extends View {

    public DailyStatisticsView(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));

    }

    private int measureWidth(int widthMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        int result = 0;

        //match_parent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        //wrap_content
        else if (specMode == MeasureSpec.AT_MOST) {
            result = 50;
        }
        //未知
        else if (specMode == MeasureSpec.UNSPECIFIED) {
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec){
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        int result = 0;

        //match_parent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        //wrap
        else if (specMode == MeasureSpec.AT_MOST) {
            result = 1000;
        }
        //未知
        else if (specMode == MeasureSpec.UNSPECIFIED) {
            result = specSize;
        }
        return result;
    }
}
