package com.bodhi.cv_paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/5 20:15
 * desc :
 */
public class CV_Paint extends View {

    private Paint paint;
    private Paint paint2;
    private Paint paint3;



    public CV_Paint(Context context) {
        this(context,null);
    }

    public CV_Paint(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CV_Paint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
    }

    private void initPaint() {
        //-------------------------画笔三种初始化方法---------------------------
        paint = new Paint();
        //开启抗锯齿功能
        paint.setAntiAlias(true);
        //在绘制时启用抖动的标志
        paint.setDither(true);

        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);

        //当前画笔的所有设置都会被覆盖掉，而替换为所选画笔的设置。
        paint3 = new Paint(paint2);
        //setFlags 方法会覆盖之前设置
        paint3.setFlags(Paint.FILTER_BITMAP_FLAG);

        Log.i("cvPaint", "paint isAntiAlias = " + paint.isAntiAlias());
        Log.i("cvPaint", "paint isDither  = " + paint.isDither());
        Log.i("cvPaint", "paint2 isAntiAlias = " + paint2.isAntiAlias());
        Log.i("cvPaint", "paint2 isDither  = " + paint2.isDither());
        Log.i("cvPaint", "paint3 isAntiAlias = " + paint3.isAntiAlias());
        Log.i("cvPaint", "paint3 isDither  = " + paint3.isDither());
        Log.i("cvPaint", "paint3 isFilterBitmap  = " + paint3.isFilterBitmap());

        //该方法会让画笔的所有设置都还原为初始状态 和刚创建时默认的状态是一样的
        paint2.reset();
        Log.i("cvPaint", "paint2 isAntiAlias = " + paint2.isAntiAlias());
        Log.i("cvPaint", "paint2 isDither  = " + paint2.isDither());



        //-------------------------画笔颜色设置---------------------------
        //alpha范围0~255 对应16进制0x00~OxFF 下面设置是等价的
        paint.setAlpha(204);
        paint2.setAlpha(0xCC);

        //4个参数的取值范围也是 0~255，对应16进制 0x00~0xFF
        paint.setARGB(204,255,255,0);
        paint2.setARGB(0xEE,0xFF,0x00,0x00);

        //最常使用setColor方法 setColor设置的颜色必须是ARGB同时存在的 否则默认Alpha通道为0，即完全透明
        paint.setColor(Color.GREEN);
        paint3.setColor(0xFFE2A588);



        //-------------------------画笔宽度---------------------------
        // 将画笔设置为描边
        paint.setStyle(Paint.Style.STROKE);
        // 设置线条宽度 STROKE模式下这个线条宽度是在线的两边向内向外各扩展50
        paint.setStrokeWidth(100);

        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(1);

