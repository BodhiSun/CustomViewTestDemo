package com.bodhi.cv_coordinatesystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/12 18:15
 * desc :自定义坐标系
 */
public class Cv_CoordinateSystem extends View {
    private Paint mGridPaint;//网格画笔
    private Point mWinSize;//屏幕尺寸
    private Point mCoo;//坐标系原点


    public Cv_CoordinateSystem(Context context) {
        this(context,null);
    }

    public Cv_CoordinateSystem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //准备屏幕尺寸
        mWinSize=new Point();
        mCoo = new Point(300,800);
        HelpUtil.loadWinSize(getContext(),mWinSize);
        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制网格
        drawGrid(canvas,mWinSize,mGridPaint);
        //绘制坐标系
        drawCoo(canvas,mCoo,mWinSize,mGridPaint);


        //绘制点
        canvas.translate(mCoo.x,mCoo.y);
        mGridPaint.setColor(Color.BLACK);
        mGridPaint.setStrokeWidth(30);
        canvas.drawPoint(100,100,mGridPaint);
        canvas.drawPoints(new float[]{
                400, 400, 500, 500,
                600, 400, 700, 350
        }, mGridPaint);


        //绘制图片
        //1.定点绘制图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        canvas.drawBitmap(bitmap,-250,-700,mGridPaint);
        //2.适用变换矩阵绘制图片
        Matrix matrix =new Matrix();
        //设置变换矩阵:缩小3倍，斜切0.5,右移150，下移100
        matrix.setValues(new float[]{1,0.5f,100*2,0,1,-200*3,0,0,3});
        canvas.drawBitmap(bitmap,matrix,mGridPaint);
        //3.图片适用矩形区域不剪裁
        RectF rectF = new RectF(-250,-250,-50,-50);
        canvas.drawBitmap(bitmap,null,rectF,mGridPaint);
        //4.图片裁剪出的矩形区域
        Rect rect2 = new Rect(-150, 100, -100, 150);
        //图片适用矩形区域
        RectF rectf2 = new RectF(-250, 50, -50, 250);
        canvas.drawBitmap(bitmap,rect2,rectf2,mGridPaint);


        //绘制Picture 优势就是节能减排
        //Picture相当于先拍一张照片，并且是在别的Canvas上 当需要的时候在贴在当前的canvas上
        //创建Picture对象
        Picture picture = new Picture();
        //确定picture产生的Canvas元件的大小，并生成Canvas元件
        Canvas recodingCanvas  = picture.beginRecording(canvas.getWidth(), canvas.getHeight());
        //Canvas元件的操作
        recodingCanvas.drawRect(100, -300, 200, -200, mGridPaint);
        recodingCanvas.drawRect(0, -200, 100, -100, mGridPaint);
        recodingCanvas.drawRect(200, -200, 300, -100, mGridPaint);
        //Canvas元件绘制结束
        picture.endRecording();
        canvas.save();
        canvas.drawPicture(picture);
        canvas.translate(0,-400);
        canvas.drawPicture(picture);
        canvas.translate(400,200);
        //同上canvas.drawPicture
        picture.draw(canvas);
        canvas.restore();



        //canvas裁剪 内裁剪和外裁剪
        //内剪裁 只留下剪裁区域
        Rect rect = new Rect(100,200,300,400);
        canvas.clipRect(rect);
        canvas.drawColor(Color.GRAY);
        //外剪裁 扣除掉剪裁区域留下外面的
        Rect rectOut = new Rect(100,200,300,400);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutRect(rectOut);
            canvas.drawColor(Color.GRAY);
        }

    }

    /**
     *  绘制坐标系
     * @param canvas  画布
     * @param mCoo  坐标系原点
     * @param mWinSize 屏幕尺寸
     * @param paint 画笔
     */
    private void drawCoo(Canvas canvas, Point mCoo, Point mWinSize, Paint paint) {
        //初始化网格画笔
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        //取消设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(null);

        //绘制横竖直线
        canvas.drawPath(HelpUtil.cooPath(mCoo,mWinSize),paint);
        //右箭头
        canvas.drawLine(mWinSize.x,mCoo.y,mWinSize.x-40,mCoo.y-20,paint);
        canvas.drawLine(mWinSize.x,mCoo.y,mWinSize.x-40,mCoo.y+20,paint);
        //下箭头
        canvas.drawLine(mCoo.x,mWinSize.y-300,mCoo.x-20,mWinSize.y-340,paint);
        canvas.drawLine(mCoo.x,mWinSize.y-300,mCoo.x+20,mWinSize.y-340,paint);
        //为坐标系绘制文字
        drawTextForCoo(canvas,mCoo,mWinSize,paint);

    }

    /**
     * 为坐标系绘制文字
     *
     * @param canvas  画布
     * @param coo     坐标系原点
     * @param winSize 屏幕尺寸
     * @param paint   画笔
     */
    private void drawTextForCoo(Canvas canvas, Point coo, Point winSize, Paint paint) {
        //绘制X,Y
        paint.setTextSize(50);
        canvas.drawText("x", winSize.x - 60, coo.y - 40, paint);
        canvas.drawText("y", coo.x+20, winSize.y - 360, paint);
        //绘制0
        paint.setTextSize(25);
        paint.setStrokeWidth(2);
        canvas.drawText(0+"",coo.x-20,coo.y+30,paint);
        //X正轴文字
        for (int i = 1; i <= (winSize.x - coo.x) / 100; i++) {
            //绘制刻度值
            paint.setStrokeWidth(2);
            canvas.drawText(100 * i + "", coo.x - 20 + 100 * i, coo.y + 40, paint);
            //绘制刻度线
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x + 100 * i, coo.y, coo.x + 100 * i, coo.y - 10, paint);
        }
        //X负轴文字
        for (int i = 1; i <= coo.x / 100; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-100 * i + "", coo.x - 20 - 100 * i, coo.y + 40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x - 100 * i, coo.y, coo.x - 100 * i, coo.y - 10, paint);
        }
        //y正轴文字
        for (int i = 1; i <= (winSize.y - coo.y) / 100; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(100 * i + "", coo.x - 60, coo.y + 10 + 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y + 100 * i, coo.x + 10, coo.y + 100 * i, paint);
        }
        //y负轴文字
        for (int i = 1; i <= coo.y / 100; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-100 * i + "", coo.x - 60, coo.y + 10 - 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y - 100 * i, coo.x + 10, coo.y - 100 * i, paint);
        }
    }

    /**
     * 绘制网格
     * @param canvas 画布
     * @param mWinSize 屏幕尺寸
     * @param mGridPaint 画笔
     */
    private void drawGrid(Canvas canvas, Point mWinSize, Paint mGridPaint) {
        //初始化网格画笔
        mGridPaint.setStrokeWidth(2);
        mGridPaint.setColor(Color.RED);
        mGridPaint.setStyle(Paint.Style.STROKE);
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        mGridPaint.setPathEffect(new DashPathEffect(new float[]{10,5},0));

        canvas.drawPath(HelpUtil.gridPath(50,mWinSize),mGridPaint);
    }


}
