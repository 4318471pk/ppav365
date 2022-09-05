package com.live.fox.language;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 需要在应用添加Context的时候就需要需要使用
 * 项目中导入Utils还没有初始完成，无法使用
 */
public class LanguageSp {

    private static final String NAME = "language.cfg";
    private static SharedPreferences sp;


    public static synchronized void saveString(Context ctx, String key, String value) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).apply();
    }

    public static String getString(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, "");
    }

    public static String getString(Context ctx, String key, String defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    public static void remove(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).apply();
    }
}
