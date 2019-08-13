package com.bodhi.cv_coordinatesystem;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/8/12 18:16
 * desc :
 */
public class HelpUtil {

    /**
     * 获取屏幕宽高存入point点里面
     * @param context
     * @param mWinSize
     */
    public static void loadWinSize(Context context, Point mWinSize) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        //屏幕宽高分别存入x,y
        mWinSize.x=dm.widthPixels;
        mWinSize.y=dm.heightPixels;
    }

    /**
     * 绘制网格:注意只有用path才能绘制虚线
     * @param step 小正方形边长
     * @param winSize 屏幕尺寸
     * @return
     */
    public static Path gridPath(int step, Point winSize) {
        Path path = new Path();
        //从上到下开始横向线条路径
        for (int i = 0; i < winSize.y / step + 1; i++) {
            path.moveTo(0,step*i);
            path.lineTo(winSize.x,step*i);
        }

        //从左到右开始竖向线条路径
        for (int i = 0; i < winSize.x / step + 1; i++) {
            path.moveTo(step*i,0);
            path.lineTo(step*i,winSize.y );
        }

        return path;
    }

    /**
     *  坐标系x,y轴路径
     * @param coo 坐标点
     * @param mWinSize 屏幕尺寸
     * @return 坐标系路径
     */
    public static Path cooPath(Point coo, Point mWinSize) {
        Path path=new Path();
        //x正半轴线
        path.moveTo(coo.x,coo.y);
        path.lineTo(mWinSize.x,coo.y);
        //x负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(0,coo.y);
        //y负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x,0);
        //y正半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x,mWinSize.y-300);
        return path;
    }
}
