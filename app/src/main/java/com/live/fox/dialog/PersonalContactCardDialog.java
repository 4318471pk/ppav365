package com.live.fox.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogPersonalContactCardBinding;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

public class PersonalContactCardDialog extends BaseBindingDialogFragment {

    DialogPersonalContactCardBinding mBind;

    public static PersonalContactCardDialog getInstance()
    {
        return new PersonalContactCardDialog();
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
            case R.id.ivClose:
                dismissAllowingStateLoss();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_personal_contact_card;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);
        int screenWidth= ScreenUtils.getScreenWidth(getActivity());
        FixImageSize.setImageSizeOnWidthWithSRC(mBind.ivBG, (int)(screenWidth * 0.75f), new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {
               RelativeLayout.LayoutParams rlContent= (RelativeLayout.LayoutParams)mBind.llContent.getLayoutParams();
                rlContent.width=(int)(width*0.9f);
                mBind.llContent.setLayoutParams(rlContent);

                RelativeLayout.LayoutParams rlRound= (RelativeLayout.LayoutParams) mBind.rlRound.getLayoutParams();
                rlRound.width=(int)(width*0.3f);
                rlRound.height=(int)(width*0.3f);
                mBind.rlRound.setLayoutParams(rlRound);

                int marginTB=(int)(height*0.058f);
                LinearLayout.LayoutParams llProgress=(LinearLayout.LayoutParams) mBind.tvProgress.getLayoutParams();
                llProgress.topMargin=marginTB;
                mBind.tvProgress.setLayoutParams(llProgress);

                int imgWH=(int)(width*0.114f);
                for (int i = 0; i <3 ; i++) {
                    ViewGroup viewGroup=(ViewGroup) mBind.llContent.getChildAt(0);
                    if(viewGroup.getChildAt(0) instanceof ImageView)
                    {
                        ViewGroup.LayoutParams vl= viewGroup.getChildAt(0).getLayoutParams();
                        vl.width=imgWH;
                        vl.height=imgWH;
                        viewGroup.getChildAt(0).setLayoutParams(vl);
                    }
                }

                mBind.contactProgress.setProgress(5f);
                LinearLayout.LayoutParams llContactProgress=(LinearLayout.LayoutParams) mBind.contactProgress.getLayoutParams();
                llContactProgress.bottomMargin=marginTB;
                llContactProgress.width=(int)(width*0.79f);
                mBind.contactProgress.setLayoutParams(llContactProgress);

                mBind.tvTips.getLayoutParams().width=(int)(width*0.79f);
                mBind.tvTitle1.setTextSize(TypedValue.COMPLEX_UNIT_PX,0.296f*width*28/166);
                float textSize=0.5125f*width*23/287;
                mBind.tvTitle2.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                mBind.tvTips.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                LinearLayout.LayoutParams llTips=(LinearLayout.LayoutParams) mBind.tvTips.getLayoutParams();
                llTips.bottomMargin=(int)(height*0.1f);
                mBind.tvTips.setLayoutParams(llTips);
                view.setVisibility(View.VISIBLE);
            }
        });

    }
}
