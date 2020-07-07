package com.bodhi.customview_luckywheel;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author : Sun
 * @version : 1.0
 * @time : 2020/3/13
 * desc :
 */
public class LuckyWheel extends RelativeLayout {
    private LuckyWheelView mLuckyWheelView;
    private Context mContext;
    private ImageView mIvStart;
    private OnLuckyWheelRotateListener rotateListener;
    private int startIcRes;
    //记录当前是否是第一次回调onMeasure
    private boolean isFirst = true;

    public void setOnLuckyWheelRotateListener(OnLuckyWheelRotateListener rotateListener){
        this.rotateListener=rotateListener;
        mLuckyWheelView.setOnRotateListener(rotateListener);
    }

    public LuckyWheel(Context context) {
        this(context,null);
    }

    public LuckyWheel(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LuckyWheel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext=context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LuckyWheelView);
        startIcRes=typedArray.getResourceId(R.styleable.LuckyWheelView_wheelStartIc,R.mipmap.ic_launcher);
        typedArray.recycle();

        //添加底图
        mLuckyWheelView = new LuckyWheelView(context,attrs);
        LayoutParams layoutParams = new  LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        mLuckyWheelView.setLayoutParams(layoutParams);
        addView(mLuckyWheelView);

        //添加开始抽奖按钮
        mIvStart = new ImageView(context);
        mIvStart.setImageResource(startIcRes);
        LayoutParams ivLp =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivLp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mIvStart.setLayoutParams(ivLp);
        addView(mIvStart);

        mIvStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStar();
            }
        });
    }

    /**
     * 开始旋转
     * @param position 旋转最终的位置 注意 从1开始逆时针递增
     */
    public void startRotate(int position){
        if (mLuckyWheelView!=null) {
            mLuckyWheelView.startRotate(position);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0,widthMeasureSpec),getDefaultSize(0,heightMeasureSpec));
        final int childWidthSize = getMeasuredWidth();

        //高度和宽度设置为一样的大小和模式
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,MeasureSpec.EXACTLY);

        //onMeasure调用获取到当前视图大小，手动按照一定的比例计算出中间开始按钮的合适大小，避免开始按钮和底图大小不适
        if (isFirst) {
            isFirst=false;
            int newW = ( int ) ((( float ) childWidthSize) * 0.35);
            int newH = ( int ) ((( float ) childWidthSize) * 0.35);
            ViewGroup.LayoutParams layoutParams = mIvStart.getLayoutParams();
            layoutParams.width = newW;
            layoutParams.height = newH;
            mIvStart.setLayoutParams(layoutParams);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void clickStar(){
        if ( rotateListener != null )
            rotateListener.rotateBefore();
    }


    public interface OnLuckyWheelRotateListener{
        void rotateBefore();
        void rotating(ValueAnimator animation);
        void rotateAfter(int position, String des);
    }
}
