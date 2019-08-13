package com.bodhi.customviewtestdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/2 11:28
 * desc :
 */
public class CustomGroup extends LinearLayout {


    public CustomGroup(Context context) {
        super(context);
    }

    public CustomGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("eventDis","---ViewGroup---dispatchTouchEvent------");

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("eventDis","---ViewGroup---onInterceptTouchEvent------");

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("eventDis","---ViewGroup---onTouchEvent------");

        return super.onTouchEvent(event);
    }
}
