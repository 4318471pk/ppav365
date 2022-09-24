package com.live.fox;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.live.fox.entity.BaseInfo;
import com.live.fox.language.LanguageType;
import com.live.fox.language.LocalManager;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.view.refreshhead.ClassicsHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 项目配置
 * 常量设置
 */
public class AppConfig {

    private static BaseInfo baseInfo;
    private static boolean isMultiLanguage = false; //是否是多语言
    private static LanguageType languageType = LanguageType.CHINESE;

    public static boolean isThLive() {
        return BuildConfig.AppFlavor.equals("ThiLive");
    }

    public static boolean isMMLive() {
        return BuildConfig.AppFlavor.equals("MMLive");
    }

    /**
     * 获取项目的域名
     *
     * @return 返回域名列表
     */
    public static List<String> getAppDomains() {
        List<String> domains = new ArrayList<>();
        if (Constant.isPublish) {
            switch (BuildConfig.AppFlavor) {
                case "MMLive":
                    domains.add("https://aa.mmconfig.com/");
                    domains.add("https://aa.mm-gateway.com/");
                    break;

                case "QQLive":
                    domains.add("https://www.qianyiec.com/");
                    domains.add("https://www.hieioojg.com/");
                    break;

                case "AiAi":
                    domains.add("https://www.aiaiconfig.com/");
                    domains.add("https://www.aiaigateway.com/");
                    domains.add("https://dd.aiaiconfig.com/");
                    domains.add("https://dd.aiaigateway.com/");
                    break;

                case "Live24":
                    domains.add("https://www.24live-config.com/");
                    domains.add("https://www.24live-gateway.com/");
                    break;

                case "ThiLive":
                    domains.add("https://ww1.thlive-config.com/");
                    domains.add("https://ww2.thlive-config.com/");
                    break;
            }
        } else {
            domains.add(Constant.BASEURL_TEST);
        }
        return domains;
    }


    /**
     * 汇率
     *
     * @return 返回汇率比例
     */
    public static String getExchangeRatio() {
        String str;
        if (AppConfig.isThLive()) {
            str = "1";
        }

        str = "1000";

        if (baseInfo != null) {
            if (!TextUtils.isEmpty(baseInfo.getCurrency())) {
                str = baseInfo.getCurrency();
            }
        }
        return str;
    }

    /**
     * 设置下拉刷新、下拉加载控件默认样式
     * （优先级最低，如果代码中使用了其他样式 则此样式会被直接覆盖）
     */
    public static void initSmartRefreshLayout(Activity activity) {
        //下拉刷新默认样式
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(activity));

        //上拉加载默认样式
        ClassicsFooter.REFRESH_FOOTER_LOADING = activity.getString(R.string.loading);
        ClassicsFooter.REFRESH_FOOTER_FINISH = "";
    }

    public static void upBaseData(String jsonData) {
        baseInfo = new Gson().fromJson(jsonData, BaseInfo.class);
    }

    /**
     * 获取货币符号
     *
     * @return 火币符号
     */
    public static String getCurrencySymbol() {
        String sym = "₫";
        if (AppConfig.isThLive()) {
            sym = "฿";
        }

        if (baseInfo != null) {
            sym = baseInfo.getCurrencySymbol();
        }
        return sym;
    }

    /**
     * 最低提现
     *
     * @return 提现金额
     */
    public static String getWithdrawLowest() {
        String ratio = "";
        if (TextUtils.isEmpty(baseInfo.getMinWithdraw())) {
            switch (BuildConfig.AppFlavor) {
                case "MMLive":
                case "Live24":
                case "AiAi":
                case "QQLive":
                    ratio = "200,000";
                    break;

                case "ThiLive":
                    ratio = "200";
                    break;
            }
        } else {
            ratio = RegexUtils.westMoney(Double.parseDouble(baseInfo.getMinWithdraw()));
        }
        return ratio;
    }

    /**
     * 获取落地页
     *
     * @return 返回落地页
     */
    public static String getLandingPage() {
        return baseInfo.getFloorUrl();
    }

    /**
     * 获取应用基础配置
     *
     * @return 应用配置信息
     */
    public static BaseInfo getBaseInfo() {
        return baseInfo;
    }

    /**
     * 是否需要多语言
     *
     * @return 返回是否配置多语言
     */
    public static boolean isMultiLanguage() {
        return isMultiLanguage;
    }

    /**
     * 设置语言
     *
     * @param context context
     */
    public static void setAppLanguage(Context context) {
        if (!BuildConfig.DEBUG && !AppConfig.isMultiLanguage()) {
            Locale locale = LocalManager.getLocalByLanguage(languageType);
            MultiLanguageUtils.changeAppLanguage(context, locale, true);
        }
    }
}
