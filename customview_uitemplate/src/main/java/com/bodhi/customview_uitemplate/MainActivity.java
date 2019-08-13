package com.bodhi.customview_uitemplate;

import android.content.Context;
import android.print.PrinterId;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;

        TopBar topBar=findViewById(R.id.topbar);
        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void onLeftClick() {
                Toast.makeText(context,"Left Click",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightClick() {
                Toast.makeText(context,"Right Click",Toast.LENGTH_SHORT).show();

            }
        });
        topBar.setLeftIsvisable(true);
        topBar.setRightIsvisable(false);
    }
}
