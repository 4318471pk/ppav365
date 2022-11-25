package com.live.fox.dialog.bottomDialog;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogLivingFansManageBinding;
import com.live.fox.entity.User;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.ToastUtils;

public class LivingAudiencesManageDialog extends BaseBindingDialogFragment {

    DialogLivingFansManageBinding mBind;
    String uid,liveId,anchorId;
    User user;
    OnOperateListener operateListener;

    public static LivingAudiencesManageDialog getInstance(String liveId,String uid,String anchorId,User user)
    {
        LivingAudiencesManageDialog dialog=new LivingAudiencesManageDialog();
        dialog.uid=uid;
        dialog.liveId=liveId;
        dialog.anchorId=anchorId;
        dialog.user=user;
        return dialog;
    }

    public void setOperateListener(OnOperateListener operateListener) {
        this.operateListener = operateListener;
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.llContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.tvSetAdmin:
                if(user!=null)
                {
                    setAdmin(!user.isSuperManager());
                }
                break;
            case R.id.tvBlock:
                if(user!=null)
                {
                    setBlockUser(!user.isReject());
                }
                break;
            case R.id.tvMute:
                if(user!=null)
                {
                    setMute(!user.isBlackChat());
                }
                break;
            case R.id.rlMain:
            case R.id.gtvCancel:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.llContent,false);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_living_fans_manage;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        if(user!=null)
        {
            if(user.isSuperManager())
            {
                mBind.tvSetAdmin.setText(getStringWithoutContext(R.string.cancelAdmin));
                mBind.tvSetAdmin.setTextColor(0xffF42C2C);
            }
            else
            {
                mBind.tvSetAdmin.setText(getStringWithoutContext(R.string.setPersonToAdmin));
                mBind.tvSetAdmin.setTextColor(0xff0F86FF);
            }

            if(user.isReject()!=null && user.isReject())
            {
                mBind.tvBlock.setText(getStringWithoutContext(R.string.cancelBlack2));
                mBind.tvBlock.setTextColor(0xffF42C2C);
            }
            else
            {
                mBind.tvBlock.setText(getStringWithoutContext(R.string.black));
                mBind.tvBlock.setTextColor(0xff0F86FF);
            }

            if(user.isBlackChat()!=null &&  user.isBlackChat())
            {
                mBind.tvMute.setText(getStringWithoutContext(R.string.cancelMuteSendingMessage));
                mBind.tvMute.setTextColor(0xffF42C2C);
            }
            else
            {
                mBind.tvMute.setText(getStringWithoutContext(R.string.muteSendingMessage));
                mBind.tvMute.setTextColor(0xff0F86FF);
            }

        }
        else
        {
            ToastUtils.showShort(getStringWithoutContext(R.string.networkBad));
        }

        startAnimate(mBind.llContent,true);
    }

    private void setMute(boolean isMute)
    {
        if(!isConditionOk() || user==null)
        {
            return;
        }

        if(isMute)
        {
            Api_Live.ins().blackChat(liveId, uid, true, new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    if(isConditionOk())
                    {
                        if(code==0)
                        {
                            user.setBlackChat(true);
                            if(operateListener!=null)
                            {
                                operateListener.operate(user.getManage(),user.isBlackChat(),user.isReject());
                            }
                            onBackPress();
                        }
                        else
                        {
                            ToastUtils.showShort(msg);
                        }
                    }
                }
            });
        }
        else
        {
            Api_Live.ins().removeLivingBlackOrMuteUser(liveId,uid,0,new JsonCallback<String>(){
                @Override
                public void onSuccess(int code, String msg, String data) {
                    if(isConditionOk())
                    {
                        if(code==0)
                        {
                            user.setBlackChat(false);
                            if(operateListener!=null)
                            {
                                operateListener.operate(user.getManage(),user.isBlackChat(),user.isReject());
                            }
                            onBackPress();
                        }
                        else
                        {
                            ToastUtils.showShort(msg);
                        }
                    }
                }
            });
        }

    }

    private void setAdmin(boolean isSet)
    {
        if(!isConditionOk() || user==null)
        {
            return;
        }

        Api_Live.ins().roomManager(uid, anchorId, isSet, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(isConditionOk())
                {
                    if(code==0)
                    {
                        user.setManage(isSet?1:0);
                        if(operateListener!=null)
                        {
                            operateListener.operate(user.getManage(),user.isBlackChat(),user.isReject());
                        }
                        onBackPress();
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }

    private void setBlockUser(boolean isBlock)
    {
        if(!isConditionOk() || user==null)
        {
            return;
        }

        if(isBlock)
        {
            Api_Live.ins().banuser(liveId, uid, new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    if(isConditionOk())
                    {
                        if(code==0)
                        {
                            user.setReject(true);
                            if(operateListener!=null)
                            {
                                operateListener.operate(user.getManage(),user.isBlackChat(),user.isReject());
                            }
                            onBackPress();
                        }
                        else
                        {
                            ToastUtils.showShort(msg);
                        }
                    }

                }
            });
        }
        else
        {
            Api_Live.ins().removeLivingBlackOrMuteUser(liveId,uid,1,new JsonCallback<String>(){
                @Override
                public void onSuccess(int code, String msg, String data) {
                    if(isConditionOk())
                    {
                        if(code==0)
                        {
                            user.setReject(false);
                            if(operateListener!=null)
                            {
                                operateListener.operate(user.getManage(),user.isBlackChat(),user.isReject());
                            }
                            onBackPress();
                        }
                        else
                        {
                            ToastUtils.showShort(msg);
                        }
                    }
                }
            });
        }
    }

    public interface OnOperateListener
    {
        void operate(int manager,boolean isBlack,boolean isMute);
    }
}
