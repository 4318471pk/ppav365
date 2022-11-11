package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.BotDialogLivingProfileBinding;
import com.live.fox.dialog.temple.LivingInterruptDialog;
import com.live.fox.entity.Audience;
import com.live.fox.entity.LivingCurrentAnchorBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.MyFollowListActivity;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankProfileView;

import org.jetbrains.annotations.NotNull;

public class LivingProfileBottomDialog extends BaseBindingDialogFragment {

    public static final int Audience=0;//用户端直播间用户
    public static final int AudienceInAnchorRoom=1;//主播端直播间 的用户
    public static final int AnchorSelf=2;//主播端直播间 主播自己
    public static final int AudienceAnchor=4;//用户端直播间主播

    LivingCurrentAnchorBean livingCurrentAnchorBean;//用户端直播间主播数据
    Audience audience;//用户端直播间用户数据
    ButtonClickListener buttonClickListener;
    BotDialogLivingProfileBinding mBind;
    String uid;
    int mode;

    public static LivingProfileBottomDialog getInstance(int mode)
    {
        LivingProfileBottomDialog dialog=new LivingProfileBottomDialog();
        dialog.mode=mode;
        return dialog;
    }

    public void setLivingCurrentAnchorBean(LivingCurrentAnchorBean livingCurrentAnchorBean) {
        this.livingCurrentAnchorBean = livingCurrentAnchorBean;
        if(livingCurrentAnchorBean!=null)
        {
            uid=livingCurrentAnchorBean.getAnchorId();
        }
    }

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public void setAudience(com.live.fox.entity.Audience audience) {
        this.audience = audience;
        if(audience!=null)
        {
            uid=audience.getUid()+"";
        }
    }

    public void setFullscreen(boolean isShowStatusBar, boolean isShowNavigationBar) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (!isShowStatusBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (!isShowNavigationBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        getDialog().getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        //隐藏标题栏
        // getSupportActionBar().hide();
        setNavigationStatusColor(Color.TRANSPARENT);
    }

    public void setNavigationStatusColor(int color) {
        //VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        if (Build.VERSION.SDK_INT >= 21) {
            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getDialog().getWindow().setNavigationBarColor(color);
            getDialog().getWindow().setStatusBarColor(color);
        }
    }

