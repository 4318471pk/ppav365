package com.tencent.demo.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tencent.demo.R;

public class XmagicSeekBarLayout extends RelativeLayout {
    private TextView powerTxt = null;
    private XmagicSeekBar xmagicSeekBar = null;
    private TextView currentTxt = null;
    private OnSeekBarChangeListener onSeekBarChangeListener = null;

    public XmagicSeekBarLayout(Context context) {
        super(context);
        this.initViews();
    }

    public XmagicSeekBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initViews();
    }

    public XmagicSeekBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initViews();
    }


    private void initViews() {
        ViewGroup view = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.xmagic_seekbar_layout, this, false);
        powerTxt = view.findViewById(R.id.power_text);
        xmagicSeekBar = view.findViewById(R.id.seekBar);
        currentTxt = view.findViewById(R.id.currentValue);
        view.removeAllViews();
        this.addView(powerTxt);
        this.addView(xmagicSeekBar);
        this.addView(currentTxt);
        xmagicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentTxt.setText(String.valueOf(progress));
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setName(String name){
        powerTxt.setText(name);
    }


    public void setProgress(int displayMinValue, int displayMaxValue, int currentValue) {
        xmagicSeekBar.setMyMin(displayMinValue);
        xmagicSeekBar.setMyMax(displayMaxValue);
        xmagicSeekBar.setMyProgress(currentValue, true);
        currentTxt.setText(String.valueOf(currentValue));
    }

    public void setProgress(int currentValue) {
        xmagicSeekBar.setMyProgress(currentValue, true);
        currentTxt.setText(String.valueOf(currentValue));
    }


    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
    }


}
