package com.bodhi.customview_slidercaptchaview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @author : Sun
 * @version : 1.0
 * @time : 2020/6/2
 * desc : 滑动验证码自定义View
 */
public class SliderCaptchaView extends AppCompatImageView {
    //滑块宽高、变换滑块阈值、允许的误差值
    private int mSliderHeight;
    private int mSliderWidth;
    private int changeSliderThreshold = 2;//触发重新生成滑块形状的阈值
    private int mMatchDeviation = 6;//滑块允许的误差值
    private long mStartDragTime;//开始滑动时的时间
    private long mInterval;//验证成功后 滑动的时间
    private int mCount;//当前滑块形状下 拖拽验证码的次数
    private boolean isSuccess;//验证是否通过

    //验证码控件的宽高
    private int mWidth;
    private int mHeight;

    //随机要扣的图(滑块阴影)的左上角点坐标、路径
    private Random mRandom;
    private int mSliderX;
    private int mSliderY;
    private Path mSliderPath;

    //滑块图片和滑块图片的透明度阴影图
    private Bitmap mSliderShadowBitmap;
    private Bitmap mSliderBitmap;

    //滑块的位移
    private int mDragerOffset;

    //滑块抠图的画笔
    private Paint mSliderPaint;
    private PorterDuffXfermode mPorterDuffXfermode;

    //滑块抠图阴影部分绘制的画笔
    private Paint mPaint;

    //滑块底部阴影的画笔
    private Paint mSliderShadowPaint;

    //验证成功的图片、画笔
    private Bitmap mSuccessBitmap;
    private Paint mSuccessPaint;
    private Paint mTextPaint;


    public SliderCaptchaView(Context context) {
        this(context, null);
    }

