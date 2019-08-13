package com.bodhi.customviewtestdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/2/12 11:49
 * desc :自定义View测试类伪代码
 *
 * 自定义View的实现方式：
 * 第一种.继承系统控件 重写特定方法
 * 第二种.组合系统控件
 * 第三种.自定义绘制控件
 *
 *
 * 步骤：
 * 1.自定义属性声明与获取
 * 2.测量onMeasure
 * 3.布局onLayout(ViewGroup)
 * 4.绘制onDraw
 * 5.onTouchEvent
 * 6.onInterceptTouchEvent(ViewGroup)
 *
 * 辅助方法：
 * onSaveInstanceState 自定义View的状态存储
 * onRestoreInstanceState 自定义View的状态获取
 *
 * 辅助类：
 * ViewConfiguration 获取常用的常量 比如mTouchSlop
 * ScaleGestureDetector 缩放手势
 * ViewDragHelper 拖拽
 */
public class CustomView1 extends ViewGroup {

    public CustomView1(Context context) {
        super(context);
    }

    public CustomView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    //view绘制过程中onMeasure方法可能触发多次
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int destHeightSize=0;
        int destWidthSize=0;
        if (heightMode==MeasureSpec.EXACTLY) {//MeasureSpec.AT_MOST/MeasureSpec.UNSPECIFIED
            destHeightSize=heightSize;
        }else if(heightMode==MeasureSpec.AT_MOST){
            destHeightSize=200;
        }else{
            destHeightSize=200;
        }

        setMeasuredDimension(destWidthSize,destHeightSize);
    }

    //view绘制过程中onLayout方法只会触发一次，所以耗时和初始化的操作可以在此方法中。
    //onLayout父控件决定子View显示的位置，当自定义View是ViewGroup时需要重写该方法
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if(child.getVisibility()==GONE){
                continue;
            }

            int orgLeft = child.getLeft();
            int orgTop = child.getTop();
            int width = child.getWidth();
            int height = child.getHeight();

            int destLeft=0;
            int destTop=0;
            destLeft=orgLeft+10;
            destTop=orgTop+10;

            child.layout(destLeft,destTop,destLeft+width,destTop+height);

//            requestLayout();
        }

    }

    //绘制内容区域，不需要考虑背景
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawArc();

        canvas.save();
        canvas.restore();

        //当View变化需要重绘的时候调用刷新
        invalidate();//主线程调用
        postInvalidate();//子线程调用

    }

    //有与用户交互需求
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                //进行一些初始化、复制等操作
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //释放各种资源、重置变量
                break;
            case MotionEvent.ACTION_CANCEL:
                //释放各种资源、重置变量
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //如果支持多指，在此设置activePointer
                int actionIndex = event.getActionIndex();
                int pointerId = event.getPointerId(actionIndex);
                int y = (int) event.getY();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //如果支持多指,且抬起的是activePointer,则重新选择一个手指为活跃的手指

                break;

        }

//        getParent().requestDisallowInterceptTouchEvent(true);//告诉父控件不要拦截子控件down/up整个手势动作
        return super.onTouchEvent(event);
    }

    //此方法只针对ViewGroup，返回true 表示拦截事件 ViewGroup自己处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        int mLastMotionY=0;
        int mTouchSlop=200;
        boolean mIsBeingDragged=false;
        switch (action&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                int yDiff = Math.abs(y = mLastMotionY);
                if(yDiff>mTouchSlop){
                    mIsBeingDragged=true;
                    mLastMotionY=y;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mLastMotionY= (int) ev.getY();
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return mIsBeingDragged;
    }

    //自定义View的状态存储
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("INSTANCE_STATUS",super.onSaveInstanceState());
        bundle.putFloat("STATUS_ALPHA",1.0f);
        return bundle;
    }

    //自定义View的状态获取
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle =(Bundle)state;
            float status_alpha = bundle.getFloat("STATUS_ALPHA");
            super.onRestoreInstanceState(bundle.getParcelable("INSTANCE_STATUS"));
        }else{
            super.onRestoreInstanceState(state);
        }
    }
}
