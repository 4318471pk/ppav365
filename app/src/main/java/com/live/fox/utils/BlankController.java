package com.live.fox.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class BlankController implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        //不能输入空格
        if (source.equals(" "))// || source.toString().contentEquals("\n")
            return "";
        else
            return null;
    }
}
