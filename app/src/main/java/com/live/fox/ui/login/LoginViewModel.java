package com.live.fox.ui.login;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.OssToken;
import com.live.fox.entity.RegisterEntity;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_User;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.manager.AppUserManger;
import com.live.fox.utils.Utils;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.live.fox.verify.NetEaseVerify;
import com.live.fox.verify.SimpleCaptchaListener;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<String> pageTitle = new MutableLiveData<>();
    private MutableLiveData<Boolean> pageBack = new MutableLiveData<>();
    private MutableLiveData<String> smsCodeTxt = new MutableLiveData<>();
    private MutableLiveData<Boolean> smsCodeIsFinish = new MutableLiveData<>();

    private MutableLiveData<LoginPageType> currentFragment = new MutableLiveData<>();

    private MutableLiveData<String> finish = new MutableLiveData<>();

    private MutableLiveData<String> mExceptionHint = new MutableLiveData<>();

    private MutableLiveData<String> mWarningHint = new MutableLiveData<>();
    private MutableLiveData<String> mSuccessHint = new MutableLiveData<>();

    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<String> pwd = new MutableLiveData<>();
    private MutableLiveData<String> cpwd = new MutableLiveData<>();
    private MutableLiveData<String> phonecode = new MutableLiveData<>();
    private MutableLiveData<String> nickname = new MutableLiveData<>();

    private MutableLiveData<Boolean> doCheckPermissionForResetpwd = new MutableLiveData<>();
    private MutableLiveData<Boolean> showLoadingDialog = new MutableLiveData<>();
    private MutableLiveData<Boolean> toMainActivity = new MutableLiveData<>();

    private LoginPageType currentPage;
    private LoginPageType theToModifyPageNext; //从哪个页面跳入完善用户信息界面的
    private WeakReference<Context> weakReference;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }


    public void loadData(LoginPageType currentPage, String phone, Context context) {
        this.currentPage = currentPage;
        username.setValue(phone);
        LogUtils.e(currentPage.toString() + ", " + phone);
        changePage(currentPage, phone, context);
    }

    public void changePage(LoginPageType page, String phone, Context context) {
        LogUtils.e("changePage==" + page);
        changePageTop(page, context);
        username.setValue(phone);
        currentFragment.setValue(page);
    }

    public void changePageTop(LoginPageType page, Context context) {
        switch (page) {
            case LoginByPhone://创建账号
                currentPage = LoginPageType.LoginByPhone;
                pageTitle.setValue(context.getString(R.string.loing_title_create_account));
                break;
            case LoginByPwd:////短信验证码
                currentPage = LoginPageType.LoginByPwd;
                pageTitle.setValue(context.getString(R.string.login_title_message));//短信验证码
                break;
            case ResetPwd://设置新密码(忘记密码）
                currentPage = LoginPageType.ResetPwd;
                pageTitle.setValue(context.getString(R.string.login_title_password));
                break;
            case SetPwd://设置新密码(没有设置过密码）
                currentPage = LoginPageType.SetPwd;
                pageTitle.setValue(context.getString(R.string.noSetPassword));
                break;
            case ModifyPwd://修改密码
                currentPage = LoginPageType.ModifyPwd;
                pageTitle.setValue(context.getString(R.string.password_change));
                break;
            case ModifyUserinfo://完善个人信息
                theToModifyPageNext = currentPage;
                currentPage = LoginPageType.ModifyUserinfo;
                pageTitle.setValue(context.getString(R.string.improveInformation));
                break;
        }
    }

    /**
     * 登录成功、完善用户信息成功后的统一处理
     *
     * @param context context
     */
    public void onLoginSuccess(Context context) {
        showLoadingDialog.setValue(true);
        Api_User.ins().getUserInfo(-1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    showLoadingDialog.setValue(false);
                    toMainActivity.setValue(true);
                } else {
                    DataCenter.getInstance().getUserInfo().loginOut();
                    showLoadingDialog.setValue(false);
                    if (code == 993) {
                        mExceptionHint.setValue(context.getString(R.string.accountStop));
                    } else {
                        mExceptionHint.setValue(msg);
                    }
                }
            }
        });
    }

    /**
     * 重置密码界面 点击确认按钮
     */
    public void onResetPassword(View v) {
        if (TextUtils.isEmpty(username.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.inputTel));
            return;
        }

        if (username.getValue() != null && username.getValue().length() != 10) {
            mExceptionHint.setValue(v.getContext().getString(R.string.telWrongInput));
            return;
        }

        if (TextUtils.isEmpty(phonecode.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.enter_verification_code));
            return;
        }
        if (TextUtils.isEmpty(pwd.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.newPassword));
            return;
        }

        if (pwd.getValue() != null) {
            if (pwd.getValue().length() < 6 || pwd.getValue().length() > 12) {
                mExceptionHint.setValue(v.getContext().getString(R.string.passworda));
                return;
            }
        }

        if (TextUtils.isEmpty(cpwd.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.pinputPassword));
            return;
        }

        if (cpwd.getValue() != null) {
            if (cpwd.getValue().length() < 6 || cpwd.getValue().length() > 12) {
                mExceptionHint.setValue(v.getContext().getString(R.string.passworda));
                return;
            }
        }

        if (!pwd.getValue().equals(cpwd.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.login_password_hint_different));
            return;
        }
        if (!RegexUtils.isPassword(pwd.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.long_account_password_rule));
            return;
        }
        doCheckPermissionForResetpwd.setValue(true);
    }

    /**
     * 注册发送验证吗
     */
    public void onGetPhoneCode(View v) {
        handleVerifyResult(v, 1);
    }

    /**
     * 找回密码
     */
    public void onGetPhoneCodeByFindPassword(View v) {
        handleVerifyResult(v, 3);
    }

    private void handleVerifyResult(View v, int type) {
        if (TextUtils.isEmpty(username.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.inputTel));
            return;
        }

        if (username.getValue() != null && username.getValue().length() != 10) {
            mExceptionHint.setValue(v.getContext().getString(R.string.telWrongInput));
            return;
        }

        NetEaseVerify.getInstance().init(v.getContext(), new SimpleCaptchaListener() {
            @Override
            public void onValidate(String result, String validate, String msg) {
                if (!TextUtils.isEmpty(validate)) {
                    Activity activity = (Activity) v.getContext();
                    activity.runOnUiThread(() -> {
                        RegisterEntity register = new RegisterEntity();
                        register.setName(username.getValue());
                        register.setType(String.valueOf(type));
                        register.setVerification(NetEaseVerify.getInstance().getVerificationNo());
                        register.setVerify(validate);
                        register.setVersion(AppUtils.getAppVersionName());
                        startPhoneCodeCountDown();
                        doSendPhoneCodeApi(register);
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.showShort(s);
            }
        });
    }

    PhoneCodeCountDowan phoneCodeCountDowan;

    public void startPhoneCodeCountDown() {
        if (phoneCodeCountDowan == null) {
            phoneCodeCountDowan = new PhoneCodeCountDowan(180 * 1000);
        }
        phoneCodeCountDowan.start();
    }

    public void doSendPhoneCodeApi(RegisterEntity register) {
        Api_Auth.ins().sendPhoneCode(register, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(data);
                if (code != 0) {
                    phoneCodeCountDowan.cancel();
                    phoneCodeCountDowan.onFinish();
                    mExceptionHint.setValue(msg);
                }
            }
        });
    }

    public void doResetPwdApi(Context context) {
        showLoadingDialog.setValue(true);
        Api_Auth.ins().resetPassword(username.getValue(), phonecode.getValue(), pwd.getValue(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(data);
                showLoadingDialog.setValue(false);
                if (code == 0) {
                    switch (currentPage) {
                        case ResetPwd:
                            ToastUtils.showShort(context.getString(R.string.resetPasswork));
                            break;
                        case SetPwd:
                            ToastUtils.showShort(context.getString(R.string.password_setting_success));
                            break;
                        case ModifyPwd:
                            ToastUtils.showShort(context.getString(R.string.password_change_success));
                            break;
                    }
                    pageBack.setValue(true);
                } else {
                    mExceptionHint.setValue(msg);
                }
            }
        });
    }

    public void doRegisterApi(View v) {
        showLoadingDialog.setValue(true);
        Api_Auth.ins().register(username.getValue(), phonecode.getValue(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                showLoadingDialog.setValue(false);
                if (data != null && data.equals("1")) {
                    changePage(LoginPageType.ModifyUserinfo, username.getValue(), v.getContext());
                } else {
                    mExceptionHint.setValue(v.getContext().getString(R.string.verification_code_error));
                }
            }
        });
    }

    class PhoneCodeCountDowan extends CountDownTimer {
        private static final int TIME_TASCK = 1000;

        public PhoneCodeCountDowan(long millisInFuture) {
            //millisInFuture倒计时总时间
            super(millisInFuture, TIME_TASCK);
        }

        @Override
        public void onFinish() {// 计时完毕
            smsCodeIsFinish.setValue(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            smsCodeIsFinish.setValue(false);
            smsCodeTxt.setValue("" + (millisUntilFinished / TIME_TASCK));
        }
    }

    /**
     * 完善信息界面 点击注册
     */
    public void onRegisterByModifyUserInfo(View v) {
        String content = nickname.getValue();
        if (StringUtils.isEmpty(content)) {
            mExceptionHint.setValue(v.getContext().getString(R.string.nicknameFull));
            return;
        }

        if (content.length() > 12) {
            mExceptionHint.setValue(v.getContext().getString(R.string.nicknameLong));
            return;
        }
        if (TextUtils.isEmpty(pwd.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.newPassword));
            return;
        }

        if (pwd.getValue() != null) {
            if (pwd.getValue().length() < 6 || pwd.getValue().length() > 12) {
                mExceptionHint.setValue(v.getContext().getString(R.string.passworda));
                return;
            }
        }

        if (TextUtils.isEmpty(cpwd.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.pinputPassword));
            return;
        }

        if (cpwd.getValue() != null) {
            if (cpwd.getValue().length() < 6 || cpwd.getValue().length() > 12) {
                mExceptionHint.setValue(v.getContext().getString(R.string.passworda));
                return;
            }
        }

        if (!pwd.getValue().equals(cpwd.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.login_password_hint_different));
            return;
        }

        if (!RegexUtils.isPassword(pwd.getValue())) {
            mExceptionHint.setValue(v.getContext().getString(R.string.long_account_password_rule));
            return;
        }
        isSubmitAllInfo = true;

        if (!isUploadImging) {//加载图片
            showLoadingDialog.setValue(true);
            updateUserInfoApi(v.getContext());
        } else {
            showLoadingDialog.setValue(true);
        }
    }

    boolean isSubmitAllInfo = false;  //是否提交所有信息
    boolean isUploadImging = false;//是否正在上传头像
    boolean uploadIsFinish = false;  //上传图片是否已回调

    Map<String, String> uploadMap = new HashMap<>();
    LocalMedia localMedia;
    String localImgPath; //最新选择的本地图片地址
    String uploadImgUrl = "";
    int sex = 1;
    OssToken mOssToken;

    public void getOssTokenAndUploadImg(Context context) {
        isUploadImging = true;

        if (mOssToken == null) {
            OkGoHttpUtil.getInstance().cancel("OssToken");
            Api_Config.ins().getOssToken(new JsonCallback<OssToken>() {
                @Override
                public void onSuccess(int code, String msg, OssToken ossToken) {
                    if (ossToken != null) LogUtils.e(ossToken.toString());
                    mOssToken = ossToken;
                    if (code == 0) {
                        if (ossToken != null) {
                            //开启线程
                            new Thread(() -> uploadImage(ossToken, context)).start();
                        } else {
                            isUploadImging = false;
                            showLoadingDialog.setValue(false);
                            mExceptionHint.setValue(context.getString(R.string.getMessageFail));
                        }
                    } else {
                        isUploadImging = false;
                        showLoadingDialog.setValue(false);
                        mExceptionHint.setValue(context.getString(R.string.connectFailRetry));
                    }
                }
            });
        } else {
            new Thread(() -> uploadImage(mOssToken, context)).start();
        }
    }

    OSS oss;

    public void uploadImage(OssToken ossToken, Context context) {
        weakReference = new WeakReference<>(context);
        if (oss == null) {
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossToken.getKey(), ossToken.getSecret(), ossToken.getToken());
            oss = new OSSClient(Utils.getApp(), ossToken.getEndpoint(), credentialProvider, new ClientConfiguration());
        }

        String startName = "";
        if (!StringUtils.isEmpty(getUsername().getValue())) {
            startName = getUsername().getValue().substring(3, 5);
        }

        // 上传文件名
        String uploadFileServerName = startName + System.currentTimeMillis() + "_avatar.png";
        String localFilePath = localMedia.isCompressed() ? localMedia.getCompressPath() : localMedia.getPath();

        LogUtils.e(uploadFileServerName + "   ," + localFilePath);

        uploadMap.put(uploadFileServerName, localFilePath);

        // 構造上傳請求
        PutObjectRequest put = new PutObjectRequest(ossToken.getBucketName(), uploadFileServerName, localFilePath);
        // 異步上傳時可以設置進度回調
        put.setProgressCallback((request, currentSize, totalSize) -> {
            //LogUtils.e("currentSize: " + currentSize + " totalSize: " + totalSize);
        });

        //上传图片有时候会卡主很长时间 这里程序判断上传图片8秒内没返回就作为失败处理
        uploadIsFinish = false;
        uploadIsFinishHandler.sendEmptyMessageDelayed(1, 20 * 1000);
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                LogUtils.e("上传成功");
                if (uploadIsFinish) {
                    //说明已经处理 这里就不在处理了
                    return;
                }
                uploadIsFinish = true;
                uploadIsFinishHandler.removeMessages(1);
                String serverImgUrl = oss.presignPublicObjectURL(ossToken.getBucketName(), uploadFileServerName);
                LogUtils.e(serverImgUrl + "," + uploadMap.get(serverImgUrl) + "," + localImgPath);
                LogUtils.e(StringUtils.getLastPart(serverImgUrl) + "   ," + uploadMap.get(StringUtils.getLastPart(serverImgUrl)) + "  ," + localImgPath);
                if (uploadMap.get(StringUtils.getLastPart(serverImgUrl)).equals(localImgPath)) {
                    uploadImgUrl = serverImgUrl.replace(ossToken.getBucketName() + "." + ossToken.getEndpoint(), SPManager.getDomain());
                    LogUtils.e("调用成功 服务器图片地址：imgUrl : " + uploadImgUrl);
                    if (isSubmitAllInfo) {
                        updateUserInfoApi(context);
                    } else {
                        isUploadImging = false;
                        isSubmitAllInfo = false;
                        showLoadingDialog.setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                showLoadingDialog.setValue(false);
                if (uploadIsFinish) {
                    //说明已经处理 这里就不在处理了
                    return;
                }

                LogUtils.e("onFailure");
                isUploadImging = false;
                isSubmitAllInfo = false;

                uploadIsFinish = true;
                uploadIsFinishHandler.removeMessages(1);

                // 請求異常
                if (clientExcepion != null) {
                    // 本地異常如網絡異常等
                    mExceptionHint.setValue(context.getString(R.string.networkFail));
                }
                if (serviceException != null) {
                    mExceptionHint.setValue(context.getString(R.string.networkFail));
                    // 服務異常
                    LogUtils.e(serviceException.getErrorCode());
                    LogUtils.e(serviceException.getRequestId());
                    LogUtils.e(serviceException.getHostId());
                    LogUtils.e(serviceException.getRawMessage());
                }
            }
        });
    }

    //上传图片有时候会卡主很长时间 这里程序判断上传图片8秒内没返回就作为失败处理
    private final Handler uploadIsFinishHandler = new Handler(msg -> {
        if (!uploadIsFinish) {
            LogUtils.e("onFailure");
            isUploadImging = false;
            //超过5秒 上传图片还没返回消息
            isSubmitAllInfo = false;
            showLoadingDialog.setValue(false);
            mExceptionHint.setValue(weakReference.get().getString(R.string.networkBad));
            return false;
        }
        return false;
    });

    //调用修改用户信息接口
    public void updateUserInfoApi(Context context) {
        Api_Auth.ins().modifyUserInfo(username.getValue(), cpwd.getValue(), uploadImgUrl, sex, nickname.getValue(),
                SPManager.getPuid(),
                new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        try {
                            showLoadingDialog.setValue(false);
                            if (data != null && code == 0) {
                                JSONObject jsonObject = new JSONObject(data);
                                String token = jsonObject.optString("token", "");
                                if (StringUtils.isEmpty(token)) {
                                    mExceptionHint.setValue(context.getString(R.string.tokenFail));
                                    return;
                                }
                                DataCenter.getInstance().getUserInfo().setToken(token);
                                onLoginSuccess(context);
                            } else {
                                mExceptionHint.setValue(msg);
                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                });
    }

    public MutableLiveData<String> getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(MutableLiveData<String> pageTitle) {
        this.pageTitle = pageTitle;
    }

    public MutableLiveData<String> getmExceptionHint() {
        return mExceptionHint;
    }

    public void setmExceptionHint(MutableLiveData<String> mExceptionHint) {
        this.mExceptionHint = mExceptionHint;
    }

    public MutableLiveData<String> getmWarningHint() {
        return mWarningHint;
    }

    public void setmWarningHint(MutableLiveData<String> mWarningHint) {
        this.mWarningHint = mWarningHint;
    }

    public MutableLiveData<String> getmSuccessHint() {
        return mSuccessHint;
    }

    public void setmSuccessHint(MutableLiveData<String> mSuccessHint) {
        this.mSuccessHint = mSuccessHint;
    }

    public MutableLiveData<String> getFinish() {
        return finish;
    }

    public void setFinish(MutableLiveData<String> finish) {
        this.finish = finish;
    }


    public MutableLiveData<LoginPageType> getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(MutableLiveData<LoginPageType> currentFragment) {
        this.currentFragment = currentFragment;
    }

    public LoginPageType getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(LoginPageType currentPage) {
        this.currentPage = currentPage;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public void setUsername(MutableLiveData<String> username) {
        this.username = username;
    }

    public MutableLiveData<String> getPwd() {
        return pwd;
    }

    public void setPwd(MutableLiveData<String> pwd) {
        this.pwd = pwd;
    }

    public MutableLiveData<String> getCpwd() {
        return cpwd;
    }

    public void setCpwd(MutableLiveData<String> cpwd) {
        this.cpwd = cpwd;
    }

    public MutableLiveData<String> getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(MutableLiveData<String> phonecode) {
        this.phonecode = phonecode;
    }

    public LoginPageType getTheToModifyPageNext() {
        return theToModifyPageNext;
    }

    public void setTheToModifyPageNext(LoginPageType theToModifyPageNext) {
        this.theToModifyPageNext = theToModifyPageNext;
    }

    public MutableLiveData<Boolean> getShowLoadingDialog() {
        return showLoadingDialog;
    }

    public void setShowLoadingDialog(MutableLiveData<Boolean> showLoadingDialog) {
        this.showLoadingDialog = showLoadingDialog;
    }

    public MutableLiveData<Boolean> getToMainActivity() {
        return toMainActivity;
    }

    public void setToMainActivity(MutableLiveData<Boolean> toMainActivity) {
        this.toMainActivity = toMainActivity;
    }

    public MutableLiveData<Boolean> getDoCheckPermissionForResetpwd() {
        return doCheckPermissionForResetpwd;
    }

    public void setDoCheckPermissionForResetpwd(MutableLiveData<Boolean> doCheckPermissionForResetpwd) {
        this.doCheckPermissionForResetpwd = doCheckPermissionForResetpwd;
    }

//    public MutableLiveData<Boolean> getDoCheckPermissionForRegister() {
//        return doCheckPermissionForRegister;
//    }
//
//    public void setDoCheckPermissionForRegister(MutableLiveData<Boolean> doCheckPermissionForRegister) {
//        this.doCheckPermissionForRegister = doCheckPermissionForRegister;
//    }

    public MutableLiveData<Boolean> getPageBack() {
        return pageBack;
    }

    public void setPageBack(MutableLiveData<Boolean> pageBack) {
        this.pageBack = pageBack;
    }

    public MutableLiveData<String> getSmsCodeTxt() {
        return smsCodeTxt;
    }

    public void setSmsCodeTxt(MutableLiveData<String> smsCodeTxt) {
        this.smsCodeTxt = smsCodeTxt;
    }

    public MutableLiveData<Boolean> getSmsCodeIsFinish() {
        return smsCodeIsFinish;
    }

    public void setSmsCodeIsFinish(MutableLiveData<Boolean> smsCodeIsFinish) {
        this.smsCodeIsFinish = smsCodeIsFinish;
    }

    public PhoneCodeCountDowan getPhoneCodeCountDowan() {
        return phoneCodeCountDowan;
    }

    public void setPhoneCodeCountDowan(PhoneCodeCountDowan phoneCodeCountDowan) {
        this.phoneCodeCountDowan = phoneCodeCountDowan;
    }

    public MutableLiveData<String> getNickname() {
        return nickname;
    }

    public void setNickname(MutableLiveData<String> nickname) {
        this.nickname = nickname;
    }

    public boolean isSubmitAllInfo() {
        return isSubmitAllInfo;
    }

    public void setSubmitAllInfo(boolean submitAllInfo) {
        isSubmitAllInfo = submitAllInfo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public LocalMedia getLocalMedia() {
        return localMedia;
    }

    public void setLocalMedia(LocalMedia localMedia) {
        this.localMedia = localMedia;
    }

    public String getLocalImgPath() {
        return localImgPath;
    }

    public void setLocalImgPath(String localImgPath) {
        this.localImgPath = localImgPath;
    }
}
