package com.live.fox.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.live.fox.AppConfig;
import com.live.fox.R;

import java.lang.ref.WeakReference;

/**
 * 金额相关的帮助类
 */
public class MoneyTextWatcher implements TextWatcher {

    private final WeakReference<EditText> editTextWeak;
    private String lastChangeStr = ""; //最后变化的文本
    private int selectionPosition = -1; //光标的位置
    private int lastSelect; //上次光标的位置
    public final static String SPLICING = ",000";
    private boolean isNeedSplicing = true;
    private AfterTextChangedListener listener;
    private final int maxInput = 9; //最大输入
    private String maxMoney;

    public MoneyTextWatcher(EditText editText, AfterTextChangedListener afterTextChangedListener) {
        listener = afterTextChangedListener;
        editTextWeak = new WeakReference<>(editText);
        editTextWeak.get().setOnClickListener(v -> changeSelection());
        editTextWeak.get().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                changeSelection();
            }
        });
    }

    public MoneyTextWatcher(EditText editText) {
        editTextWeak = new WeakReference<>(editText);
        editTextWeak.get().setOnClickListener(v -> changeSelection());
        editTextWeak.get().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                changeSelection();
            }
        });
    }

    public void setNeedSplicing(boolean needSplicing) {
        isNeedSplicing = needSplicing;
    }

    public void setMaxMoney(String maxMoney) {
        this.maxMoney = maxMoney;
    }

    /**
     * 改变光标的位置
     */
    private void changeSelection() {
        String editStr = editTextWeak.get().getText().toString();
        int position = editTextWeak.get().getSelectionStart(); //光标的位置
        int lastPosition = editStr.length() - SPLICING.length(); //最后能选择的位置
        if (position > lastPosition) {
            selectionPosition = lastPosition;
            if (selectionPosition > 0) {
                editTextWeak.get().setSelection(selectionPosition);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        lastSelect = editTextWeak.get().getSelectionStart();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String money = charSequence.toString();
        LogUtils.e("onTextChanged : " + lastSelect);

        //最后变化的文本和改变后的文本相同
        if (!TextUtils.isEmpty(lastChangeStr) && !TextUtils.isEmpty(money)) {
            if (lastChangeStr.equals(money)) {
                return;
            }
        }

        //第一个数字不能是（.）点
        if (money.startsWith(".")) {
            editTextWeak.get().setText("");
            return;
        }

        //如果第一个数字为0，第二个不为点，就不允许输入
        if (money.startsWith("0") && money.trim().length() > 1) {
            String second = money.substring(1, 2);
            if (!TextUtils.equals(".", second)) {
                editTextWeak.get().setText("0");
                editTextWeak.get().setSelection(0);
                return;
            }
        }

        // 限制最多能输入9位整数
        if (money.contains(".")) {
            if (money.indexOf(".") > maxInput) {
                charSequence = money.subSequence(0, maxInput) + money.substring(money.indexOf("."));
                editTextWeak.get().setText(charSequence);
                editTextWeak.get().setSelection(9);
            }
        } else {
            if (money.length() > maxInput) {
                charSequence = money.subSequence(0, maxInput);
                editTextWeak.get().setText(charSequence.toString());
                editTextWeak.get().setSelection(9);
            }
        }

        //判断小数点后只能输入两位
        if (money.contains(".")) {
            if (charSequence.length() - 1 - money.indexOf(".") > 2) {
                charSequence = money.subSequence(0,
                        money.indexOf(".") + 3);
                editTextWeak.get().setText(charSequence);
                editTextWeak.get().setSelection(charSequence.length());
            }
        }
        selectionPosition = editTextWeak.get().getSelectionStart();
    }

    @Override
    public void afterTextChanged(Editable editable) {

        String afterChang = editable.toString();
        if (afterChang.equals(SPLICING)) {
            editTextWeak.get().setText("");
            return;
        }

        if (TextUtils.isEmpty(afterChang)) {
            if (listener != null) {
                listener.onTextChange("");
            }
            return;
        }

        if (!TextUtils.isEmpty(maxMoney)) {
            String maxInput = afterChang.replace(",", "");
            String moneyMax = maxMoney.replace(",", "");
            if (Double.parseDouble(maxInput) > Double.parseDouble(moneyMax)) {
                String toast = String.format(editTextWeak.get().getContext().getString(R.string.max_input_money), maxMoney);
                ToastUtils.showShort(toast);
                return;
            }
        }

        if (lastChangeStr.equals(afterChang)) {
            return;
        }

        if (editTextWeak.get().getText().toString().trim() != null &&
                !editTextWeak.get().getText().toString().trim().equals("")) {
            if (editTextWeak.get().getText().toString().trim().charAt(0) == '.') {
                String str = "0" + editTextWeak.get().getText().toString().trim();
                editTextWeak.get().setText(str);
                editTextWeak.get().setSelection(1);
            }
        }

        if (!AppConfig.isThLive() && !afterChang.endsWith(SPLICING) && isNeedSplicing) {
            afterChang += SPLICING;
        }

        isNeedSplicing = true;

        //如果最后编辑的文本和上次记录的文本不一样
        lastChangeStr = RegexUtils.stringEditTextMoney(afterChang);
        editTextWeak.get().setText(lastChangeStr);

        if (listener != null) {
            listener.onTextChange(lastChangeStr);
        }

        //当前光标的位置
        if (lastSelect <= maxInput) {
            String temp = lastChangeStr.replace(",", "");
            int moveStep = lastChangeStr.length() - temp.length() - 1;
            editTextWeak.get().setSelection(lastSelect + moveStep);
        }
        LogUtils.e("afterTextChanged : lastChangeStr ->" + lastChangeStr + " afterChang:" + afterChang);
        //LogUtils.e("afterTextChanged : lastSelect ->" + lastSelect + " currentSelect:" + currentSelect);
    }

    public interface AfterTextChangedListener {
        void onTextChange(String text);
    }
}
