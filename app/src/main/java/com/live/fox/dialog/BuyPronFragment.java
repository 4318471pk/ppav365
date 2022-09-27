package com.live.fox.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Gift;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.ShopActivity;
import com.live.fox.utils.ArithUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.SpanUtils;


/**
 * 购买商城道具
 */
public class BuyPronFragment extends DialogFragment implements View.OnClickListener {

    TextView tvTitle;
    ImageView ivClose;
    TextView ivBuy;
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;
    LinearLayout layout4;
    TextView tvDay1;
    TextView tvDay2;
    TextView tvDay3;
    TextView tvDay4;
    TextView tvMoney1;
    TextView tvMoney2;
    TextView tvMoney3;
    TextView tvMoney4;

    ShopActivity shopActivity;
    Gift gift;
    int selPos = 0;
    private Dialog dialog;

    public static BuyPronFragment newInstance(Gift gift) {
        BuyPronFragment fragment = new BuyPronFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("gift", gift);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            gift = (Gift) bundle.getSerializable("gift");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_buypron, container, false);
        initView(view);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消

        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics dm = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        dialog.getWindow().setLayout(dm.widthPixels, dialog.getWindow().getAttributes().height);
        dialog.getWindow().setDimAmount(0.4f);
    }

    private void initView(View dialog) {
        tvTitle = dialog.findViewById(R.id.tv_title);
        ivClose = dialog.findViewById(R.id.iv_close);
        ivBuy = dialog.findViewById(R.id.iv_buy);
        layout1 = dialog.findViewById(R.id.layout_money1);
        layout2 = dialog.findViewById(R.id.layout_money2);
        layout3 = dialog.findViewById(R.id.layout_money3);
        layout4 = dialog.findViewById(R.id.layout_money4);
        tvDay1 = dialog.findViewById(R.id.tv_day1);
        tvDay2 = dialog.findViewById(R.id.tv_day2);
        tvDay3 = dialog.findViewById(R.id.tv_day3);
        tvDay4 = dialog.findViewById(R.id.tv_day4);
        tvMoney1 = dialog.findViewById(R.id.tv_money1);
        tvMoney2 = dialog.findViewById(R.id.tv_money2);
        tvMoney3 = dialog.findViewById(R.id.tv_money3);
        tvMoney4 = dialog.findViewById(R.id.tv_money4);

        ivBuy.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);

        LogUtils.e(gift.getGoldCoin() * 180 + "");
        LogUtils.e(ArithUtil.mul(gift.getGoldCoin() * 180, 0.7) + "");
        tvMoney1.setText(RegexUtils.westMoney(gift.getGoldCoin() * 7) + getString(R.string.gold));
        tvMoney2.setText(RegexUtils.westMoney((long) (gift.getGoldCoin() * 30 * 0.9 + 0.1)) + getString(R.string.gold));
        tvMoney3.setText(RegexUtils.westMoney((long) (gift.getGoldCoin() * 90 * 0.8 + 0.1)) + getString(R.string.gold));
        tvMoney4.setText(RegexUtils.westMoney((long) (gift.getGoldCoin() * 180 * 0.7 + 0.1)) + getString(R.string.gold));

        setBuySel(0);

        SpanUtils spanUtils = new SpanUtils();
        spanUtils.append(getString(R.string.buy));
        spanUtils.append(gift.getGname()).setForegroundColor(Color.parseColor("#EF6189"));
        tvTitle.setText(spanUtils.create());
    }

    public void setBuySel(int selPos) {
        this.selPos = selPos;
        layout1.setBackgroundResource(R.drawable.buypron_unsel);
        tvDay1.setTextColor(Color.parseColor("#EF6189"));
        tvMoney1.setTextColor(Color.parseColor("#EF6189"));
        layout2.setBackgroundResource(R.drawable.buypron_unsel);
        tvDay2.setTextColor(Color.parseColor("#EF6189"));
        tvMoney2.setTextColor(Color.parseColor("#EF6189"));
        layout3.setBackgroundResource(R.drawable.buypron_unsel);
        tvDay3.setTextColor(Color.parseColor("#EF6189"));
        tvMoney3.setTextColor(Color.parseColor("#EF6189"));
        layout4.setBackgroundResource(R.drawable.buypron_unsel);
        tvDay4.setTextColor(Color.parseColor("#EF6189"));
        tvMoney4.setTextColor(Color.parseColor("#EF6189"));

        switch (selPos) {
            case 0:
                layout1.setBackgroundResource(R.drawable.shape_corners_20_them);
                tvDay1.setTextColor(Color.WHITE);
                tvMoney1.setTextColor(Color.WHITE);
                break;
            case 1:
                layout2.setBackgroundResource(R.drawable.shape_corners_20_them);
                tvDay2.setTextColor(Color.WHITE);
                tvMoney2.setTextColor(Color.WHITE);
                break;
            case 2:
                layout3.setBackgroundResource(R.drawable.shape_corners_20_them);
                tvDay3.setTextColor(Color.WHITE);
                tvMoney3.setTextColor(Color.WHITE);
                break;
            case 3:
                layout4.setBackgroundResource(R.drawable.shape_corners_20_them);
                tvDay4.setTextColor(Color.WHITE);
                tvMoney4.setTextColor(Color.WHITE);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_money1:
                setBuySel(0);
                break;
            case R.id.layout_money2:
                setBuySel(1);
                break;
            case R.id.layout_money3:
                setBuySel(2);
                break;
            case R.id.layout_money4:
                setBuySel(3);
                break;
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_buy:
                doBuyPronApi();
                break;
        }
    }

    public void doBuyPronApi() {
        int days = 7;
        switch (selPos) {
            case 0:
                days = 7;
                break;
            case 1:
                days = 30;
                break;
            case 2:
                days = 90;
                break;
            case 3:
                days = 180;
                break;
        }
        if (getActivity() instanceof ShopActivity) {
            shopActivity = (ShopActivity) getActivity();
            shopActivity.showLoadingDialog(getString(R.string.buying), false, true);
        }

        Api_User.ins().buyCar(gift.getGid(), days, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String result) {
                if (shopActivity != null) {
                    shopActivity.hideLoadingDialog();
                    if (code == 0) {
                        dismiss();
                        shopActivity.showToastTip(true, getString(R.string.buyCarSuccess));
                    } else {
                        shopActivity.showToastTip(false, msg);
                    }
                }
            }
        });
    }


    OnBtnClick btnClick;

    public void setBtnClick(OnBtnClick btnClick) {
        this.btnClick = btnClick;
    }

    public interface OnBtnClick {
        void onClick(int id);
    }

}
