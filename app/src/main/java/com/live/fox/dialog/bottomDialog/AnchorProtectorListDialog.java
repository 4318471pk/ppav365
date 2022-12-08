package com.live.fox.dialog.bottomDialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.AnchorProtectorAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogAnchorlistProtectorBinding;
import com.live.fox.entity.AnchorGuardListBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.living.LivingControlPanel;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.Strings;
import com.live.fox.view.myHeader.MyWaterDropHeader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AnchorProtectorListDialog extends BaseBindingDialogFragment {

    DialogAnchorlistProtectorBinding mBind;
    AnchorProtectorAdapter adapter;
    String uid,liveId;
    AnchorGuardListBean anchorGuardListBean;
    OnRefreshDataListener onRefreshDataListener;
    AnchorGuardListBean.LiveGuardBean self;
    boolean isAnchor;

    public static AnchorProtectorListDialog getInstance(String uid,String liveId,AnchorGuardListBean anchorGuardListBean,boolean isAnchor)
    {
        AnchorProtectorListDialog anchorProtectorListDialog=new AnchorProtectorListDialog();
        anchorProtectorListDialog.uid=uid;
        anchorProtectorListDialog.liveId=liveId;
        anchorProtectorListDialog.anchorGuardListBean=anchorGuardListBean;
        anchorProtectorListDialog.isAnchor=isAnchor;
        return anchorProtectorListDialog;
    }

    public void setOnRefreshDataListener(OnRefreshDataListener onRefreshDataListener) {
        this.onRefreshDataListener = onRefreshDataListener;
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
    public boolean onBackPress() {
        startAnimate(mBind.rllContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        if(ClickUtil.isClickWithShortTime(view.getId(),1000))
        {
            return;
        }
        switch (view.getId())
        {
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.rllContent,false);
                break;
            case R.id.introdution:
                break;
            case R.id.ivBeMyProtector:
                startAnimate(mBind.rllContent, false, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        BuyAndBeProtectorDialog dialog=BuyAndBeProtectorDialog.getInstance(uid,liveId,self);
                        FragmentManager fragmentManager=getFragmentManager();
                        if(fragmentManager==null)
                        {
                            fragmentManager=getActivity().getSupportFragmentManager();
                        }
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragmentManager,dialog);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_anchorlist_protector;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);
        int screenHeight= ScreenUtils.getScreenHeight(getActivity());
        int screenWidth=ScreenUtils.getScreenWidth(getActivity());
        mBind.rllContent.getLayoutParams().height=(int)(screenHeight*0.7f);
        mBind.anchorProtect.getLayoutParams().width=(int)(screenWidth*0.35f);

        Drawable drawable=mBind.ivBeMyProtector.getBackground();
        int dip31=ScreenUtils.dp2px(getActivity(),31);
        int width=drawable.getIntrinsicWidth()*dip31/drawable.getIntrinsicHeight();
        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) mBind.ivBeMyProtector.getLayoutParams();
        rl.width=width;
        rl.height=dip31;
        rl.topMargin=dip31/31*2;
        rl.bottomMargin=dip31/31*2;
        rl.rightMargin=dip31/31*2;
        mBind.ivBeMyProtector.setLayoutParams(rl);
        view.setVisibility(View.VISIBLE);


        if(isAnchor){
            mBind.ivBeMyProtector.setVisibility(View.GONE);
        }

        List<AnchorGuardListBean.LiveGuardBean> list=new ArrayList<>();
        if(anchorGuardListBean!=null && anchorGuardListBean.getLiveGuardList()!=null && anchorGuardListBean.getLiveGuardList().size()>0)
        {
            setTopView();

            String myUid=String.valueOf(DataCenter.getInstance().getUserInfo().getUser().getUid());
            boolean isContain=false;
            for (int i = 0; i <anchorGuardListBean.getLiveGuardList().size() ; i++) {
                if(i>0)
                {
                    list.add(anchorGuardListBean.getLiveGuardList().get(i));
                }
                if (anchorGuardListBean.getLiveGuardList().get(i).getUid().equals(myUid)) {
                   isContain=true;
                }
            }
//            if(isContain)
//            {
//                mBind.rlFloating.setVisibility(View.INVISIBLE);
//            }
        }
        adapter=new AnchorProtectorAdapter(getActivity(),list);
        if(anchorGuardListBean!=null && anchorGuardListBean.getGuardCount()>0)
        {
            StringBuilder sb=new StringBuilder();
            sb.append(getStringWithoutContext(R.string.protect)).append("(").append(anchorGuardListBean.getGuardCount()).append(")");
            mBind.tvDialogTitle.setText(sb.toString());
        }
        else
        {
            mBind.tvDialogTitle.setText(getStringWithoutContext(R.string.protect));
        }
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(width,ScreenUtils.dp2px(getActivity(),20)));
        adapter.addHeaderView(linearLayout);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rvMain.setLayoutManager(linearLayoutManager);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(ScreenUtils.dp2px(getActivity(),5)));
        mBind.rvMain.setAdapter(adapter);

        startAnimate(mBind.rllContent,true);

        getGuardList();
    }

    private void setTopView()
    {
        if (anchorGuardListBean.getLiveGuardList().size() > 0)
        {
            AnchorGuardListBean.LiveGuardBean liveGuardBean=anchorGuardListBean.getLiveGuardList().get(0);
            String tips=getResources().getString(R.string.tip11);
            mBind.tvTitle.setText(String.format(tips,liveGuardBean.getWeekUpAmount()+""));

            SpanUtils spanUtils=new SpanUtils();
            if(ChatSpanUtils.appendLevelIcon(spanUtils,liveGuardBean.getUserLevel(),getActivity()))
            {
                spanUtils.append(" ");
            }
            if(ChatSpanUtils.appendVipLevelRectangleIcon(spanUtils,liveGuardBean.getVipLevel(),getActivity()))
            {
                spanUtils.append(" ");
            }
            if(Strings.isDigitOnly(liveGuardBean.getGuardLevel()))
            {
                if(ChatSpanUtils.appendGuardIcon(spanUtils,Integer.valueOf(liveGuardBean.getGuardLevel()),getActivity()))
                {
                    spanUtils.append(" ");
                }
            }
            mBind.tvIcons.setText(spanUtils.create());
            mBind.tvNickName.setText(liveGuardBean.getNickname());
            mBind.anchorProtect.setDecorationIndex(liveGuardBean.getVipLevel());
            GlideUtils.loadCircleImage(getActivity(),liveGuardBean.getAvatar(),
                    0,0,mBind.anchorProtect.getIvProfile());

        }else
        {
            mBind.tvTitle.setText(getResources().getString(R.string.protectTag2));
            mBind.tvNickName.setText("");
            mBind.tvIcons.setText("");
        }
    }

    private void getGuardList()
    {
        if(!isConditionOk())
        {
            return;
        }

        String selfUid=DataCenter.getInstance().getUserInfo().getUser().getUid()+"";
        Api_Live.ins().queryGuardListByAnchor(liveId, uid, new JsonCallback<AnchorGuardListBean>() {
            @Override
            public void onSuccess(int code, String msg, AnchorGuardListBean data) {
                if(code==0)
                {
                    if(isConditionOk() && getArg().equals(liveId) && data!=null) {
                        if (onRefreshDataListener != null) {
                            onRefreshDataListener.onRefresh(data);
                        }
                        anchorGuardListBean=data;
                        setTopView();
                        List<AnchorGuardListBean.LiveGuardBean> list=new ArrayList<>();

                        for (int i = 0; i < data.getLiveGuardList().size(); i++) {
                            if (data.getLiveGuardList().get(i).getUid().equals(selfUid)) {
                                self=data.getLiveGuardList().get(i);
                            }
                            if(i>0)
                            {
                                list.add(anchorGuardListBean.getLiveGuardList().get(i));
                            }
                        }

                        adapter.setNewData(list);
                        if(list.size()>0)
                        {
                            StringBuilder sb=new StringBuilder();
                            sb.append(getStringWithoutContext(R.string.protect)).append("(").append(anchorGuardListBean.getGuardCount()).append(")");
                            mBind.tvDialogTitle.setText(sb.toString());
                        }
                        else
                        {
                            mBind.tvDialogTitle.setText(getStringWithoutContext(R.string.protect));
                        }
                    }
                }
                else
                {

                }
            }
        });
    }

    public interface OnRefreshDataListener
    {
        void onRefresh(AnchorGuardListBean anchorGuardListBean);
    }
}
