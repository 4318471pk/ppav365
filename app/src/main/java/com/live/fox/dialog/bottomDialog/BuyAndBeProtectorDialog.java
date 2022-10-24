package com.live.fox.dialog.bottomDialog;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogBuyBeprotectorBinding;
import com.live.fox.utils.device.ScreenUtils;

public class BuyAndBeProtectorDialog extends BaseBindingDialogFragment {

    DialogBuyBeprotectorBinding mBind;

    public static BuyAndBeProtectorDialog getInstance()
    {
        return new BuyAndBeProtectorDialog();
    }

    @Override
    public void onClickView(View view) {

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
        }

        //0.14 item lr 0.115
        int itemWidth=(int)(screenHeight*0.14f);
        int marginWidth=(int)(screenHeight*0.115f);
        int itemMargin=(int)(screenHeight*0.35f)/3;

        for (int i = 0; i <accessStrs.length ; i++) {
            View item=View.inflate(getActivity(),R.layout.item_buy_protector_dialog,null);
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setMargins(i>0?itemMargin:marginWidth,0,i==accessStrs.length?marginWidth:0,0);

            ImageView ivItemBG=item.findViewById(R.id.ivItemBG);
            ImageView ivItemLogo=item.findViewById(R.id.ivItemLogo);
            TextView tvPrivilegeTitle=item.findViewById(R.id.tvPrivilegeTitle);
            tvPrivilegeTitle.setText(accessStrs[i]);

            mBind.llLogos.addView(item,ll);
        }

        view.setVisibility(View.VISIBLE);
    }
}
