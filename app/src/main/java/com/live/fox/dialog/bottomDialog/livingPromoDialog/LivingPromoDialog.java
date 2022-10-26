package com.live.fox.dialog.bottomDialog.livingPromoDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogLivingPromoBinding;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LivingPromoDialog extends BaseBindingDialogFragment {

    DialogLivingPromoBinding mBind;
    List<String> strings=new ArrayList<>();

    public static LivingPromoDialog getInstance()
    {
        return new LivingPromoDialog();
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
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_living_promo;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);
        String str1=getResources().getString(R.string.game_activity);
        String str2=getResources().getString(R.string.living_activity);
        strings.add(str1);
        strings.add(str2);

        int widthScreen= ScreenUtils.getScreenWidth(getActivity());
        int heightScreen=ScreenUtils.getScreenHeight(getActivity());

        mBind.rllContent.getLayoutParams().height=(int)(heightScreen*0.7f);
        mBind.tabLayout.setGradient(0xffA800FF,0xffEA00FF);
        mBind.vpMain.setAdapter(new BaseFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getFragment(int position) {
                switch (position)
                {
                    case 0:
                        return GamePromoFragment.getInstance();
                    case 1:
                        return LivingPromoFragment.getInstance();
                }
                return null;
            }

            @Override
            public String getTitle(int position) {
                return strings.get(position);
            }

            @Override
            public int getItemCount() {
                return strings.size();
            }
        });

        mBind.tabLayout.setTabWidthPX(widthScreen/2);
        mBind.tabLayout.setViewPager(mBind.vpMain);
        view.setVisibility(View.VISIBLE);

        startAnimate(mBind.rllContent,true);
    }
}
