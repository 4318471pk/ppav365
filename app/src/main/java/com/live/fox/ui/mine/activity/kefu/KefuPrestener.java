package com.live.fox.ui.mine.activity.kefu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Kefu;
import com.live.fox.server.Api_Config;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by appleMac on 2020/2/28.
 */
public class KefuPrestener extends KefuContract.Presenter {

    /**
     * 让Presenter与View绑定，此地方为固定写法，bindPresenter时初始化
     */
    public KefuPrestener(KefuContract.View view) {
        attachView(view);
    }


    /**
     * 页面初始化后开始加载数据
     * 注：界面初始化控件后 需手动调用此方法，用于将流程转移到P层
     */
    public void loadData() {
        doGetServiceList();
    }

    public void doGetServiceList() {
        if (!isViewAttach()) return;
        Api_Config.ins().getServer(new JsonCallback<List<Kefu>>() {
            @Override
            public void onSuccess(int code, String msg, List<Kefu> data) {
                if (code == 0) {
                    if (isViewAttach()) mView.onGetServiceListSuccess(data);
                } else {
                    if (isViewAttach()) mView.showToastTip(false, msg);
                }
            }
        });
    }


    public void toBrowserByKefu(String url) {
        IntentUtils.toBrowser(mView.getCtx(), url);
    }

    public void openQQ(String qq, Context context) {
        mView.showLoadingDialog();
        ClipboardUtils.copyText(qq);
        ToastUtils.showShort(context.getString(R.string.customQQCut));
        //打開QQ
        openQQApp(qq);
    }

    public void openQQApp(String qq) {
        List<String> qqPakName = new ArrayList<>();
        qqPakName.add("com.tencent.mobileqq"); //QQ
        qqPakName.add("com.tencent.tim");      //TIM
        qqPakName.add("com.tencent.mobileqqi"); //QQ國際版
        qqPakName.add("com.tencent.qqlite"); //QQ 輕聊版
        qqPakName.add("com.tencent.minihd.qq"); //QQ HD
        qqPakName.add("com.tencent.qq.kddi"); //QQ日本版

        boolean isOpen = false;
        for (String pkg : qqPakName) {
            PackageManager packageManager = mView.getCtx().getPackageManager();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage(pkg);
            if (intent == null) {
                isOpen = false;
            } else {
                isOpen = true;
                mView.getCtx().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1")));
                return;
            }
        }

        if (isOpen == false) {
            mView.hideLoadingDialog();
        }
    }

    @Override
    public void onActivityResume(Activity activity) {
        super.onActivityResume(activity);
        mView.hideLoadingDialog();
    }

    /**
     * 取消页面的所有请求及弹框
     * 注：界面onDestory时会主动调用此方法
     */
    public void cancelAllHttpAndDialog() {
        LogUtils.e("cancelAllHttpAndDialog");
        OkGoHttpUtil.getInstance().cancel(Api_Config.getCserver);
    }

}
