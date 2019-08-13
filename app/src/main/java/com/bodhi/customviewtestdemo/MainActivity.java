package com.bodhi.customviewtestdemo;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    CustomView tv;
    CustomGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.tv);
        layout=findViewById(R.id.layout);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("eventDis","---Activity---dispatchTouchEvent------");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("eventDis","---Activity---onTouchEvent------");

        return super.onTouchEvent(event);
    }

}
