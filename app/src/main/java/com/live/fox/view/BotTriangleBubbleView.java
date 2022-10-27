package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundLinearLayout;
import com.live.fox.R;
import com.live.fox.utils.device.ScreenUtils;

public class BotTriangleBubbleView extends LinearLayout {

    RoundLinearLayout roundLinearLayout;
    onCLickItemListener onCLickItemListener;

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

    public void setOnCLickItemListener(BotTriangleBubbleView.onCLickItemListener onCLickItemListener) {
        this.onCLickItemListener = onCLickItemListener;
    }

    private void initView()
    {
        setOrientation(VERTICAL);
        int dip10= ScreenUtils.getDip2px(getContext(),10);
        int llWidth=dip10*12;

        roundLinearLayout=new RoundLinearLayout(getContext());
        roundLinearLayout.getDelegate().setCornerRadius(dip10/2);
        roundLinearLayout.getDelegate().setBackgroundPressColor(0xffffffff);
        roundLinearLayout.getDelegate().setBackgroundColor(0xffffffff);
        roundLinearLayout.setOrientation(VERTICAL);
        roundLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(llWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(roundLinearLayout);

        View triangle=new View(getContext());
        int wh=(int)(dip10*1.5f);
        LinearLayout.LayoutParams llTriangle= new LinearLayout.LayoutParams(wh,wh);
        llTriangle.leftMargin=(int)(llWidth*0.35f);
//        llTriangle.topMargin=-1*dip10/10;
        triangle.setLayoutParams(llTriangle);
        triangle.setBackground(getResources().getDrawable(R.drawable.triangle_white));
        addView(triangle);

        String leftStrs[]=getResources().getStringArray(R.array.giftListTag);
        String rightStrs[]=getResources().getStringArray(R.array.giftListName);

        for (int i = 0; i < leftStrs.length; i++) {
            RelativeLayout relativeLayout=new RelativeLayout(getContext());
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(llWidth,dip10*3);
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
            right.setText(rightStrs[i]);
            right.setPadding(0,0,dip10,0);
            relativeLayout.addView(right);

            roundLinearLayout.addView(relativeLayout);

            relativeLayout.setTag(leftStrs[i]);

            relativeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onCLickItemListener!=null)
                    {
                        onCLickItemListener.onClick((String) v.getTag());
                    }
                }
            });

            if(i<leftStrs.length-1)
            {
                ImageView line=new ImageView(getContext());
                LinearLayout.LayoutParams llLine=new LinearLayout.LayoutParams(llWidth-dip10, dip10/20);
                llLine.leftMargin=dip10/2;
                llLine.rightMargin=dip10/2;
                line.setLayoutParams(llLine);
                line.setBackgroundColor(0xffE4E1ED);
                roundLinearLayout.addView(line);
            }
        }

    }


    public interface onCLickItemListener
    {
        void onClick(String amount);
    }


}
