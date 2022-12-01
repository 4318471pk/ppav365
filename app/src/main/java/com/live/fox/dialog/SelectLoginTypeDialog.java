package com.live.fox.dialog;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogSelectLoginTypeBinding;
import com.live.fox.ui.login.LoginModeSelActivity;

/**
 * 选择登录方式
 */
public class SelectLoginTypeDialog extends BaseBindingDialogFragment {

    DialogSelectLoginTypeBinding mBind;
    OnSelectLoginTypeListener onSelectLoginTypeListener;

    public static SelectLoginTypeDialog getInstance()
    {
        SelectLoginTypeDialog selectLoginTypeDialog=new SelectLoginTypeDialog();
        return selectLoginTypeDialog;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.tvPhoneLogin:
                if(onSelectLoginTypeListener!=null)
                {
                    onSelectLoginTypeListener.onSelectLoginType(0,this);
                }
                break;
            case R.id.tvGuestLogin:
                if(onSelectLoginTypeListener!=null)
                {
                    onSelectLoginTypeListener.onSelectLoginType(1,this);
                }
                break;
        }
    }

    public void dismissWithAnimate()
    {
        startAnimate(mBind.rlMain,false);
    }

    public void setOnSelectLoginTypeListener(OnSelectLoginTypeListener onSelectLoginTypeListener) {
        this.onSelectLoginTypeListener = onSelectLoginTypeListener;
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_select_login_type;
    }

    @Override
    public void initView(View rootView) {
        mIsKeyCanback=false;
        mIsOutCanback=false;
        mBind=getViewDataBinding();
        mBind.setClick(this);

        startAnimate(mBind.rlMain,true);
    }

    public interface OnSelectLoginTypeListener
    {
        void onSelectLoginType(int type,SelectLoginTypeDialog dialog);
    }


}
