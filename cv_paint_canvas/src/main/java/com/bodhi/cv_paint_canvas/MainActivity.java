package com.bodhi.cv_paint_canvas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    FrameLayout rootlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootlayout=findViewById(R.id.rootlayout);


        rootlayout.addView(new Cv_View(this));

    }



}
