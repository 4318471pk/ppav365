package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogBuyBeprotectorBinding;
import com.live.fox.dialog.temple.TempleDialog2;
import com.live.fox.entity.AvailableGuardBean;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Order;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BuyAndBeProtectorDialog extends BaseBindingDialogFragment {

    DialogBuyBeprotectorBinding mBind;
    List<RelativeLayout> itemsRL=new ArrayList<>();
    List<ImageView> imageViews=new ArrayList<>();
    String uid,liveId;
    int selectedPosition=0;


    public static BuyAndBeProtectorDialog getInstance(String uid,String liveId)
    {
        BuyAndBeProtectorDialog buyAndBeProtectorDialog=new BuyAndBeProtectorDialog();
        buyAndBeProtectorDialog.uid=uid;
        buyAndBeProtectorDialog.liveId=liveId;
        return buyAndBeProtectorDialog;
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
    public boolean onBackPress() {
        startAnimate(mBind.rllContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.rllContent,false);
                break;
            case R.id.gtvExchangeDiamond:
                RechargeActivity.startActivity(requireActivity(), false);
                break;
            case R.id.ivBeMyProtector:
                for (int i = 0; i <itemsRL.size() ; i++) {
                    if(itemsRL.get(i).isSelected())
                    {
                        AvailableGuardBean bean=(AvailableGuardBean)itemsRL.get(i).getTag();
                        openGuard(bean);
                        break;
                    }
                }
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

        getAvailableGuardList();
        view.setVisibility(View.GONE);

        User user= DataCenter.getInstance().getUserInfo().getUser();
        if(user.getGold()!=null)
        {
            mBind.tvBalance.setText(getStringWithoutContext(R.string.balance2));
            mBind.tvBalance.append(user.getGold(0.0f).toPlainString());
        }
        if(user.getDiamond()!=null)
        {
            mBind.tvDiamond.setText(getStringWithoutContext(R.string.diamond2));
            mBind.tvDiamond.append(user.getDiamond("0.0").toPlainString());
        }

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

        startAnimate(mBind.rllContent,true);
    }

    private void onSelectedItems(AvailableGuardBean bean)
    {
        Drawable selectedBG=getResources().getDrawable(R.drawable.circle_ff77fa_ff65bf);
        Drawable unSelectedBG=getResources().getDrawable(R.drawable.circle_ceaac7);
        switch (bean.getGuardLevel())
        {
            case 1:
                imageViews.get(imageViews.size()-1).setImageDrawable(unSelectedBG);
                imageViews.get(imageViews.size()-2).setImageDrawable(unSelectedBG);
                break;
            case 2:
                imageViews.get(imageViews.size()-1).setImageDrawable(unSelectedBG);
                imageViews.get(imageViews.size()-2).setImageDrawable(selectedBG);
                break;
            case 3:
                imageViews.get(imageViews.size()-1).setImageDrawable(selectedBG);
                imageViews.get(imageViews.size()-2).setImageDrawable(selectedBG);
                break;
        }
    }

    private void getAvailableGuardList()
    {
        int dip10=ScreenUtils.getDip2px(getContext(),10);

        int width=(ScreenUtils.getScreenWidth(getContext())-dip10*4)/3;
        Api_Order.ins().buyAvailableGuard(new JsonCallback<List<AvailableGuardBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<AvailableGuardBean> data) {
                if(code==0)
                {
                    if(isConditionOk() && data!=null)
                    {
                        for (int i = 0; i <data.size() ; i++) {
                            AvailableGuardBean bean=data.get(i);
                            View view=View.inflate(getContext(),R.layout.item_guard_available,null);
                            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(width,dip10*12);
                            ll.leftMargin=dip10;

                            TextView tvCostDiamond=view.findViewById(R.id.tvCostDiamond);
                            TextView tvName=view.findViewById(R.id.tvName);
                            ImageView ivGuard=view.findViewById(R.id.ivGuard);
                            LinearLayout.LayoutParams llPic=(LinearLayout.LayoutParams)ivGuard.getLayoutParams();
                            llPic.width=(int)(width*0.58f);
                            ivGuard.setLayoutParams(llPic);

                            tvName.setText(bean.getName());
                            tvCostDiamond.setText(bean.getOpenPrice()+"");
                            GlideUtils.loadDefaultImage(getContext(),bean.getImgUrl(),0,ivGuard);
                            view.setTag(bean);
                            mBind.llContentList.addView(view,ll);
                        }

                        for (int i = 0; i <mBind.llContentList.getChildCount(); i++) {
                            RelativeLayout relativeLayout=(RelativeLayout)mBind.llContentList.getChildAt(i);

                            itemsRL.add(relativeLayout);
                            relativeLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (int j = 0; j < itemsRL.size(); j++) {
                                        itemsRL.get(j).setSelected(false);
                                    }
                                    v.setSelected(true);
                                    AvailableGuardBean bean=(AvailableGuardBean)v.getTag();
                                    onSelectedItems(bean);
                                }
                            });
                        }

                        if(mBind.llContentList.getChildCount()>0)
                        {
                            itemsRL.get(0).performClick();
                        }
                    }
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void openGuard(AvailableGuardBean bean)
    {
        User user=DataCenter.getInstance().getUserInfo().getUser();
        if(user.getDiamond("0").compareTo(new BigDecimal(bean.getOpenPrice()))>=0)
        {
            Api_Order.ins().buyGuard(uid,liveId, bean, new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    if(isConditionOk() && code==0)
                    {
                        ToastUtils.showShort(R.string.operateSuccess);
                        dismissAllowingStateLoss();
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        }
        else
        {
            showInsufficientDiamondDialog();
        }

    }

    private void showInsufficientDiamondDialog()
    {
        TempleDialog2 templeDialog= TempleDialog2.getInstance();
        templeDialog.setOnCreateDialogListener(new TempleDialog2.OnCreateDialogListener() {
            @Override
            public void onCreate(TempleDialog2 dialog) {
                dialog.mBind.tvTitle.setText(getStringWithoutContext(R.string.dialogTitle2));
                dialog.mBind.gtCommit.setText(getStringWithoutContext(R.string.confirm));
                dialog.mBind.gtCancel.setText(getStringWithoutContext(R.string.cancel));
                dialog.mBind.tvContent.setText(getStringWithoutContext(R.string.InsufficientDiamond));
            }

            @Override
            public void clickCancel(TempleDialog2 dialog) {
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void clickOk(TempleDialog2 dialog) {
                RechargeActivity.startActivity(requireActivity(), false);
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void clickClose(TempleDialog2 dialog) {
                dialog.dismissAllowingStateLoss();
            }
        });
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),templeDialog);
    }
}
