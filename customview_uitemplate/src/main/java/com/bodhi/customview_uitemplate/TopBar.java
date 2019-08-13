package com.bodhi.customview_uitemplate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author : Sun
 * @version : 1.0
 * create time : 2019/2/12 16:12
 * desc :
 */
public class TopBar extends RelativeLayout {
    private Button leftButton,rightButton;
    private TextView tvTitle;

    private String leftText;
    private int leftTextColor;
    private Drawable leftBg;

    private String rightText;
    private int rightTextColor;
    private Drawable rightBg;

    private String title;
    private int titleTextColor;
    private float titleTextSize;

    private LayoutParams leftLP,rightLP,titleLP;

    public TopBar(Context context) {
        super(context);
    }

    public TopBar(final Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.TopBar);

        leftTextColor =ta.getColor(R.styleable.TopBar_leftTextColor,0);
        leftBg=ta.getDrawable(R.styleable.TopBar_leftBackground);
        leftText=ta.getString(R.styleable.TopBar_leftText);

        rightTextColor =ta.getColor(R.styleable.TopBar_rightTextColor,0);
        rightBg=ta.getDrawable(R.styleable.TopBar_rightBackground);
        rightText=ta.getString(R.styleable.TopBar_rightText);

        title =ta.getString(R.styleable.TopBar_title);
        titleTextColor=ta.getColor(R.styleable.TopBar_titleTextColor,0);
        titleTextSize=ta.getDimension(R.styleable.TopBar_titleTextSize,0);

        ta.recycle();

        leftButton= new Button(context);
        rightButton= new Button(context);
        tvTitle=new TextView(context);

        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBg);
        leftButton.setText(leftText);

        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBg);
        rightButton.setText(rightText);

        tvTitle.setText(title);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setGravity(Gravity.CENTER);

        setBackgroundColor(0xFFF59563);

        leftLP = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        leftLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftButton,leftLP);

        rightLP = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        rightLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightButton,rightLP);

        titleLP=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        titleLP.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(tvTitle,titleLP);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarClickListener.onLeftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarClickListener.onRightClick();
            }
        });
    }

    private TopBarClickListener topBarClickListener;
    public void setOnTopBarClickListener(TopBarClickListener topBarClickListener){
        this.topBarClickListener=topBarClickListener;
    }

    public interface TopBarClickListener{
         void onLeftClick();
         void onRightClick();
    }

    public void setLeftIsvisable(boolean isVisible){
        leftButton.setVisibility(isVisible==true?VISIBLE:GONE);
    }

    public void setRightIsvisable(boolean isVisible){
        rightButton.setVisibility(isVisible==true?VISIBLE:GONE);
    }
}
