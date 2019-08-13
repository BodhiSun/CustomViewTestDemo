package com.bodhi.customview_linearlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/7/2 11:16
 * desc :
 */
public class MyView extends View {
    private int defalutSize;

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //第二个参数就是我们在styles.xml文件中的<declare-styleable>标签
        //即属性集合的标签，在R文件中名称为R.styleable+name
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyView);

        //第一个参数为属性集合里面的属性，R文件名称：R.styleable+属性集合名称+下划线+属性名称
        //第二个参数为，如果没有设置这个属性，则设置的默认的值
        defalutSize=ta.getDimensionPixelSize(R.styleable.MyView_default_size,100);

        //最后记得将TypedArray对象回收
        ta.recycle();
    }

    private int getMeasureSize(int defaultSize, int measureSpec){
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {

            case MeasureSpec.UNSPECIFIED://如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;

            case MeasureSpec.AT_MOST://如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize=size;
                break;

            case MeasureSpec.EXACTLY://如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
        }

        return mySize;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width  = getMeasureSize(defalutSize, widthMeasureSpec);
        int height   = getMeasureSize(defalutSize, heightMeasureSpec);

        if (width < height) {
            height = width;
        } else {
            width = height;
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //调用父View的onDraw函数，因为View这个类帮我们实现了一些
        // 基本的而绘制功能，比如绘制背景颜色、背景图片等
        super.onDraw(canvas);

        //也可以是getMeasuredHeight()/2,本例中我们已经将宽高设置相等了
        int r = getMeasuredWidth() / 2;

        //圆心的横坐标为当前的View的左边起始位置+半径
        int centerX = r;

        //圆心的纵坐标为当前的View的顶部起始位置+半径
        int centerY = r;

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);

        //开始绘制
        canvas.drawCircle(centerX,centerY,r,paint);
    }
}
