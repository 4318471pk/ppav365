package com.live.fox.utils;

import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public abstract class OnClickFrequentlyListener implements View.OnClickListener {

    private long vtime=0;
    private long periodTime=1000;

    public OnClickFrequentlyListener() {
    }

    public OnClickFrequentlyListener(long periodTime) {
        this.periodTime = periodTime;
    }

    @Override
    public void onClick(View view) {
        long cTime=System.currentTimeMillis();
        if(cTime-periodTime>vtime)
        {
            vtime=cTime;
            onClickView(view);
        }
    }

    public abstract void onClickView(View view);
}