        paint3.setStyle(Paint.Style.STROKE);
        //当画笔宽度设置为0px的时候 称为hairline mode(发际线模式)不受画布缩放的影响始终为1px
        paint3.setStrokeWidth(0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //-------------------------矩形向内缩小---------------------------
        canvas.drawCircle(300,300,150,paint);

        Rect rect = new Rect(0, 0, 600, 600);
        // 注意这里，考虑到画笔线条的宽度 可以将矩形向内缩小适当宽度
        rect.inset(50,50);
        canvas.drawRect(rect,paint);

    //---------------------------hairline mode发际线模式 画布缩放 --------------------------------------
        canvas.drawCircle(300,800,100,paint2);
        canvas.drawCircle(600,800,100,paint3);
        //canvas缩放2倍 paint2画笔的宽度也会缩放2倍即1px->2px，而hairline mode的paint3始终为1px
        canvas.scale(2,2,450,1000);
        canvas.drawCircle(300,1000,100,paint2);
        canvas.drawCircle(600,1000,100,paint3);

    //--------------------------- 画笔模式三种模式------------------------------------------
        Paint paint4 = new Paint(paint3);
        canvas.scale(0.5f,0.5f,450,1000);
        paint4.setStrokeWidth(20);

        //描边，只绘制图形轮廓 实际大小=半径+边框/2
        paint4.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(900,120,100,paint4);

        //填充内容，也是画笔的默认模式 实际大小=半径
        paint4.setStyle(Paint.Style.FILL);
        canvas.drawCircle(900,370,100,paint4);

        //描边+填充，同时绘制轮廓和填充内容。 实际大小=半径+边框/2
        paint4.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(900,600,100,paint4);



        //---------------------------  画笔三种线帽 -----------------------------------------
        Paint paintCap = new Paint();
        paintCap.setStyle(Paint.Style.STROKE);
        paintCap.setAntiAlias(true);
        paintCap.setStrokeWidth(30);
        float pointX=100;
        float lineStartX=320;
        float lineStopX=700;
        float y;

        //默认是BUTT 无线帽
        y=1300;
        paintCap.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawPoint(pointX,y,paintCap);
        canvas.drawLine(lineStartX,y,lineStopX,y,paintCap);

        //方形线帽 SQUARE 线帽是在线段外的
        y=1400;
        paintCap.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawPoint(pointX,y,paintCap);
        canvas.drawLine(lineStartX,y,lineStopX,y,paintCap);

        //圆形线帽 ROUND 线帽是在线段外的
        y=1500;
        paintCap.setStrokeCap(Paint.Cap.ROUND);
        //Round的状态下 绘制的点是圆的
        canvas.drawPoint(pointX,y,paintCap);
        canvas.drawLine(lineStartX,y,lineStopX,y,paintCap);

        //------------------------   线段连接方式三种拐角类型 -----------------------------------
        Paint paintJoin=new Paint(paintCap);
        paintJoin.setStrokeCap(Paint.Cap.BUTT);

        //尖角 MITER(默认模式) 即在拐角处延长外边缘，直到相交位置，但是如果夹角足够小 接近于0时 那么交点
        //位置就会延长在无限远位置 为了避免这种情况 如果连接模式为MITER时 当角度小于一定程度自动变为BEVEL
        //这个角度大约是28.96° miter = 1/ sin(angle/2) ,angle是两条线的形成的夹角 miter是对长度的限制
        paintJoin.setStrokeJoin(Paint.Join.MITER);
        // 设置 Miter Limit，参数并不是角度是长度 根据公式角度为11.48
        paintJoin.setStrokeMiter(10);

        Path path =new Path();
        path.moveTo(100,1600);
        path.lineTo(300,1600);
        path.lineTo(100,1750);
        canvas.drawPath(path,paintJoin);

        //平角 BEVEL
        paintJoin.setStrokeJoin(Paint.Join.BEVEL);
        Path path2 =new Path();
        path2.moveTo(400,1600);
        path2.lineTo(600,1600);
        path2.lineTo(400,1700);
        canvas.drawPath(path2,paintJoin);

        //圆角 ROUND
        paintJoin.setStrokeJoin(Paint.Join.ROUND);
        Path path3 =new Path();
        path3.moveTo(700,1600);
        path3.lineTo(900,1600);
        path3.lineTo(700,1700);
        canvas.drawPath(path3,paintJoin);

        //------------------------   PathEffect 6种效果 -----------------------------------
        //PathEffect 在绘制之前修改几何路径 可以实现划线，自定义填充效果和自定义笔触效果
        //Canvas.drawLine() 和 Canvas.drawLines() 方法画直线时，setPathEffect()是不支持硬件加速的；
        //PathDashPathEffect 对硬件加速的支持也有问题，当使用PathDashPathEffect的时候，最好也把硬件加速关了。

        //CornerPathEffect   圆角效果，将尖角替换为圆角。
        //DashPathEffect     虚线效果，用于各种虚线效果。
        //PathDashPathEffect Path虚线效果，虚线中的间隔使用 Path 代替。
        //DiscretePathEffect 让路径分段随机偏移。
        //SumPathEffect      两个 PathEffect 效果组合，同时绘制两种效果。
        //ComposePathEffect  两个 PathEffect 效果叠加，先使用效果1，之后使用效果2。


        //(1)CornerPathEffect 圆角 可以将线段之间的任何锐角替换为指定半径的圆角(适用于 STROKE 或 FILL 样式)
        Paint paintEffect=new Paint(paintCap);
        paintEffect.setStyle(Paint.Style.STROKE);
        paintEffect.setStrokeWidth(5);
        paintEffect.setStrokeCap(Paint.Cap.BUTT);

        Path pathCorner =new Path();
        pathCorner.moveTo(100,1800);
        pathCorner.lineTo(200,1900);
        pathCorner.lineTo(300,1800);
        pathCorner.lineTo(400,1900);
        canvas.drawPath(pathCorner,paintEffect);

        //save 和 restore 必须是成对出现的
        //save保存当前矩阵并剪辑到私有堆栈上。
        canvas.save();
        //用指定的平移 预设置当前矩阵(路径)
        canvas.translate(400,0);
        paintEffect.setColor(0x99FF0000);
        paintEffect.setPathEffect(new CornerPathEffect(100));
        canvas.drawPath(pathCorner,paintEffect);
        //删除所有自上次保存调用以来对矩阵/剪辑状态的修改 平衡之前对save()的调用
        canvas.restore();

        paintEffect.setColor(0x99FFFF00);
        canvas.drawPath(pathCorner,paintEffect);



        //CornerPathEffect可以实现圆角矩形效果 但是不论圆角有多大，它也不会变成圆形或者椭圆。
        canvas.translate(100,2000);
        paintEffect.setPathEffect(new CornerPathEffect(200));
        paintEffect.setColor(0x99FF33F1);
        canvas.drawRect(new RectF(0,0,200,200),paintEffect);

        canvas.save();
        canvas.translate(300,0);
        paintEffect.setPathEffect(null);
        //直接绘制圆角矩形的 如果圆角足够大时，那么绘制出来就会是圆或者椭圆
        canvas.drawRoundRect(new RectF(0,0,200,200),200,200,paintEffect);
        canvas.restore();



         //CornerPathEffect 也可以让手绘效果更加圆润。Main2Activity->CV_Draw


        //(2)DashPathEffect 用于实现虚线效果(适用于 STROKE 或 FILL_AND_STROKE 样式)。
        Paint paintDash=new Paint(paintCap);
        paintDash.setStyle(Paint.Style.STROKE);
        paintDash.setStrokeWidth(20);
        paintDash.setStrokeCap(Paint.Cap.BUTT);
        paintDash.setColor(Color.RED);

        Path path_dash=new Path();
        path_dash.lineTo(1500,0);

        canvas.save();
        canvas.translate(0,300);
        //intervals：必须为偶数个，用于控制显示和隐藏的长度。phase：相位，可以简单的理解为起始偏移量
        paintDash.setPathEffect(new DashPathEffect(new float[]{100,50},0));
        canvas.drawPath(path_dash,paintDash);
        canvas.restore();

        canvas.save();
        canvas.translate(0,400);
        paintDash.setPathEffect(new DashPathEffect(new float[]{100,50},50));
        canvas.drawPath(path_dash,paintDash);
        canvas.restore();



     //(3)PathDashPathEffect 实现虚线效果 虚线中显示的部分可以指定为一个Path(适用于STROKE或FILL_AND_STROKE样式)。有问题
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE );
        paint.setAntiAlias(true);

