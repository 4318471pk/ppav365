package com.live.fox.dialog.bottomDialog;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogAddNewbankCardBinding;
import com.live.fox.databinding.DialogBankListBinding;
import com.live.fox.entity.BankListBean;
import com.live.fox.entity.WithdrawChannelTypeBean;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BankListDialog extends BaseBindingDialogFragment {

    DialogBankListBinding mBind;

    BankListAdapter bankListAdapter;
    List<BankListBean> mData = new ArrayList<>();


    public static BankListDialog getInstance()
    {
        return new BankListDialog();
    }
    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_bank_list;
    }

    @Override
    public void initView(View view) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mBind=getViewDataBinding();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rc.setLayoutManager(layoutManager);
        bankListAdapter = new BankListAdapter(mData);
        mBind.rc.setAdapter(bankListAdapter);

        getBankList(true);

        mBind.rlBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        mBind.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        Animation animation= new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0
                ,Animation.RELATIVE_TO_PARENT,1f
                ,Animation.RELATIVE_TO_PARENT,0f);
        animation.setDuration(300);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBind.rlBG.setBackgroundColor(0x88000000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBind.llMain.startAnimation(animation);
    }


    public void getBankList(boolean isStart){
        if (isStart && mData.size() > 0) {
            bankListAdapter.notifyDataSetChanged();
            return;
        }
        if (isStart) {
            showLoadingDialog();
        }

        Api_Order.ins().getBankList(new JsonCallback<List<BankListBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BankListBean> data) {
                if (isStart) {
                    dismissLoadingDialog();
                }
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    mData.clear();
                    mData.addAll(data);
                    if (isStart) {
                        bankListAdapter.notifyDataSetChanged();
                    }

                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });


    }


    class BankListAdapter extends BaseQuickAdapter<BankListBean, BaseViewHolder> {

        public BankListAdapter(List data) {
            super(R.layout.item_bank_name, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BankListBean data) {
            TextView tvName = helper.getView(R.id.tv);
            ImageView iv = helper.getView(R.id.iv);
            tvName.setText(data.getName());

            GlideUtils.loadImage(mContext, data.getIcon(), iv);

            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAllowingStateLoss();
                    if (bankListChoice != null) {
                        bankListChoice.choice(data.getValue(), data.getName());
                    }
                }
            });
        }
    }

    public interface BankListChoice {
        public void choice(String value, String name);
    }

    private BankListChoice bankListChoice = null;

    public void setAddNewBankCardSuc(BankListChoice back) {
        this.bankListChoice = back;
    }


}
