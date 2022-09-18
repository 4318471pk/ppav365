package com.live.fox;

import static com.live.fox.Constant.SPUtilKey.ACCESS_ID;
import static com.live.fox.Constant.SPUtilKey.ACCESS_KEY;
import static com.live.fox.Constant.SPUtilKey.IM_SDK_APP_ID;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lahm.library.EasyProtectorLib;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.CommonDialog;
import com.live.fox.entity.BaseInfo;
import com.live.fox.entity.CountryCode;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.svga.AdmissionManager;
import com.live.fox.svga.BadgesManager;
import com.live.fox.svga.GiftManager;
import com.live.fox.manager.SPManager;
import com.live.fox.network.ApiService;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Promotion;
import com.live.fox.server.Api_User;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.net.shoot.sharetracesdk.AppData;
import cn.net.shoot.sharetracesdk.ShareTrace;
import cn.net.shoot.sharetracesdk.ShareTraceInstallListener;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by appleMac on 2020/2/28.
 * 启动页主持类
 */

public class SplashPresenter {

    private final int TO_MainActivity = 0X01;
    private SplashActivity context;

    //是否有启屏广告页
    boolean isHaveOpenScreen = false;
    boolean isOpenScreenFinish = true;

    //是否要连接IM
    boolean isConnectIm = false;
    boolean isConnectImFinish = true;

    /**
     * 让Presenter与View绑定，此地方为固定写法，bindPresenter时初始化
     */
    public SplashPresenter(SplashActivity context) {
        this.context = context;
    }

    List<String> domains = new ArrayList<>();

    /**
     * 页面初始化后开始加载数据
     * 注：界面初始化控件后 需手动调用此方法，用于将流程转移到P层
     */
    public void loadData() {
        SPManager.clearIsShownAppNotice();
        SPManager.clearIsShownAppUpdate();

        CommonDialog dialog = new CommonDialog();

        handDomains();

        if (Build.VERSION.SDK_INT < 21) {  //5.0以下的手機不讓玩
            showAlertDialog(context.getString(R.string.sjbbgdwfcw),
                    context.getString(R.string.see),
                    (dialogInterface, i) -> context.finish());
            return;
        }

//        if (!BuildConfig.DEBUG) {   //模拟器不让玩 此方法可以检测出绝大部分模拟器
//            if (EasyProtectorLib.checkIsRunningInEmulator(context, null)) {
//                showAlertDialog(context.getString(R.string.czbzcmlqsyx),
//                        context.getString(R.string.see),
//                        (dialogInterface, i) -> context.finish());
//                return;
//            }
//        }
        doCheckPermissions();
    }

    /**
     * 设置基础域名
     */
    private void handDomains() {
        domains = AppConfig.getAppDomains();
    }

    public void showAlertDialog(String msg, String btnTxt, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(btnTxt, onClickListener)
                .show();
    }