    private  void setAndroidNativeLightStatusBar( boolean dark) {
        View decor =getDialog().getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

//        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        setFullscreen(true, true);
        setAndroidNativeLightStatusBar( true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.layoutFollow:
                MyFollowListActivity.startActivity(requireActivity(), false);
                break;
            case R.id.layoutFans:
                MyFollowListActivity.startActivity(requireActivity(), true);
                break;
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.rlContent,false);
                break;
            case R.id.tvReport:
                startAnimate(mBind.rlContent, false, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        FragmentManager fragmentManager=getParentFragmentManager();
                        if(fragmentManager==null)
                        {
                            fragmentManager=getActivity().getSupportFragmentManager();
                        }

                        ReportAnchorDialog reportAnchorDialog=ReportAnchorDialog.getInstance(uid);
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragmentManager,reportAnchorDialog);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case R.id.tv1:
//                Long uid=DataCenter.getInstance().getUserInfo().getUser().getUid();
                if(Strings.isDigitOnly(uid))
                {
                    UserDetailActivity.startActivity(requireActivity(),Long.valueOf(uid));
                }
                break;
            case R.id.tv2:
                startAnimate(mBind.rlContent, false, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(buttonClickListener!=null && Strings.isDigitOnly(uid))
                        {
                            buttonClickListener.onClick(uid,false,true);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
            case R.id.tv3:
                switch (mode)
                {
                    case Audience:
                        //没有这个功能
                        break;
                    case AudienceAnchor://用户端直播间主播
                        if(livingCurrentAnchorBean!=null && !TextUtils.isEmpty(livingCurrentAnchorBean.getAnchorId()))
                        {
                            follow(livingCurrentAnchorBean.getAnchorId());
                        }
                        break;
                    case AudienceInAnchorRoom:
                        LivingAudiencesManageDialog livingAudiencesManageDialog=LivingAudiencesManageDialog.getInstance();
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),livingAudiencesManageDialog);
                        break;
                    case AnchorSelf:

                        break;
                }
                break;
        }

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.bot_dialog_living_profile;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.INVISIBLE);
        switch (mode)
        {
            case Audience:
                mBind.tv2.setText(getResources().getString(R.string.tagHim));
                mBind.tv3.setText("");
                if(audience!=null)
                {
                    GlideUtils.loadCircleImage(getActivity(), audience.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,
                            mBind.rpv.getProfileImage());
                    mBind.rpv.setIndex(RankProfileView.NONE,RankProfileView.NONE,false);
                    StringBuilder sb=new StringBuilder();
                    sb.append(getStringWithoutContext(R.string.identity_id_3));
                    sb.append(audience.getUid());
                    mBind.tvID.setText(sb.toString());
                    mBind.tvName.setText(audience.getNickname());
                    getUserInfo(audience.getUid()+"");
                }
                break;
            case AudienceAnchor:
                mBind.tv2.setText(getResources().getString(R.string.tagHim));
                if(livingCurrentAnchorBean!=null)
                {
                    GlideUtils.loadCircleImage(getActivity(), livingCurrentAnchorBean.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,
                            mBind.rpv.getProfileImage());
                    mBind.rpv.setIndex(RankProfileView.NONE,RankProfileView.NONE,false);
                    StringBuilder sb=new StringBuilder();
                    sb.append(getStringWithoutContext(R.string.identity_id_3));
                    sb.append(livingCurrentAnchorBean.getAnchorId());
                    mBind.tvID.setText(sb.toString());
                    mBind.tvName.setText(livingCurrentAnchorBean.getNickname());
                    getUserInfo(livingCurrentAnchorBean.getAnchorId());
                    if(livingCurrentAnchorBean.getFollow()!=null && livingCurrentAnchorBean.getFollow())
                    {
                        mBind.tv3.setEnabled(false);
                        mBind.tv3.setTextColor(0xffA2A0A9);
                        mBind.tv3.setText(getStringWithoutContext(R.string.concerned));
                    }
                    else
                    {
                        mBind.tv3.setTextColor(0xffFF008A);
                        mBind.tv3.setText(getStringWithoutContext(R.string.follow2));
                    }
                }
                break;
            case AudienceInAnchorRoom:
                mBind.tv2.setText("");
                mBind.tv2.setEnabled(false);
                mBind.tv3.setText(getResources().getString(R.string.manager));
                mBind.tvReport.setVisibility(View.INVISIBLE);
                break;
            case AnchorSelf:
                mBind.llBotView.setVisibility(View.INVISIBLE);
                mBind.tvReport.setVisibility(View.INVISIBLE);
                break;
        }

        mBind.rpv.setOnConfirmWidthAndHeightListener(new RankProfileView.OnConfirmWidthAndHeightListener() {
            @Override
            public void onValue(int width, int height) {
                RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) mBind.roundLL.getLayoutParams();
                rl.topMargin=height/2;
                mBind.roundLL.setLayoutParams(rl);

                RelativeLayout.LayoutParams rlName=(RelativeLayout.LayoutParams) mBind.tvName.getLayoutParams();
                rlName.topMargin=height/2+ ScreenUtils.getDip2px(getActivity(),5);
                mBind.tvName.setLayoutParams(rlName);

                view.setVisibility(View.VISIBLE);
            }
        });


        startAnimate(mBind.rlContent,true);
    }


    private void getUserInfo(String uid)
    {
        try {
            Api_User.ins().getUserInfo(Long.valueOf(uid), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String userJson) {
                    if (code == 0) {
                        User user=new Gson().fromJson(userJson,User.class);
                        if(user!=null)
                        {
                            StringBuilder sb=new StringBuilder();
                            sb.append(getStringWithoutContext(R.string.identity_id_3));
                            sb.append(uid);
                            mBind.tvID.setText(sb.toString());

                            SpanUtils spanUtils=new SpanUtils();
                            spanUtils.append(user.getNickname()).setFontSize(16,true)
                                    .setForegroundColor(0xff404040).setAlign(Layout.Alignment.ALIGN_CENTER);
                            ChatSpanUtils.appendSexIcon(spanUtils,user.getSex(),getContext());
                            mBind.tvName.setText(spanUtils.create());

                            sb.delete(0,sb.length());
                            if(TextUtils.isEmpty(user.getCity()) && TextUtils.isEmpty(user.getProvince()))
                            {
                                mBind.tvLocation.setText(getStringWithoutContext(R.string.mars));
                            }
                            else
                            {
                                if(!TextUtils.isEmpty(user.getProvince()))
                                {
                                   sb.append(user.getProvince());
                                }
                                if(!TextUtils.isEmpty(user.getCity()))
                                {
                                    sb.append("-").append(user.getCity());
                                }
                                mBind.tvLocation.setText(sb.toString());
                            }

                            if(TextUtils.isEmpty(user.getSignature()))
                            {
                                mBind.tvSignature.setText(getStringWithoutContext(R.string.tips9));
                            }
                            else
                            {
                                mBind.tvSignature.setText(user.getSignature());
                            }

                            if(user.getFans()!=null)
                            {
                                mBind.tvFollowNum.setText(user.getFollows()+"");
                            }

                            if(user.getFollows()!=null)
                            {
                                mBind.tvFansnum.setText(user.getFans()+"");
                            }

                            SpanUtils icons=new SpanUtils();
                            if(user.getUserLevel()!=null)
                            {
                                ChatSpanUtils.appendLevelIcon(icons,user.getUserLevel(),getContext());
                            }

                            mBind.tvSmallLogo.setText(icons.create());

                        }
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

    }

    private void follow(String targetId)
    {
        mBind.tv3.setEnabled(false);
        Api_User.ins().followUser(targetId, true, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(isConditionOk())
                {
                    mBind.tv3.setEnabled(true);
                    if(code==0)
                    {
                        mBind.tv3.setEnabled(false);
                        mBind.tv3.setTextColor(0xffA2A0A9);
                        mBind.tv3.setText(getStringWithoutContext(R.string.concerned));
                        if(buttonClickListener!=null)
                        {
                            buttonClickListener.onClick(targetId,true,false);
                        }
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }

    public interface ButtonClickListener
    {
        void onClick(String uid,boolean follow,boolean tagSomeone);
    }
}
