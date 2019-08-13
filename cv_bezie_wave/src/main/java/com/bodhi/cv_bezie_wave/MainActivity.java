package com.bodhi.cv_bezie_wave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Cv_Bezie_Gesture cv_bezie_gesture;
    Cv_Bezie_Wave cv_bezie_Wave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv_bezie_gesture=findViewById(R.id.cv_bezie_gesture);
        cv_bezie_Wave=findViewById(R.id.cv_bezie_Wave);
        cv_bezie_Wave.startAnim();
    }

    public void resetGesture(View view) {
        cv_bezie_gesture.reset();
    }
}
