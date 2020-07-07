package com.bodhi.customview_odometer_bubble;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * @author : Sun
 * @version : 1.0
 * @time : 2019/12/24
 * desc :气泡自定义View
 */
public class BubbleView extends View {
    private int coinPaintWidth;
    private int numPaintWidth;
    private int countDownPaintWidth;
    private int coinNumTextSize;
    private int colorCoinNum;
    private int colorCountDown;
    private int mCountDownRadius;
    private int coinInnerEdge;
    private int coinNum;
    private float countDownTime;
    private float currentTime;
    private boolean isCountDownFinish;
    private boolean isShowNum;
    private boolean isShowGoldBg;
    private boolean isClickAble;
    private int floatAnimateTime;
    private float currentHeight;

    private Paint coinPaint;
    private Paint numPaint;
    private Paint.FontMetrics fontMetrics;
    private Paint countDownPaint;
    private RectF mRectCoin;
    private RectF mRectCoinBg;

    private BubbleClickListener bubbleClickListener;

    /**
     * 对图形进行处理的矩阵类
     */
    private ObjectAnimator transAnimator;
    private ValueAnimator valueAnimator;


    public BubbleView(Context context) {
        this(context, null);
    }

    public BubbleView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        coinPaintWidth = 1;
        numPaintWidth = 2;
        countDownPaintWidth = 1;
        coinNumTextSize = sp2px(14);
        colorCoinNum = Color.parseColor("#dd6812");
        colorCountDown = Color.parseColor("#1F000000");
        mCountDownRadius = dp2px(15);
        coinInnerEdge = dp2px(5);
        coinNum = 0;
//        countDownTime = 2.0f * 60.0f;
        countDownTime = 0f;
        currentTime = 0.0f;
        isCountDownFinish = false;
        isClickAble = true;
        currentHeight = getTranslationY();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BubbleView);
        isShowNum = typedArray.getBoolean(R.styleable.BubbleView_isShowNum, true);
        isShowGoldBg = typedArray.getBoolean(R.styleable.BubbleView_isShowGoldBg, true);
        floatAnimateTime = typedArray.getInt(R.styleable.BubbleView_floatAnimateTime, 2000);

        typedArray.recycle();
    }


    private void initPaint() {
        coinPaint = new Paint();
        coinPaint.setAntiAlias(true);
        coinPaint.setStyle(Paint.Style.STROKE);
        coinPaint.setStrokeWidth(coinPaintWidth);
        coinPaint.setStrokeCap(Paint.Cap.ROUND);

        numPaint = new Paint();
        numPaint.setAntiAlias(true);
        numPaint.setStyle(Paint.Style.FILL);
        numPaint.setStrokeWidth(numPaintWidth);
        numPaint.setTextSize(coinNumTextSize);
        numPaint.setColor(colorCoinNum);
        numPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = numPaint.getFontMetrics();

        countDownPaint = new Paint();
        countDownPaint.setAntiAlias(true);
        countDownPaint.setStyle(Paint.Style.FILL);
        countDownPaint.setStrokeWidth(countDownPaintWidth);
        countDownPaint.setColor(colorCountDown);
        countDownPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int mWidth, mHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = getPaddingLeft() + mCountDownRadius * 2 + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSize);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = getPaddingTop() + mCountDownRadius * 2 + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, heightSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
        mCountDownRadius = Math.min((getMeasuredWidth() - getPaddingLeft() - getPaddingRight()), getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 2;

        mRectCoin = new RectF(-mCountDownRadius + coinInnerEdge, -mCountDownRadius + coinInnerEdge, mCountDownRadius - coinInnerEdge, mCountDownRadius - coinInnerEdge);
        mRectCoinBg = new RectF(-mCountDownRadius, -mCountDownRadius, mCountDownRadius, mCountDownRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画图片
        drawBitmap(canvas);

        //画金币数(具体数字或？)
        if (isCountDownFinish || !isShowGoldBg||countDownTime==0)
            drawText(canvas);

        //画倒计时圆
        if (!isCountDownFinish && isShowGoldBg&&countDownTime>0)
            drawCircle(canvas);

    }

    private void drawBitmap(Canvas canvas) {
        canvas.translate(getPaddingLeft() + mCountDownRadius, getPaddingTop() + mCountDownRadius);

        Bitmap coin = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_home_coin);
        if(coin!=null&&mRectCoin!=null)
            canvas.drawBitmap(coin, null, mRectCoin, coinPaint);

        if (isShowGoldBg) {
            Bitmap coinbg = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_home_coinbg);
            if (coinbg != null && mRectCoinBg != null)
                canvas.drawBitmap(coinbg, null, mRectCoinBg, coinPaint);
        }

    }

    private void drawText(Canvas canvas) {

        int textBaseLine = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        if (isShowNum)
            canvas.drawText(coinNum + "", 0, textBaseLine, numPaint);
        else
            canvas.drawText("?", 0, textBaseLine, numPaint);

    }

    private void drawCircle(Canvas canvas) {
        canvas.drawArc(mRectCoinBg, 270, ((countDownTime - currentTime) / countDownTime) * 360, true, countDownPaint);

        int textBaseLine = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);

        canvas.drawText(formatStr((int)countDownTime - (int)currentTime), 0, textBaseLine, numPaint);
    }

    private String formatStr(int second){
//        System.out.println("bubbSec:"+second);
        long sec = second;
        long min = sec / 60;
        String fMin = String.format("%02d", min);
        String fSec = String.format("%02d", sec%60);

        return fMin+":"+fSec;
    }

    public boolean isShow() {
        return isClickAble;
    }

    public BubbleView show() {
        if (isClickAble) return this;

        isClickAble = true;
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY).setDuration(400).start();
        return this;
    }

    public BubbleView dismissd() {
        isClickAble = false;
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY).setDuration(300).start();
        return this;
    }

    public BubbleView setCoinNum(int coinNum) {
        this.coinNum = coinNum;
        invalidate();
        return this;
    }

    public BubbleView setCurrentProgress(float currentTime, float countDownTime) {
        if (currentTime < 0 || countDownTime < 0 || currentTime > countDownTime) {
            throw new RuntimeException("time value is illegal");
        }

        this.currentTime = currentTime;
        this.countDownTime = countDownTime;
        return this;

    }

    public BubbleView startCountDownAnimate() {
        if (countDownTime <= 0) return this;

        stopCountDownAnimate();

        float startCountDown = currentTime;
        valueAnimator = ValueAnimator.ofFloat(startCountDown, countDownTime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentTime = (Float) animation.getAnimatedValue();
                if (currentTime == countDownTime)
                    isCountDownFinish = true;
                else {
                    isCountDownFinish = false;
                }
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration((int) (countDownTime - startCountDown) * 1000);
        valueAnimator.start();

        return this;
    }

    public BubbleView stopCountDownAnimate() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
        }
        return this;
    }

    public void startFloatAnimate() {
        if (transAnimator != null && transAnimator.isRunning()) return;

        transAnimator = ObjectAnimator.ofFloat(this, "translationY", getTranslationY(), 30f, getTranslationY());
        transAnimator.setDuration(floatAnimateTime);
        transAnimator.setInterpolator(new LinearInterpolator());
        transAnimator.setRepeatMode(ValueAnimator.RESTART);
        transAnimator.setRepeatCount(ValueAnimator.INFINITE);
        transAnimator.setStartDelay(500);
        transAnimator.start();
    }

    public void stopFloatAnimate() {

        if (transAnimator != null) {
            transAnimator.cancel();
            transAnimator.removeAllUpdateListeners();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (bubbleClickListener != null && isClickAble)
                bubbleClickListener.bubbleClick(isCountDownFinish, countDownTime - currentTime, getId());
        }
        return true;
    }

    public void setOnClickListener(BubbleClickListener bubbleClickListener) {
        this.bubbleClickListener = bubbleClickListener;
    }

    public interface BubbleClickListener {
        void bubbleClick(boolean isCountFinish, float remainTime, int viewId);
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }


}
