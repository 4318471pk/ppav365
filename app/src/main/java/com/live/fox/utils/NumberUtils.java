package com.live.fox.utils;


import android.os.Build;
import android.os.LocaleList;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

/**
 * getRandom             : 获得范围内的随机数
 */
public class NumberUtils {

    //获得范围内的随机数
    public static int getRandom(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }

    public static String getRandomString() {
        Random random = new Random();
        return random.nextInt(10) + " " + random.nextInt(10) + " " + random.nextInt(10) + " " + random.nextInt(10);
    }

    /**
     * 数字过大时，保留1位小数 并带一个单位万或者亿
     */
    public static String convertInt2(long num) {
//        LogUtils.e("rq:" + num);
        if (num <= 9999 && num >= 0) {
            return num + "";
        }
        if (num > 9999 && num <= 99999999) {
//            LogUtils.e("rq 2:" + num);
            float f = num / 10000.f;
            DecimalFormat decimalFormat = new DecimalFormat("##.0");
            return decimalFormat.format(f) + "W";
        } else if (num > 99999999 && num <= 999999999) {
//            LogUtils.e("rq 3:" + num);
            float f = num / 1000000f;
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            return decimalFormat.format(f) + "M";
        } else if (num > 999999999) {
            LogUtils.e("rq 4:" + num);
            float f = num / 1000000000f;
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            return decimalFormat.format(f) + "B";
        } else {
            return num + "";
        }
    }

    /**
     * 数字过大时，保留2位小数 并带一个单位万或者亿
     */
    public static String convertInt1(long num) {
        if (num <= 999 && num >= 0) {
            return String.valueOf(num);
        }
        if (num > 999 && num <= 999999) {
            float f = num / 1000f;
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            return decimalFormat.format(f).replace(",", ".") + "K";
        } else if (num > 999999 && num <= 999999999) {
            float f = num / 1000000f;
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            return decimalFormat.format(f).replace(",", ".") + "M";
        } else if (num > 999999999) {
            float f = num / 1000000000f;
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            return decimalFormat.format(f).replace(",", ".") + "B";
        } else {
            return String.valueOf(num);
        }
    }

    public static boolean isEnglish() {
        Locale locales = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locales = LocaleList.getDefault().get(0);
        } else locales = Locale.getDefault();

        String language = locales.getLanguage() + "-" + locales.getCountry();
        return language.startsWith("en");
    }

}
