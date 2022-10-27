package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundLinearLayout;
import com.live.fox.R;
import com.live.fox.utils.device.ScreenUtils;

public class BotTriangleBubbleView extends LinearLayout {

    RoundLinearLayout roundLinearLayout;

    public BotTriangleBubbleView(Context context) {
        this(context,null);
    }

    public BotTriangleBubbleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BotTriangleBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        setOrientation(VERTICAL);
        int dip10= ScreenUtils.getDip2px(getContext(),10);
        int llWidth=(int)(ScreenUtils.getScreenWidth(getContext())*0.3f);

        roundLinearLayout=new RoundLinearLayout(getContext());
        roundLinearLayout.getDelegate().setCornerRadius(dip10);
        roundLinearLayout.getDelegate().setBackgroundPressColor(0xffF5F1F8);
        roundLinearLayout.getDelegate().setBackgroundColor(0xffF5F1F8);
        roundLinearLayout.setOrientation(VERTICAL);
        roundLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(llWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(roundLinearLayout);

        View triangle=new View(getContext());
        int wh=(int)(dip10*1.5f);
        triangle.setLayoutParams(new LinearLayout.LayoutParams(wh,wh));
        triangle.setBackground(getResources().getDrawable(R.drawable.triangle_white));
        addView(triangle);

        String leftStrs[]=getResources().getStringArray(R.array.giftListTag);
        String rightStrs[]=getResources().getStringArray(R.array.giftListName);

        for (int i = 0; i < leftStrs.length; i++) {
            RelativeLayout relativeLayout=new RelativeLayout(getContext());
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(llWidth,dip10*3);
            ll.topMargin=dip10/10;
            relativeLayout.setLayoutParams(ll);

            TextView left=new TextView(getContext());
            left.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            left.setTextColor(0xffFF008A);
            left.setText(leftStrs[i]);
            left.setGravity(Gravity.CENTER);
            left.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            left.setPadding(dip10,0,0,0);
            relativeLayout.addView(left);

            TextView right=new TextView(getContext());
            right.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            right.setTextColor(0xff404040);
            right.setText(rightStrs[i]);
            right.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            right.setLayoutParams(rl);
            right.setText(leftStrs[i]);
            right.setPadding(0,0,dip10,0);
            relativeLayout.addView(right);

            relativeLayout.addView(left);
            roundLinearLayout.addView(relativeLayout);
        }


    }




}
