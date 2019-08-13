package com.bodhi.cv_linearlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/5 10:55
 * desc : 设计一个container，实现内部控件自动换行。即里面的控件能够根据长度来判断当前行是否容得下它，
 * 进而决定是否转到下一行显示
 *
 *绘制流程分为三步：测量、布局、绘制 :
 * onMeasure()：测量自己的大小，为正式布局提供建议。（注意，只是建议，至于用不用，要看onLayout）;
 * onLayout():使用layout()函数对所有子控件布局；
 * onDraw():根据布局的位置绘图；
 *
 */
public class CV_LinearLayout extends ViewGroup {

    public CV_LinearLayout(Context context) {
        super(context);
    }

    public CV_LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CV_LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * onMeasure()的作用就是根据container内部的子控件计算自己的宽和高，最后通过
     * setMeasuredDimension（int width,int height)设置进去；
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     *
     * 两个参数他们是父类传递过来给当前view的一个建议值,即想把当前view的尺寸设置为宽widthMeasureSpec,
     * 高heightMeasureSpec
     * 两个参数他们是由mode+size两部分组成都是32位的。前两位代表mode(测量模式)，后面30位是实际数值（size）。
     * 三种模式:
     * UNSPECIFIED(未指定):父元素不对子元素施加任何束缚，子元素可以得到任意想要的大小；UNSPECIFIED=00
     * EXACTLY(精确):父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；EXACTLY=01
     * AT_MOST(至多):子元素至多达到指定大小的值;AT_MOST =10
     *
     * XML布局和模式有如下对应关系：
     * wrap_content-> MeasureSpec.AT_MOST
     * match_parent -> MeasureSpec.EXACTLY
     * 具体值 -> MeasureSpec.EXACTLY
     * 当模式是MeasureSpec.EXACTLY时，我们就没必要设定我们计算的大小了，因为这个大小是用户指定的，我们不应更改。
     * 当模式是MeasureSpec.AT_MOST时，也就是说用户将布局设置成了wrap_content，我们就需要将大小设定为我们计算的数值，
     *
     * 总体来讲，onMeasure()中计算出的width和height，就是当XML布局设置为layout_width="wrap_content"、
     * layout_height="wrap_content"时所占的宽和高；即整个container所占的最小矩形
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //提取对应模式和数值
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);

        int height=0;
        int width=0;
        int count=getChildCount();
        for (int i = 0; i < count; i++) {
            //测量子控件
            View child  = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);

            //获得子控件的高度和宽度
            int childHeight=child.getMeasuredHeight();
            int childWidth=child.getMeasuredWidth();

            //获取子控件margin属性信息 重新计算子控件宽高
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            childHeight=childHeight+lp.topMargin+lp.bottomMargin;
            childWidth=childWidth+lp.leftMargin+lp.rightMargin;

            //因为我们是垂直排列其内部所有的VIEW，所以container所占宽度应该是所有子控件中最大子控件宽度，
            //所占高度应该是所有子控件累加总的高度
            height+=childHeight;
            width=Math.max(childWidth,width);

        }

        //测量完成以后通过setMeasuredDimension(int,int)设置给系统。
        setMeasuredDimension(modeW==MeasureSpec.EXACTLY?sizeW:width,modeH==MeasureSpec.EXACTLY?sizeH:height);
    }

    /**
     *在这部分，就是根据自己的意愿把内部的各个控件排列起来,我们要完成的是将所有的控件垂直排列
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     *
     * getMeasuredWidth()与getWidth()区别主要体现在下面几点：
     *1.首先getMeasureWidth()方法在measure()过程结束后就可以获取到，getWidth()方法要在layout()过程结束后才能获取到。
     *2.getMeasureWidth()方法中的值是通过setMeasuredDimension()方法来进行设置的，而getWidth()方法中的值
     * 则是通过layout(left,top,right,bottom)方法设置的,getWidth()的取值就是这里的右坐标减去左坐标的宽度。
     *
     * 所以说setMeasuredDimension()提供的测量结果只是为布局提供建议，最终的取用与否要看layout()函数。
     *
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top=0;
        int count =getChildCount();

        for (int i = 0; i < count; i++) {
            View child  = getChildAt(i);

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            //如在onMeasure中考虑了margin属性信息，同样我们在布局时仍然将间距加到控件里就好了
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();

            child.layout(lp.leftMargin,top+lp.topMargin,childWidth+lp.leftMargin,top+childHeight+lp.topMargin);
            top+=childHeight+lp.topMargin+lp.bottomMargin;
        }
    }

    //为了支持xml中子控件的margin属性起作用 重写下面三个方法
    //因为默认的generateLayoutParams（）函数只会提取layout_width、layout_height的值
    //只有MarginLayoutParams（）才具有提取margin间距的功能
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }
}
