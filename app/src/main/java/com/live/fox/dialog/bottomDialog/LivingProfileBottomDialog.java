package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.BotDialogLivingProfileBinding;
import com.live.fox.dialog.temple.LivingInterruptDialog;
import com.live.fox.manager.DataCenter;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankProfileView;

import org.jetbrains.annotations.NotNull;

public class LivingProfileBottomDialog extends BaseBindingDialogFragment {

    public static final int Audience=0;//用户端直播间
    public static final int AudienceInAnchorRoom=1;//主播端直播间 的用户
    public static final int AnchorSelf=2;//主播端直播间 主播自己

    BotDialogLivingProfileBinding mBind;
    int mode;

    public static LivingProfileBottomDialog getInstance(int mode)
    {
        LivingProfileBottomDialog dialog=new LivingProfileBottomDialog();
        dialog.mode=mode;
        return dialog;
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
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragmentManager,ReportAnchorDialog.getInstance());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case R.id.tv1:
                Long uid=DataCenter.getInstance().getUserInfo().getUser().getUid();
                if(uid!=null)
                {
                    UserDetailActivity.startActivity(requireActivity(), uid);
                }
                break;
            case R.id.tv2:
                break;
            case R.id.tv3:
                switch (mode)
                {
                    case Audience:

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
                mBind.tv3.setText(getResources().getString(R.string.follow2));
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

        mBind.rpv.setIndex(RankProfileView.NONE,0);
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

        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(10, getActivity()));
        mBind.tvSmallLogo.setText(spanUtils.create());

        startAnimate(mBind.rlContent,true);
    }


}
