package com.live.fox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.live.fox.R;
import com.live.fox.utils.ToastUtils;
import com.luozm.captcha.Captcha;


/**
 * 提醒弹框
 */
public class YzmDialog extends Dialog {

    private Captcha captcha;

    public YzmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }


    public void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_yzm, null);
        setContentView(view);
        //设置点击屏幕不自动消失
        setCanceledOnTouchOutside(false);
        //设置返回键也不消失
        setCancelable(false);
        captcha = view.findViewById(R.id.captCha);
        captcha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                if(btnSureClick!=null){
                    btnSureClick.onClick();
                    ToastUtils.showShort("Thành công");
                    dismiss();
                }
                return "Thành công";
            }

            @Override
            public String onFailed(int count) {
                return "Thất bại";
            }

            @Override
            public String onMaxFailed() {
                dismiss();
                ToastUtils.showShort("Vượt quá số lần quy định, mời thử lại sau");
               return "Thất bại";
            }

        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
    YzmDialog.OnBtnSureClick btnSureClick;

    public void setBtnSureClick(YzmDialog.OnBtnSureClick btnClick) {
        this.btnSureClick = btnClick;
    }

    public interface OnBtnSureClick {
        void onClick();
    }
}
