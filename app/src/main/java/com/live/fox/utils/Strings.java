package com.live.fox.utils;

import android.text.TextUtils;

import com.live.fox.ConstantValue;
import com.live.fox.manager.SPManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * String  工具类
 */
public final class Strings {

    private Strings() {
    }

    public static String toString(Collection collection) {
        if (collection == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----print collections-----" + collection.getClass().getName() + "-----------");
        stringBuilder.append("\n");
        int count = 0;
        for (Object obj : collection) {
            count++;
            stringBuilder.append(count + obj.toString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public static String toDateString(long timestamp) {
        if (0 == timestamp) {
            return "unknown";
        }
        Date date = new Date(timestamp);
        return date.toString();
    }

    public static String toString(String[][] array) {
        if (null == array || array.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(Arrays.toString(array[i]));
            builder.append("\n");
        }
        return builder.toString();
    }

    public static String[] toArray(String value, String divider) {
        if (null == value || value.length() == 0) {
            return new String[0];
        }
        String[] property = value.split(divider);
        return property;
    }

    public static String toString(String[] array, String divider) {
        if (null == array || array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String property : array) {
            builder.append(property);
            builder.append(divider);
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static boolean isNumAndLetterUnion(String string) {
        boolean hasletter = false;
        boolean hasnum = false;
        if (TextUtils.isEmpty(string)) return false;
        for (int i = 0; i < string.length(); i++) {
            int index = string.charAt(i);
            //65 90 A Z
            //97 122 a z
            //48 57 0 9
            if (index >= 65 && index <= 90) {
                hasletter = true;
            } else if (index >= 97 && index <= 122) {
                hasletter = true;
            } else if (index >= 48 && index <= 57) {
                hasnum = true;
            } else {
                return false;
            }
        }
        return hasnum && hasletter;
    }

    public static boolean isNumOrLetter(String string) {
        boolean hasLetterOrNum = false;
        if (TextUtils.isEmpty(string)) return false;
        for (int i = 0; i < string.length(); i++) {
            int index = string.charAt(i);
            //65 90 A Z
            //97 122 a z
            //48 57 0 9
            if (index >= 65 && index <= 90) {
                hasLetterOrNum = true;
            } else if (index >= 97 && index <= 122) {
                hasLetterOrNum = true;
            } else if (index >= 48 && index <= 57) {
                hasLetterOrNum = true;
            } else {
                return false;
            }
        }
        return hasLetterOrNum;
    }

    public static boolean isDigitOnly(String string) {
        if (TextUtils.isEmpty(string)) return false;
        for (int i = 0; i < string.length(); i++) {
            int index = string.charAt(i);
            //65 90 A Z
            //97 122 a z
            //48 57 0 9
            if (index >= 48 && index <= 57) {

            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean isLetterOnly(String string) {
        if (TextUtils.isEmpty(string)) return false;
        for (int i = 0; i < string.length(); i++) {
            int index = string.charAt(i);
            //65 90 A Z
            //97 122 a z
            //48 57 0 9
            if (index >= 65 && index <= 90) {
            } else if (index >= 97 && index <= 122) {
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean equalAmongof(String target, String... str) {
        if (str == null || target == null) {
            return false;
        }

        for (int i = 0; i < str.length; i++) {
            if (target.equals(str[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAmongof(String target, String... str) {
        if (str == null || target == null) {
            return false;
        }

        for (int i = 0; i < str.length; i++) {
            if (target.contains(str[i])) {
                return true;
            }
        }
        return false;
    }

    //length 代表保留几位
    public static String cutOff(BigDecimal val, int length) {
        if (val == null) {
            return "";
        }
        if(length==0)
        {
            return val.intValue()+"";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("##0.");
        for (int i = 0; i < length; i++) {
            stringBuilder.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(stringBuilder.toString());
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        String format = decimalFormat.format(val);
        return format;
    }

    //length 代表保留几位
    public static String cutOff(String val, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("##0.");
        for (int i = 0; i < length; i++) {
            stringBuilder.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(stringBuilder.toString());
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        if (!TextUtils.isEmpty(val)) {
            try {
                BigDecimal bigDecimal=new BigDecimal(val);
                if(length==0)
                {
                    return bigDecimal.intValue()+"";
                }
                String format = decimalFormat.format(bigDecimal);
                return format;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return val;
    }

    //length 代表保留几位
    public static String cutOff(Float val, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("##0.");
        for (int i = 0; i < length; i++) {
            stringBuilder.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(stringBuilder.toString());
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        if (val!=null) {
            try {
                BigDecimal bigDecimal=new BigDecimal(val);
                if(length==0)
                {
                    return bigDecimal.intValue()+"";
                }
                String format = decimalFormat.format(bigDecimal);
                return format;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return "";
    }

    //url connection
    public static String urlConnect(String downloadUrl) {
        String domain = SPUtils.getInstance().getString(ConstantValue.resourceDomain,"");
        boolean isDomainEndWithBackSlash=domain.endsWith("/");

        StringBuilder sb = new StringBuilder();

        if(!TextUtils.isEmpty(downloadUrl))
        {
            if (!downloadUrl.toLowerCase().startsWith("http") && !downloadUrl.toLowerCase().startsWith("https")) {
                sb.append(domain);
                if ( downloadUrl.startsWith("/")) {
                    sb.append(downloadUrl);
                } else {
                    sb.append("/").append(downloadUrl);
                }
            }
            else
            {
                return downloadUrl;
            }

        }
        else
        {
            return domain;
        }

        return sb.toString();
    }


    /***
     * 获取url 指定name的value;
     * @param url
     * @param name
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

    /***
     * 设置url 指定name的value;如果本身存在会被替换
     * @param url
     * @param name
     * @return
     */
    public static String setValueByName(String url, String name,String value) {
        if(TextUtils.isEmpty(url))return "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        StringBuilder sb=new StringBuilder();
        sb.append(url.substring(0,index+1));

        String[] keyValue = temp.split("&");

        boolean isExits=false;
        for (String str : keyValue) {
            sb.append("&");
            if (str.contains(name+"=")) {
                isExits=true;
                sb.append(name).append("=").append(value);
            }
            else
            {
                sb.append(str);
            }
        }

        if(!isExits)
        {
            sb.append("&").append(name).append("=").append(value);
        }
        return sb.toString();
    }


    /***
     * 添加url 指定name的value;
     * @param url
     * @param name
     * @return
     */

    public static String addValueByName(String url, String name,String value) {
        if(TextUtils.isEmpty(url))return "";

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(url);
        if(url.charAt(url.length()-1)=='?' || url.charAt(url.length()-1)=='&')
        {
            stringBuilder.append(name).append("=").append(value);
        }
        else
        {
            if(url.contains("&"))
            {
                stringBuilder.append("&").append(name).append("=").append(value);
            }
            else
            {
                if(url.contains("?"))
                {
                    stringBuilder.append("&").append(name).append("=").append(value);
                }
                else
                {
                    stringBuilder.append("?").append(name).append("=").append(value);
                }
            }
        }

        return stringBuilder.toString();

    }

}
