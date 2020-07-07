package com.bodhi.customview_luckywheel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Sun
 * @version : 1.0
 * @time : 2020/3/12
 * desc : 幸运大转盘自定义View
 */
public class LuckyWheelView extends View {
    private Context mContext;
    private int sectorNum;//扇形数量 默认8个
    private int rotateTimes;//旋转圈数 默认三圈
    private int perSectorRotateTime;//每个扇形旋转时间
    private float perSectorAngle;//每个扇形的角度
    private int edgeDecorateRes;//转盘边缘装饰图片
    private Bitmap edgeDecorateBitmap;
    private int wheelBigIcRes;//只用一张切好的转盘图时的大图id
    private Bitmap wheelBigIcBitmap;
    private float mTextSize;
    private String[] sectorNames=new String[]{"1","2","3","4","5","6","7","8"};
    private Integer[] sectorIcons=new Integer[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    int defaultBg=Color.parseColor("#000000");
    int defaultBg2=Color.parseColor("#FF0000");
    private Integer[] sectorBgColors=new Integer[]{defaultBg,defaultBg,defaultBg,defaultBg,defaultBg,defaultBg,defaultBg,defaultBg};
    private Integer[] sectorTextColors=new Integer[]{defaultBg2,defaultBg2,defaultBg2,defaultBg2,defaultBg2,defaultBg2,defaultBg2,defaultBg2};
    private List<Bitmap> sectorIconsBitmap ;

    private Paint mTextPaint;
    private Paint mPaint;
    //视图的大小
    private int mWidth;
    //中心点横坐标
    private int mCenter;
    //扇形的半径
    private int mRadius;
    //扇形的半径外边界 减掉70防止溢出
    private int mRadiusEdge=50;
    //当前的角度
    private float currAngle = 0;
    //上次的停留的位置
    private int lastPosition;


    public LuckyWheelView(Context context) {
        this(context,null);
    }

    public LuckyWheelView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LuckyWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
        initPaint();
    }

    private void init(Context context, AttributeSet attrs) {
        mContext=context;
//        setBackgroundColor(Color.TRANSPARENT);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LuckyWheelView);
        sectorNum=typedArray.getInteger(R.styleable.LuckyWheelView_sectorNum,8);
        rotateTimes=typedArray.getInteger(R.styleable.LuckyWheelView_rotateTimes,3);
        perSectorRotateTime=typedArray.getInteger(R.styleable.LuckyWheelView_perSectorRotateTime,60);
        edgeDecorateRes=typedArray.getResourceId(R.styleable.LuckyWheelView_edgeDecorateRes,R.mipmap.ic_launcher);
        wheelBigIcRes=typedArray.getResourceId(R.styleable.LuckyWheelView_wheelBigIc,-1);
        int sectorNamesRes=typedArray.getResourceId(R.styleable.LuckyWheelView_sectorNames,-1);//扇形上面的文字描述
        int sectorIconsRes=typedArray.getResourceId(R.styleable.LuckyWheelView_sectorIcons,-1);//扇形上面的小图片
        int sectorBgColorRes=typedArray.getResourceId(R.styleable.LuckyWheelView_sectorBgColors,-1);//扇形背景颜色
        int sectorTextColorRes=typedArray.getResourceId(R.styleable.LuckyWheelView_sectorTextColors,-1);//扇形文字颜色
        typedArray.recycle();

        perSectorAngle=(float)(360.0/sectorNum);
        edgeDecorateBitmap = BitmapFactory.decodeResource(mContext.getResources(),edgeDecorateRes);
        if(wheelBigIcRes!=-1){
            wheelBigIcBitmap = BitmapFactory.decodeResource(mContext.getResources(),wheelBigIcRes);
        }
        if (sectorNamesRes != -1){
            sectorNames = mContext.getResources().getStringArray(sectorNamesRes);
        }
        if (sectorIconsRes != -1) {
            //根据xml文件中的图片名称获取对应的图片id
            String[] sectorIconsStr = mContext.getResources().getStringArray(sectorIconsRes);
            List<Integer> sectorIconList = new ArrayList<>();
            for(int i=0;i<sectorIconsStr.length;i++){
                sectorIconList.add(mContext.getResources().getIdentifier(sectorIconsStr[i],"mipmap",mContext.getPackageName()));
            }
            sectorIcons = sectorIconList.toArray(new Integer[sectorIconList.size()]);
        }
        if(sectorBgColorRes!=-1){
            String[] sectorBgColorStr = context.getResources().getStringArray(sectorBgColorRes);
            for(int i=0;i<sectorBgColorStr.length;i++){
                sectorBgColors[i]=Color.parseColor(sectorBgColorStr[i]);
            }
        }
        if(sectorTextColorRes!=-1){
            String[] sectorTextColorStr = context.getResources().getStringArray(sectorTextColorRes);
            for(int i=0;i<sectorTextColorStr.length;i++){
                sectorTextColors[i]=Color.parseColor(sectorTextColorStr[i]);
            }
        }
        sectorIconsBitmap = new ArrayList<>();
        for(int i=0;i<sectorIcons.length;i++){
            Bitmap bitmap =  BitmapFactory.decodeResource(mContext.getResources(),sectorIcons[i]);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(1f,1f);
            matrix.postRotate(perSectorAngle*i);
            Bitmap bmp =Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
            sectorIconsBitmap.add(bmp);
        }
    }

