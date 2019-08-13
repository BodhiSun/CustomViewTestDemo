package com.bodhi.customview_calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/2/12 18:39
 * desc :
 */
public class CustomCalendar extends LinearLayout {
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView tvDate;
    private GridView gridView;

    private Calendar curDate = Calendar.getInstance();
    private String displayFormat;

    public CustomCalendarListener calendarListener;

    public CustomCalendar(Context context) {
        super(context);
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
        bindControl(context);
        bindControlEvent();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomCalendar);
        try {
            displayFormat = ta.getString(R.styleable.CustomCalendar_dateFormat);
            if (displayFormat == null) {
                displayFormat = "MMM yyyy";
            }
        } finally {
            ta.recycle();
        }

        renderCalendar();
    }

    private void bindControl(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.calendar_view, this, true);

        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        tvDate = findViewById(R.id.tv_date);
        gridView = findViewById(R.id.calendar_grid);
    }

    private void bindControlEvent() {
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, -1);
                renderCalendar();
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, 1);
                renderCalendar();
            }
        });
    }

    private void renderCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
        tvDate.setText(sdf.format(curDate.getTime()));

        List<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -prevDays);

        int maxCellCount = 6 * 7;
        while (cells.size() < maxCellCount) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridView.setAdapter(new CalendarAdapter(getContext(), cells));
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (calendarListener != null) {

                    calendarListener.onItemLongPress((Date) parent.getItemAtPosition(position));
                    return true;
                }

                return false;
            }
        });

    }

    private class CalendarAdapter extends ArrayAdapter<Date> {

        LayoutInflater inflater;

        public CalendarAdapter(Context context, List<Date> days) {
            super(context, R.layout.calendar_tv_day, days);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Date date = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_tv_day, parent, false);
            }
            int day = date.getDate();
            ((TextView) convertView).setText(String.valueOf(day));

            Date now = new Date();
            boolean isSameMonth = false;
            isSameMonth = date.getMonth() == now.getMonth();
            if (isSameMonth) {
                ((CustomCalendarTextView) convertView).setTextColor(Color.parseColor("#000000"));
            } else {
                ((CustomCalendarTextView) convertView).setTextColor(Color.parseColor("#666666"));
            }

            if (now.getDate() == date.getDate() && now.getMonth() == date.getMonth() && now.getYear() == date.getYear()) {
                ((CustomCalendarTextView) convertView).setTextColor(Color.parseColor("#FF0000"));
                ((CustomCalendarTextView) convertView).isToday = true;
            }

            return convertView;
        }
    }

    public interface CustomCalendarListener {
        void onItemLongPress(Date date);
    }


}
