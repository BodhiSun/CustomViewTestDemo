package com.bodhi.customview_odometer_bubble;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BubbleView.BubbleClickListener {

    OdometerView odometerView;
    BubbleView bubbleView1;
    BubbleView bubbleView2;
    BubbleView bubbleView3;
    BubbleView bubbleView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        odometerView = findViewById(R.id.odometerView);
        bubbleView1 = findViewById(R.id.bubbleView1);
        bubbleView2 = findViewById(R.id.bubbleView2);
        bubbleView3 = findViewById(R.id.bubbleView3);
        bubbleView4 = findViewById(R.id.bubbleView4);


        odometerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                odometerView.setFirstCurProgress(6000);
            }
        },500);
        odometerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                odometerView.setCurProgress(2500);
            }
        },2000);


        bubbleView1.setOnClickListener(this);
        bubbleView2.setOnClickListener(this);
        bubbleView3.setOnClickListener(this);
        bubbleView4.setOnClickListener(this);
        bubbleView1.setCoinNum(10).setCurrentProgress(60, 60).startCountDownAnimate().startFloatAnimate();
        bubbleView2.setCoinNum(15).setCurrentProgress(60, 60).startCountDownAnimate().startFloatAnimate();
        bubbleView3.setCoinNum(12).setCurrentProgress(50, 60).startCountDownAnimate().startFloatAnimate();
        bubbleView4.setCoinNum(15).setCurrentProgress(60, 60).startCountDownAnimate().startFloatAnimate();
    }

    @Override
    public void bubbleClick(boolean isCountFinish, float remainTime, int viewId) {
        switch (viewId){
            case R.id.bubbleView1:
                normalBubbleClick(isCountFinish,bubbleView1);
                break;
            case R.id.bubbleView2:
                normalBubbleClick(isCountFinish,bubbleView2);
                break;
            case R.id.bubbleView3:
                normalBubbleClick(isCountFinish,bubbleView3);
                break;
            case R.id.bubbleView4:
                normalBubbleClick(isCountFinish,bubbleView4);
                break;
        }
    }

    public void normalBubbleClick(boolean isCountFinish,BubbleView bubbleView){
        if (!isCountFinish) {
            return;
        }
        bubbleView.dismissd();

        bubbleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                bubbleView1.setCoinNum(10).setCurrentProgress(60, 60).startCountDownAnimate().startFloatAnimate();
                bubbleView2.setCoinNum(15).setCurrentProgress(0, 60).startCountDownAnimate().startFloatAnimate();
                bubbleView3.setCoinNum(12).setCurrentProgress(60, 60).startCountDownAnimate().startFloatAnimate();
                bubbleView4.setCoinNum(25).setCurrentProgress(60, 60).startCountDownAnimate().startFloatAnimate();
                bubbleView1.show();
                bubbleView2.show();
                bubbleView3.show();
                bubbleView4.show();
            }
        },1500);
    }
}
