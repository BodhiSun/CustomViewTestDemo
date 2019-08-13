package com.bodhi.cv_paint_canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/7 10:58
 * desc :
 */
public class Cv_View extends View {
    public Cv_View(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画笔基本属性
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿功能
        paint.setColor(Color.RED);//设置画笔颜色
        paint.setStyle(Paint.Style.FILL);//设置填充样式
        paint.setStrokeWidth(5);//设置画笔宽度
        //radius:阴影的倾斜度  dx:水平位移  dy:垂直位移
//        paint.setShadowLayer(10,15,15,Color.GRAY);

        //设置画布背景颜色
        canvas.drawRGB(255,255,255);

        //画圆
        canvas.drawCircle(190,200,150,paint);

        //画直线
        canvas.drawLine(400,100,600,300,paint);

        //画多条直线 每两个坐标形成一个点 没四个坐标形成两个点构成一条直线 即只能为4的倍数
        float[] linePts = new float[]{100,400,300,600,400,600,800,400};
        canvas.drawLines(linePts,paint);

        canvas.translate(0,700);

        //画点
        paint.setStrokeWidth(20);
        canvas.drawPoint(100,0,paint);

        //画多个点
        float[] pts=new float[]{200,0,300,100,400,50,600,0,700,100,800,50};
        // pts:点的合集 两个数值一个点  offset:集合中跳过的数值个数   参与绘制的数值的个数
        canvas.drawPoints(pts,2,8,paint);

        canvas.translate(0,150);

        //画矩形 RectF Rect都是矩形辅助类，区别不大
        //直接传入矩形的四个点，画出矩形
        canvas.drawRect(10,10,100,100,paint);

        RectF rectF = new RectF(120,10,210,100);
        //使用RectF构造
        canvas.drawRect(rectF,paint);

        Rect rect = new Rect(230,10,320, 100);
        //使用Rect构造
        canvas.drawRect(rect,paint);

        //画圆角矩形
        //直接传入矩形的四个点，以及圆角大小  画出矩形
        canvas.drawRoundRect(500,10,700,100,20,20,paint);

        RectF roundCornerRectF= new RectF(800,10,1000,100);
        //使用RectF构造 rx，ry 表示角度大小
        canvas.drawRoundRect(roundCornerRectF,20,20,paint);

        canvas.translate(0,100);

        //画圆形 cx,cy 圆心坐标 radius半径
        canvas.drawCircle(150,150,100,paint);

        //画椭圆 椭圆是根据矩形生成的  以矩形的长为椭圆的X轴，矩形的宽为椭圆的Y轴
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        RectF rectFOval = new RectF(400,50,800,250);
        canvas.drawOval(rectFOval,paint);

        canvas.translate(0,300);

        //画弧 弧是椭圆的一部分 椭圆是根据矩形来生成的  所以弧也是根据矩形来生成的
        RectF rectFArc = new RectF(100,10,400,100);
        canvas.drawArc(rectFArc,0,90,true,paint);
        RectF rectFArc2 = new RectF(500,10,800,100);
        canvas.drawArc(rectFArc2,0,90,false,paint);

        paint.setColor(0x66888888);
        canvas.drawOval(rectFArc,paint);
        canvas.drawOval(rectFArc2,paint);
        paint.setColor(0x33888888);
        canvas.drawRect(rectFArc,paint);
        canvas.drawRect(rectFArc2,paint);




    }
}
