package com.bodhi.customview_progress;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private RoundProgressBarWithProgress roundProgress;
    private CustomHorizontalProgressBar horizontalProgress;
    Handler handler  = new Handler(Looper.getMainLooper());
    int maxProgress=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roundProgress = findViewById(R.id.roundProgress);
        horizontalProgress = findViewById(R.id.horizontalProgress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (maxProgress <= 100) {
                    maxProgress++;

                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            horizontalProgress.setProgress(maxProgress);

                            roundProgress.setProgress(maxProgress);
                        }
                    });
                }
            }
        }).start();
    }
}
