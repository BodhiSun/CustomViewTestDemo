package com.bodhi.customview_progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/2/13 18:37
 * desc :
 */
public class CustomHorizontalProgressBar extends ProgressBar {

    private final int DEFAULT_TEXT_SIZE = 10;//sp
    private final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private final int DEFAULT_UNREACH_COLOR = 0xFFD3D6DA;
    private final int DEFAULT_UNREACH_HEIGHT = 2;//dp
    private final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    private final int DEFAULT_REACH_HEIGHT = 2;//dp
    private final int DEFAULT_TEXT_OFFSET = 10;//dp

    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnReachColor = DEFAULT_UNREACH_COLOR;
    protected int mUnReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    protected int mReachColor = DEFAULT_REACH_COLOR;
    protected int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    protected Paint mPaint = new Paint();
    private int mRealWidth;

    public CustomHorizontalProgressBar(Context context) {
        this(context, null);
    }

    public CustomHorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);

    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CustomHorizontalProgressBar);
        mTextSize = (int) ta.getDimension(R.styleable.CustomHorizontalProgressBar_progress_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.CustomHorizontalProgressBar_progress_text_color, mTextColor);
        mTextOffset = (int) ta.getDimension(R.styleable.CustomHorizontalProgressBar_progress_text_offset, mTextOffset);
        mUnReachColor = ta.getColor(R.styleable.CustomHorizontalProgressBar_progress_unreach_color, mUnReachColor);
        mUnReachHeight = (int) ta.getDimension(R.styleable.CustomHorizontalProgressBar_progress_unreach_height, mUnReachHeight);
        mReachColor = ta.getColor(R.styleable.CustomHorizontalProgressBar_progress_reach_color, mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.CustomHorizontalProgressBar_progress_reach_height, mReachHeight);

        ta.recycle();
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthSize, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            result = heightSize;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight)) + getPaddingTop() + getPaddingBottom();

            if (heightMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, heightSize);
            }
        }

        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();

        //translate移动坐标位置
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        boolean noNeedUnReach = false;

        //draw reach bar
        String textProgress = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(textProgress);

        float ratio = getProgress() * 1.0f / getMax();
        float progressX = ratio * mRealWidth;
        if(progressX+textWidth>mRealWidth){
            progressX=mRealWidth-textWidth;
            noNeedUnReach=true;
        }

        float endX = progressX - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        //draw text
        mPaint.setColor(mTextColor);
        int y =(int) (-(mPaint.descent()+mPaint.ascent())/2);
        canvas.drawText(textProgress,progressX,y,mPaint);

        //draw unreach barpostInvalidate
        if(!noNeedUnReach){
            float startX = progressX+textWidth+mTextOffset/2;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(startX,0,mRealWidth,0,mPaint);
        }

        canvas.restore();

    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

}
