package com.bodhi.cv_canvas_layer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/12 15:25
 * desc :图层与画布
 * 除了save()和restore()以外，还有其它一些函数来保存和恢复画布状态
 * saveLayer()和restoreToCount， saveLayerAlpha
 *
 * saveLayer:会创建一个全新透明的bitmap(画布)，大小与指定保存的区域一致，其后的绘图操作都放在这个bitmap(画布)上进行
 * 在绘制结束后，会直接盖在上一层的Bitmap(画布)上显示。只有调用restore()、resoreToCount()后,才会返回到原始画布上绘制。
 *
 * saveLayerAlpha:相比saveLayer，多一个alpha参数，用以指定新建画布透明度,取值范围为0-255，可以用16进制的oxAA表示
 * 这个函数的意义也是在调用的时候会新建一个bitmap画布，以后的各种绘图操作都作用在这个画布上，但这个画布是有透明度的，
 * 透明度就是通过alpha值指定的。
 *
 * Mode.SRC_IN的效果：在处理源图像时，以显示源图像为主，在相交时利用目标图像(把之前画布上所有的内容都做
 * 为目标图像)的透明度来改变源图像的透明度和饱和度。当目标图像透明度为0时，源图像就完全不显示。
 *
 */
public class Cv_layer extends View {
    private int width = 400;
    private int height = 400;
    private Bitmap dstBmp;
    private Bitmap srcBmp;
    private Paint mPaint;



    public Cv_layer(Context context) {
        this(context,null);
    }

    public Cv_layer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        srcBmp =creatSrcBmp(width,height);
        dstBmp =creatDstBmp(width,height);
        mPaint=new Paint();
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //整个画布变成绿色 即透明度不在是0
        canvas.drawColor(Color.GREEN);

       //saveLayer会创建一个指定保存的区域的全新透明的画布
        int layerID  = canvas.saveLayer(0, 0, width * 2, height * 2, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(dstBmp,0,0,mPaint);
        //setXfermode图形混合 SRC_IN即透明度不为0的交集中src部分
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(srcBmp,width/2,height/2,mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerID);

        canvas.drawBitmap(dstBmp,width,0,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(srcBmp,width*1.5f,height/2,mPaint);
        mPaint.setXfermode(null);


        canvas.drawBitmap(dstBmp,0,height*2,mPaint);
        //saveLayerAlpha会创建一个指定保存的区域的全新指定透明度的画布
        int layerIDAlpha  = canvas.saveLayerAlpha(0, 0, width * 2, height * 4,0x88, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(srcBmp,width/2,height*2.5f,mPaint);
        canvas.restoreToCount(layerIDAlpha);

    }


    private Bitmap creatDstBmp(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFFFFCC44);
        canvas.drawOval(new RectF(0,0,width,height),paint);
        return bitmap;
    }

    private Bitmap creatSrcBmp(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF66AAFF);
        canvas.drawRect(0,0,width,height,paint);
        return bitmap;
    }


}
