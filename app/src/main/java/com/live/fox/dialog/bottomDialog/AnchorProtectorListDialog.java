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
import com.live.fox.server.Api_Live;
import com.live.fox.ui.living.LivingControlPanel;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.device.ScreenUtils;
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

    public static AnchorProtectorListDialog getInstance(String uid,String liveId,AnchorGuardListBean anchorGuardListBean)
    {
        AnchorProtectorListDialog anchorProtectorListDialog=new AnchorProtectorListDialog();
        anchorProtectorListDialog.uid=uid;
        anchorProtectorListDialog.liveId=liveId;
        anchorProtectorListDialog.anchorGuardListBean=anchorGuardListBean;
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
                        BuyAndBeProtectorDialog dialog=BuyAndBeProtectorDialog.getInstance(uid,liveId);
                        FragmentManager fragmentManager=getParentFragmentManager();
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
        mBind.rllContent.getLayoutParams().height=(int)(screenHeight*0.7f);
        mBind.anchorProtect.getLayoutParams().height=(int)(screenHeight*0.35f);
        mBind.anchorProtect.setDecorationIndex(5);

        Drawable drawable=mBind.ivBeMyProtector.getBackground();
        int dip31=ScreenUtils.getDip2px(getActivity(),31);
        int width=drawable.getIntrinsicWidth()*dip31/drawable.getIntrinsicHeight();
        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) mBind.ivBeMyProtector.getLayoutParams();
        rl.width=width;
        rl.height=dip31;
        rl.topMargin=dip31/31*2;
        rl.bottomMargin=dip31/31*2;
        rl.rightMargin=dip31/31*2;
        mBind.ivBeMyProtector.setLayoutParams(rl);
        view.setVisibility(View.VISIBLE);

        List<AnchorGuardListBean.LiveGuardBean> list=new ArrayList<>();
        if(anchorGuardListBean!=null && anchorGuardListBean.getLiveGuardList()!=null)
        {
            list.addAll(anchorGuardListBean.getLiveGuardList());
        }
        adapter=new AnchorProtectorAdapter(getActivity(),list);
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(width,ScreenUtils.getDip2px(getActivity(),20)));
        adapter.addHeaderView(linearLayout);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rvMain.setLayoutManager(linearLayoutManager);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getActivity(),5)));
        mBind.rvMain.setAdapter(adapter);

        startAnimate(mBind.rllContent,true);

        getGuardList();
    }


    private void getGuardList()
    {
        if(!isConditionOk())
        {
            return;
        }

        Api_Live.ins().queryGuardListByAnchor(liveId, uid, new JsonCallback<AnchorGuardListBean>() {
            @Override
            public void onSuccess(int code, String msg, AnchorGuardListBean data) {
                if(code==0)
                {
                    if(isConditionOk() && getArg().equals(liveId) && data!=null)
                    {
                        adapter.setNewData(anchorGuardListBean.getLiveGuardList());
                        if(onRefreshDataListener!=null)
                        {
                            onRefreshDataListener.onRefresh(data);
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
