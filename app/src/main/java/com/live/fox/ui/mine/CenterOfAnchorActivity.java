package com.live.fox.ui.mine;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.live.fox.AnchorLiveActivity;
import com.live.fox.BuildConfig;
import com.live.fox.Constant;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityCenterAnchorBinding;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.AuthActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

public class CenterOfAnchorActivity extends BaseBindingViewActivity {

    ActivityCenterAnchorBinding mBind;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, CenterOfAnchorActivity.class));
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_center_anchor;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        setActivityTitle(getString(R.string.centerOfAnchor));
        getTvTitleRight().setText(getResources().getString(R.string.goOpenSteam));
        getTvTitleRight().setTextColor(0xffFF008A);
        Drawable leftIcon = getDrawable(R.mipmap.icon_small_live);
        getTvTitleRight().setCompoundDrawablesRelativeWithIntrinsicBounds(leftIcon, null, null, null);
        getTvTitleRight().setCompoundDrawablePadding(ScreenUtils.dp2px(this, 3));
        getTvTitleRight().setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                openLive();
            }
        });

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(this));

    }

    private void openLive() {
        boolean careraPermission = false;
        boolean mircPermission = false;
        if (ContextCompat.checkSelfPermission(CenterOfAnchorActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            careraPermission = true;
        }

        if (ContextCompat.checkSelfPermission(CenterOfAnchorActivity.this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            mircPermission = true;
        }

        if (!careraPermission || !mircPermission) {
            RxPermissions rxPermissions = new RxPermissions(CenterOfAnchorActivity.this);
            Disposable subscribe = rxPermissions.request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        if (granted) {
                            checkAuth();
                        } else { // 有的权限被拒绝或被勾选不再提示
                            LogUtils.e("有的权限被拒绝");
                            new AlertDialog.Builder(CenterOfAnchorActivity.this)
                                    .setCancelable(false)
                                    .setMessage(getString(R.string.notePermission))
                                    .setPositiveButton(getString(R.string.see), (dialog, which) -> LogUtils.e("权限被拒绝"))
                                    .show();
                        }
                    });
        } else {
            checkAuth();
        }
    }

    /**
     * 开始直播
     */
    public void checkAuth() {
        Api_Live.ins().getAnchorAuth(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 && data != null) {
                    Log.e("checkAuth",data);
//                    try {
//                        JSONObject jb = new JSONObject(data);
//                        int auth = jb.optInt("auth");
//                        User user = DataCenter.getInstance().getUserInfo().getUser();
//                        if (user == null) {
//                            LogUtils.e("主播状态：" + "开启直播出错，用户信息失败");
//                            return;
//                        }
//
//                        user.setAuth(auth);
//                        DataCenter.getInstance().getUserInfo().updateUser(user);
//                        if (auth == 2 && BuildConfig.IsAnchorClient) {
//                            Constant.isAppInsideClick = true;
//                            Intent intent = new Intent(CenterOfAnchorActivity.this, AnchorLiveActivity.class);
//                            startActivity(intent);
//                        } else if (auth == 1) { //1待审核
//                            showToastTip(false, getString(R.string.certificating));
//                        } else { //未认证
//                            DialogFactory.showTwoBtnDialog(CenterOfAnchorActivity.this,
//                                    getString(R.string.certiGo), getString(R.string.cancel),
//                                    getString(R.string.goCerti), (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
//                                        dialog.dismiss();
//                                        AuthActivity.startActivity(CenterOfAnchorActivity.this);
//                                    });
//                        }
//                    } catch (Exception e) {
//                        e.getStackTrace();
//                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
