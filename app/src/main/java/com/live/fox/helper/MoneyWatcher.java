package com.live.fox.helper;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.live.fox.BuildConfig;

/**
 * 金额相关
 * EditText TextWatcher
 */
public class MoneyWatcher implements TextWatcher {

    private EditText editText;

    private String lastStr = "";

    private final Runnable mRunnable = () -> {
        setCurrentSel();
        editText.setOnClickListener(view -> setCurrentSel());
    };

    public MoneyWatcher(EditText editText) {
        this.editText = editText;
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.postDelayed(mRunnable, 100);
            }
        });
    }


    public void cancelSetSel() {
        editText.removeCallbacks(mRunnable);
    }

    private void setCurrentSel() {
        String str = editText.getText().toString();
        if (str.contains(",")) {
            int last = str.lastIndexOf(",");
            int maxSel = str.substring(0, last).length();
            int startSel = editText.getSelectionStart();
            if (startSel > maxSel) {
                editText.setSelection(maxSel);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


    @Override
    public void afterTextChanged(Editable editable) {
        String etStr = editable.toString();
        if (TextUtils.isEmpty(etStr) || lastStr.equals(etStr)) {

            return;
        }

        String endStr = "";
        if (!BuildConfig.AppFlavor.equals("ThiLive")) {
            endStr = ",000";
        }

        if (endStr.equals(etStr)) {
            editable.clear();
        } else if (!lastStr.equals(etStr)) {
            int eoCount = etStr.split(",").length - 1;//原字符串逗号数量
            String beforeStr = etStr.replaceAll(",", "");
            StringBuilder dealStr = new StringBuilder(beforeStr);
            int enCount = (beforeStr.length() - 1) / 3;//一共需要多少个逗号
            dealStr.reverse();

            for (int i = 0; i < enCount; i++) {
                dealStr.insert((i + 1) * 3 + i, ",");
            }

            dealStr.reverse();

            if (!etStr.equals(dealStr.toString())) {
                int sel = editText.getSelectionStart();
                editable.replace(0, etStr.length(), dealStr.toString());
                int cCount = enCount - eoCount;
                if (cCount > 0) {
                    editText.setSelection(sel + cCount);
                }
                lastStr = dealStr.toString();
                return;
            }
        }

        //非泰国盘下，不以,000结尾，就补充上,000
        if (!BuildConfig.AppFlavor.equals("ThiLive") && !lastStr.endsWith(",000")) {
            lastStr = editable.toString() + endStr;
        } else {
            lastStr = editable.toString();
        }

        editText.setText(lastStr);

    }

}
