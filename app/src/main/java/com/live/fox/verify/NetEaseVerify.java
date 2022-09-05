package com.live.fox.verify;

import android.content.Context;

import com.live.fox.entity.NetEaseVerifyEntity;
import com.live.fox.server.Api_Auth;
import com.live.fox.common.JsonCallback;
import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaConfiguration;
import com.netease.nis.captcha.CaptchaListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 网易易盾严重帮助类
 */
public class NetEaseVerify {

    private static NetEaseVerify instance;
    private CaptchaConfiguration captchaConfiguration;
    private final List<CaptchaConfiguration.LangType> langTypes = new ArrayList<>();
    private String verificationNo;  //后台接口返回的验证方式
    private String setVerify;

    private NetEaseVerify() {
        langTypes.add(CaptchaConfiguration.LangType.LANG_ZH_CN);
        langTypes.add(CaptchaConfiguration.LangType.LANG_ZH_CN);    //1. 中文
        langTypes.add(CaptchaConfiguration.LangType.LANG_EN);       //2.英文
        langTypes.add(CaptchaConfiguration.LangType.LANG_ZH_TW);    //3.繁体
        langTypes.add(CaptchaConfiguration.LangType.LANG_JA);       //4.日文
        langTypes.add(CaptchaConfiguration.LangType.LANG_KO);       //5.韩文
        langTypes.add(CaptchaConfiguration.LangType.LANG_TH);       //6.泰文
        langTypes.add(CaptchaConfiguration.LangType.LANG_VI);       //7.越南语
        langTypes.add(CaptchaConfiguration.LangType.LANG_FR);       //8.法语
        langTypes.add(CaptchaConfiguration.LangType.LANG_RU);       //9.俄语
        langTypes.add(CaptchaConfiguration.LangType.LANG_AR);       //10.阿拉伯语
        langTypes.add(CaptchaConfiguration.LangType.LANG_DE);       //11.德语
        langTypes.add(CaptchaConfiguration.LangType.LANG_IT);       //12.意大利语
        langTypes.add(CaptchaConfiguration.LangType.LANG_HE);       //13.希伯来语
        langTypes.add(CaptchaConfiguration.LangType.LANG_HI);       //14.印地语
        langTypes.add(CaptchaConfiguration.LangType.LANG_ID);       //15.印尼语
        langTypes.add(CaptchaConfiguration.LangType.LANG_MY);       //16.缅甸语
        langTypes.add(CaptchaConfiguration.LangType.LANG_LO);       //17.老挝语
        langTypes.add(CaptchaConfiguration.LangType.LANG_MS);       //18.马来语
        langTypes.add(CaptchaConfiguration.LangType.LANG_PL);       //19.波兰语
        langTypes.add(CaptchaConfiguration.LangType.LANG_PT);       //20.葡萄牙语
        langTypes.add(CaptchaConfiguration.LangType.LANG_ES);       //21.西班牙语
        langTypes.add(CaptchaConfiguration.LangType.LANG_TR);       //22.土耳其语
    }

    public synchronized static NetEaseVerify getInstance() {
        if (instance == null) {
            instance = new NetEaseVerify();
        }
        return instance;
    }

    public void init(Context context, CaptchaListener listener) {
        Api_Auth.ins().getNetEaseVerify(new JsonCallback<NetEaseVerifyEntity>() {
            @Override
            public void onSuccess(int code, String msg, NetEaseVerifyEntity data) {
                if (data != null) {
                    verificationNo = data.getVerificationNo();
                    captchaConfiguration = new CaptchaConfiguration
                            .Builder()
                            .captchaId(data.getVerificationNo())
                            .listener(listener)
                            .languageType(getLanguage(data.getLanguage()))
                            .build(context);
                    Captcha captcha = Captcha.getInstance().init(captchaConfiguration);
                    captcha.validate();
                }
            }
        });
    }

    public String getSetVerify() {
        return setVerify;
    }

    public void setSetVerify(String setVerify) {
        this.setVerify = setVerify;
    }

    public String getVerificationNo() {
        return verificationNo;
    }

    /**
     * 设置语言
     *
     * @param language 语言类别
     * @return 类型
     */
    private CaptchaConfiguration.LangType getLanguage(int language) {
        return langTypes.get(language);
    }
}
