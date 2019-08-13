package com.bodhi.customviewtestdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/2 11:29
 * desc :
 */
public class CustomView extends android.support.v7.widget.AppCompatTextView {


    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("eventDis","---View---dispatchTouchEvent------");

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("eventDis","---View---onTouchEvent------");

        return super.onTouchEvent(event);
    }
}
