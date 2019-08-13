package com.bodhi.cv_canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/12 11:00
 * desc :canvas变换与操作
 */
public class Cv_canvas extends View {


    public Cv_canvas(Context context) {
        super(context);
    }

    public Cv_canvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paintRed = new Paint();
        paintRed.setAntiAlias(true);
        paintRed.setColor(Color.RED);
        paintRed.setStrokeWidth(5);
        paintRed.setStyle(Paint.Style.STROKE);
        Paint paintGray = new Paint(paintRed);
        paintGray.setColor(Color.GRAY);

        //一、translate平移 画布的原状是以左上角为原点 向右是X轴正方向正数负方向负数，向下是Y轴正方向
        //二、显示所画东西的该屏幕不是Canvas 屏幕显示与Canvas不是一个概念 Canvas相当于一个透明图层
        Rect rectTrans = new Rect(0,0,400,200);
        canvas.drawRect(rectTrans,paintRed);
        canvas.translate(100,100);
        canvas.drawRect(rectTrans,paintGray);
        //调用平移、旋转等函数来对Canvas进行了操作，那么这个操作是不可逆的 画布的后续位置都是这些操作后的位置
        canvas.translate(-50,50);
        //Canvas画图时（即调用Draw系列函数），都会产生一个透明图层 在这个图层上画图，画完之后覆盖在屏幕上显示
        canvas.drawRect(rectTrans,paintRed);


        //三、Rotate旋转 看起来觉得是图片旋转了 但旋转的是画布 在此画布上画的东西显示出来的时候看起来就是旋转的
        canvas.translate(550,-150);
        canvas.drawRect(rectTrans,paintRed);
        //画布默认构造的旋转是围绕坐标原点来旋转的 正数是顺时针旋转，负数指逆时针旋转
        canvas.rotate(15);
        canvas.drawRect(rectTrans,paintGray);
        //也可以指定旋转的中心点坐标 构造
        canvas.rotate(-15);
        canvas.rotate(-30,400,0);
        canvas.drawRect(rectTrans,paintGray);


        //四、scale缩放 这里X、Y轴的密度的改变 显示到图形上就会正好相同 比如X轴缩小，那么显示的图形也会缩小
        canvas.rotate(30,400,0);
        canvas.translate(-600,500);
        canvas.drawRect(rectTrans,paintRed);
        //sx:水平方向伸缩的比例 sx为小数为缩小，sx为整数为放大 sy:垂直方向伸缩的比例
        canvas.scale(0.5f, 1);
        canvas.drawRect(rectTrans,paintGray);


        //五、skew扭曲 其实译成斜切更合适 这里全是倾斜角度的tan值 比如在X轴方向上倾斜60度,tan60=根号3,小数对应1.732
        canvas.scale(2, 1);//2*0.5=1 原来的比例
        canvas.translate(500,0);
        canvas.drawRect(rectTrans,paintRed);
        canvas.skew(1.732f,0);//X轴倾斜60度，Y轴不变
        canvas.drawRect(rectTrans,paintGray);


        //六、clip剪裁 通过与Rect、Path、Region等构造取交、并、差等集合运算来获得最新的画布形状 这个操作也是不可逆的。
        //七、save()、restore() 画布的保存与恢复 前面的所有对画布的不可逆的操作 都可以用这两个方法提前进行实时保存和恢复
        canvas.skew(-1.732f,0);
        canvas.translate(-500,250);

        //每次调用Save（）函数，都会把当前的画布的状态进行保存，然后放入特定的栈中；
        canvas.save();//保存的画布大小为全屏幕大小

        canvas.clipRect(new Rect(0, 0, getWidth(), 600));
        canvas.drawColor(Color.RED);
        //保存画布大小为Rect(0, 0, getWidth(), 600)
        canvas.save();

        canvas.clipRect(new Rect(100, 100, 500, 500));
        canvas.drawColor(Color.GREEN);
        //保存画布大小为Rect(100, 100, 500, 500)
        canvas.save();

        canvas.clipRect(new Rect(200, 200, 400, 400));
        canvas.drawColor(Color.BLUE);
        //保存画布大小为Rect(200, 200, 400, 400)
        canvas.save();

        canvas.clipRect(new Rect(250, 250, 350, 350));
        canvas.drawColor(Color.GRAY);


        //每当调用Restore（）函数，就会把栈中最顶层的画布状态取出来，并按照这个状态恢复当前的画布
        //将栈顶的画布状态取出来，作为当前画布，并画成黄色背景
//        canvas.restore();
//        canvas.restore();
//        canvas.drawColor(Color.YELLOW);




    }
}
