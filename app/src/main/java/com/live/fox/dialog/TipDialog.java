package com.live.fox.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.utils.NumberUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 仿IOS提示框
 */
public class TipDialog extends Dialog {

    private DialogButtonOnClickListener listener_1, listener_2;
    private Context context;
    private TextView tv_des;
    private View line_1;
    private Button btn_1;
    private View line_2;
    private Button btn_2;
    private TextView tvPromptTitle;
    private RelativeLayout reYZM;
    private EditText etYZM;
    private TextView tvYZM;
    private boolean isYZMShow = false;
    private RelativeLayout ripple_btn1, ripple_btn2;

    public TipDialog(final Context context) {
        super(context, R.style.CommonDialog);
        setCanceledOnTouchOutside(true);
        getWindow().getAttributes().width = -1;
        getWindow().getAttributes().height = -2;
        getWindow().getAttributes().y = 0;
        getWindow().setGravity(Gravity.CENTER_VERTICAL);
        getWindow().setAttributes(getWindow().getAttributes());
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
        this.context = context;
        setContentView(R.layout.dialog_tip);

        tv_des = (TextView) findViewById(R.id.tv_des);
        tvPromptTitle = (TextView) findViewById(R.id.tvPromptTitle);
        line_1 = findViewById(R.id.line_1);
        btn_1 = (Button) findViewById(R.id.btn_1);
        line_2 = findViewById(R.id.line_2);
        btn_2 = (Button) findViewById(R.id.btn_2);
        ripple_btn1 = (RelativeLayout) findViewById(R.id.rl_layout1);
        ripple_btn2 = (RelativeLayout) findViewById(R.id.rl_layout2);
        reYZM = findViewById(R.id.reYZM);
        etYZM = findViewById(R.id.etYZM);
        tvYZM = findViewById(R.id.tvYZM);
        tvPromptTitle.setVisibility(View.GONE);
        tv_des.setVisibility(View.GONE);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener_1 != null) {
                    listener_1.onClick(view, TipDialog.this);
                }
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener_2 != null) {
                    if (isYZMShow) {
                        String tvYZMString = StringUtils.filterEmptyChar(tvYZM.getText().toString());
                        String etYZMString = etYZM.getText().toString();
                        if (TextUtils.isEmpty(etYZMString)) {
                            ToastUtils.showShort(context.getString(R.string.dialog_info_verification));
                            return;
                        }
                        if (!tvYZMString.equals(etYZMString)) {
                            ToastUtils.showShort(context.getString(R.string.dialog_info_verification_error));
                            return;
                        }
                    }
                    listener_2.onClick(view, TipDialog.this);
                }
            }
        });

    }

    public void setTextDes(String text) {
        tv_des.setVisibility(View.VISIBLE);
        tv_des.setText(text);
    }

    public void setContentTextSize(float size) {
        tv_des.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    public void setTextDesColor(int textColor) {
        tv_des.setTextColor(textColor);
    }

    public void setButton1Color(int textColor) {
        btn_1.setTextColor(textColor);
    }

    public void setButton1TextSize(float textSize) {
        btn_1.setTextSize(textSize);
    }

    public void setButton2TextSize(float textSize) {
        btn_2.setTextSize(textSize);
    }

    public void setButton2Color(int textColor) {
        btn_2.setTextColor(textColor);
    }

    public void setPromptTitle(String promptTitle) {
        tvPromptTitle.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(promptTitle)) {
            tvPromptTitle.setText(promptTitle);
        } else {
            tvPromptTitle.setText(context.getString(R.string.dialog_words));
        }
    }

    public void setYZMVisible(boolean isYZMShow) {
        this.isYZMShow = isYZMShow;
        tvYZM.setText(NumberUtils.getRandomString());
        reYZM.setVisibility(View.VISIBLE);
    }

    public void setButton1(String text, DialogButtonOnClickListener clickListener) {
        this.btn_1.setText(text);
        this.ripple_btn1.setVisibility(View.VISIBLE);
        this.listener_1 = clickListener;
        setCanceledOnTouchOutside(true);
        fixlayout();
    }

    public void setButton2(String text, DialogButtonOnClickListener clickListener) {
        this.btn_2.setText(text);
        this.ripple_btn2.setVisibility(View.VISIBLE);
        this.listener_2 = clickListener;
        setCanceledOnTouchOutside(true);
        fixlayout();
    }

    public void setButton1Gone() {
        this.ripple_btn1.setVisibility(View.GONE);
    }

    public void setButton2Gone() {
        this.ripple_btn2.setVisibility(View.GONE);
    }

    public void setNoPomptTitle() {
        tvPromptTitle.setVisibility(View.GONE);
    }

    public void setNoDes() {
        tv_des.setVisibility(View.GONE);
    }


    private void fixlayout() {
        if (ripple_btn1.getVisibility() == View.VISIBLE && ripple_btn2.getVisibility() == View.GONE) {
            // 顯示btn1 隱藏btn2
            line_1.setVisibility(View.VISIBLE);
            line_2.setVisibility(View.GONE);

        } else if (ripple_btn1.getVisibility() == View.GONE && ripple_btn2.getVisibility() == View.VISIBLE) {
            // 顯示btn2 隱藏btn1
            line_1.setVisibility(View.VISIBLE);
            line_2.setVisibility(View.GONE);

        } else if (ripple_btn1.getVisibility() == View.VISIBLE && ripple_btn2.getVisibility() == View.VISIBLE) {
            // 兩個btn都顯示
            line_1.setVisibility(View.VISIBLE);
            line_2.setVisibility(View.VISIBLE);

        } else {
            // 都隐藏
            line_1.setVisibility(View.GONE);
            line_2.setVisibility(View.GONE);
            ripple_btn1.setVisibility(View.GONE);
            ripple_btn2.setVisibility(View.GONE);
        }
    }

    public interface DialogButtonOnClickListener {
        void onClick(View button, TipDialog dialog);
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


}
