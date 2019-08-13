package com.bodhi.customview_progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/2/27 16:20
 * desc :
 */
public class RoundProgressBarWithProgress extends CustomHorizontalProgressBar{

    private int mRadius=dp2px(30);

    private int mMaxPaintWidth;

    public RoundProgressBarWithProgress(Context context) {
        this(context,null);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mReachHeight = (int) (mUnReachHeight*2f);//为了视觉效果好看

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRoundProgressBar);
        mRadius= (int) typedArray.getDimension(R.styleable.CustomRoundProgressBar_round_progress_radius,mRadius);

        typedArray.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//连接处为弧形
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(mReachHeight,mUnReachHeight);
        int expect = mRadius*2+mMaxPaintWidth+getPaddingLeft()+getPaddingRight();

        int width = resolveSize(expect,widthMeasureSpec);
        int height = resolveSize(expect,heightMeasureSpec);

        int realWidth = Math.min(width,height);

        mRadius = (realWidth-getPaddingLeft()-getPaddingRight()-mMaxPaintWidth)/2;

        setMeasuredDimension(realWidth,realWidth);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text = getProgress()+"%";
        float textWidth=mPaint.measureText(text);
        float textHeight = (mPaint.descent()+mPaint.ascent())/2;

        canvas.save();

        canvas.translate(getPaddingLeft()+mMaxPaintWidth/2,getPaddingTop()+mMaxPaintWidth/2);
        mPaint.setStyle(Paint.Style.STROKE);

        //draw unreach bar
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);

        //draw reach bar
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle= getProgress()*1.0f/getMax()*360;
        canvas.drawArc(new RectF(0,0,mRadius*2,mRadius*2),0,sweepAngle,false,mPaint);

        //draw text
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text,mRadius-textWidth/2,mRadius-textHeight,mPaint);

        canvas.restore();
    }
}
