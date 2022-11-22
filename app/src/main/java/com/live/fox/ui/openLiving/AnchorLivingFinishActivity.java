package com.live.fox.ui.openLiving;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.LiveFinishActivity;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityAnchorFinishLivingBinding;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.LiveClose;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.SPUtils;

public class AnchorLivingFinishActivity extends BaseBindingViewActivity {

    ActivityAnchorFinishLivingBinding mBind;


    public static void startActivity(Context context, String liveId, boolean isKick) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, AnchorLivingFinishActivity.class);
        intent.putExtra("liveId", liveId);
        intent.putExtra("isKick", isKick);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String liveId) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, AnchorLivingFinishActivity.class);
        intent.putExtra("liveId", liveId);
        context.startActivity(intent);
    }

    @Override
    public boolean isHasHeader() {
        return false;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.gtvBack:
                finish();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_anchor_finish_living;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        setWindowsFlag();
        doCloseRoomApi();
    }

    public void doCloseRoomApi() {
        showLoadingDialogWithNoBgBlack();

        String liveId =getIntent().getStringExtra("liveId");
        String uid= String.valueOf(DataCenter.getInstance().getUserInfo().getUser().getUid());
        boolean isKick =getIntent().getBooleanExtra("isKick",false);

        Api_Live.ins().liveStop(uid, liveId, isKick, new JsonCallback<LiveClose>() {
            @Override
            public void onSuccess(int code, String msg, LiveClose data) {
                hideLoadingDialog();
                if (data != null) LogUtils.e("liveStop result : " + new Gson().toJson(data));
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    mBind.ivLivingTimeCost.setText(minutesToString(data.getLiveMinutes()));
                    mBind.ivLivingProfit.setText(RegexUtils.formatNumber((long) data.getProfit()));
                }
            }
        });
    }

    private String minutesToString(int liveMinutes) {
        int h = liveMinutes / 60;
        int m = liveMinutes % 60;
        String str1 = h < 10 ? "0" + h : h + "";
        String str2 = m < 10 ? "0" + m : m + "";
        return str1 + ":" + str2;
    }
}
