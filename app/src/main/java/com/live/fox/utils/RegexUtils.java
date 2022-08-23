package com.live.fox.utils;


import android.text.TextUtils;

import com.live.fox.AppConfig;
import com.live.fox.utils.utilconstants.RegexConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * isMobileSimple : 验证手机号（简单）
 * isMobileExact  : 验证手机号（精确）
 * isTel          : 验证电话号码
 * isIDCard15     : 验证身份证号码 15 位
 * isIDCard18     : 验证身份证号码 18 位
 * isEmail        : 验证邮箱
 * isURL          : 验证 URL
 * isZh           : 验证汉字
 * isUsername     : 验证用户名
 * isDate         : 验证 dd-MM-yyyy 格式的日期校验，已考虑平闰年
 * isIP           : 验证 IP 地址
 * isMatch        : 判断是否匹配正则
 * getMatches     : 获取正则匹配的部分
 * getSplits      : 获取正则匹配分组
 * getReplaceFirst: 替换正则匹配的第一部分
 * getReplaceAll  : 替换所有正则匹配的部分
 */
public final class RegexUtils {

    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    ///////////////////////////////////////////////////////////////////////////
    // If u want more please visit http://toutiao.com/i6231678548520731137
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return whether input matches regex of simple mobile.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
//    public static boolean isMobileSimple(final CharSequence input) {
//        return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, input);
//    }

    /**
     * Return whether input matches regex of exact mobile.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMobileExact(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_MOBILE_EXACT, input);
    }

    /**
     * Return whether input matches regex of telephone number.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isTel(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_TEL, input);
    }

    /**
     * Return whether input matches regex of id card number which length is 15.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isIDCard15(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_ID_CARD15, input);
    }

    /**
     * Return whether input matches regex of id card number which length is 18.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isIDCard18(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_ID_CARD18, input);
    }

    /**
     * Return whether input matches regex of email.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isEmail(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_EMAIL, input);
    }

    /**
     * Return whether input matches regex of url.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isURL(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_URL, input);
    }

    /**
     * Return whether input matches regex of Chinese character.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isZh(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_ZH, input);
    }

    /**
     * Return whether input matches regex of username.
     * <p>scope for "a-z", "A-Z", "0-9", "_", "Chinese character"</p>
     * <p>can't end with "_"</p>
     * <p>length is between 6 to 20</p>.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isUsername(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_USERNAME, input);
    }

    public static boolean isPassword(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_PASSWORD, input);
    }

    /**
     * Return whether input matches regex of date which pattern is "dd-MM-yyyy".
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDate(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_DATE, input);
    }

    /**
     * Return whether input matches regex of ip address.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isIP(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_IP, input);
    }

    /**
     * Return whether input matches the regex.
     *
     * @param regex The regex.
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * Return the list of input matches the regex.
     *
     * @param regex The regex.
     * @param input The input.
     * @return the list of input matches the regex
     */
    public static List<String> getMatches(final String regex, final CharSequence input) {
        if (input == null) return null;
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * Splits input around matches of the regex.
     *
     * @param input The input.
     * @param regex The regex.
     * @return the array of strings computed by splitting input around matches of regex
     */
    public static String[] getSplits(final String input, final String regex) {
        if (input == null) return null;
        return input.split(regex);
    }

    /**
     * Replace the first subsequence of the input sequence that matches the
     * regex with the given replacement string.
     *
     * @param input       The input.
     * @param regex       The regex.
     * @param replacement The replacement string.
     * @return the string constructed by replacing the first matching
     * subsequence by the replacement string, substituting captured
     * subsequences as needed
     */
    public static String getReplaceFirst(final String input,
                                         final String regex,
                                         final String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * Replace every subsequence of the input sequence that matches the
     * pattern with the given replacement string.
     *
     * @param input       The input.
     * @param regex       The regex.
     * @param replacement The replacement string.
     * @return the string constructed by replacing each matching subsequence
     * by the replacement string, substituting captured subsequences
     * as needed
     */
    public static String getReplaceAll(final String input,
                                       final String regex,
                                       final String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }


    /**
     * 将double的数字格式化成
     * 000,000,000.00
     * 类型
     *
     * @param data 要格式化的double
     * @return 返回格式之后的字符
     */
    public static String westMoney(double data) {
        if (AppConfig.isThLive()) {
            return new DecimalFormat("#,##0.00").format(data);
        }
        return formatNumber((long) data);
    }


    /**
     * 将long类型的数字格式化成
     * 000,000,000
     *
     * @param data 需要格式化的long
     * @return 返回格式化好的
     */
    public static String formatNumber(long data) {
        String decimal = new DecimalFormat("#,###").format(data);
        if (decimal.contains(".")) {
            decimal = decimal.replace(".", ",");
        }
        return decimal;
    }

    /**
     * 将String的数字格式化成
     * 000,000,000.00
     * 类型
     *
     * @param data 要格式化的String
     * @return 返回格式之后的字符
     */
    public static String stringMoney(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        data = data.replace(",", "");
        return new DecimalFormat("#,##0.00").format(Double.parseDouble(data));
    }

    /**
     * 将String的数字格式化成
     * 000,000,000
     * 类型
     *
     * @param data 要格式化的String
     * @return 返回格式之后的字符
     */
    public static String stringEditTextMoney(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }

        if (data.length() < 3) {
            return data;
        }

        if (data.contains(".")) {  //包含小数点
            String[] split = data.split("\\.");
            if (split.length == 1) {
                return data;
            }

            if (split[1].length() == 1) {
                return new DecimalFormat("#,##0.0")
                        .format(Double.parseDouble(data.replace(",", "")));
            } else {
                return new DecimalFormat("#,##0.00")
                        .format(Double.parseDouble(data.replace(",", "")));
            }
        }

        long format = Long.parseLong(data.replace(",", ""));
        String formatResult = new DecimalFormat("#,###").format(format);
        String formatStr = formatResult.replace(".", ",");
        LogUtils.e("afterTextChanged : formatStr ->" + formatStr);
        return formatStr;
    }

}
