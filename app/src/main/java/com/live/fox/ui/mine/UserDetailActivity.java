package com.live.fox.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.ui.chat.ChatActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 用户详情
 */
public class UserDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivHead;
    private TextView tvName;
    private TextView tvIcon;
    private TextView tvCirclenum;
    private TextView tvFollownum;
    private TextView tvFansnum;
    private TextView tvIdnum;
    private TextView tvCopyId;
    private TextView tvCity;
    private TextView tvSign;
    private TextView btnFollow;
    private TextView btnLetter;
    private TextView tvEditInfo;
    private boolean isFollow;

    long uid;
    User user;

    public static void startActivity(Context context, long uid) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, UserDetailActivity.class);
        i.putExtra("uid", uid);
        context.startActivity(i);
    }

    public static void startActivityForResult(Activity activity, long uid) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra("uid", uid);
        activity.startActivityForResult(intent, 100);
    }

    public void getIntentData() {
        if (getIntent() != null) {
            uid = getIntent().getLongExtra("uid", 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdetatil_activity);

        getIntentData();
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, true);

        ivHead = findViewById(R.id.iv_head);
        tvName = findViewById(R.id.tv_name);
        tvIcon = findViewById(R.id.tv_icon);
        tvCirclenum = findViewById(R.id.tv_circlenum);
        tvFollownum = findViewById(R.id.tv_follownum);
        tvFansnum = findViewById(R.id.tv_fansnum);
        tvIdnum = findViewById(R.id.tv_idnum);
        tvCopyId = findViewById(R.id.tvCopyId);
        tvCity = findViewById(R.id.tv_city);
        tvSign = findViewById(R.id.tv_sign);
        btnFollow = findViewById(R.id.btn_follow);
        btnLetter = findViewById(R.id.btn_letter);
        tvEditInfo = findViewById(R.id.tvEditInfo);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_follow).setOnClickListener(this);
        findViewById(R.id.btn_letter).setOnClickListener(this);
        tvCopyId.setOnClickListener(this);
        tvEditInfo.setOnClickListener(this);

        if (uid == DataCenter.getInstance().getUserInfo().getUser().getUid()) {
            btnFollow.setVisibility(View.GONE);
            btnLetter.setVisibility(View.GONE);
            tvEditInfo.setVisibility(View.VISIBLE);
        }
        doGetUserInfoByUidApi(uid);
    }

    public void refreshPage(String userJson, Context context) {
        user = new Gson().fromJson(userJson, User.class);
        tvIcon.setText(ChatSpanUtils.ins().getAllIconSpan(user, context));
        tvCirclenum.setText("0");
        tvFollownum.setText(String.valueOf(user.getFollows()));
        tvFansnum.setText(String.valueOf(user.getFans()));
        tvIdnum.setText(getString(R.string.identity_id_2) + user.getUid());
        tvCity.setText(getString(R.string.city) + user.getCity());
        tvSign.setText(getString(R.string.sign) + (StringUtils.isEmpty(user.getSignature()) ? getString(R.string.noWrite) : user.getSignature()));

        GlideUtils.loadDefaultImage(UserDetailActivity.this, user.getAvatar(), ivHead);
        tvName.setText(user.getNickname());
        if (DataCenter.getInstance().getUserInfo().getUser().getUid() == user.getUid()) {
            btnFollow.setVisibility(View.GONE);
            btnLetter.setVisibility(View.GONE);
        } else {
            btnFollow.setVisibility(View.VISIBLE);
            btnLetter.setVisibility(View.VISIBLE);
        }

        updateFollow();
    }

    public void updateFollow() {
        if (user.isFollow()) {
            btnFollow.setBackgroundResource(R.drawable.shape_white_round_20);
            btnFollow.setTextColor(Color.parseColor("#868686"));
            btnFollow.setText(getString(R.string.focused));
            isFollow = true;
        } else {
            btnFollow.setBackgroundResource(R.drawable.btn1_userdetail);
            btnFollow.setTextColor(Color.WHITE);
            btnFollow.setText(getString(R.string.focus));
            isFollow = false;
        }
    }

    /**
     * 获取用户信息
     */
    public void doGetUserInfoByUidApi(long uid) {
        Api_User.ins().getUserInfo(uid, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    refreshPage(data, context);
                } else {
                    ToastUtils.showShort(msg);
                    finish();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitUserDetailActivity();
    }

    public void exitUserDetailActivity() {
        Intent intent = new Intent();
        intent.putExtra("ISFOLLOW", isFollow);
        intent.putExtra("FANNUM", user.getFans());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_back:
                Constant.isAppInsideClick = true;
                exitUserDetailActivity();
                break;
            case R.id.btn_follow:
                Api_User.ins().follow(user.getUid(), !user.isFollow(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        if (result != null) LogUtils.e("follow result : " + result);
                        if (code == 0 && result != null) {
                            user.setFollow(!user.isFollow());
                            user.setFans(user.isFollow() ? user.getFans() + 1 : user.getFans() - 1);
                            tvFansnum.setText(user.getFans() + "");
                            updateFollow();
                        }

                    }
                });
                break;
            case R.id.btn_letter:
                ChatActivity.startActivity(UserDetailActivity.this, user);
                break;
            case R.id.tvCopyId:
                ClipboardUtils.copyText(String.valueOf(user.getUid()));
                showToastTip(true, getString(R.string.userCopy));
                break;
            case R.id.tvEditInfo:
                EditUserInfoActivity.startActivity(this, DataCenter.getInstance().getUserInfo().getUser().getPhone());
                break;
        }
    }
}
