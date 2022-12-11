package com.live.fox.ui.mine.noble;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.live.fox.R;
import com.live.fox.adapter.NobleEquityAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentNobleBinding;
import com.live.fox.dialog.CommonDialog;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.NobleEquityBean;
import com.live.fox.entity.NobleListBean;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ResourceUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.ToastUtils;
import com.luck.picture.lib.rxbus2.ThreadMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NobleNewFragment extends BaseBindingFragment {

    FragmentNobleBinding mBind;
    NobleEquityAdapter nobleEquityAdapter;
    private CommonDialog commonDialog;

    CountDownTimer countDownTimer;

    int resourceIdWords[];
    List<NobleEquityBean> mList = new ArrayList<>();
    int position;

    public static NobleNewFragment newInstance(int position) {
        NobleNewFragment fragment = new NobleNewFragment();
        fragment.position=position;
        return fragment;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_noble;
    }

    public NobleListBean getNobleEquityBean() {
        NobleActivity nobleActivity=(NobleActivity)getActivity();
        return nobleActivity.getVipInfoList().get(position);
    }

    public int getMyLevel() {
        NobleActivity nobleActivity=(NobleActivity)getActivity();
        return nobleActivity.getMyLevel();
    }

    public long getOutTime() {
        NobleActivity nobleActivity=(NobleActivity)getActivity();
        return nobleActivity.getOutTime();
    }

    public void setMyLevel(int level) {
        NobleActivity nobleActivity=(NobleActivity)getActivity();
        nobleActivity.setMyLevel(level);
    }

    public void setOutTime(long outTime) {
        NobleActivity nobleActivity=(NobleActivity)getActivity();
        nobleActivity.setOutTime(outTime);
    }

    public UserAssetsBean getUserAssetsBean() {
        NobleActivity nobleActivity=(NobleActivity)getActivity();
        return nobleActivity.getUserAssetsBean();
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        resourceIdWords=new ResourceUtils().getResourcesID(R.array.wordsResources);
        commonDialog = new CommonDialog();
        setBuyView();

        mBind.ivNoble.setImageResource(resourceIdWords[position]);
        if (position==1) {
            mBind.tvTips1.setTextColor(getResources().getColor(R.color.color00FCFF));
            mBind.tvTips2.setTextColor(getResources().getColor(R.color.color00FCFF));
        }

        mBind.tvTips1.setText(Html.fromHtml(String.format(getString(R.string.noble_tips1),
                Strings.cutOff(getNobleEquityBean().getOpenPrice(),0), Strings.cutOff(getNobleEquityBean().getOpenGiveDiamond(),0) )));


        mBind.tvTips2.setText(Html.fromHtml(String.format(getString(R.string.noble_tips2),
                Strings.cutOff(getNobleEquityBean().getRenewalPrice(),0), Strings.cutOff(getNobleEquityBean().getRenewalGiveDiamond(),0) )));

        showNoble();
        setData();

        nobleEquityAdapter = new NobleEquityAdapter(this.getActivity(), mList, getNobleEquityBean().getVipName());
        mBind.gv.setAdapter(nobleEquityAdapter);

        mBind.tvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setPopCharge();
                showTips();
               // showDialog(getString(R.string.dialogTitle2), getString(R.string.goto_charge_diamond), true);
            }
        });


