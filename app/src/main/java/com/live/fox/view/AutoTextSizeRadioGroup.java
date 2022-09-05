package com.live.fox.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.device.ScreenUtils;


/**
 * 在一横行中 显示所有的RadioButton 保证每个RadioButton宽度相等 且文字都一行显示 (调整RadioGroup的每个RadioButton宽度和其中的文字大小 )
 * 需要调用adjusTextSize才有用
 */
public class AutoTextSizeRadioGroup extends RadioGroup {

    Context context;

    /**
     * {@inheritDoc}
     */
    public AutoTextSizeRadioGroup(Context context) {
        this(context, null);
    }


    /**
     * {@inheritDoc}
     */
    public AutoTextSizeRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(HORIZONTAL);
        init();
    }

    private void init() {

    }


    //调整RadioGroup下每个ButtonButton
    public void adjusTextSize(int radioButtonCount){
        //每个RadioButt直接的总间隔
        int viewSpaceWidth = (radioButtonCount-1) * DeviceUtils.dp2px(context, 5);
        //计算出每个RadioButton的宽度
        int viewWidth = (ScreenUtils.getScreenWidth(context)-getPaddingLeft()-getPaddingRight() - viewSpaceWidth) / radioButtonCount;
        //调整每个RadioButton的宽度和字体大小
        for (int i = 0; i < getChildCount(); i++) {
            if(getChildAt(i) instanceof RadioButton){
                RadioButton rb = (RadioButton)getChildAt(i);
                LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) rb.getLayoutParams();
                linearParams.width = viewWidth;
                //根据View最大宽度和所需显示的文字 来计算出适合字体大小 要求一行显示所有文字
                resetSizeTextView(rb, rb.getText().toString(), viewWidth);
            }
        }
    }



    //根据View最大宽度和所需显示的文字 来计算出适合字体大小 要求一行显示所有文字
    private void resetSizeTextView(RadioButton radioButton, String text, float maxWidth){
        Paint paint = radioButton.getPaint();
        float textWidth = paint.measureText(text);
        int textSizeInDp = 18;

        if(textWidth > maxWidth){
            for(;textSizeInDp > 0; textSizeInDp--){
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSizeInDp);
                paint = radioButton.getPaint();
                textWidth = paint.measureText(text);
                if(textWidth <= maxWidth){
                    break;
                }
            }
        }
        radioButton.invalidate();
    }

}