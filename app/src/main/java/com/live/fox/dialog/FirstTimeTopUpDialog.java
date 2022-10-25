package com.live.fox.dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogFirsttimeTopupBinding;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.ImageUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;

import org.jetbrains.annotations.NotNull;

public class FirstTimeTopUpDialog extends BaseBindingDialogFragment {

    DialogFirsttimeTopupBinding mBind;

    public static FirstTimeTopUpDialog getInstance() {
        return new FirstTimeTopUpDialog();
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
        return R.layout.dialog_firsttime_topup;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);

        int screenWidth = ScreenUtils.getScreenWidth(getActivity());
        FixImageSize.setImageSizeOnWidthWithSRC(mBind.ivContent, screenWidth, new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {


                RelativeLayout.LayoutParams rlContent = (RelativeLayout.LayoutParams) mBind.rllContent.getLayoutParams();
                rlContent.height = (int) (height * 0.47f);
                rlContent.width = (int) (screenWidth * 0.79f);
                rlContent.topMargin = (int) (height * 0.41f);
                mBind.rllContent.setLayoutParams(rlContent);

                mBind.llItemList.getLayoutParams().height = (int) (rlContent.height * 0.554f);

                RelativeLayout.LayoutParams rlGoCharge = (RelativeLayout.LayoutParams) mBind.ivGoCharge.getLayoutParams();
                rlGoCharge.width = (int) (screenWidth * 0.64f);
                rlGoCharge.height = (int) (height * 0.1f);
                mBind.ivGoCharge.setLayoutParams(rlGoCharge);

                int dip7_5 = ScreenUtils.dp2px(getActivity(), 7.5f);
                int itemWidth = (int) (rlContent.width * 0.3f);
                for (int i = 0; i < 3; i++) {
                    View itemView = View.inflate(getActivity(), R.layout.item_firsttime_topup, null);
                    LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                    ll.leftMargin = dip7_5;
                    itemView.setLayoutParams(ll);
                    mBind.llItemList.addView(itemView);
                }

                view.setVisibility(View.VISIBLE);
            }
        });

        SpanUtils spanUtils = new SpanUtils();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_diamond);
        int width = ScreenUtils.dp2px(getActivity(), 16);
        int height = ScreenUtils.dp2px(getActivity(), 12.5f);
        spanUtils.appendImage(ImageUtils.scale(bitmap, width, height), SpanUtils.ALIGN_CENTER);
        spanUtils.append(" 388钻 + 特惠大礼包");
        mBind.tvTips.setText(spanUtils.create());

        String strs[] = new String[]{"50元", "100元", "200元", "500元"};
        for (int i = 0; i < mBind.rrgAmount.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mBind.rrgAmount.getChildAt(i);
            radioButton.setText(strs[i]);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        buttonView.setBackground(new ColorDrawable(0));
                    } else {
                        buttonView.setBackground(getResources().getDrawable(R.drawable.radio_79a0ff_aa0af2));
                    }
                }
            });
        }
        RadioButton radioButton = (RadioButton) mBind.rrgAmount.getChildAt(0);
        radioButton.setChecked(true);
    }
}
