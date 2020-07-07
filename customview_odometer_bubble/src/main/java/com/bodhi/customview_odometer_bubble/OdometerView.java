package com.bodhi.customview_odometer_bubble;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author : Sun
 * @version : 1.0
 * @time : 2019/12/23
 * desc :里程表自定义View
 */
public class OdometerView extends View {
    private int colorDialUnReach;
    private int colorDialReach;
    private int colorProgressPoint;
    private int dialTextSize;
    private int strokeWidthDial;
    private String tipDial;
    private int curStepTextSize;
    private int tipTextSize;
    private int animPlayTime;

    private int radiusDial;
    private int mRealRadius;
    private int currentValue;

    private Paint arcPaint;
    private Paint pointerPaint;
    private Paint.FontMetrics fontMetrics;
    private Paint stepTipPaint;
    private RectF mRect;

    public OdometerView(Context context) {
        this(context, null);
    }

    public OdometerView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OdometerView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        colorDialReach = Color.parseColor("#FFFFFF");
        colorDialUnReach = Color.parseColor("#55FFFFFF");
        colorProgressPoint = Color.parseColor("#29B6FE");
        dialTextSize = sp2px(11);
        strokeWidthDial = dp2px(8);
        tipDial = "继续加油奥~";
        curStepTextSize = sp2px(35);
        tipTextSize = sp2px(13);
        animPlayTime = 1000;
        radiusDial = dp2px(128);
    }

    private void initPaint() {
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(strokeWidthDial);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        pointerPaint = new Paint();
        pointerPaint.setAntiAlias(true);
        pointerPaint.setTextSize(dialTextSize);
        pointerPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = pointerPaint.getFontMetrics();

        stepTipPaint = new Paint();
        stepTipPaint.setAntiAlias(true);
        stepTipPaint.setTextAlign(Paint.Align.CENTER);
        stepTipPaint.setFakeBoldText(true);
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
            mWidth = getPaddingLeft() + radiusDial * 2 + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSize);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = getPaddingTop() + radiusDial * 2 + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, heightSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);

        radiusDial = Math.min((getMeasuredWidth() - getPaddingLeft() - getPaddingRight()), getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        mRealRadius = radiusDial - strokeWidthDial / 2;
        mRect = new RectF(-mRealRadius, -mRealRadius, mRealRadius, mRealRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆弧
        drawArc(canvas);

        //画刻度线
        drawPointerLine(canvas);

        //画刻度盘中心的图片、当前进度、提示文字
        drawTitleDial(canvas);

        //画当前进度
        drawCurrenProgress(canvas);

    }

    private void drawCurrenProgress(Canvas canvas) {
        //圆弧进度
        arcPaint.setColor(colorDialReach);
        canvas.drawArc(mRect, 150, (int) ((currentValue/100>60?60:currentValue/100) * 4), false, arcPaint);

        //刻度线进度
        canvas.save();
        canvas.rotate(150);
        //一共需要绘制61个表针
        pointerPaint.setColor(colorDialReach);
        for (int i = 0; i <= (currentValue/100>60?60:currentValue/100); i++) {
            if (i == 15 || i == 30 || i == 45 || i == 60) {//长表针
                pointerPaint.setStrokeWidth(6);
                canvas.drawLine(radiusDial, 0, radiusDial - strokeWidthDial - dp2px(10), 0, pointerPaint);

                drawPointerText(canvas, i);
            } else {//短表针
                pointerPaint.setStrokeWidth(3);
                canvas.drawLine(radiusDial, 0, radiusDial - strokeWidthDial - dp2px(5), 0, pointerPaint);
            }
            canvas.rotate(4f);
        }

        //画进度上的原点
        canvas.drawCircle(radiusDial - strokeWidthDial / 2, -strokeWidthDial, dp2px(8), pointerPaint);
        pointerPaint.setColor(colorProgressPoint);
        canvas.drawCircle(radiusDial - strokeWidthDial / 2, -strokeWidthDial , dp2px(4), pointerPaint);
        canvas.restore();
    }

    private void drawArc(Canvas canvas) {
        canvas.translate(getPaddingLeft() + radiusDial, getPaddingTop() + radiusDial);
        arcPaint.setColor(colorDialUnReach);
        canvas.drawArc(mRect, 150, 240, false, arcPaint);
    }


    private void drawPointerLine(Canvas canvas) {
        canvas.save();
        canvas.rotate(150);
        //一共需要绘制61个表针
        for (int i = 0; i < 61; i++) {
            pointerPaint.setColor(colorDialUnReach);

            if (i == 15 || i == 30 || i == 45 || i == 60) {//长表针
                pointerPaint.setStrokeWidth(6);
                canvas.drawLine(radiusDial-strokeWidthDial, 0, radiusDial - strokeWidthDial - dp2px(10), 0, pointerPaint);

                drawPointerText(canvas, i);
            } else {//短表针
                pointerPaint.setStrokeWidth(3);
                canvas.drawLine(radiusDial-strokeWidthDial, 0, radiusDial - strokeWidthDial - dp2px(5), 0, pointerPaint);
            }
            canvas.rotate(4f);
        }
        canvas.restore();
    }

    private void drawPointerText(Canvas canvas, int i) {
        canvas.save();
        int currentCenterX = (int) (radiusDial - strokeWidthDial - dp2px(6) - pointerPaint.measureText(String.valueOf(i)));
        canvas.translate(currentCenterX, 0);
        //因为无论哪个刻度的数值正面看都是水平的
        canvas.rotate(360 - 150 - 4f * i);

        int textBaseLine = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        canvas.drawText(String.valueOf(i*100), 0, textBaseLine, pointerPaint);
        canvas.restore();
    }

    private void drawTitleDial(Canvas canvas) {
        //画当前步数文字
        stepTipPaint.setColor(colorDialReach);
        stepTipPaint.setTextSize(curStepTextSize);
        canvas.drawText(currentValue+"", 0, 0, stepTipPaint);
        stepTipPaint.setTextSize(tipTextSize);
        canvas.drawText("步", stepTipPaint.measureText(currentValue+"")+dp2px(15), 0, stepTipPaint);

//        //画顶部图片
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        RectF rectF = new RectF(-bitmap.getWidth() / 4,-bitmap.getHeight()*3 /4 - radiusDial /4,bitmap.getWidth() / 4,-bitmap.getHeight() / 4 - radiusDial / 4);
//        canvas.drawBitmap(bitmap, null,rectF, stepTipPaint);


        //画下边提示文字
        stepTipPaint.setTextSize(tipTextSize);
        stepTipPaint.setColor(colorDialUnReach);
        canvas.drawText(tipDial, 0, radiusDial * 1 / 4, stepTipPaint);

    }

    public void setFirstCurProgress(int progress) {
        if (progress < 0) {
            throw new RuntimeException("progress range is illegal");
        }
        if (progress == 0) return;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, progress);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                currentValue = (Math.round((float) animation.getAnimatedValue() * 100)) / 100;
                currentValue = (int)animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(animPlayTime);
        valueAnimator.start();
    }

    public void setCurProgress(int progress) {
        if (progress < 0) {
            throw new RuntimeException("progress range is illegal");
        }
        currentValue = progress;
        invalidate();
    }


    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

}
