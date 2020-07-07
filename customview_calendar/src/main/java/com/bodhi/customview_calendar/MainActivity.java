package com.bodhi.customview_calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CustomCalendar.CustomCalendarListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomCalendar customCalendar = findViewById(R.id.custom_calendar);
        customCalendar.calendarListener =this;
    }

    @Override
    public void onItemLongPress(Date date) {
        DateFormat df = SimpleDateFormat.getDateInstance();
        Toast toast = Toast.makeText(this, df.format(date), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,-200);
        toast.show();
    }
}
