package com.bodhi.cv_paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/6 16:08
 * desc :PathEffect-CornerPathEffect 手绘圆润效果
 */
public class CV_Draw extends View {
    Paint mPaint = new Paint();
    PathEffect mPathEffect = new CornerPathEffect(300);
    Path mPath = new Path();

    public CV_Draw(Context context) {
        this(context,null);
    }

    public CV_Draw(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CV_Draw(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_CANCEL|MotionEvent.ACTION_MOVE:
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制原始路径
        canvas.save();
        mPaint.setColor(Color.GRAY);
        mPaint.setPathEffect(null);
        canvas.drawPath(mPath,mPaint);
        canvas.restore();

        // 绘制带有效果的路径
        canvas.save();
        canvas.translate(0,canvas.getHeight()/2);
        mPaint.setColor(Color.RED);
        mPaint.setPathEffect(mPathEffect);
        canvas.drawPath(mPath,mPaint);
        canvas.restore();

    }
}
