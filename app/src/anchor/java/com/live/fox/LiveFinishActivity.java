package com.live.fox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.LiveClose;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.rank.AnchorRankActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StatusBarUtil;
import com.makeramen.roundedimageview.RoundedImageView;


public class LiveFinishActivity extends BaseActivity {

    ImageView ivBg;
    RoundedImageView ivAvatar;
    TextView tvFinishtip;
    TextView ivLivelevel;
    TextView ivLivelength;
    TextView ivLiveprofit;
    TextView tvSure;
    LinearLayout layou_value;
    ProgressBar ivLoading;


    private Anchor anchor;
    String finishTip = "";
    boolean isKick = false; //是否是管理员或者后台强制关播

    public static void startActivity(Context context, Anchor anchor, String finishTip, boolean isKick) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, LiveFinishActivity.class);
        intent.putExtra("anchor", anchor);
        intent.putExtra("finishTip", finishTip);
        intent.putExtra("isKick", isKick);
        context.startActivity(intent);
    }


    public void getIntentVlaue() {
        Intent intent = getIntent();
        anchor = (Anchor) intent.getSerializableExtra("anchor");
        finishTip = intent.getStringExtra("finishTip");
        isKick = intent.getBooleanExtra("isKick", false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //保持屏幕常亮
        setContentView(R.layout.livefinish_activity);
        initView();

        StatusBarUtil.setStatusBarFulAlpha(LiveFinishActivity.this);
        BarUtils.setStatusBarVisibility(LiveFinishActivity.this, true);
        BarUtils.setStatusBarLightMode(LiveFinishActivity.this, true);

        getIntentVlaue();

        ActivityUtils.finishActivity(AnchorRankActivity.class);

        tvFinishtip.setText(finishTip);
        ivLivelevel.setText(getString(R.string.anchorLive));
        GlideUtils.loadCircleRingImage(LiveFinishActivity.this, anchor.getAvatar(),
                1, Color.WHITE, R.drawable.img_default,
                R.drawable.img_default, ivAvatar);
        GlideUtils.loadImage(LiveFinishActivity.this, anchor.getAvatar(), ivBg);
        layou_value.setVisibility(View.INVISIBLE);
        ivLoading.setVisibility(View.VISIBLE);

        doCloseRoomApi();
    }

    private void initView() {
        ivBg = findViewById(R.id.iv_bg);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvFinishtip = findViewById(R.id.tv_finishtip);
        ivLivelevel = findViewById(R.id.iv_livelevel);
        ivLivelength = findViewById(R.id.iv_livelength);
        ivLiveprofit = findViewById(R.id.iv_liveprofit);
        layou_value = findViewById(R.id.layou_value);
        ivLoading = findViewById(R.id.iv_loading);
        findViewById(R.id.tv_sure).setOnClickListener(view -> finish());
    }

    public void doCloseRoomApi() {
        Api_Live.ins().liveStop(anchor.getAnchorId(), anchor.getLiveId(), isKick, new JsonCallback<LiveClose>() {
            @Override
            public void onSuccess(int code, String msg, LiveClose data) {
                if (data != null) LogUtils.e("liveStop result : " + new Gson().toJson(data));
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    SPUtils.getInstance("liveforanchor").clear();
                    ivLoading.setVisibility(View.GONE);
                    layou_value.setVisibility(View.VISIBLE);
                    ivLivelength.setText(minutesToString(data.getLiveMinutes()));
                    ivLiveprofit.setText(RegexUtils.formatNumber((long) data.getProfit()));
                } else {
                    ivLoading.setVisibility(View.GONE);
                    LogUtils.e("关播失败:" + msg);
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
