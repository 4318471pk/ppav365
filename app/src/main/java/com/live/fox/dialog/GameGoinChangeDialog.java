package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.AppConfig;
import com.live.fox.R;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 游戏金币转换
 */
public class GameGoinChangeDialog extends DialogFragment implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView ivLogo;
    private TextView tvRightDes;
    private EditText et;

    boolean isShow = true;
    double gamecoin;
    double mBalance;

    int type = 1; //1开元 2AG
    String typeBiStr;

    public static GameGoinChangeDialog newInstance(double gamecoin, int type, double mBalance) {
        GameGoinChangeDialog fragment = new GameGoinChangeDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putDouble("gamecoin", gamecoin);
        bundle.putDouble("balance", mBalance);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_gamecoinchange, container, false);
        initView(view);
        return view;
    }

    private void initView(View bindSource) {
        tvTitle = bindSource.findViewById(R.id.tv_title);
        ivLogo = bindSource.findViewById(R.id.iv_logo);
        tvRightDes = bindSource.findViewById(R.id.tv_rightdes);
        et = bindSource.findViewById(R.id.et_);
        bindSource.findViewById(R.id.iv_close).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_down).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_up).setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", 1);
            gamecoin = bundle.getDouble("gamecoin", 0);
            mBalance = bundle.getDouble("balance", 0);
        }

        isShow = true;
        refreshPageByType();
        et.requestFocus();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        setCancelable(false);
        return dialog;
    }

    /**
     * type 1开元 2AG
     */
    public void refreshPageByType() {
        String goldCoin = "1";
        String lotteryNumCoin = "";
        String lottery = "";
        String game = "";
        switch (type) {
            case 1:
                tvTitle.setText(getString(R.string.kaiyuancc));
                ivLogo.setImageResource(R.drawable.logo_kyqp);
                typeBiStr = getString(R.string.kaiyuanbi);
                et.setHint(getString(R.string.EamountConverted));
                lotteryNumCoin = AppConfig.getExchangeRatio();
                if (AppConfig.isThLive()) {
                    lottery = "MP";
                } else {
                    lottery = "V8";
                }
                break;

            case 2:
                tvTitle.setText(getString(R.string.AGYoplay));
                ivLogo.setImageResource(R.drawable.logo_ag);
                typeBiStr = getString(R.string.AGGameCoin);
                lotteryNumCoin = getRation();
                lottery = "BG";
                break;

            case 4:
                tvTitle.setText(getString(R.string.tycooncc));
                ivLogo.setImageResource(R.drawable.ic_rich);
                typeBiStr = getString(R.string.tycoongc);
                tvRightDes.setText(getString(R.string.goldCoinsTycoon));
                goldCoin = "10";
                lotteryNumCoin = AppConfig.getExchangeRatio();
                lottery = "BG";
                break;
            case 6:
                tvTitle.setText(getString(R.string.TYYoplay));
                ivLogo.setImageResource(R.drawable.logo_cmb);
                typeBiStr = getString(R.string.TYGameCoin);
                tvRightDes.setText(getString(R.string.goldCoinToGamet));
                goldCoin = "1";
                lotteryNumCoin = getRation();
                lottery = "CMD";
                break;

            case 7:
                tvTitle.setText("SABASports");
                ivLogo.setImageResource(R.drawable.img_saba);
                typeBiStr = "SABA";
                goldCoin = "1";
                lotteryNumCoin = getRation();
                lottery = "SABA";
                break;
        }
        String strFor = String.format(getString(R.string.my_balance), goldCoin, lotteryNumCoin, lottery);
        tvRightDes.setText(strFor);
        String unitFor = String.format(getString(R.string.amount_conversion_hint),lottery);
        et.setHint(unitFor);
    }

    public boolean isShow() {
        return isShow;
    }


    private String getRation() {
        String ratio;
        if (AppConfig.isThLive()) {
            ratio = AppConfig.getExchangeRatio();
        } else {
            ratio = "1";
        }
        return ratio;
    }

    @Override
    public void dismiss() {
        isShow = false;
        super.dismiss();
    }

    OnBtnSureClick btnSureClick;

    public void setBtnSureClick(OnBtnSureClick btnClick) {
        this.btnSureClick = btnClick;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_down:
                String content = et.getText().toString().trim();
                if (StringUtils.isEmpty(content)) return;
                try {
                    long changeCoin = Long.parseLong(content);

                    if (changeCoin == 0) {
                        ToastUtils.showShort(typeBiStr + getString(R.string.cannotEntered));
                        return;
                    }
                    if (changeCoin > mBalance) {
                        ToastUtils.showShort(getString(R.string.insufficientBalance));
                        return;
                    }
                    if (btnSureClick != null) {
                        btnSureClick.onClick(1, changeCoin);
                    }
                    dismiss();
                } catch (Exception e) {
                    ToastUtils.showShort(getString(R.string.inputMoney));
                }

                break;
            case R.id.tv_up:
                String content1 = et.getText().toString().trim();
                if (StringUtils.isEmpty(content1)) return;
                long changeCoin1 = Long.parseLong(content1);
                if (changeCoin1 == 0) {
                    ToastUtils.showShort(typeBiStr + getString(R.string.cannotEntered));
                    return;

                }
                if (changeCoin1 > gamecoin * Double.parseDouble(AppConfig.getExchangeRatio())) {
                    ToastUtils.showShort(getString(R.string.lessGoldCoin));
                    return;
                }
                if (btnSureClick != null) {
                    btnSureClick.onClick(2, changeCoin1);
                }
                dismiss();
                break;
        }
    }

    public interface OnBtnSureClick {
        /**
         * 按钮点击
         *
         * @param type 1 转出 2 转入
         * @param coin 金额
         */
        void onClick(int type, long coin);
    }
}
