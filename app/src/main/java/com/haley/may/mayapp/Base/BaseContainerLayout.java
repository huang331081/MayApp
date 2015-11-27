package com.haley.may.mayapp.Base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import com.haley.may.mayapp.Style.IStyleChangedEvent;
import com.haley.may.mayapp.Style.StyleManager;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by haley on 2015/11/23.
 */
public class BaseContainerLayout extends FrameLayout implements IStyleChangedEvent {
    private final static String TAG = "ContainerLayout";

    public BaseContainerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    @Override
    public void onEventMainThread(StyleManager.StyleEventInfo styleEventInfo) {
        StyleManager.StyleEventInfo previousStyleEventInfo = StyleManager.getInstance().getPreviousStyle();
        if (!previousStyleEventInfo.equals(styleEventInfo)) {
            int beginRGB = this.getContext().getResources().getColor(previousStyleEventInfo.getBackGround());
            final int beginR = beginRGB & 0xff;
            final int beginG = (beginRGB & 0xff00) >> 8;
            final int beginB = (beginRGB & 0xff0000) >> 16;

            //int endRGB = 0xff0080FF;
            int endRGB = this.getContext().getResources().getColor(styleEventInfo.getBackGround());

            final int endR = endRGB & 0xff;
            final int endG = (endRGB & 0xff00) >> 8;
            final int endB = (endRGB & 0xff0000) >> 16;


            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {

                    int tempR = (int) (beginR + (endR - beginR) * interpolatedTime);
                    int tempG = (int) (beginG + (endG - beginG) * interpolatedTime);
                    int tempB = (int) (beginB + (endB - beginB) * interpolatedTime);

                    BaseContainerLayout.this.setBackgroundColor(tempR | tempG << 8 | tempB << 16 | 0xff000000);
                    BaseContainerLayout.this.invalidate();

                    super.applyTransformation(interpolatedTime, t);
                }
            };
            animation.setDuration(1000);
            this.startAnimation(animation);
        }

    }
}
