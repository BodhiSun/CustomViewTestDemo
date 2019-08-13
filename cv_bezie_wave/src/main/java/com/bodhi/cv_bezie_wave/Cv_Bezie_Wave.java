package com.bodhi.cv_bezie_wave;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/8 17:35
 * desc :使用rQuadTo实现波浪线
 */
public class Cv_Bezie_Wave extends View {
    private Paint mPaint;
    private Path mPath;
    private int mItemWaveLength = 1000;//默认波长 也可以控制波的弧度
    private int dx;//动画向右偏移的位置

    public Cv_Bezie_Wave(Context context) {
        this(context,null);
    }

    public Cv_Bezie_Wave(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
    }


    int num=0;
    int preDx=Integer.MIN_VALUE;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        int originY = 200;
        int halfWaveLen = mItemWaveLength/2;

        //控制y上下偏移的距离
        int h=0;
        if(dx<preDx){
            num++;
        }
        preDx=dx;
        if(num%2==0){
            //上下偏移和向右偏移用的一个ValueAnimator 所以除4控制幅度
            h=dx/4;//向下偏移
        }else{
            //回弹 向上偏移
            h=mItemWaveLength/4-dx/4;
        }
        h=0;

        //将整个波形不断像右平移 就达到看起来动的效果 x向右平移向右动 y上下平移上下动
        mPath.moveTo(-mItemWaveLength+dx,originY+h);
        for(int i=-mItemWaveLength;i<=getWidth()+mItemWaveLength;i+=mItemWaveLength){
            //前半个波长
            mPath.rQuadTo(halfWaveLen/2,-100,halfWaveLen,0);
            //后半个波长
            mPath.rQuadTo(halfWaveLen/2,100,halfWaveLen,0);
        }

        //lineTo 把整体波形闭合起来
        //闭合右边 画波浪最右边点到整个View右下角点的线段
        mPath.lineTo(getWidth(),getHeight());
        //闭合下边 画整个View右下角点到左下角点的线段
        mPath.lineTo(0,getHeight());
        //闭合左边 线段收尾相连
        mPath.close();

        canvas.drawPath(mPath,mPaint);

    }

    public void startAnim(){
        //最大只要我们移动一个波长的长度，波纹就会重合，就可以实现无限循环了
        ValueAnimator animator = ValueAnimator.ofInt(0,mItemWaveLength);
        animator.setDuration(1000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx= (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

}