//        mBind.layoutOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buyNoble();
//            }
//        });

    }

    @Override
    public void notifyFragment() {
        super.notifyFragment();
        if(isActivityOK())
        {
            setBuyView();
        }
    }

    private void setBuyView(){
        if (position+1 == getMyLevel()) {
            mBind.tvOpen.setText(getString(R.string.now_xufei));
            mBind.tvOpen.setBackground(getResources().getDrawable(R.mipmap.ljxf));
            mBind.layoutTime.setVisibility(View.VISIBLE);
            mBind.tvOpenGet.setVisibility(View.GONE);
            if (getOutTime() > 0) {
                String format =  getStringWithoutContext(R.string.noble_time);
                String time = TimeUtils.long2String(getOutTime(),format);
                mBind.tvTime.setText(time);
                mBind.tvTime.setVisibility(View.VISIBLE);
            }
            else
            {
                mBind.tvTime.setVisibility(View.INVISIBLE);
            }

        } else {
            mBind.tvOpen.setText(getString(R.string.now_start));
            mBind.tvOpen.setBackground(getResources().getDrawable(R.mipmap.ljkt));
            mBind.tvOpenGet.setVisibility(View.VISIBLE);
            mBind.layoutTime.setVisibility(View.GONE);
            mBind.tvTime.setVisibility(View.INVISIBLE);
        }
    }

    private void buyNoble(){
        showLoadingDialog();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        //HashMap<String, Object> commonParams = new HashMap<>();
        commonParams.put("levelId", getNobleEquityBean().getId());
        Api_Order.ins().buyNoble(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(!isActivityOK())
                {
                    return;
                }

                if (code == 0 ) {
                    try{
                        JSONObject obj = new JSONObject(data);
                        //新购买的话刷新所有界面
                        setMyLevel(getNobleEquityBean().getVipLevel());
                        String vipImg = obj.getString("vipImg");
                        setOutTime(obj.optLong("expireTime",0l));
                        NobleActivity nobleActivity=(NobleActivity)getActivity();
                        nobleActivity.notifyAllFragments();
                        setPopCharge();
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }

                } else {
                    ToastUtils.showShort(msg);
                }

                dismissLoadingDialog();
            }
        }, commonParams);
    }




    private void showTips(){
        float diamonds=0.0f;
        if (getUserAssetsBean()!=null)
        {
            diamonds=getUserAssetsBean().getVipDiamond()+getUserAssetsBean().getDiamond();
        }
        String tips =  "";
        if (getMyLevel() == getNobleEquityBean().getVipLevel()) {

            if (diamonds < getNobleEquityBean().getOpenPrice()) {
                tips= getString(R.string.goto_charge_diamond);
            } else {
                tips =  String.format(getString(R.string.confirm_buy_noble_2),
                        Strings.cutOff(getNobleEquityBean().getOpenPrice(),0), getString(R.string.noble_2) + "." + getNoble());
            }
        } else  {
            if (getMyLevel() > getNobleEquityBean().getVipLevel()) {
                tips = getString(R.string.cannot_open);
                showCannotOpenDia(getString(R.string.dialog_words), tips);
                return;
            } else {
                if (diamonds < getNobleEquityBean().getRenewalPrice()) {
                    tips= getString(R.string.goto_charge_diamond);
                } else{
                    tips = String.format(getString(R.string.confirm_buy_noble),
                            Strings.cutOff(getNobleEquityBean().getRenewalPrice(),0), getString(R.string.noble_2) + "." + getNoble());
                }
            }
        }

        showDialog(getString(R.string.dialog_words), tips, false);
    }


    private void showDialog(String content, String tips, boolean isCharge) {
        commonDialog.setDialogContent(
                content,
                tips,
                getString(R.string.button_cancel),
                getString(R.string.button_confirm),
                view -> commonDialog.dismiss(),
                view -> {
                    commonDialog.dismiss();
                    if (isCharge) { //充值
                        RechargeActivity.startActivity(requireActivity());
                    } else { //购买
                        buyNoble();
                    }

                });
        commonDialog.show(this.getActivity().getSupportFragmentManager(), "noble dialog");
    }

    private void showCannotOpenDia(String content, String tips) {
        commonDialog.setDialogContent(
                content,
                tips,
                getString(R.string.button_cancel),
                getString(R.string.button_confirm),
                view -> commonDialog.dismiss(),
                view -> {
                    commonDialog.dismiss();
                });
        commonDialog.show(this.getActivity().getSupportFragmentManager(), "noble2 dialog");
    }


    private void setPopCharge( ){
        View popupView = getLayoutInflater().inflate(R.layout.pop_noble_buy,null);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(null);
        TextView tvTitle = popupView.findViewById(R.id.tv_title);
        String buy = getResources().getString(R.string.con_you_buy);
        if (getMyLevel() == getNobleEquityBean().getVipLevel()) {
            buy = getResources().getString(R.string.con_you_xufei);
        }
        String string = "<font color='#ffffff'> " + buy + "</font>" +
                "<font color='#E2B361'> " +getResources().getString(R.string.noble_2) + "." + getNoble() + "</font>" +
                "<font color='#ffffff'> " +getResources().getString(R.string.tab_change_success)+ "</font>";
        tvTitle.setText(Html.fromHtml(string));

//        ImageView iv = popupView.findViewById(R.id.iv);
//        GlideUtils.loadImage(this.getActivity(), vipImg, iv);

        TextView tvTime = popupView.findViewById(R.id.tv_time);
        setRootAlpha(0.35f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //在dismiss中恢复透明度
                setRootAlpha(1f);
            }
        });

        popupWindow.showAtLocation(mBind.getRoot(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

        countDownTimer=new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(isActivityOK())
                {
                    tvTime.setText(millisUntilFinished / 1000 + "s");
                }

            }
            public void onFinish() {
                if(isActivityOK())
                {
                    countDownTimer.cancel();
                    popupWindow.dismiss();
                }
            }
        }.start();
    }

    private void setRootAlpha(float al){
        if(isActivityOK())
        {
            WindowManager.LayoutParams lp=this.getActivity().getWindow().getAttributes();
            lp.alpha= al;
            this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            this.getActivity().getWindow().setAttributes(lp);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(countDownTimer!=null)
        {
            countDownTimer.cancel();
        }
    }

    private void showNoble(){
        if (position+1 == NobleActivity.NANJUE) {
            mBind.tvPower.setText("5/13");
        } else if (position+1 == NobleActivity.ZIJUE || position+1 == NobleActivity.BOJUE) {
            mBind.tvPower.setText("7/13");
            if (position+1 == NobleActivity.ZIJUE) {
                mBind.layout.setBackground(getResources().getDrawable(R.mipmap.zijue_p));
            } else {
                mBind.layout.setBackground(getResources().getDrawable(R.mipmap.bojue_p));
            }
        } else if (position+1 == NobleActivity.HOUJUE) {
            mBind.tvPower.setText("8/13");
            mBind.layout.setBackground(getResources().getDrawable(R.mipmap.houjue_p));
        } else if (position+1 == NobleActivity.GONGJUE) {
            mBind.tvPower.setText("11/13");
            mBind.layout.setBackground(getResources().getDrawable(R.mipmap.gongjue_p));
        } else if (position+1 == NobleActivity.QINWANG) {
            mBind.tvPower.setText("12/13");
            mBind.layout.setBackground(getResources().getDrawable(R.mipmap.qinwang_p));
        } else if (position+1 == NobleActivity.KING) {
            mBind.tvPower.setText("13/13");
            mBind.layout.setBackground(getResources().getDrawable(R.mipmap.guowang_p));
        }

    }

    private String getNoble(){
        if (position+1 == NobleActivity.NANJUE) {
            return getString(R.string.nanjue);
        } else if (position+1 == NobleActivity.ZIJUE ) {
            return getString(R.string.zijue);
        }  else if (position+1 == NobleActivity.BOJUE ) {
            return getString(R.string.zijue);
        } else if (position+1 == NobleActivity.HOUJUE) {
            return getString(R.string.houjue);
        } else if (position+1 == NobleActivity.GONGJUE) {
            return getString(R.string.gongjue);
        } else if (position+1 == NobleActivity.QINWANG) {
            return getString(R.string.qinwang);
        }
        return getString(R.string.king);
    }


    private void setData(){
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_1), getString(R.string.noble_equity_tips_1),
                getResources().getDrawable(R.mipmap.equity1)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_2), getString(R.string.noble_equity_tips_2),
                getResources().getDrawable(R.mipmap.equity2)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_3), getString(R.string.noble_equity_tips_3),
                getResources().getDrawable(R.mipmap.equity3)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_4), getString(R.string.noble_equity_tips_4),
                getResources().getDrawable(R.mipmap.equity4)));

        NobleEquityBean bean = new NobleEquityBean(getString(R.string.noble_equity_title_5), getString(R.string.noble_equity_tips_5),
                getResources().getDrawable(R.mipmap.zi_kuang));
        bean.setShowPhoto(true);
        if(position+1 == NobleActivity.ZIJUE) {
            bean.setImg(getResources().getDrawable(R.mipmap.zi_kuang));
        } else if(position+1 == NobleActivity.BOJUE) {
            bean.setImg(getResources().getDrawable(R.mipmap.bo_kuagn));
        } else if(position+1 == NobleActivity.HOUJUE) {
            bean.setImg(getResources().getDrawable(R.mipmap.hou_kuang));
        } else if(position+1 == NobleActivity.GONGJUE) {
            bean.setImg(getResources().getDrawable(R.mipmap.gong_kuang));
        } else if(position+1 == NobleActivity.QINWANG) {
            bean.setImg(getResources().getDrawable(R.mipmap.qin_kuang));
        } else if(position+1 == NobleActivity.KING) {
            bean.setImg(getResources().getDrawable(R.mipmap.wang_kuang));
        }
        bean.setUlr(getNobleEquityBean().getVipFrams());
        mList.add(bean);



        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_6), getString(R.string.noble_equity_tips_6),
                getResources().getDrawable(R.mipmap.equity6), getShow(5)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_7), getString(R.string.noble_equity_tips_7),
                getResources().getDrawable(R.mipmap.equity7), getShow(6)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_8), getString(R.string.noble_equity_tips_8),
                getResources().getDrawable(R.mipmap.equity8), getShow(7)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_9), getString(R.string.noble_equity_tips_9),
                getResources().getDrawable(R.mipmap.equity9), getShow(8)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_10), getString(R.string.noble_equity_tips_10),
                getResources().getDrawable(R.mipmap.equity10), getShow(9)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_11), getString(R.string.noble_equity_tips_11),
                getResources().getDrawable(R.mipmap.equity5), getShow(10)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_12), getString(R.string.noble_equity_tips_12),
                getResources().getDrawable(R.mipmap.equity11), getShow(11)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_13), getString(R.string.noble_equity_tips_13),
                getResources().getDrawable(R.mipmap.equity12), getShow(12)));

    }

    private boolean getShow(int pos) {
        if (position+1 == NobleActivity.NANJUE) {
            if (pos > 4) {
                return false;
            } else {
                return true;
            }
        } else if (position+1 == NobleActivity.ZIJUE || position+1 == NobleActivity.BOJUE) {
            if (pos > 6) {
                return false;
            } else {
                return true;
            }
        } else if (position+1 == NobleActivity.HOUJUE) {
            if (pos > 7) {
                return false;
            } else {
                return true;
            }
        } else if (position+1 == NobleActivity.GONGJUE) {
            if (pos > 10) {
                return false;
            } else {
                return true;
            }
        } else if (position+1 == NobleActivity.QINWANG) {
            if (pos > 11) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }



}
