package com.bodhi.cv_canvas_layer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/12 14:29
 * desc :Canvas与图层
 *
 * 如何获得一个Canvas对象:
 * 方法一：自定义view时， 重写onDraw、dispatchDraw方法
 * 无论是View还是ViewGroup对它们俩的调用顺序都是onDraw()->dispatchDraw()
 * 但在ViewGroup中，当它有背景的时候就会调用onDraw()方法，否则就会跳过onDraw()直接调用dispatchDraw()；
 * 在View中，onDraw()和dispatchDraw()都会被调用的，所以无论把绘图代码放在这两个哪个方法中都可以得到效果
 * 结论：在绘制View控件时，需要重写onDraw()函数，在绘制ViewGroup时，需要重写dispatchDraw()函数。
 *
 * 方法二：使用Bitmap创建 其中bitmap可以从图片加载，也可以创建等其他方式
 * 如果我们用bitmap构造了一个canvas，那这个canvas上绘制的图像也都会保存在这个bitmap上，而不是画在View上
 * 如果想画在View上就必须使用OnDraw（Canvas canvas）函数中传进来的canvas画一遍bitmap才能画到view上
 *
 * 方法三：SurfaceHolder.lockCanvas() 在操作SurfaceView时需要用到Canvas
 *
 */
public class Cv_canvas extends View {


    public Cv_canvas(Context context) {
        super(context);
    }

    public Cv_canvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Cv_canvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("cv_canvas","---onMeasure---");
    }

    /**
     * onDraw()的意思是绘制视图自身
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("cv_canvas","---onDraw---");

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);


        //使用Bitmap创建Canvas
        //方法一：新建一个空白bitmap
        Bitmap bitmap = Bitmap.createBitmap(500,200,Bitmap.Config.ARGB_8888);
        //方法二：从图片中加载
        Bitmap bitmapLoad=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        Canvas mBmpCanvas  =new Canvas(bitmap);
        mBmpCanvas.drawColor(Color.BLACK);
        //将文字画在了mBmpCanvas上，也就是我们新建bitmap图片上！目前图片跟我们view没有任何关系
        mBmpCanvas.drawText("风萧萧兮易水寒",0,100,paint);

        //把bitmap图片画到view上 才能正常文字
        canvas.drawBitmap(bitmap,0,0,paint);

    }

    /**
     * dispatchDraw()是绘制子视图
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.e("cv_canvas","---dispatchDraw---");

    }




}
