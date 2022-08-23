package com.live.fox.base;

import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.contract.BetCartContract;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.model.BetCartModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;


public class BetCartPresenter extends BetCartContract.Presenter {

    public BetCartPresenter() {
        mModel = new BetCartModel();
    }

    @Override
    public void doPushCart(Map<String, Object> map) {
        mView.onShowProgress();
        mModel.doPushCart(map, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String result) {
                if (code == Constant.Code.SUCCESS &&
                        "ok".equals(msg) || "success".contains(msg)) {
                    mView.onPushCart();
                } else {
                    mView.onError(msg);
                }
                mView.onHideProgress();
            }
        });
    }

    @Override
    public void doGetGamePeriodInfo(Map<String, Object> map) {
        mModel.doGetGamePeriodInfo(map, new JsonCallback<GamePeriodInfoVO>() {
            @Override
            public void onSuccess(int code, String msg, GamePeriodInfoVO result) {
                if (code == Constant.Code.SUCCESS) {
                    mView.onGetGamePeriodInfo(result);
                } else {
                    mView.onError(msg);
                }
            }
        });
    }

    /**
     * 计算总价格
     *
     * @param items
     */
    public String caculteTotal(List<MinuteTabItem> items) {
        BigDecimal sum = new BigDecimal(0);
        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                MinuteTabItem item = items.get(i);
                sum = sum.add(new BigDecimal(item.betMoney).multiply(new BigDecimal(item.mutiple)));
            }
        }
        return sum.setScale(0, RoundingMode.HALF_DOWN).toString();
    }

    public String caculteTotalhn(List<MinuteTabItem> items) {
        BigDecimal sum = new BigDecimal(0);
        if (!items.isEmpty()) {
            MinuteTabItem item = items.get(0);//取该项的倍数
            sum = sum.add(new BigDecimal(item.betMoney)
                    .multiply(new BigDecimal(item.mutiple).multiply(new BigDecimal(item.getBetCount()))));
        }
        return sum.setScale(0, RoundingMode.HALF_DOWN).toString();
    }

    public void initDialog(Dialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_game_cart);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setDimAmount(0.05f);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
    }
}
