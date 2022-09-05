package com.live.fox.base;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.contract.MinuteGameContract;
import com.live.fox.entity.response.CpGameResultInfoVO;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.model.MinuteGameModel;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.List;
import java.util.Map;

public class MinuteGamePresenter extends MinuteGameContract.Presenter {
    public MinuteGamePresenter() {
        mModel = new MinuteGameModel();
    }

    @Override
    public void doGetGamePeriodInfo(Map map) {
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

    @Override
    public void doGetLastGameResult(Map map) {
        mModel.doGetLastGameResult(map, new JsonCallback<List<CpGameResultInfoVO>>() {
            @Override
            public void onSuccess(int code, String msg, List<CpGameResultInfoVO> result) {
                if (code == Constant.Code.SUCCESS) {
                    mView.onGetLastGameResult(result);
                } else {
                    mView.onError(msg);
                }

            }
        });
    }

    public Dialog initDialog(Dialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_one_minutegame);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0.05f);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);

        return dialog;
    }

    /**
     * 显示popupWindow
     */
    public void showPopwindow(Context context, @NonNull List<CpGameResultInfoVO> list, View v, boolean isLHC) {
        //加载弹出框的布局
        View contentView = LayoutInflater.from(context).inflate(R.layout.cp_pop_layout, null);
        RecyclerView rvPrizeNumList = (RecyclerView) contentView.findViewById(R.id.rvPrizeNumList);
        rvPrizeNumList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvPrizeNumList.addItemDecoration(new RecycleViewDivider(
                context, LinearLayoutManager.HORIZONTAL, ScreenUtils.dip2px(context, 1), ContextCompat.getColor(context, com.luck.picture.lib.R.color.color_69)));
        rvPrizeNumList.setAdapter(new BaseQuickAdapter<CpGameResultInfoVO, BaseViewHolder>(R.layout.adapter_prizer_cp, list) {
            @Override
            protected void convert(BaseViewHolder helper, CpGameResultInfoVO item) {
                String code = item.getCode();
                String[] split = code.split(",");
                if (isLHC) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < split.length - 2; i++) {
                        sb.append(split[i] + ",");
                    }
                    if ("1".equals(split[split.length - 1])) {
                        helper.setText(R.id.tvLuckNum, Html.fromHtml(sb + "<font color='#d00f18'>" + split[split.length - 2] + "</font>"));
                    } else if ("2".equals(split[split.length - 1])) {
                        helper.setText(R.id.tvLuckNum, Html.fromHtml(sb + "<font color='#17ba0f'>" + split[split.length - 2] + "</font>"));
                    } else {
                        helper.setText(R.id.tvLuckNum, Html.fromHtml(sb + "<font color='#0f24c5'>" + split[split.length - 2] + "</font>"));
                    }
                } else {
                    helper.setText(R.id.tvLuckNum, item.getCode());
                }
            }
        });
        PopupWindow window = new PopupWindow(v, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setContentView(contentView);
        window.setOutsideTouchable(false);
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        window.setBackgroundDrawable(null);
        window.setAnimationStyle(R.style.PopupWindowAnimStyle2);
        window.showAsDropDown(v);
        //进入退出的动画，指定刚才定义的style
    }
}
