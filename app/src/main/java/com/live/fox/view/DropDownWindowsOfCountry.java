package com.live.fox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.luck.picture.lib.tools.ScreenUtils;


public class DropDownWindowsOfCountry extends PopupWindow {

    int viewWidth,viewHeight;
    LinearLayout linearLayout;
    public DropDownWindowsOfCountry(Context context) {
        super(context);
        viewHeight= (int)(ScreenUtils.getScreenHeight(context)*0.4f);
        viewWidth=ScreenUtils.getScreenWidth(context)- ScreenUtils.dip2px(context,140);
        linearLayout=new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setBackground(context.getResources().getDrawable(R.drawable.retangle_round_white));

        setContentView(linearLayout);
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
}
