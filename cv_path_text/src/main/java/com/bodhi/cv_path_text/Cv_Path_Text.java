package com.bodhi.cv_path_text;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/7 14:57
 * desc :
 */
public class Cv_Path_Text extends View {


    public Cv_Path_Text(Context context) {
        super(context);
    }

    public Cv_Path_Text(Context context,AttributeSet attrs) {
        super(context, attrs);
    }

    public Cv_Path_Text(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paintPath=new Paint();
        paintPath.setAntiAlias(true);
        paintPath.setColor(Color.RED);
        paintPath.setStyle(Paint.Style.STROKE);
        paintPath.setStrokeWidth(5);

        //直线路径
        Path path =new Path();
        path.moveTo(10,10);//设定起始点
        path.lineTo(10,100);//第一条直线终点，同时也是第二条直线的起点
        path.lineTo(300,100);//画第二条直线
        path.lineTo(300,70);//第三条直线
        path.close();//调用Close()会将路径首尾点连接起来，形成闭环
        canvas.drawPath(path,paintPath);

        //矩形路径
        Path pathRect = new Path();
        RectF rectf = new RectF(400,10,800,100);
        pathRect.addRect(rectf,Path.Direction.CW);//第二个参数是生成方向 cw顺时针 ccw逆时针
        canvas.drawPath(pathRect,paintPath);
        //顺时针或逆时针生成方向 主要是影响依据生成方向排版的文字
//        paintPath.setTextSize(40);
//        paintPath.setColor(Color.GRAY);
//        canvas.drawTextOnPath("12341234像首歌,绿色军营绿色军营教会我",pathRect,0,25,paintPath);

        canvas.translate(0,110);
        //圆角矩形路径
        Path pathRound = new Path();
        RectF rectF1 = new RectF(50,50,200,150);
        pathRound.addRoundRect(rectF1,10,15,Path.Direction.CW);//第一个构造函数构建统一圆角大小
        canvas.drawPath(pathRound,paintPath);
        RectF rect2 =  new RectF(250, 50, 400, 150);
        float radio[] ={10,15,10,15,40,45,40,45};//必须传入8个数值四组，分别对应每个角所使用的椭圆的横轴半径和纵轴半径
        pathRound.addRoundRect(rect2,radio,Path.Direction.CW);//第二个构造函数定制每个角的圆角大小
        canvas.drawPath(pathRound,paintPath);

        //圆形路径
        Path pathCicle = new Path();
        pathCicle.addCircle(500,100,60,Path.Direction.CW);
        canvas.drawPath(pathCicle,paintPath);

        //椭圆路径
        Path pathOval = new Path();
        RectF rectFOval = new RectF(600,50,850,150);
        pathOval.addOval(rectFOval,Path.Direction.CW);
        canvas.drawPath(pathOval,paintPath);

        //弧形路径
        Path pathArc = new Path();
        RectF rectFArc= new RectF(850,50,1000,150);
        pathArc.addArc(rectFArc,0,100);
        canvas.drawPath(pathArc,paintPath);


        //-------------文字相关效果-------------
        //普通设置
        Paint paintText =new Paint();
        paintText.setStrokeWidth(5);
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(10);
        //样式设置
        paintText.setFakeBoldText(true);//设置是否为粗体文字
        paintText.setUnderlineText(true);//设置下划线
        paintText.setTextSkewX(-0.25f);//设置字体水平倾斜度，普通斜体字是-0.25
        paintText.setStrikeThruText(true);//设置带有删除线效果
        //其他设置
        paintText.setTextScaleX(2);//只会将水平方向拉伸，高度不会变

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth (3);
        paint.setAntiAlias(true);
        paint.setTextSize(60);

        //绘图样式的区别：
        canvas.translate(0,130);
        String text="12341234像首歌";
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(text,10,100,paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawText(text,10,170,paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(text,10,240,paint);

        //文字样式设置及倾斜度正负区别
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(true);//粗体
        paint.setUnderlineText(true);//下划线
        paint.setStrikeThruText(true);//带有删除线效果
        paint.setTextSkewX( -0.25f);//水平倾斜度，普通斜体字是-0.25，可见往右斜
        canvas.drawText(text,500,100,paint);
        paint.setTextSkewX(0.25f);//水平倾斜度设置为：0.25，往左斜
        canvas.drawText(text,500,180,paint);

        //水平拉伸设置
        paint.setTextSkewX(0);//默认请倾斜度是0
        paint.setUnderlineText(false);
        paint.setStrikeThruText(false);
        canvas.drawText(text,500,260,paint);
        paint.setTextScaleX(2);
        canvas.drawText(text,50,340,paint);

        //截取一部分字体
        paint.setTextScaleX(1);//默认值是1
        canvas.drawText(text,6,11,10,420,paint);

        //指定各个文字位置 必须和文字数相同
        float[] pos = new float[]{300,420,400,480,500,450,600,420};
        canvas.drawPosText("生命不息",pos,paint);

        //沿路径绘制
        canvas.translate(0,500);
        paint.setStyle(Paint.Style.STROKE);
        Path cirPath = new Path();
        cirPath.addCircle(220,200,150,Path.Direction.CW);
        canvas.drawPath(cirPath,paint);
        Path cirPath2 = new Path();
        cirPath2.addCircle(720,200,150,Path.Direction.CCW);
        canvas.drawPath(cirPath2,paint);

        paint.setColor(Color.GRAY);
        canvas.drawTextOnPath("世上无难事只怕有心人",cirPath,0,0,paint);
        canvas.drawTextOnPath("世上无难事只怕有心人",cirPath2,80,30,paint);


        //字体样式设置-使用系统自带的字体 (貌似没效果)
        canvas.translate(0,400);
        Typeface font = Typeface.create("宋体",Typeface.NORMAL);
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(font);
        canvas.drawText("世上无难事只怕有心人",10,100,paint);

        //字体样式设置-自字义字体
        AssetManager am = getContext().getAssets();
        //通过从Asset中获取外部字体来显示字体样式
        Typeface typefaceNC1=Typeface.createFromAsset(am,"fonts/尼彩手机字体.TTF");
        Typeface typefaceLL2=Typeface.createFromAsset(am,"fonts/萝莉体.ttc");
        paint.setTypeface(typefaceNC1);
        canvas.drawText("世上无难事只怕有心人",10,200,paint);
        paint.setTypeface(typefaceLL2);
        canvas.drawText("世上无难事只怕有心人",10,300,paint);

    }
}
