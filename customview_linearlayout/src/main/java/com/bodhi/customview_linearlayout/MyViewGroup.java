package com.bodhi.customview_linearlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/7/2 12:05
 * desc :
 */
public class MyViewGroup extends ViewGroup {
    public MyViewGroup(Context context) {
        this(context,null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //将所有的子View进行测量，这会触发每个子View的onMeasure函数
        //注意要与measureChild区分，measureChild是对单个view进行测量
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        if (childCount == 0) {//如果没有子View,当前ViewGroup没有存在的意义，不用占用空间
            setMeasuredDimension(0, 0);
        } else {
            //如果宽高都是包裹内容
            if(widthMode==MeasureSpec.AT_MOST&&heightMode==MeasureSpec.AT_MOST){
                //我们将高度设置为所有子View的高度相加，宽度设为子View中最大的宽度
                int height = getTotalHeight();
                int width = getMaxChildWidth();
                setMeasuredDimension(width,height);

            }else if(heightMode==MeasureSpec.AT_MOST){//如果只有高度是包裹内容
                //宽度设置为ViewGroup自己的测量宽度，高度设置为所有子View的高度总和
                setMeasuredDimension(widthSize,getTotalHeight());

            }else if(widthMode==MeasureSpec.AT_MOST){//如果只有宽度是包裹内容
                //宽度设置为子View中宽度最大的值，高度设置为ViewGroup自己的测量值
                setMeasuredDimension(getMaxChildWidth(),heightSize);

            }
        }
    }

    /***
     * 获取子View中宽度最大的值
     */
    private int getMaxChildWidth(){
        int childCount = getChildCount();
        int maxWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View childView  = getChildAt(i);
            maxWidth=childView.getMeasuredWidth()>maxWidth?childView.getMeasuredWidth():maxWidth;
        }
        return maxWidth;
    }

    /***
     * 将所有子View的高度相加
     **/
    private int getTotalHeight(){
        int childCount = getChildCount();
        int totalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            totalHeight+=childView.getMeasuredHeight();
        }

        return totalHeight;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count  = getChildCount();
        //记录当前的高度位置
        int curHeight = 0;

        //将子View逐个摆放

        for (int i = 0; i < count; i++) {
            View child  = getChildAt(i);
            int height  = child.getMeasuredHeight();
            int width  = child.getMeasuredWidth();
            boolean isEven = i%2==0;

            //摆放子View，参数分别是子View矩形区域的左、上、右、下边
            child.layout(0,curHeight,width, isEven? (int) (curHeight + height * 1.5) :(curHeight+height));
            curHeight+=height;
        }


    }
}
