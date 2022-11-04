package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogLivingEditGametypeBinding;
import com.live.fox.utils.device.ScreenUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

public class EditLivingGameTypeDialog extends BaseBindingDialogFragment {

    DialogLivingEditGametypeBinding mBind;

    public static EditLivingGameTypeDialog getInstance() {
        return new EditLivingGameTypeDialog();
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                startAnimate(mBind.rllContent,false);
                break;
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
        setWindowsFlag();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_living_edit_gametype;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);
        setWindowsFlag();

        addTabs();
        startAnimate(mBind.rllContent,true);
    }

    private void addTabs() {
        int screenWidth = ScreenUtils.getScreenWidth(getActivity());
        int itemWidth = (int) (screenWidth * 0.2f);
        int dip2 = ScreenUtils.getDip2px(getActivity(), 2);
        for (int i = 0; i < 10; i++) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView title = new TextView(getActivity());
            title.setTextColor(0xffffffff);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            title.setText("极速快三");
            title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(title);

            RelativeLayout relativeLayout = new RelativeLayout(getActivity());
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(itemWidth , itemWidth );
            rl.topMargin=dip2*3;
            relativeLayout.setLayoutParams(rl);
            relativeLayout.setPadding(dip2, dip2, dip2, dip2);
            relativeLayout.setId(R.id.gameTypeImg);

            RoundedImageView roundedImageView = new RoundedImageView(getActivity());
            roundedImageView.setCornerRadius(dip2 * 5);
            roundedImageView.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth - dip2, itemWidth - dip2));
            roundedImageView.setImageDrawable(getResources().getDrawable(R.drawable.img_default));
            relativeLayout.addView(roundedImageView);

            linearLayout.addView(relativeLayout);

            mBind.tabLayout.addTab(mBind.tabLayout.newTab().setCustomView(linearLayout));
        }

        mBind.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RelativeLayout relativeLayout = tab.getCustomView().findViewById(R.id.gameTypeImg);
                relativeLayout.setBackground(getResources().getDrawable(R.drawable.bg_storke_ff008a_2dip));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                RelativeLayout relativeLayout = tab.getCustomView().findViewById(R.id.gameTypeImg);
                relativeLayout.setBackground(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                RelativeLayout relativeLayout = tab.getCustomView().findViewById(R.id.gameTypeImg);
                relativeLayout.setBackground(getResources().getDrawable(R.drawable.bg_storke_ff008a_2dip));
            }
        });
    }


}
