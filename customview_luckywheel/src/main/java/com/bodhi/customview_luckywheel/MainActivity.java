package com.bodhi.customview_luckywheel;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    LuckyWheel luckyWheel,luckyWheel2,luckyWheel3;
    private boolean isRotating = false;
    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        luckyWheel = findViewById(R.id.luckyWheel);
        luckyWheel2 = findViewById(R.id.luckyWheel2);
        luckyWheel3 = findViewById(R.id.luckyWheel3);
        mRandom = new Random();

        luckyWheel.setOnLuckyWheelRotateListener(new LuckyWheel.OnLuckyWheelRotateListener() {
            @Override
            public void rotateBefore() {
                if (!isRotating)
                    luckyWheel.startRotate( mRandom.nextInt(7)+1);
            }

            @Override
            public void rotating(ValueAnimator animation) {
                isRotating = true;
            }

            @Override
            public void rotateAfter(int position, String des) {
                isRotating = false;
                Toast.makeText(MainActivity.this, "结束 position:" + position + "   " + des, Toast.LENGTH_SHORT).show();

            }
        });

        luckyWheel2.setOnLuckyWheelRotateListener(new LuckyWheel.OnLuckyWheelRotateListener() {
            @Override
            public void rotateBefore() {
                if (!isRotating)
                    luckyWheel2.startRotate( mRandom.nextInt(7)+1);
            }

            @Override
            public void rotating(ValueAnimator animation) {
                isRotating = true;
            }

            @Override
            public void rotateAfter(int position, String des) {
                isRotating = false;
                Toast.makeText(MainActivity.this, "结束 position:" + position + "   " + des, Toast.LENGTH_SHORT).show();

            }
        });

        luckyWheel3.setOnLuckyWheelRotateListener(new LuckyWheel.OnLuckyWheelRotateListener() {
            @Override
            public void rotateBefore() {
                if (!isRotating)
                    luckyWheel3.startRotate( mRandom.nextInt(7)+1);
            }

            @Override
            public void rotating(ValueAnimator animation) {
                isRotating = true;
            }

            @Override
            public void rotateAfter(int position, String des) {
                isRotating = false;
                Toast.makeText(MainActivity.this, "结束 position:" + position, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
