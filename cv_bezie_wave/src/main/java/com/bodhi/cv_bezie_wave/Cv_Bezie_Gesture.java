package com.bodhi.cv_bezie_wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/8 16:23
 * desc : 利用手势绘制路径lineto(线与线之间有明显的转折)-利用二阶贝赛尔曲线quadTo实现线与线之间的平滑过渡
 */
public class Cv_Bezie_Gesture extends View {
    //记录手指滑动轨迹
    Path mPath = new Path();
    Paint mPaint;


    public Cv_Bezie_Gesture(Context context) {
        this(context, null);
    }

    public Cv_Bezie_Gesture(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0x554e4e4e);
        canvas.drawPath(mPath, mPaint);
    }

    public void reset() {
        mPath.reset();
        //主线程刷新 调用onDraw方法 (一定要在UI线程执行，如果不是在UI线程就会报错)
        invalidate();
    }


//    //lineto的方法实现手势路径的绘制
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPath.moveTo(event.getX(),event.getY());
//                //return true表示当前控件已经消费了下按动作 之后的ACTION_MOVE、ACTION_UP动作也会继续传递到当前控件中
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                mPath.lineTo(event.getX(),event.getY());
//                //子线程刷新 调用onDraw方法 (可以在任何线程中执行而不会出错。利用handler给主线程发送刷新界面消息)
//                postInvalidate();
//                break;
//            default:
//                break;
//
//
//        }
//        return super.onTouchEvent(event);
//    }


    //使用quadTo二阶贝塞尔曲线的方法是 找到前一个线段的中点作为起始点 后一个线段的中点作为终止点 两线段交
    //点作为控制点 (这种设计方法会把开头的线段一半和结束的线段的一半抛弃掉)
    private float mPreX,mPreY;//前一个点的x,y
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(),event.getY());
                mPreX=event.getX();
                mPreY=event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX=(mPreX+event.getX())/2;
                float endY=(mPreY+event.getY())/2;
                mPath.quadTo(mPreX,mPreY,endX,endY);
                mPreX = event.getX();
                mPreY =event.getY();
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

}