        RectF rectF = new RectF(0, 0, 50, 50);

        // 方形
        Path rectPath = new Path();
        rectPath.addRect(rectF, Path.Direction.CW);
        rectPath.lineTo(800,0);

        //圆形 椭圆
        Path ovalPath= new Path();
        ovalPath.addOval(rectF,Path.Direction.CW);
        ovalPath.lineTo(1000,0);

        //子弹形状
        Path bulletPath = new Path();
        bulletPath.lineTo(rectF.centerX(),rectF.top);
        bulletPath.addArc(rectF,-90,180);
        bulletPath.lineTo(rectF.left,rectF.bottom);
        bulletPath.lineTo(rectF.left,rectF.top);

        // 绘制分割线 - 方形虚线
        canvas.save();
        canvas.translate(0,500);
        //shape: Path图形  advance: 图形占据长度 phase: 相位差 style: 转角样式 TRANSLATE转角处对图形平移 ROTATE对图形旋转 MORPH对图形变形。
        paint.setPathEffect(new PathDashPathEffect(rectPath, rectF.width()*1.5f, 0, PathDashPathEffect.Style.MORPH));
        canvas.drawPath(rectPath,paint);
        canvas.restore();

        // 绘制分割线 - 圆形虚线
        canvas.save();
        canvas.translate(0,600);
        paint.setPathEffect(new PathDashPathEffect(ovalPath, rectF.width()*1.5f, 0, PathDashPathEffect.Style.TRANSLATE));
        canvas.drawPath(ovalPath,paint);
        canvas.restore();

