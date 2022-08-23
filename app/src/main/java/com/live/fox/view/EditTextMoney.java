package com.live.fox.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.live.fox.AppConfig;
import com.live.fox.R;
import com.live.fox.utils.MoneyTextWatcher;

import java.util.Objects;


/**
 * 带监听贯标改变的位置监听
 * 如果要跳转位置 在xml中设置paddingRight
 */
public class EditTextMoney extends AppCompatEditText {

    private Drawable imgEnable;
    private final Context mContext;

    public EditTextMoney(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextMoney(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public EditTextMoney(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        imgEnable = ContextCompat.getDrawable(mContext, R.drawable.delete_ic);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    /**
     * 设置删除图片
     */
    private void setDrawable() {
        if (length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgEnable, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgEnable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e("Money", "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        Log.i("Selection", "selStart =" + selStart + " selEnd=" + selEnd);
        String text = Objects.requireNonNull(getText()).toString();
        if (TextUtils.isEmpty(text) || AppConfig.isThLive()) {
            return;
        }

        //最后一个千位符的位置
        if (text.contains(MoneyTextWatcher.SPLICING)) {
            int index = text.lastIndexOf(",");
            if (selStart - index > 0) {
                setSelection(index);
            }
        }
    }
}