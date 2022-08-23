package com.live.fox.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Local管理
 */
public class LocalManager {

    private static final List<Locale> localeList;

    static {
        localeList = new ArrayList<>();
        localeList.add(Locale.CHINA);
        localeList.add(new Locale("vi", "VN"));
        localeList.add(new Locale("th", "TH"));
    }

    /**
     * 通过地区获取 Local
     *
     * @param region 地区
     * @return local
     */
    public static Locale getLocalByRegion(String region) {
        Locale locale;
        switch (region) {
            case "vi":
                locale = new Locale("vi", "VN");
                break;

            case "th":
                locale = new Locale("th", "TH");
                break;

            case "cn":
            default:
                locale = Locale.CHINA;
                break;
        }

        return locale;
    }

    public static Locale getLocalByPosition(int position) {
        return localeList.get(position);
    }

    /**
     * 获取语言
     *
     * @param language
     * @return
     */
    public static Locale getLocalByLanguage(LanguageType language) {
        switch (language) {
            case CHINESE:
                return localeList.get(0);
            case Vietnam:
                return localeList.get(1);
            case Thailand:
                return localeList.get(2);

        }
        return localeList.get(0);
    }

}
