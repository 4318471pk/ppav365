package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.AnchorProtectorAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogAnchorlistProtectorBinding;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AnchorProtectorListDialog extends BaseBindingDialogFragment {

    DialogAnchorlistProtectorBinding mBind;
    AnchorProtectorAdapter adapter;

    public static AnchorProtectorListDialog getInstance()
    {
        return new AnchorProtectorListDialog();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                startAnimate(mBind.rllContent,false);
                break;
            case R.id.introdution:
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

        List<String> strings=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add(" ");
        }

        adapter=new AnchorProtectorAdapter(getActivity(),strings);
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(width,ScreenUtils.getDip2px(getActivity(),20)));
        adapter.addHeaderView(linearLayout);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rvMain.setLayoutManager(linearLayoutManager);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getActivity(),5)));
        mBind.rvMain.setAdapter(adapter);

        startAnimate(mBind.rllContent,true);
    }


}
