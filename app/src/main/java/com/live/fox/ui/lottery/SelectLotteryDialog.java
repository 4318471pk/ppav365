package com.live.fox.ui.lottery;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogSelectLotteryBinding;
import com.live.fox.entity.SelectLotteryBean;
import com.live.fox.ui.lottery.adapter.SelectLotteryAdapter;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectLotteryDialog extends BaseBindingDialogFragment {

    DialogSelectLotteryBinding mBind;

    boolean isLottery = true; //true:选择游戏  false:选择游戏状态
    SelectLotteryAdapter mAdapter;
    List<SelectLotteryBean> mData = new ArrayList<>();

    public static SelectLotteryDialog newInstance(boolean isLottery) {
        Bundle args = new Bundle();
        args.putBoolean("isLottery",isLottery);
        SelectLotteryDialog fragment = new SelectLotteryDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClickView(View view) {
        if (view == mBind.rlMain) {
            dismissAllowingStateLoss();
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_select_lottery;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);
        isLottery = getArguments().getBoolean("isLottery",isLottery);
        if (!isLottery) {
            mBind.tvTitle.setText(getResources().getString(R.string.select_status));
            addStatus();
            LinearLayout.LayoutParams linearParams =  (LinearLayout.LayoutParams)mBind.gv.getLayoutParams();
            linearParams.height = DeviceUtils.dp2px(this.getContext(), 135);
            mBind.gv.setLayoutParams(linearParams);
        } else {
            test();
        }

        mAdapter = new SelectLotteryAdapter(this.getContext(), mData);
        mBind.gv.setAdapter(mAdapter);

        mBind.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectLotterySuc != null) {
                    selectLotterySuc.select(mData.get(position).getName(), 0001);
                    dismissAllowingStateLoss();
                }
            }
        });
    }

    private void test(){
        mData.add(new SelectLotteryBean("一分时时彩")); mData.add(new SelectLotteryBean("一分时时彩"));
        mData.add(new SelectLotteryBean("一分时时彩")); mData.add(new SelectLotteryBean("一分时时彩"));
        mData.add(new SelectLotteryBean("一分时时彩")); mData.add(new SelectLotteryBean("一分时时彩"));
        mData.add(new SelectLotteryBean("一分时时彩")); mData.add(new SelectLotteryBean("一分时时彩"));
        mData.add(new SelectLotteryBean("一分时时彩")); mData.add(new SelectLotteryBean("一分时时彩"));
        mData.add(new SelectLotteryBean("一分时时彩")); mData.add(new SelectLotteryBean("一分时时彩"));
    }

    private void addStatus(){
        mData.clear();
        mData.add(new SelectLotteryBean(getResources().getString(R.string.all_zt)));
        mData.add(new SelectLotteryBean(getResources().getString(R.string.wait_kc)));
        mData.add(new SelectLotteryBean(getResources().getString(R.string.yi_zz)));
        mData.add(new SelectLotteryBean(getResources().getString(R.string.no_zz)));
        mData.add(new SelectLotteryBean(getResources().getString(R.string.heju)));

    }

    public interface SelectLotterySuc{
        public void select(String name, int code);
    }

    private SelectLotterySuc selectLotterySuc;

    public void setSelectLotterySuc(SelectLotterySuc suc){
        this.selectLotterySuc = suc;
    }
}
