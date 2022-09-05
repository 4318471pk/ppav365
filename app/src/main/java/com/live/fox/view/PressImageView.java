package com.live.fox.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * 仿IOS 图片按下或选中时 图片会变半透明 ImageView
 * @author : BaoZhou
 * @date : 2019/3/20 10:01
 */
public class PressImageView extends AppCompatImageView {
    private boolean enableState = true;
    private float pressedAlpha = 0.4f;
    private float unableAlpha = 0.3f;

    public PressImageView(Context context) {
        super(context);
    }

    public PressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (enableState) {
            if (isPressed()) {
                setAlpha(pressedAlpha);
            } else if (!isEnabled()) {
                setAlpha(unableAlpha);
            } else {
                setAlpha(1.0f);
            }
        }
    }

    public PressImageView enableState(boolean enableState) {
        this.enableState = enableState;
        return this;
    }

    public PressImageView pressedAlpha(float pressedAlpha) {
        this.pressedAlpha = pressedAlpha;
        return this;
    }

    public PressImageView unableAlpha(float unableAlpha) {
        this.unableAlpha = unableAlpha;
        return this;
    }
}
