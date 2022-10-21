package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogAnchorlistProtectorBinding;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

public class AnchorProtectorListDialog extends BaseBindingDialogFragment {

    DialogAnchorlistProtectorBinding mBind;

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
        mBind.ivBeMyProtector.setLayoutParams(rl);
        view.setVisibility(View.VISIBLE);


    }


}
