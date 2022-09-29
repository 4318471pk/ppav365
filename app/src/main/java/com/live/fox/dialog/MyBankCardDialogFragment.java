package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.BankInfo;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.withdraw.BindCardActivity;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;



public class MyBankCardDialogFragment extends DialogFragment {

    BaseQuickAdapter adapter;
    private RecyclerView rv;
    private RoundTextView tv_tjcxk;
    private String mobile;
    private String trueName;
    private String cardNo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_my_bankcard);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0.1f);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_bankcard, container, false);
    }

    private void getAllLotteryLatestResult() {
        Api_User.ins().getCardInfo(new JsonCallback<List<BankInfo>>() {
            @Override
            public void onSuccess(int code, String msg, List<BankInfo> data) {
                if (code == 0) {
                    if (data == null || data.size() == 0) {
                        ToastUtils.showShort(getString(R.string.noList));
                    } else {
                        adapter.setNewData(data);
                    }
                } else {
                    ToastUtils.showShort(msg);
                }

            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData(getArguments());

    }

    private void initData(Bundle arguments) {
        if (arguments != null) {
            cardNo = arguments.getString("cardNo");
            getAllLotteryLatestResult();
        }
    }

    public static MyBankCardDialogFragment newInstance(String cardNo) {
        MyBankCardDialogFragment fragment = new MyBankCardDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cardNo", cardNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        rv = (RecyclerView) view.findViewById(R.id.rv_);
        tv_tjcxk = view.findViewById(R.id.tv_tjcxk);
        setRecycleView();
        tv_tjcxk.setOnClickListener(v -> {
            if (StringUtils.isEmpty(mobile)) {
                return;
            }
            if (adapter.getData().size() < ((BankInfo) adapter.getData().get(0)).getBankCount()) {
                BindCardActivity.startActivity(getContext(), mobile, trueName);
                dismiss();
            } else {
                ToastUtils.showShort(getString(R.string.toast_tip_reached));
            }
        });
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_my_bankcard, new ArrayList<BankInfo>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                BankInfo myTZResult = (BankInfo) item;
                if (cardNo.equals(myTZResult.getCardNo())) {
                    helper.getView(R.id.ll_p).setBackgroundResource(R.drawable.bankbgb);
                } else {
                    helper.getView(R.id.ll_p).setBackgroundResource(R.drawable.shape_white_round_20);
                }
                mobile = myTZResult.getMobile();
                trueName = myTZResult.getTrueName();
                helper.setText(R.id.tv_bankname, myTZResult.getBankName());
                helper.setText(R.id.tv_cardtopno, myTZResult.getCardNo().substring(0, 4));
                helper.setText(R.id.tv_cardendno, myTZResult.getCardNo().substring(myTZResult.getCardNo().length() - 4));
                ImageView bankLogo = helper.getView(R.id.iv_);
                Glide.with(requireContext()).load(myTZResult.getLogs()).into(bankLogo);
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            BankInfo myTZResult = (BankInfo) adapter.getData().get(position);
            changeCard(myTZResult.getCardNo());
        });
    }

    private void changeCard(String cardNo) {
        Api_User.ins().changeCard(cardNo, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    if ("Ok".equals(msg)) {
                        ToastUtils.showShort(getString(R.string.toast_tip_change));
                        dismiss();
                    } else {
                        ToastUtils.showShort(getString(R.string.toast_tip_exchange_failed));
                    }
                } else {
                    ToastUtils.showShort(msg);
                    dismiss();
                }

            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onItemClickListener != null) {
            onItemClickListener.onSuccess();
        }
    }

    public interface OnItemClickListener {
        void onSuccess();
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
