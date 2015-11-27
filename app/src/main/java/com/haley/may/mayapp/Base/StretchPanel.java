package com.haley.may.mayapp.Base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Copyright 2014 soso_fy（flytopc@hotmail.com）
/**
 * StretchPanel可以设置始终展示的视图和拓展视图
 */
public class StretchPanel extends LinearLayout {

    private static final String TAG = StretchPanel.class.getSimpleName();

    protected View contentView;
    protected View stretchView;

    private int stretchHeight;
    private OnStretchListener mListener;
    private int mAnimationDuration = 300;
    private boolean isOpened = false;

    public interface OnStretchListener {
        /**
         * 动画结束监听
         * @param isOpened 当前的stretchView是否是打开的,
         * <p>与com.example.widget.isStretchViewOpened isStretchViewOpened()}的返回值是一致的
         */
        public void onStretchFinished(boolean isOpened);
    }


    public StretchPanel(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public StretchPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        init(context, attrs);
    }

    @SuppressLint("NewApi")
    public StretchPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(LinearLayout.VERTICAL);
        init(context, attrs);
    }

    // 获取自定义的属性值
    private void init(Context context, AttributeSet attrs) {
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StretchPanel);
//        mContentViewLayoutId = ta.getResourceId(R.styleable.StretchPanel_contentView, -1);
//        mStretchViewLayoutId = ta.getResourceId(R.styleable.StretchPanel_stretchView, -1);
//        if (mContentViewLayoutId > 0) {
//            View view  = View.inflate(context, mContentViewLayoutId, null);
//            setContentView(view);
//        }
//        if (mStretchViewLayoutId > 0) {
//            View view = View.inflate(context, mStretchViewLayoutId, null);
//            setStretchView(view);
//        }
//        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        if (stretchHeight == 0 && stretchView != null) {
            stretchView.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
            stretchHeight = stretchView.getMeasuredHeight();
            stretchView.getLayoutParams().height = 0;
            //Log.i(TAG, "-->>stretchview height = " + stretchHeight);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public View getContentView() {
        return this.contentView;
    }

    public View getStretchView() {
        return this.stretchView;
    }

    public void setContentView(View view) {
        if (view != null) {
            if (this.contentView != null) {
                removeView(this.contentView);
            }
            this.contentView = view;
            addView(contentView, 0);
        }
    }

    public void setStretchView(View view) {
        if (view != null) {
            if (this.stretchView != null) {
                removeView(this.stretchView);
                // 在重新设置时，将该值置为0，否则新view将不能显示正确的高度
                this.stretchHeight = 0;
            }
            this.stretchView = view;
            addView(stretchView);
        }
    }

    /**
     * 设置动画的监听
     * @param listener
     */
    public void setOnStretchListener(OnStretchListener listener) {
        this.mListener = listener;
    }

    /**
     * 当前的视图是否已经展开
     * @return
     */
    public boolean isStretchViewOpened() {
        return isOpened;
    }

    /**
     * 设置展开（或者收缩）动画的时间，默认300ms
     *  duration
     */
    public void setStretchAnimationDuration(int durationMs) {
        if (durationMs >= 0) {
            this.mAnimationDuration = durationMs;
        }
        else {
            throw new IllegalArgumentException("Animation duration cannot be negative");
        }
    }


    /**
     * 设置点击事件的处理view
     * <p>如果不设置，点击事件需要在外部处理；如果设置的话，该view之前的点击事件将被覆盖掉
     *  view
     */
    public void setHandleClikeEventOnThis(View view) {
        if (view != null) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (isOpened) {
                        closeStretchView();
                    } else {
                        openStretchView();
                    }
                }
            });
        }
    }

    /**
     * 展开视图
     */
    public void openStretchView() {
        if (stretchView != null && isOpened == false) {
            post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    StretchAnimation animation = new StretchAnimation(0, stretchHeight);
                    animation.setDuration(mAnimationDuration);
                    animation.setAnimationListener(animationListener);
                    stretchView.startAnimation(animation);
                    invalidate();
                }
            });
        }
    }

    /**
     * 收起视图
     */
    public void closeStretchView() {
        if (stretchView != null && isOpened == true) {
            post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    StretchAnimation animation = new StretchAnimation(stretchHeight, 0);
                    animation.setDuration(mAnimationDuration);
                    animation.setAnimationListener(animationListener);
                    stretchView.startAnimation(animation);
                    invalidate();
                }
            });
        }
    }

    private AnimationListener animationListener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub  
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub        
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            isOpened = !isOpened;
            if (mListener != null) {
                mListener.onStretchFinished(isOpened);
            }
        }
    };

    /**
     * 伸缩动画
     */
    private class StretchAnimation extends Animation {
        private int startHeight;
        private int deltaHeight;

        public StretchAnimation(int startH, int endH) {
            this.startHeight = startH;
            this.deltaHeight = endH - startH;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // TODO Auto-generated method stub
            if (stretchView != null) {
                LayoutParams params = (LayoutParams) stretchView.getLayoutParams();
                params.height = (int) (startHeight + deltaHeight * interpolatedTime);
                stretchView.setLayoutParams(params);
            }
        }
    }

}