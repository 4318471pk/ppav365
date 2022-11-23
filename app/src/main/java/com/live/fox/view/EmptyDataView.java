package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.utils.device.ScreenUtils;

public class EmptyDataView extends RelativeLayout {

    LinearLayout view;

    public EmptyDataView(Context context) {
        super(context);
        initView();
    }

    public EmptyDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EmptyDataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        view=(LinearLayout)View.inflate(getContext(), R.layout.view_empty,null);
        view.setBackgroundColor(0xffF5F1F8);
        addView(view, ScreenUtils.getScreenWidth(getContext()),ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    public EmptyDataView setTvEmpty(String s)
    {
        TextView textView=view.findViewById(R.id.tv_empty);
        textView.setText(s);
        return this;
    }


    public EmptyDataView setHeight(int height)
    {
        view.getLayoutParams().height=height;
        return this;
    }
}
