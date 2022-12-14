package com.live.fox;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lahm.library.EasyProtectorLib;
import com.live.fox.common.JsonCallback;
import com.live.fox.db.LocalGiftDao;
import com.live.fox.db.LocalMountResourceDao;
import com.live.fox.db.LocalSendGiftDao;
import com.live.fox.db.LocalUserGuardDao;
import com.live.fox.db.LocalUserLevelDao;
import com.live.fox.db.LocalUserTagResourceDao;
import com.live.fox.dialog.CommonDialog;
import com.live.fox.entity.BaseInfo;
import com.live.fox.entity.CountryCode;
import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.entity.SendGiftResourceBean;
import com.live.fox.entity.UserGuardResourceBean;
import com.live.fox.entity.UserLevelResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.manager.ResourceDownloadService;
import com.live.fox.svga.AdmissionManager;
import com.live.fox.svga.BadgesManager;
import com.live.fox.svga.GiftManager;
import com.live.fox.manager.SPManager;
import com.live.fox.network.ApiService;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Promotion;
import com.live.fox.server.Api_User;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * ??????????????????
 */


public class SplashPresenter {

    private final int TO_MainActivity = 0X01;
    private SplashActivity context;

    //????????????????????????
    boolean isHaveOpenScreen = false;
    boolean isOpenScreenFinish = true;

    //???????????????IM
    boolean isConnectIm = false;
    boolean isConnectImFinish = true;

    /**
     * ???Presenter???View????????????????????????????????????bindPresenter????????????
     */
    public SplashPresenter(SplashActivity context) {
        this.context = context;
    }

    List<String> domains = new ArrayList<>();

    /**
     * ????????????????????????????????????
     * ?????????????????????????????? ???????????????????????????????????????????????????P???
     */
    public void loadData() {
        SPManager.clearIsShownAppNotice();
        SPManager.clearIsShownAppUpdate();

        CommonDialog dialog = new CommonDialog();

        handDomains();

        if (Build.VERSION.SDK_INT < 21) {  //5.0????????????????????????
            showAlertDialog(context.getString(R.string.sjbbgdwfcw),
                    context.getString(R.string.see),
                    (dialogInterface, i) -> context.finish());
            return;
        }

//        if (!BuildConfig.DEBUG) {   //?????????????????? ?????????????????????????????????????????????
//            if (EasyProtectorLib.checkIsRunningInEmulator(context, null)) {
//                showAlertDialog(context.getString(R.string.czbzcmlqsyx),
//                        context.getString(R.string.see),
//                        (dialogInterface, i) -> context.finish());
//                return;
//            }
//        }
        doCheckPermissions();
       // context.goToMain();
    }

    /**
     * ??????????????????
     */
    private void handDomains() {
        domains = AppConfig.getAppDomains();
        SPUtils.getInstance().put(ConstantValue.BaseDomain, domains.get(0));
    }

    public void showAlertDialog(String msg, String btnTxt, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(btnTxt, onClickListener)
                .show();
    }

