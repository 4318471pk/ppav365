package com.live.fox.view;

import android.content.Context;
import android.content.res.TypedArray;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;



import com.live.fox.R;

public class AgencyCenterItemView extends LinearLayout {


    private TextView tvNums;


    public AgencyCenterItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AgencyCenterItemView, defStyle, 0);
        String title = a.getString(R.styleable.AgencyCenterItemView_title);
        boolean showLine = a.getBoolean(R.styleable.AgencyCenterItemView_show_line, true);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_agency_center, this);

        TextView tvTitle = findViewById(R.id.tvTtile);
        tvNums = findViewById(R.id.tvNums);
        tvTitle.setText(title);
        View line = findViewById(R.id.line);

        if (showLine) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.INVISIBLE);
        }


    }



    public AgencyCenterItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AgencyCenterItemView(Context context) {
        this(context, null);
    }

    public void setNums(String s) {
        this.tvNums.setText(s);
    }



}

