package com.live.fox.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.entity.User;
import com.live.fox.utils.LogUtils;

public class LivingClickTextSpan<T> extends ClickableSpan {

    int nameColor;
    T bean;
    OnClickTextItemListener onClickTextItemListener;

    public LivingClickTextSpan(T bean, int nameColor) {
        this.nameColor = nameColor;
        this.bean=bean;
    }

    public void setOnClickTextItemListener(OnClickTextItemListener onClickTextItemListener) {
        this.onClickTextItemListener = onClickTextItemListener;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(nameColor);
        ds.setUnderlineText(false);
    }


    @Override
    public void onClick(View widget) {
        LogUtils.e("onClick");
        if(onClickTextItemListener!=null)
        {
            onClickTextItemListener.onClick(bean);
        }
    }

    public interface OnClickTextItemListener<T>
    {
        void onClick(T bean);
    }
}