        // 绘制分割线 - 子弹型
        canvas.translate(0, 700);
        paint.setPathEffect(new PathDashPathEffect(bulletPath, rectF.width() * 1.5f, 0, PathDashPathEffect.Style.TRANSLATE));
        canvas.drawLine(0, 0, 1200, 0, paint);



        //(4)DiscretePathEffect 可以让 Path 产生随机偏移效果。
        Paint paintDiscrete = new Paint();
        paintDiscrete.setStyle(Paint.Style.STROKE );
        paintDiscrete.setAntiAlias(true);
        paintDiscrete.setStrokeWidth(5);
        paintDiscrete.setColor(Color.GRAY);
        //segmentLength: 分段长度  deviation: 偏移距离
        paintDiscrete.setPathEffect(new DiscretePathEffect(100,10));

        Path pathDiscrete=new Path();
        pathDiscrete.lineTo(1000,0);

        canvas.save();
        canvas.translate(0,100);
        canvas.drawPath(pathDiscrete,paintDiscrete);
        canvas.restore();



        //(5)SumPathEffect 用于合并两种效果，它相当于两种效果都绘制一遍。
        Paint paintSum = new Paint(paintDiscrete);

        PathEffect cornerPathEffect = new CornerPathEffect(100);
        PathEffect dashPathEffect =new DashPathEffect(new float[]{40,20},0);
        PathEffect sumPathEffect = new SumPathEffect(cornerPathEffect, dashPathEffect);

        RectF rectSum = new RectF(0, 0, 250, 250);

        canvas.save();
        canvas.translate(0,200);
        paintSum.setPathEffect(sumPathEffect);
        canvas.drawRect(rectSum,paintSum);
        canvas.restore();



        //(6)ComposePathEffect 也是合并两种效果，只不过先应用一种效果后再次叠加另一种效果 交换参数得到的效果是不同的。
        //首先应用 innerpe 再应用 outerpe
        PathEffect composePathEffect = new ComposePathEffect( dashPathEffect,cornerPathEffect);
        PathEffect composePathEffect2 = new ComposePathEffect( cornerPathEffect,dashPathEffect);

        canvas.save();
        canvas.translate(0,500);
        paintSum.setPathEffect(composePathEffect);
        canvas.drawRect(rectSum,paintSum);
        canvas.restore();

        canvas.save();
        canvas.translate(400,500);
        paintSum.setPathEffect(composePathEffect2);
        canvas.drawRect(rectSum,paintSum);
        canvas.restore();



        //---------------   getFillPath 根据原始Path(src)获取预处理后的Path(dst) -----------------
        Paint paintPath= new Paint(paintSum);
        paintPath.setPathEffect(null);
        Path arcPath = new Path();
        arcPath.addArc(new RectF(50,50,300,300),30,300);

        //原始图形
        canvas.save();
        canvas.translate(0,800);
        canvas.drawPath(arcPath,paintPath);
        canvas.restore();

        //设置了StrokeWidth和Cap
        canvas.save();
        canvas.translate(300,800);
        paintPath.setStyle(Paint.Style.STROKE);
        paintPath.setStrokeCap(Paint.Cap.ROUND);
        paintPath.setStrokeWidth(50);
        canvas.drawPath(arcPath,paintPath);
        canvas.restore();

        //getFillPath之后
        canvas.save();
        canvas.translate(600,800);
        Path borderPath = new Path();
        paintPath.getFillPath(arcPath,borderPath);

        Paint paintTest = new Paint();
        paintTest.setStyle(Paint.Style.STROKE);
        paintTest.setStrokeWidth(2);
        paintTest.setAntiAlias(true);
        canvas.drawPath(borderPath,paintTest);
        canvas.restore();


    }

}