    /**
     * ????????????
     */
    public void doCheckPermissions() {
        RxPermissions rxPermissions = new RxPermissions(context);
        Disposable result = rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {  // ?????????????????????
                        doBaseApi();
                    } else {    // ?????????????????????????????????????????????
                        LogUtils.e("?????????????????????");
                        showAlertDialog(context.getString(R.string.smqx),
                                context.getString(R.string.immiPer),
                                (dialogInterface, i) -> doCheckPermissions());
                    }
                });
        if (result.isDisposed()) {
            LogUtils.e("Disposable");
        }
    }

    private final Handler mSplashHandler = new Handler(Looper.myLooper()){

        @Override
        public void dispatchMessage(@NonNull @NotNull Message msg) {
            super.dispatchMessage(msg);

            if (msg.what == TO_MainActivity) {   //??????????????????????????????????????????
                LogUtils.e("????????????");
                if (!isHaveOpenScreen && !isConnectIm && context != null) {
                    //???????????? && ???IM?????? ????????????
                    context.goToMain();
                    return ;
                }

                if (isHaveOpenScreen && isOpenScreenFinish && isConnectIm && isConnectImFinish) {
                    //????????????&&??????????????? && ???IM??????&&IM???????????? ????????????
                    if (context != null) {
                        context.goToMain();
                        return ;
                    }
                }

                if (isHaveOpenScreen && isOpenScreenFinish && !isConnectIm) {
                    //????????????&&??????????????? && ???IM?????? ????????????
                    if (context != null) {
                        context.goToMain();
                        return ;
                    }
                }

                if (isConnectIm && isConnectImFinish && !isHaveOpenScreen) {
                    //IM??????&&IM???????????? && ???????????? ????????????
                    if (context != null) {
                        context.goToMain();
                        return ;
                    }
                }
            }
        }
    };


    /**
     * ????????????????????????
     * ??????Base??????  ????????????????????????
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
                            .header("X-Language", MultiLanguageUtils.getRequestHeader());//????????????
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

                JsonObject body = response.body();
                if (body != null) {
                    LogUtils.e("doBaseApi: " + response.body().toString());
//                    doGetTraceInstall();//??????????????????
                    JsonObject data = body.getAsJsonObject("data");
                    JsonArray jsonArray = data.getAsJsonArray("configSystemBaseList");
                    if (data != null && !TextUtils.isEmpty(data.toString())) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                            String code=jsonObject.get("code").getAsString();
                            String value=jsonObject.get("value").getAsString();
                            if(code==null || value==null)
                            {
                                continue;
                            }

                            switch (jsonObject.get("code").getAsString()) {
                                case "maintain":
                                    //???????????? ????????????:1??????0??????
                                    if(value.equals("0"))
                                    {
                                        showAlertDialog(context.getString(R.string.xtwhz),
                                                context.getString(R.string.see),
                                                (dialogInterface, index) -> context.finish());
                                    }
                                    break;
                                case "shareUrl":
                                    //????????????
                                    SPUtils.getInstance().put(ConstantValue.shareUrl, value);
                                    break;
                                case "resourceDomain":
                                    //????????????
                                    if(!TextUtils.isEmpty(value) && value.length()>3)
                                    {
                                        if(value.endsWith("/"))
                                        {
                                            SPUtils.getInstance().put(ConstantValue.resourceDomain, value.substring(0,value.length()-1));
                                        }
                                        else
                                        {
                                            SPUtils.getInstance().put(ConstantValue.resourceDomain, value);
                                        }
                                        //??????????????????????????????
                                        ResourceDownloadService.startService(context);
                                    }
                                    break;
                            }
                        }

                        mSplashHandler.sendEmptyMessageDelayed(TO_MainActivity, 0);
                    } else {
                        ToastUtils.showShort(context.getString(R.string.jiexiWrong));
                        context.finish();
                    }
                } else {
                    ToastUtils.showShort(context.getString(R.string.jiexiWrong));
                    context.finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                LogUtils.e("doBaseApi onFailure: " + call.request().url() + "   " + t.getMessage());
            }
        });
    }



    /**
     * Base???????????????????????????????????????
     *
     * @param data ????????????????????????
     */
    public void doSomethingAfterDoBaseApiSuccess(String data) {
        try {
            LogUtils.e("AfterDoBase???start" + data);
//            BadgesManager.ins().downloadAllBadges();  //????????????

            AppConfig.upBaseData(data);
//            String imSdkIdKye = SPUtils.getInstance().getString(IM_SDK_APP_ID);
//            if (!imSdkIdKye.equals(baseInfo.getSdkappid())) {
//                SPUtils.getInstance().put(IM_SDK_APP_ID, baseInfo.getSdkappid());
//            }

//            if (baseInfo.getSdkappid() != null) {
//                AppIMManager.init(baseInfo.getSdkappid());  //?????????IM SDK
//            }

//            SPManager.saveShareUrl(baseInfo.getShareUrl());
//            SPManager.saveDomain(baseInfo.getDomain());  //oss.pycywj.com  oss.jwanfu.com
//            SPManager.saveDomainTwo(baseInfo.getDomainTwo());
//            SPManager.saveIsGameStart(baseInfo.getIsGameStart());
//            SPManager.saveIsCpStart(baseInfo.getIsCpStart());
//            SPManager.saveIsCpButton(baseInfo.getIsCpButton());

            //??????????????????????????????????????????
//            String accessId = baseInfo.getAndroidTpnsAccessId();
//            String accessKey = baseInfo.getAndroidTpnsAccessKey();
//            String accessSaved = SPUtils.getInstance().getString(ACCESS_ID);
//            String accessKeySaved = SPUtils.getInstance().getString(ACCESS_KEY);

//            if (!TextUtils.isEmpty(accessId) && !TextUtils.isEmpty(accessKey)) {
//                if (!accessSaved.equals(accessId) && !accessKey.equals(accessKeySaved)) {
//                    SPUtils.getInstance().put(ACCESS_ID, accessId);
//                    SPUtils.getInstance().put(ACCESS_KEY, accessKey);
//                }
//            }
//
//            if (baseInfo.getMaintain()) {  //????????????
//                showAlertDialog(context.getString(R.string.xtwhz),
//                        context.getString(R.string.see),
//                        (dialogInterface, i) -> context.finish());
//                return;
//            }

            GiftManager.ins().downloadAllGifts();  //????????????

            AdmissionManager.getInstance().downloadAdmissionGiftList();  //??????????????????

//            String openScreen = baseInfo.getOpenScreen();
//            if (!StringUtils.isEmpty(openScreen) && !openScreen.equals("null")) {   //???????????? ??????????????????
//                isHaveOpenScreen = true;
//                isOpenScreenFinish = false;
//                context.showAdView(openScreen, baseInfo.getOpenScreenUrl());
//            } else {
//                mSplashHandler.sendEmptyMessageDelayed(TO_MainActivity, 0);
//            }
        } catch (Exception e) {
            LogUtils.e("AfterDoBase???err" + e.getMessage());
            showAlertDialog(context.getString(R.string.net_work_error_tip),
                    context.getString(R.string.retry), (dialogInterface, i) -> doBaseApi());
        }
    }

    /**
     * ????????????????????????
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

        if (!SPManager.getISFirstInstall()) {  //???????????????
            SPManager.saveFirstInstall();

            Api_Promotion.ins().installStat(SPManager.getPuid(), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    LogUtils.e("installStat " + code + "," + msg);
                }
            });
        }
    }

}
