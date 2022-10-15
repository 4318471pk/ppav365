package com.live.fox.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static boolean checkPhone(String tel){
        Pattern p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

}