    public void showAlertDialog(String msg, String btnTxt, DialogInterface.
            OnClickListener onPositiveClickListener,
                                DialogInterface.OnClickListener onNegativeClickListener) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(btnTxt, onPositiveClickListener)
                .setNegativeButton(context.getString(R.string.cancel), onNegativeClickListener)
                .show();
    }

    /**
     * 检查权限
     */
    public void doCheckPermissions() {
        RxPermissions rxPermissions = new RxPermissions(context);
        Disposable result = rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {  // 所有权限都同意
                        doBaseApi();
                    } else {    // 有的权限被拒绝或被勾选不再提示
                        LogUtils.e("有的权限被拒绝");
                        showAlertDialog(context.getString(R.string.smqx),
                                context.getString(R.string.immiPer),
                                (dialogInterface, i) -> doCheckPermissions());
                    }
                });
        if (result.isDisposed()) {
            LogUtils.e("Disposable");
        }
    }

    private final Handler mSplashHandler = new Handler(msg -> {

        if (msg.what == TO_MainActivity) {   //如果是登錄狀態，直接進入首頁
            LogUtils.e("跳主界面");
            if (!isHaveOpenScreen && !isConnectIm && context != null) {
                //无开屏页 && 无IM连接 跳主界面
                MainActivity.startActivity(context);
                context.finish();
            }

            if (isHaveOpenScreen && isOpenScreenFinish && isConnectIm && isConnectImFinish) {
                //有开屏页&&开屏页结束 && 有IM连接&&IM连接成功 跳主界面
                if (context != null) {
                    MainActivity.startActivity(context);
                    context.finish();
                }
            }

            if (isHaveOpenScreen && isOpenScreenFinish && !isConnectIm) {
                //有开屏页&&开屏页结束 && 无IM连接 跳主界面
                if (context != null) {
                    MainActivity.startActivity(context);
                    context.finish();
                }
            }

            if (isConnectIm && isConnectImFinish && !isHaveOpenScreen) {
                //IM连接&&IM连接成功 && 无开屏页 跳主界面
                if (context != null) {
                    MainActivity.startActivity(context);
                    context.finish();
                }
            }
        }
        return false;
    });


    /**
     * 请求基础配置信息
     * 請求Base接口  获取应用配置信息
     */
    public void doBaseApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .callTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("X-Language", MultiLanguageUtils.getRequestHeader());//请求语言
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(domains.get(0))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<JsonObject> configInfo = apiService.getConfigInfo(String.valueOf(3));
        configInfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                LogUtils.e("doBaseApi onResponse: " +call.request().url());
                JsonObject body = response.body();
                if (body != null) {
                    SPUtils.getInstance("basedomain").put("domain", domains.get(0));
                    doGetTraceInstall();
                    JsonObject data = body.getAsJsonObject("data");
                    if (data != null && !TextUtils.isEmpty(data.toString())) {
                        doSomethingAfterDoBaseApiSuccess(data.toString());
                    } else {
                        context.finish();
                    }
                } else {
                    context.finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                LogUtils.e("doBaseApi onFailure: " + call.request().url() + "   " + t.getMessage());
                domains.remove(0);
                if (domains.size() > 0) { //请求失败，尝试下一个域名
                    doBaseApi();
                } else { //基础域名都挂了
                    showAlertDialog(context.getString(R.string.lianjieFail),
                            context.getString(R.string.retrya),
                            (dialogInterface, i) -> {
                                LogUtils.e("Dialog try again");
                                handDomains();
                                doBaseApi();
                            },
                            (dialogInterface, i) -> {
                                LogUtils.e("Dialog finish");
                                context.finish();
                            });
                }
            }
        });
    }

    /**
     * Base接口请求成功后做的一些操作
     *
     * @param data 后台返回基础数据
     */
    public void doSomethingAfterDoBaseApiSuccess(String data) {
        try {
            LogUtils.e("AfterDoBase：start" + data);
            BadgesManager.ins().downloadAllBadges();  //下載徽章
            BaseInfo baseInfo = new Gson().fromJson(data, BaseInfo.class);

            if (StringUtils.isEmpty(baseInfo.getAppServiceUrl())) {
                showAlertDialog(context.getString(R.string.net_work_error_tip),
                        context.getString(R.string.retry), (dialogInterface, i) -> doBaseApi());
                return;
            }

            AppConfig.upBaseData(data);
            String imSdkIdKye = SPUtils.getInstance().getString(IM_SDK_APP_ID);
            if (!imSdkIdKye.equals(baseInfo.getSdkappid())) {
                SPUtils.getInstance().put(IM_SDK_APP_ID, baseInfo.getSdkappid());
            }

            if (baseInfo.getSdkappid() != null) {
                AppIMManager.init(baseInfo.getSdkappid());  //初始换IM SDK
            }

            SPManager.saveServerDomain(baseInfo.getAppServiceUrl());
            SPManager.saveShareUrl(baseInfo.getShareUrl());
            SPManager.saveDomain(baseInfo.getDomain());  //oss.pycywj.com  oss.jwanfu.com
            SPManager.saveDomainTwo(baseInfo.getDomainTwo());
            SPManager.saveIsGameStart(baseInfo.getIsGameStart());
            SPManager.saveIsCpStart(baseInfo.getIsCpStart());
            SPManager.saveIsCpButton(baseInfo.getIsCpButton());

            //推送消息如果后台变更了就更新
            String accessId = baseInfo.getAndroidTpnsAccessId();
            String accessKey = baseInfo.getAndroidTpnsAccessKey();
            String accessSaved = SPUtils.getInstance().getString(ACCESS_ID);
            String accessKeySaved = SPUtils.getInstance().getString(ACCESS_KEY);

            if (!TextUtils.isEmpty(accessId) && !TextUtils.isEmpty(accessKey)) {
                if (!accessSaved.equals(accessId) && !accessKey.equals(accessKeySaved)) {
                    SPUtils.getInstance().put(ACCESS_ID, accessId);
                    SPUtils.getInstance().put(ACCESS_KEY, accessKey);
                }
            }

            if (baseInfo.getMaintain()) {  //系统维护
                showAlertDialog(context.getString(R.string.xtwhz),
                        context.getString(R.string.see),
                        (dialogInterface, i) -> context.finish());
                return;
            }

            GiftManager.ins().downloadAllGifts();  //下載禮物

            AdmissionManager.getInstance().downloadAdmissionGiftList();  //下载入场动画

            String openScreen = baseInfo.getOpenScreen();
            if (!StringUtils.isEmpty(openScreen) && !openScreen.equals("null")) {   //有启屏页 显示启屏广告
                isHaveOpenScreen = true;
                isOpenScreenFinish = false;
                context.showAdView(openScreen, baseInfo.getOpenScreenUrl());
            } else {
                mSplashHandler.sendEmptyMessageDelayed(TO_MainActivity, 0);
            }
        } catch (Exception e) {
            LogUtils.e("AfterDoBase：err" + e.getMessage());
            showAlertDialog(context.getString(R.string.net_work_error_tip),
                    context.getString(R.string.retry), (dialogInterface, i) -> doBaseApi());
        }
    }

    /**
     * 自动获取安装参数
     */
    private void doGetTraceInstall() {
        ShareTrace.getInstallTrace(new ShareTraceInstallListener() {
            @Override
            public void onInstall(AppData data) {
                String channelCode = data.getParamsData();
                if (StringUtils.isEmpty(channelCode)) {
                    SPManager.savePuid("0");
                } else {
                    SPManager.savePuid(channelCode);
                }
            }

            @Override
            public void onError(int code, String msg) {
                Log.i("ShareTraceTest", "onInstall,  Get install trace info error. code==" + msg);
            }
        });

        if (!SPManager.getISFirstInstall()) {  //第一次安装
            LogUtils.e("Banner 安装");
            SPManager.saveFirstInstall();

            Api_Promotion.ins().installStat(SPManager.getPuid(), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    LogUtils.e("installStat " + code + "," + msg);
                }
            });
        }
    }

    /**
     * 取消页面的所有请求及弹框
     * 注：界面onDestory时会主动调用此方法
     */
    public void cancelAllHttpAndDialog() {
        LogUtils.e("cancelAllHttpAndDialog");
        OkGoHttpUtil okGoHttpUtil = OkGoHttpUtil.getInstance();
        okGoHttpUtil.cancel(Api_Config.getBaseConfig);
        okGoHttpUtil.cancel(Api_Config.getAdVert);
        okGoHttpUtil.cancel(Api_Auth.refreshToken);
        okGoHttpUtil.cancel(Api_User.getUserInfo);
    }
}
