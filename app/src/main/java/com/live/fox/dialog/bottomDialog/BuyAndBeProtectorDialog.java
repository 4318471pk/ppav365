package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogBuyBeprotectorBinding;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BuyAndBeProtectorDialog extends BaseBindingDialogFragment {

    DialogBuyBeprotectorBinding mBind;
    List<RelativeLayout> itemsRL=new ArrayList<>();
    List<ImageView> imageViews=new ArrayList<>();
    int position;


    public static BuyAndBeProtectorDialog getInstance()
    {
        return new BuyAndBeProtectorDialog();
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
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.rllContent,false);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_buy_beprotector;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);
        int screenHeight= ScreenUtils.getScreenHeight(getActivity());
        int screenWidth=ScreenUtils.getScreenWidth(getActivity());
        mBind.rllContent.getLayoutParams().height=(int)(screenHeight*0.5f);
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

        String strs[]=getActivity().getResources().getStringArray(R.array.buyProtectorTitles);
        String accessStrs[]=getActivity().getResources().getStringArray(R.array.buyProtectorAccessTitles);
        int drawables[]={R.mipmap.icon_privilege1,R.mipmap.icon_privilege2,R.mipmap.icon_privilege3,R.mipmap.icon_privilege4};

        for (int i = 0; i <mBind.llContentList.getChildCount(); i++) {
            RelativeLayout relativeLayout=(RelativeLayout)mBind.llContentList.getChildAt(i);
            LinearLayout linearLayout=(LinearLayout)relativeLayout.getChildAt(0);
            TextView title=(TextView) linearLayout.getChildAt(1);
            title.setText(strs[i]);

            TextView price=(TextView) linearLayout.getChildAt(2);
            price.setText("999");
            itemsRL.add(relativeLayout);
            relativeLayout.setTag(i);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < itemsRL.size(); j++) {
                        itemsRL.get(j).setSelected(false);
                    }
                    v.setSelected(true);
                    position=(int)v.getTag();
                    onSelectedItems(position);
                }
            });
        }

        //0.14 item lr 0.115
        int itemWidth=(int)(screenWidth*0.14f);
        int marginWidth=(int)(screenWidth*0.115f);
        int itemMargin=(int)(screenWidth*0.21f)/3;


        for (int i = 0; i <accessStrs.length ; i++) {
            View item=View.inflate(getActivity(),R.layout.item_buy_protector_dialog,null);
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setMargins(i>0?itemMargin:marginWidth,0,i==accessStrs.length?marginWidth:0,0);

            RelativeLayout relativeLayout=item.findViewById(R.id.rlContent);
            LinearLayout.LayoutParams llContent=(LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
            llContent.height=itemWidth;
            llContent.width=itemWidth;
            relativeLayout.setLayoutParams(llContent);

            ImageView ivItemBG=item.findViewById(R.id.ivItemBG);
            ImageView ivItemLogo=item.findViewById(R.id.ivItemLogo);
            imageViews.add(ivItemBG);
            RelativeLayout.LayoutParams rlLogo=(RelativeLayout.LayoutParams) ivItemLogo.getLayoutParams();
            rlLogo.width=(int)(itemWidth*0.5f);
            rlLogo.height=(int)(itemWidth*0.5f);
            ivItemLogo.setLayoutParams(rlLogo);
            TextView tvPrivilegeTitle=item.findViewById(R.id.tvPrivilegeTitle);
            tvPrivilegeTitle.setText(accessStrs[i]);

            ivItemBG.setImageDrawable(getResources().getDrawable(R.drawable.circle_ff77fa_ff65bf));
            ivItemLogo.setImageDrawable(getResources().getDrawable(drawables[i]));

            mBind.llLogos.addView(item,ll);
        }
        view.setVisibility(View.VISIBLE);

        itemsRL.get(0).performClick();

        startAnimate(mBind.rllContent,true);
    }

    private void onSelectedItems(int position)
    {
        Drawable selectedBG=getResources().getDrawable(R.drawable.circle_ff77fa_ff65bf);
        Drawable unSelectedBG=getResources().getDrawable(R.drawable.circle_ceaac7);
        switch (position)
        {
            case 0:
                imageViews.get(imageViews.size()-1).setImageDrawable(unSelectedBG);
                imageViews.get(imageViews.size()-2).setImageDrawable(unSelectedBG);
                break;
            case 1:
                imageViews.get(imageViews.size()-1).setImageDrawable(unSelectedBG);
                imageViews.get(imageViews.size()-2).setImageDrawable(selectedBG);
                break;
            case 2:
                imageViews.get(imageViews.size()-1).setImageDrawable(selectedBG);
                imageViews.get(imageViews.size()-2).setImageDrawable(selectedBG);
                break;
        }
    }
}
