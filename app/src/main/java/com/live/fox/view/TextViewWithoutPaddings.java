package com.live.fox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;


//有些手机里TextView会有默认的padding值，这个类就是去除这些padding值
public class TextViewWithoutPaddings extends AppCompatTextView {

    private final Paint mPaint = new Paint();

    private final Rect mBounds = new Rect();

    public TextViewWithoutPaddings(Context context) {
        super(context);
    }

    public TextViewWithoutPaddings(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewWithoutPaddings(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public TextViewWithoutPaddings(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        final String text = calculateTextParams();

        final int left = mBounds.left;
        final int bottom = mBounds.bottom;
        mBounds.offset(-mBounds.left, -mBounds.top);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getCurrentTextColor());
        canvas.drawText(text, -left, mBounds.bottom - bottom, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateTextParams();
        setMeasuredDimension(mBounds.right - mBounds.left, -mBounds.top + mBounds.bottom);
    }

    private String calculateTextParams() {
        final String text = getText().toString();
        final int textLength = text.length();
        mPaint.setTextSize(getTextSize());
        mPaint.getTextBounds(text, 0, textLength, mBounds);
        if (textLength == 0) {
            mBounds.right = mBounds.left;
        }
        return text;
    }

}