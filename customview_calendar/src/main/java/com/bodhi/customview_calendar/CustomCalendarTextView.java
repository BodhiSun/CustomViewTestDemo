package com.bodhi.customview_calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/2/13 16:06
 * desc :
 */
public class CustomCalendarTextView extends android.support.v7.widget.AppCompatTextView {

    public boolean isToday = false;
    private Paint paint = new Paint();

    public CustomCalendarTextView(Context context) {
        super(context);
    }

    public CustomCalendarTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl();
    }

    public CustomCalendarTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl();
    }

    private void initControl(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#FF0000"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isToday) {
            //translate移动坐标位置
            canvas.translate(getWidth()/2,getHeight()/2);
            canvas.drawCircle(0,0,getHeight()/2-5,paint);
        }

    }
}
