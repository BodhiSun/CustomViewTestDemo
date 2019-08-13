package com.bodhi.customview_linearlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取WindowManager服务引用
        WindowManager wm = (WindowManager) getSystemService(getApplication().WINDOW_SERVICE);

        //布局参数layoutParams相关设置略...
        View view=LayoutInflater.from(getApplication()).inflate(R.layout.float_layout, null);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        //添加view
//        wm.addView(view, layoutParams);

    }
}
