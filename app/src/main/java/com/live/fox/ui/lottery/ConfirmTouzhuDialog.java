package com.live.fox.ui.lottery;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogChoumaBinding;
import com.live.fox.databinding.DialogConfirmTouzhuBinding;
import com.live.fox.entity.SelectLotteryBean;
import com.live.fox.ui.lottery.adapter.BeishuAdapter;
import com.live.fox.ui.lottery.adapter.ChouMaAdapter;
import com.live.fox.ui.lottery.adapter.ConfirmTouzhuAdapter;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfirmTouzhuDialog extends BaseBindingDialogFragment {

    DialogConfirmTouzhuBinding mBind;

    ConfirmTouzhuAdapter touzhuAdapter;
    List<SelectLotteryBean> touzhuList = new ArrayList<>();

    BeishuAdapter beishuAdapter;
    List<SelectLotteryBean> beishuList = new ArrayList<>();

    InputChouMaDialog inputChouMaDialog;
    YusheBeishuDialog yusheBeishuDialog;

    public static ConfirmTouzhuDialog newInstance() {
        Bundle args = new Bundle();
        ConfirmTouzhuDialog fragment = new ConfirmTouzhuDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClickView(View view) {
        if (view == mBind.rlMain) {
            dismissAllowingStateLoss();
        } else if (view == mBind.tvConfirm) {

            dismissAllowingStateLoss();
        }
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen_2);
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_confirm_touzhu;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);
        addData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rcTouzhu.setLayoutManager(layoutManager);
        touzhuAdapter = new ConfirmTouzhuAdapter(touzhuList);
        touzhuAdapter.setSelectChouMaSuc(new ConfirmTouzhuAdapter.ConfirmTouzhuItemListener() {
            @Override
            public void onMoneyClick(int pos, String money) {
                if (inputChouMaDialog == null) {
                    inputChouMaDialog = InputChouMaDialog.newInstance(
                            getResources().getString(R.string.edit_zhudan_money), false,    touzhuList.get(pos).getName());
                }
                inputChouMaDialog.show(getActivity().getSupportFragmentManager(), "input zhudao dialog");
                inputChouMaDialog.setChouMaInputListener(new InputChouMaDialog.ChouMaInputListener() {
                    @Override
                    public void confirm(String money, boolean isChouma) {
                        if (!isChouma) {
                            touzhuList.get(pos).setName(money);
                            touzhuAdapter.notifyItemChanged(pos);
                        }
                    }
                });
            }

            @Override
            public void delete(int pos) {
                touzhuList.remove(pos);
                touzhuAdapter.notifyItemRemoved(pos);
            }
        });
        mBind.rcTouzhu.setAdapter(touzhuAdapter);


        String ysData = SPUtils.getInstance().getString(YusheBeishuDialog.TOUZHU_YUSHE);
        if (!TextUtils.isEmpty(ysData)) {
            setBeishuList(ysData);
        } else {
            String s = "1,2,5,10,20";
            SPUtils.getInstance().put(YusheBeishuDialog.TOUZHU_YUSHE, s);
            setBeishuList(s);
        }

        addZdyBeishu();

        beishuAdapter = new BeishuAdapter(beishuList);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this.getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBind.rcBeishu.setLayoutManager(layoutManager2);
        mBind.rcBeishu.setAdapter(beishuAdapter);
        beishuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < beishuList.size(); i ++) {
                    beishuList.get(i).setSelect(false);
                }
                if (beishuList.get(position).isZdy()) {
                    if (yusheBeishuDialog == null) {
                        yusheBeishuDialog = YusheBeishuDialog.newInstance();
                    }
                    yusheBeishuDialog.show(getActivity().getSupportFragmentManager(), "input yushe dialog");
                    yusheBeishuDialog.setListener(new YusheBeishuDialog.YusheBeishuListener() {
                        @Override
                        public void confirm(String beishu) {
                            String[] arr = beishu.split(",");
                            beishuList.clear();
                            setBeishuList(beishu);
                            addZdyBeishu();
                            beishuAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    beishuList.get(position).setSelect(true);
                    beishuAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void setBeishuList(String s) {
        String[] arr = s.split(",");
        for (int i = 0; i < arr.length; i++) {
            beishuList.add(new SelectLotteryBean(arr[i]));
        }
    }

    private void addZdyBeishu(){
        SelectLotteryBean bean = new SelectLotteryBean();
        bean.setZdy(true);
        beishuList.add(bean);
    }

    private void addData(){
        touzhuList.clear();
        touzhuList.add(new SelectLotteryBean("2")); touzhuList.add(new SelectLotteryBean("5"));
        touzhuList.add(new SelectLotteryBean("10")); touzhuList.add(new SelectLotteryBean("50"));
        touzhuList.add(new SelectLotteryBean("100")); touzhuList.add(new SelectLotteryBean("500"));
    }



}
