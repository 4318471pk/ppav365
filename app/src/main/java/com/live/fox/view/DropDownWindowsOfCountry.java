package com.live.fox.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.entity.CountryCode;
import com.live.fox.utils.DensityUtils;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.List;


public class DropDownWindowsOfCountry extends PopupWindow {

    int viewWidth,viewHeight;
    LinearLayout linearLayout;
    OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public DropDownWindowsOfCountry(Context context,int width) {
        super(context);
        viewHeight= (int)(ScreenUtils.getScreenHeight(context)*0.4f);
        viewWidth=width;
        linearLayout=new LinearLayout(context);
        linearLayout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollView=new ScrollView(context);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.setFillViewport(true);
        scrollView.setBackground(context.getResources().getDrawable(R.drawable.retangle_round_white));
        scrollView.addView(linearLayout);
        setContentView(scrollView);

        setWidth(viewWidth);
        setHeight(viewHeight);

    }


    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }

    @Override
    public void dismiss() {
        if(getContentView().getContext() instanceof BaseActivity)
        {
            BaseActivity baseActivity=(BaseActivity) getContentView().getContext();
            if(!baseActivity.isFinishing() && !baseActivity.isDestroyed())
            {
                super.dismiss();
            }
        }
    }

    public void setData(List<CountryCode> countryCodes)
    {
        linearLayout.removeAllViews();
        int padding2= DensityUtils.dp2px(linearLayout.getContext(),2);
        for (int i = 0; i < countryCodes.size(); i++) {
            CountryCode countryCode=countryCodes.get(i);
            RelativeLayout rlItem=new RelativeLayout(linearLayout.getContext());
            rlItem.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(rlItem);

            TextView country=new TextView(linearLayout.getContext());
            country.setText(countryCode.getCityCode());
            country.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            country.setPadding(padding2*8,padding2*5,padding2*8,padding2*5);
            country.setTextColor(0xffbbbbbb);
            country.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            rlItem.addView(country);

            TextView countrySuffix=new TextView(linearLayout.getContext());
            countrySuffix.setText(countryCode.getAreaCode());
            RelativeLayout.LayoutParams rlSuffix= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rlSuffix.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            countrySuffix.setLayoutParams(rlSuffix);
            countrySuffix.setPadding(padding2*8,padding2*5,padding2*8,padding2*5);
            countrySuffix.setTextColor(0xffbbbbbb);
            countrySuffix.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            rlItem.addView(countrySuffix);
            rlItem.setTag(countryCode);

            rlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItemListener.onClickItemListener((CountryCode)view.getTag());
                }
            });

        }

    }

    public interface OnClickItemListener
    {
        void onClickItemListener(CountryCode countryCode);
    }
}