    public SliderCaptchaView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliderCaptchaView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //初始化属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SliderCaptchaView, defStyleAttr, 0);
        int mSliderSize = (int) ta.getDimension(R.styleable.SliderCaptchaView_sliderSize, 150);//xml设置的dp会自动转换成px，所以如果没有设置此属性默认设置150px
        mSliderWidth = mSliderSize;
        mSliderHeight = mSliderSize;
        changeSliderThreshold = ta.getInteger(R.styleable.SliderCaptchaView_changeSliderThreshold, 2);
        float deviation = (int) ta.getDimension(R.styleable.SliderCaptchaView_matchDeviation, 6);
        mMatchDeviation = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, deviation, getResources().getDisplayMetrics());

        //初始化画笔
        mSliderPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0x77000000);
        mPaint.setMaskFilter(new BlurMaskFilter(20,BlurMaskFilter.Blur.SOLID));// 设置画笔遮罩滤镜
        mSliderShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mSliderShadowPaint.setColor(Color.BLACK);
        mSliderShadowPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
        mSuccessPaint =new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mSuccessPaint.setColor(0x77ffffff);
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(8);
        mTextPaint.setTextSize(43);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mRandom = new Random();
        mSliderPath = new Path();
        mSuccessBitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.success);

    }

    //放在onSizeChanged方法初始化滑块相关数据 因为onSizeChanged只回调一次
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        //如果不在post中的话getImageMatrix()获取的值是原图的矩阵，并不是ImageView缩放的矩阵
        post(new Runnable() {
            @Override
            public void run() {
                createCaptcha();
            }
        });
    }

    //生成验证码区域
    private void createCaptcha() {
        if (getDrawable() != null) {
            createSliderPath();
            createSliderBitmap();
            //拖动的位移重置
            mDragerOffset = 0;
            invalidate();
        }
    }

    //生成滑块的抠图 和抠图背景
    private void createSliderBitmap() {
        mSliderBitmap = getBitmapByPathFromBitmap(((BitmapDrawable) getDrawable()).getBitmap(), mSliderPath);
        mSliderShadowBitmap = mSliderBitmap.extractAlpha();
    }

    //根据路径和原始图片返回抠图
    private Bitmap getBitmapByPathFromBitmap(Bitmap bitmap, Path path) {
        //以控件宽高 create一块bitmap 并把创建的bitmap作为画板
        Bitmap tempBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBitmap);
        // 抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        //绘制用于遮罩的圆形
        canvas.drawPath(path,mSliderPaint);
        //设置遮罩模式(图像混合模式)
        mSliderPaint.setXfermode(mPorterDuffXfermode);
        //考虑到scaleType等因素，要用Matrix对Bitmap进行缩放
        canvas.drawBitmap(bitmap,getImageMatrix(),mSliderPaint);
        mSliderPaint.setXfermode(null);
        return tempBitmap;
    }

    //生成滑块路径
    private void createSliderPath() {

        int gap = mSliderWidth / 3;//gap是指凹凸的起点和顶点的距离 宽度/3 效果比较好

        //随机生成验证码阴影(即要抠图的位置)左上角x y点 生成策略为右半边在满足能容下完整滑块+4个凸点的前提下的随机位置
        mSliderX = mWidth / 2 + mRandom.nextInt(mWidth / 2 - mSliderWidth - gap);
        mSliderY = gap + mRandom.nextInt(mHeight - mSliderHeight - gap);

        //每次生成都重置path
        mSliderPath.reset();
        mSliderPath.lineTo(0,0);

        //从左上角开始 绘制一个不规则的阴影
        mSliderPath.moveTo(mSliderX,mSliderY);
        //上边
        mSliderPath.lineTo(mSliderX + gap, mSliderY);
        drawPartCircle(new PointF(mSliderX+gap,mSliderY),new PointF(mSliderX+gap*2,mSliderY),mSliderPath,mRandom.nextBoolean());
        mSliderPath.lineTo(mSliderX+mSliderWidth,mSliderY);
        //右边
        mSliderPath.lineTo(mSliderX+mSliderWidth,mSliderY+gap);
        drawPartCircle(new PointF(mSliderX+mSliderWidth,mSliderY+gap),new PointF(mSliderX+mSliderWidth,mSliderY+gap*2),mSliderPath,mRandom.nextBoolean());
        mSliderPath.lineTo(mSliderX+mSliderWidth,mSliderY+mSliderHeight);
        //下边
        mSliderPath.lineTo(mSliderX+mSliderWidth-gap,mSliderY+mSliderHeight);
        drawPartCircle(new PointF(mSliderX+mSliderWidth-gap,mSliderY+mSliderHeight),new PointF(mSliderX+mSliderWidth-gap*2,mSliderY+mSliderHeight),mSliderPath,mRandom.nextBoolean());
        mSliderPath.lineTo(mSliderX,mSliderY+mSliderHeight);
        //左边
        mSliderPath.lineTo(mSliderX,mSliderY+mSliderHeight-gap);
        drawPartCircle(new PointF(mSliderX,mSliderY+mSliderHeight-gap),new PointF(mSliderX,mSliderY+mSliderHeight-gap*2),mSliderPath,mRandom.nextBoolean());
        mSliderPath.close();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //继承自ImageView Bitmap ImageView已经帮我们draw好了,所以只需在上面绘制和验证码相关的部分

        //首先绘制验证码阴影
        if(mSliderPath!=null){
            canvas.drawPath(mSliderPath,mPaint);
        }

        //绘制滑块
        if(mSliderBitmap!=null&&mSliderShadowBitmap!=null){
            // 先绘制阴影
            canvas.drawBitmap(mSliderShadowBitmap,-mSliderX+mDragerOffset,0,mSliderShadowPaint);
            canvas.drawBitmap(mSliderBitmap,-mSliderX+mDragerOffset,0,null);
        }

        //检测是否验证完成，绘制验证完成状态
        if(isSuccess){
            //绘制白色蒙层、成功图片和提示文字
            canvas.drawRect(0,0,mWidth,mHeight,mSuccessPaint);
            canvas.drawBitmap(mSuccessBitmap,mWidth/2.0f- mSuccessBitmap.getWidth() / 2.0f,mHeight/2.0f- mSuccessBitmap.getHeight() / 2.0f,null);
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            canvas.drawText("共用时：" + new BigDecimal(mInterval / 1000.0).setScale(1,
                    BigDecimal.ROUND_UP).toString() + "s", mWidth / 2.0f, mHeight / 2 +
                    mSuccessBitmap.getWidth() / 2.0f + 30 + distance, mTextPaint);
        }
    }

    //开始滑动
    public void startDragSlider(){
        mStartDragTime = System.currentTimeMillis();
        if (mCaptchaDragListener != null)
            mCaptchaDragListener.onStart();
    }

    //滑动过程中改变偏移值
    public void setDragOffset(int offset){
        mDragerOffset = offset;
        invalidate();
    }

    //滑动停止 校验是否通过
    public void stopDragSlider(){
        mCount++;

        //验证成功
        if(Math.abs(mDragerOffset-mSliderX)<mMatchDeviation){
            isSuccess = !isSuccess;
            mInterval = System.currentTimeMillis() - mStartDragTime;
            if (mCaptchaDragListener != null)
                mCaptchaDragListener.onVerifySuccess(mInterval);
            invalidate();
            return;
        }

        //验证失败
        if(mCount<changeSliderThreshold){
            //次数不达上限滑块返回初始位置
            mDragerOffset = 0;
            if (mCaptchaDragListener != null)
                mCaptchaDragListener.onVerifyFailure();
            invalidate();

        }else{
            //次数已达重新载入图片，重新生成滑块
            mCount = 0;
            mDragerOffset = 0;
            if (mCaptchaDragListener != null)
                mCaptchaDragListener.onReload(this);
            createCaptcha();
        }
    }

    //最大可滑动值
    public int getMaxSwipeValue(){
        return mWidth-mSliderWidth;
    }



    private CaptchaDragListener mCaptchaDragListener;

    public void setCaptchaDragListener(CaptchaDragListener captchaDragListener) {
        this.mCaptchaDragListener = captchaDragListener;
    }

    public interface CaptchaDragListener {
        void onStart();

        void onVerifySuccess(long interval);

        void onVerifyFailure();

        void onReload(SliderCaptchaView slider);
    }



    /**
     * 传入起点、终点 坐标、凹凸和Path。
     * 会自动绘制凹凸的半圆弧
     *
     * @param start 起点坐标
     * @param end   终点坐标
     * @param path  半圆会绘制在这个path上
     * @param outer 是否凸半圆
     */
    public static void drawPartCircle(PointF start, PointF end, Path path, boolean outer) {
        float c = 0.551915024494f;
        //中点
        PointF middle = new PointF(start.x + (end.x - start.x) / 2, start.y + (end.y - start.y) / 2);
        //半径
        float r1 = (float) Math.sqrt(Math.pow((middle.x - start.x), 2) + Math.pow((middle.y - start.y), 2));
        //gap值
        float gap1 = r1 * c;

        if (start.x == end.x) {
            //绘制竖直方向的

            //是否是从上到下
            boolean topToBottom = end.y - start.y > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (topToBottom) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸的 两个半圆
                path.cubicTo(start.x + gap1 * flag, start.y,
                        middle.x + r1 * flag, middle.y - gap1 * flag,
                        middle.x + r1 * flag, middle.y);
                path.cubicTo(middle.x + r1 * flag, middle.y + gap1 * flag,
                        end.x + gap1 * flag, end.y,
                        end.x, end.y);
            } else {
                //凹的 两个半圆
                path.cubicTo(start.x - gap1 * flag, start.y,
                        middle.x - r1 * flag, middle.y - gap1 * flag,
                        middle.x - r1 * flag, middle.y);
                path.cubicTo(middle.x - r1 * flag, middle.y + gap1 * flag,
                        end.x - gap1 * flag, end.y,
                        end.x, end.y);
            }
        } else {
            //绘制水平方向的

            //是否是从左到右
            boolean leftToRight = end.x - start.x > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (leftToRight) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸 两个半圆
                path.cubicTo(start.x, start.y - gap1 * flag,
                        middle.x - gap1 * flag, middle.y - r1 * flag,
                        middle.x, middle.y - r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y - r1 * flag,
                        end.x, end.y - gap1 * flag,
                        end.x, end.y);
            } else {
                //凹 两个半圆
                path.cubicTo(start.x, start.y + gap1 * flag,
                        middle.x - gap1 * flag, middle.y + r1 * flag,
                        middle.x, middle.y + r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y + r1 * flag,
                        end.x, end.y + gap1 * flag,
                        end.x, end.y);
            }
        }
    }
}
