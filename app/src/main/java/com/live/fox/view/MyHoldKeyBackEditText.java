package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

public class MyHoldKeyBackEditText extends EditText {

    OnKeyBackListener onKeyBackListener;

    public MyHoldKeyBackEditText(Context context) {
        super(context);
    }

    public MyHoldKeyBackEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHoldKeyBackEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnKeyBackListener(OnKeyBackListener onKeyBackListener) {
        this.onKeyBackListener = onKeyBackListener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_UP && keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(onKeyBackListener!=null)
            {
                onKeyBackListener.onKeyBack();
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public interface OnKeyBackListener{
        void onKeyBack();
    }
}
