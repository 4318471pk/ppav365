package com.live.fox.language;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

import java.util.Locale;

/**
 * 切换应用内的语言
 */
public class MultiLanguageUtils {

    public static final String LANGUAGE = "language";
    public static final String COUNTRY = "country";
    public static Locale appLocal;

    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context);
        } else {
            setConfiguration(context);
            return context;
        }
    }

    /**
     * 创建应用的Resources
     *
     * @param context context
     * @return 创建设置的对应Local的context
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Context createConfigurationResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale systemLocale = getSystemLocale(context);

        //默认为中文
        //如果用户设置了语言，根据用户设置的语言显示语言
        Locale appLocal;
        String savedLocal = LanguageSp.getString(context, LANGUAGE);
        String savedCountry = LanguageSp.getString(context, COUNTRY);

        if (!TextUtils.isEmpty(savedLocal) && !TextUtils.isEmpty(savedCountry)) {  //用户设置过语言
            if (isSameLocal(systemLocale, savedLocal, savedCountry)) {
                appLocal = systemLocale;
            } else {
                appLocal = new Locale(savedLocal, savedCountry);
            }
        } else {
            appLocal = systemLocale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(new LocaleList(appLocal));
        } else {
            configuration.setLocale(appLocal);
        }
        MultiLanguageUtils.appLocal = appLocal;
        return context.createConfigurationContext(configuration);
    }

    /**
     * 判断和系统的Local是否一致
     *
     * @param systemLocale 系统Local
     * @param savedLocal   保存的Local信息
     * @param savedCountry 保存的国家
     * @return 是否一直
     */
    private static boolean isSameLocal(Locale systemLocale, String savedLocal, String savedCountry) {
        String appLanguage = systemLocale.getLanguage();
        String appCountry = systemLocale.getCountry();
        return appLanguage.equals(savedLocal) && appCountry.equals(savedCountry);
    }

    /**
     * 获取系统的Local
     *
     * @param context context
     * @return 系统Local
     */
    private static Locale getSystemLocale(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = configuration.getLocales().get(0);//获取系统首选语言
        } else {
            locale = configuration.locale;
        }
        return locale;
    }

    /**
     * 设置系统语言
     *
     * @param context context
     */
    private static void setConfiguration(Context context) {
        Locale systemLocal = getSystemLocale(context);
        //如果用户设置了语言，则以用户设置的为主
        Locale locale;
        String savedLocal = LanguageSp.getString(context, LANGUAGE);
        String savedCountry = LanguageSp.getString(context, COUNTRY);
        if (!TextUtils.isEmpty(savedLocal) && !TextUtils.isEmpty(savedCountry)) {
            if (isSameLocal(systemLocal, savedLocal, savedCountry)) {
                locale = systemLocal;
            } else {
                locale = new Locale(savedLocal, savedCountry);
            }
        } else {
            locale = systemLocal;
        }

        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        //语言更换生效的代码!
        resources.updateConfiguration(configuration, dm);
        appLocal=locale;
    }


    /**
     * 保存多语言信息到sp中
     *
     * @param locale 保存语言local
     */
    public static void saveLanguageInfo(Context context, Locale locale) {
        LanguageSp.saveString(context, LANGUAGE, locale.getLanguage());
        LanguageSp.saveString(context, COUNTRY, locale.getCountry());
    }

    /**
     * 更改应用语言
     */
    public static void changeAppLanguage(Context context, Locale locale) {
        changeLocal(context, locale, false);
    }

    /**
     * 更改应用语言
     */
    public static void changeAppLanguage(Context context, Locale locale, boolean isPersistence) {
        changeLocal(context, locale, isPersistence);
    }

    /**
     * 更改系统语言
     *
     * @param context       context
     * @param locale        local
     * @param isPersistence 是否保存
     */
    private static void changeLocal(Context context, Locale locale, boolean isPersistence) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            context.createConfigurationContext(configuration);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            }
        }
        resources.updateConfiguration(configuration, metrics);

        if (isPersistence) {
            appLocal = locale;
            saveLanguageInfo(context, locale);
        }
    }

    /**
     * 判断是否和保持的一直
     *
     * @param context 当前使用的context
     * @return 返回比对结果
     */
    public static boolean isSameWithSetting(Context context) {
        Locale locale = getSystemLocale(context);
        String language = locale.getLanguage();
        String country = locale.getCountry();

        String saveLanguage = LanguageSp.getString(context, LANGUAGE);
        String saveCountry = LanguageSp.getString(context, COUNTRY);
        return !language.equals(saveLanguage) || !country.equals(saveCountry);
    }

    /**
     * 多语言参数
     * String language；
     * CN:  中文简体
     * EN:  英语
     * YN:  越南语
     * TW: 中文繁体-台湾
     * YD:  印度语
     * THAI: 泰文
     *
     * @return 对应的语言
     */
    public static String getRequestHeader() {
        String language = appLocal.getLanguage();
        switch (language) {
            case "vi":
                return "YN";
            case "th":
                return "THAI";
            case "zh":
                return "CN";
            case "tw":
                return "TW";
            case "en":
                return "EN";
        }
        return "EN";
    }
}
