package com.bodhi.cv_bezie_wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/8 11:58
 * desc :
 *
 * 一阶贝赛尔曲线:对于一阶贝赛尔曲线，可以理解为在起始点和终点形成的这条直线上，匀速移动的点
 *
 *  二阶贝赛尔曲线:是由两个一阶贝赛尔曲线，起始点P0,终点P2，控制点P1组成 然后在某一时刻p0和p1形成一条一阶
 * 贝塞尔曲线 上面运动的点记做Q0，p1和p2也形成一条一阶贝塞尔曲线 运动点记做Q1 最后Q0和Q1又形成一条一阶贝
 * 塞尔曲线 在他们这条一阶贝塞尔曲线动态移动的点B的移动轨迹 就是二阶贝塞尔曲线的最终形态
 *
 */
public class Cv_Bezie extends View {
    public Cv_Bezie(Context context) {
        super(context);
    }

    public Cv_Bezie(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Cv_Bezie(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //path中的二阶贝塞尔曲线
//        path.quadTo(float x1, float y1, float x2, float y2);
//        path.rQuadTo(float dx1, float dy1, float dx2, float dy2);
        //path中的三阶贝塞尔曲线
//        path.cubicTo(float x1, float y1, float x2, float y2,float x3, float y3);
//        path.rCubicTo(float x1, float y1, float x2, float y2,float x3, float y3);


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(20);

        //二阶贝塞尔曲线-quadTo
        Path path = new Path();
        //整条线的起始点是通过Path.moveTo(x,y)来指定的 没有调用Path.moveTo以控件左上角(0,0)为起始点
        path.moveTo(100,300);
        //参数中(x1,y1)是控制点坐标，(x2,y2)是终点坐标
        path.quadTo(200,200,300,300);
        //连续调用quadTo()，前一个quadTo()的终点，就是下一个quadTo()函数的起点
        path.quadTo(400,400,500,300);
        canvas.drawPath(path,paint);

        //二阶贝塞尔曲线-rQuadTo 和上面quadTo轨迹是一样的
        paint.setStrokeWidth(5);
        paint.setColor(Color.GRAY);
        path.reset();
        //整条线的起始终点是通过Path.moveTo(x,y)来指定的
        path.moveTo(100,300);
        //这四个参数传递的都是相对值，相对上一个终点的位移值。
        //dx1:控制点X坐标 相对上一个终点X坐标的位移坐标，正值表示相加，负值表示相减
        //dy1:控制点Y坐标 相对上一个终点Y坐标的位移坐标。正值表示相加，负值表示相减；
        //dx1:终点X坐标 相对上一个终点X坐标的位移值，正值表示相加，负值表示相减
        //dy2:终点Y坐标 相对上一个终点Y坐标的位移值，正值表示相加，负值表示相减
        path.rQuadTo(100,-100,200,0);
        path.rQuadTo(100,100,200,0);
        canvas.drawPath(path,paint);


    }





}
