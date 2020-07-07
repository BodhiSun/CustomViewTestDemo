package com.bodhi.customview_slidercaptchaview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SliderCaptchaView ccv;

    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ccv=findViewById(R.id.scv);
        seekBar=findViewById(R.id.seekBar);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("seekbar"," onProgressChanged   :"+progress);
                ccv.setDragOffset(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("seekbar"," onStartTrackingTouch   ");
                seekBar.setMax(ccv.getMaxSwipeValue());
                ccv.startDragSlider();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("seekbar"," onStopTrackingTouch   ");
                ccv.stopDragSlider();
            }
        });

        ccv.setCaptchaDragListener(new SliderCaptchaView.CaptchaDragListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onVerifySuccess(long interval) {
                seekBar.setEnabled(false);
                Toast toast = Toast.makeText(MainActivity.this, "验证成功", Toast.LENGTH_SHORT);
                toast.cancel();
                toast.show();
            }

            @Override
            public void onVerifyFailure() {
                seekBar.setProgress(0);
            }

            @Override
            public void onReload(SliderCaptchaView slider) {
                seekBar.setProgress(0);
//                ccv.setImageResource(R.mipmap.cartoon_bg);
                Toast toast = Toast.makeText(MainActivity.this, "验证失败", Toast.LENGTH_SHORT);
                toast.cancel();
                toast.show();
            }
        });
    }
}