    private void initPaint() {
        mTextSize=14*getScale();

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true); //设置边界模糊
        mTextPaint.setTextSize(mTextSize);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //WRAP_CONTENT的时候默认800
        int desiredWidth =800;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;
        if(widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else if(widthMode==MeasureSpec.AT_MOST){
            width = Math.min(desiredWidth,widthSize);
        }else{
            width=desiredWidth;
        }

        mWidth =width;
        mCenter=mWidth/2;
        mRadius=mWidth/2-mRadiusEdge;

        setMeasuredDimension(width,width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画背景装饰图片
        if(wheelBigIcBitmap==null) {
            Rect decorateBgRect = new Rect(0, 0, mWidth, mWidth);
            canvas.drawBitmap(edgeDecorateBitmap, null, decorateBgRect, mPaint);
        }

        // 计算初始角度 正上方偏左一半扇形角度
        float startAngle = -90-perSectorAngle/2;
        int width = getWidth()-getPaddingLeft()-getPaddingRight();
        int height = getHeight()-getPaddingTop()-getPaddingBottom();

        for(int i=0;i<sectorNum;i++){
            mPaint.setColor(sectorBgColors[i]);
            //扇形的区域范围
            RectF rectF = new RectF(mCenter - mRadius, mCenter - mRadius, mCenter + mRadius, mCenter + mRadius);
            canvas.drawArc(rectF,startAngle,perSectorAngle,true,mPaint);
            mTextPaint.setColor(sectorTextColors[i]);
            drawSectorText(startAngle,sectorNames[i],mTextPaint,canvas);

            int imgWidth =mRadius/3;

            int w = ( int ) (Math.abs(Math.cos(Math.toRadians(Math.abs(180 - perSectorAngle * i)))) *
                    imgWidth + imgWidth * Math.abs(Math.sin(Math.toRadians(Math.abs(180 - perSectorAngle * i)))));
            int h = ( int ) (Math.abs(Math.sin(Math.toRadians(Math.abs(180 - perSectorAngle * i)))) *
                    imgWidth + imgWidth * Math.abs(Math.cos(Math.toRadians(Math.abs(180 - perSectorAngle * i)))));

            float angle = ( float ) Math.toRadians(startAngle + perSectorAngle / 2);

            //确定图片在圆弧中 中心点的位置
            float x = ( float ) (width / 2 + (mRadius / 2 + mRadius / 12) * Math.cos(angle));
            float y = ( float ) (height / 2 + (mRadius / 2 + mRadius / 12) * Math.sin(angle));
            // 确定绘制图片的矩形区域
            RectF rectFIcon = new RectF(x - w / 2, y - h / 2, x + w / 2, y + h / 2);
            canvas.drawBitmap(sectorIconsBitmap.get(i),null,rectFIcon,null);

            //旋转开始角度
            startAngle+=perSectorAngle;
        }

        if(wheelBigIcBitmap!=null){
            //画大图
            Rect wheelBigRect =new Rect(0,0,mWidth,mWidth);
            canvas.drawBitmap(wheelBigIcBitmap,null,wheelBigRect,mPaint);
        }

    }

    //绘制文字
    private void drawSectorText(float startAngle, String sectorName, Paint mTextPaint, Canvas canvas) {
        Path path = new Path();
        RectF rectF = new RectF(mCenter - mRadius, mCenter - mRadius, mCenter + mRadius, mCenter + mRadius);
        path.addArc(rectF, startAngle, perSectorAngle);

        float textWidth = mTextPaint.measureText(sectorName);
        //在圆弧沿线上的偏移
        float hOffset = (float)(Math.sin(perSectorAngle/2/180*Math.PI)*mRadius)-textWidth/2;

        canvas.drawTextOnPath(sectorName,path,hOffset,mRadius/6,mTextPaint);
    }


    private float getScale(){
        TextView textView =new TextView(mContext);
        textView.setTextSize(1);
        return textView.getTextSize();
    }

    private LuckyWheel.OnLuckyWheelRotateListener rotateListener;
    public void setOnRotateListener(LuckyWheel.OnLuckyWheelRotateListener rotateListener) {
        this.rotateListener=rotateListener;
    }

    /**
     * 开始转动
     * pos 位置1开始按照逆时针递增的 当前指的是第一个他左边的是第二个
     */
    public void startRotate(final int position) {
        //最低圈数是rotateTimes圈
        int newAngle = ( int ) (360 * rotateTimes + (position - 1) * perSectorAngle + currAngle - (lastPosition == 0 ? 0 : ((lastPosition - 1) * perSectorAngle)));
        //计算目前的角度划过的扇形份数
        int num = ( int ) ((newAngle - currAngle) / perSectorAngle);
        ObjectAnimator anim = ObjectAnimator.ofFloat(LuckyWheelView.this, "rotation", currAngle, newAngle);
        currAngle = newAngle;
        lastPosition = position;
        // 动画的持续时间，执行多久
        anim.setDuration(num * perSectorRotateTime);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //将动画的过程态回调给调用者
                if ( rotateListener != null )
                    rotateListener.rotating(animation);
            }
        });
        final float[] f = {0};
        anim.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float t) {
                float f1 = ( float ) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
//                Log.e("lucky", "" + t + "     " + (f[0] - f1));
                f[0] = ( float ) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
                return f[0];
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //当旋转结束的时候回调给调用者当前所选择的内容
                if ( rotateListener != null ) {
                    String des = sectorNames[(sectorNum - position + 1) % sectorNum].trim();
                    rotateListener.rotateAfter(position, des);
                }
            }
        });
        // 正式开始启动执行动画
        anim.start();
    }
}
