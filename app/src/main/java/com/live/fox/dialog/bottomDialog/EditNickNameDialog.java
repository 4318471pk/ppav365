package com.live.fox.dialog.bottomDialog;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogEditNicknameBinding;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.editprofile.EditUserInfoActivity;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.utils.ToastUtils;

public class EditNickNameDialog extends BaseBindingDialogFragment {

    DialogEditNicknameBinding mBind;
    OnPersonalDataChangeListener onPersonalDataChangeListener;

    public static EditNickNameDialog getInstance()
    {
        return new EditNickNameDialog();
    }


    public void setOnPersonalDataChangeListener(OnPersonalDataChangeListener onPersonalDataChangeListener) {
        this.onPersonalDataChangeListener = onPersonalDataChangeListener;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
            case R.id.tvCancel:
                mBind.rlMain.setEnabled(false);
                dismissAllowingStateLoss();
                break;
            case R.id.tvConfirm:
                editNickName();
                break;

        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_edit_nickname;
    }

    @Override
    public void initView(View view) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mBind=getViewDataBinding();
        mBind.setClick(this);

        Animation animation= new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0
                ,Animation.RELATIVE_TO_PARENT,1f
                ,Animation.RELATIVE_TO_PARENT,0f);
        animation.setDuration(300);

        startAnimate();

    }

    private void editNickName()
    {
        String nickname=mBind.etNickName.getText().toString();
        if(nickname.length()==0)
        {
            ToastUtils.showShort(mBind.etNickName.getHint().toString());
            return;
        }
        User user=new User();
        user.setNickname(nickname);
        showLoadingDialog();
        Api_User.ins().modifyUserInfo(user, 2, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                dismissLoadingDialog();
                if(code==0)
                {
                    DataCenter.getInstance().getUserInfo().updateUser(user);
                    if(getActivity()!=null && (getActivity() instanceof EditUserInfoActivity))
                    {
                        EditUserInfoActivity editUserInfoActivity=(EditUserInfoActivity)getActivity();
                        editUserInfoActivity.refreshPage();
                    }
                    dismissAllowingStateLoss();
                    if(onPersonalDataChangeListener!=null)
                    {
                        onPersonalDataChangeListener.onDataChange();
                    }
                }
            }

        });

    }


    public void startAnimate(){

        Animation animation= new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0
                ,Animation.RELATIVE_TO_PARENT,1f
                ,Animation.RELATIVE_TO_PARENT,0f);
        animation.setDuration(300);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBind.rllMain.startAnimation(animation);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 0x88);
        //开始延时时长
        valueAnimator.setStartDelay(0);
        //动画时长
        valueAnimator.setDuration(300);
        //重复次数
        valueAnimator.setRepeatCount(0);
        //设置重复模式 ValueAnimator.RESTART正序重新开始  ValueAnimator.REVERSE逆序重新开始
//        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取当前值
                int number = (int) animation.getAnimatedValue();
                mBind.rlMain.setBackgroundColor(number<<24);
            }
        });
        //开启动画
        valueAnimator.start();
    }

    public interface OnPersonalDataChangeListener
    {
        public void onDataChange();
    }
}
