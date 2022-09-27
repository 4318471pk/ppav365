package com.live.fox.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.BlankController;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 修改个性签名
 */
public class EditorMarkActivity extends BaseHeadActivity
        implements
        View.OnClickListener {


    String TAG = EditorMarkActivity.class.getSimpleName();


    private TextView leftNum;
    private TextInputEditText nickMark;
    private String mark;
    private int mNum = 30; // 輸入字數的最大值


    public static void startActivity(Activity activity, String mark) {
        Constant.isAppInsideClick=true;
        Intent intent = new Intent(activity, EditorMarkActivity.class);
        intent.putExtra("mark", mark);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editmark_activity);

        initData(getIntent());
        setView();
    }


    public void initData(Intent intent) {
        if (intent != null) {
            mark = intent.getStringExtra("mark");
        }
    }


    public void setView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.modifyPersonalSignature), true, getString(R.string.save), true);
        getTvRight().setTextColor(Color.parseColor("#EB4A81"));

        nickMark = findViewById(R.id.sign_editor);
        leftNum = findViewById(R.id.left_num);
        if (!StringUtils.isEmpty(mark)) {
            nickMark.setText(mark);
            Editable etext = nickMark.getText();
            Selection.setSelection(etext, etext.length());
        } else {
            mark = "";
        }

        leftNum.setText(String.valueOf(30 - mark.length()));
        nickMark.setFilters(new InputFilter[]{new BlankController(), new InputFilter.LengthFilter(30)});
        nickMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                leftNum.setText(String.valueOf(30 - nickMark.getText().length()));
            }
        });
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_head_right:
                KeyboardUtils.hideSoftInput(view);
                String newMark = nickMark.getText().toString().trim();
                if (StringUtils.isEmpty(newMark)) {
                    ToastUtils.showShort(getString(R.string.enteredYourSignature));
                    return;
                }
                LogUtils.e(newMark);
                if (nickMark.getText().length() > 30) {
                    ToastUtils.showShort(getString(R.string.signatureExceed));
                    return;
                }

                doUpdateUserInfoApi(newMark);
                break;
        }
    }

    public void doUpdateUserInfoApi(String newMark) {
        User user = DataCenter.getInstance().getUserInfo().getUser();
        user.setSignature(newMark);
        Api_User.ins().modifyUserInfo(user, 4, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(!StringUtils.isEmpty(data)) LogUtils.e("modifyUserInfo result : " + data);
                if (code == 0) {
                    user.setSignature(newMark);
                    SPManager.saveUserInfo(user);
                    showToastTip(true, getString(R.string.modifySuccess));
                    finish();
                } else {
                    showToastTip(false, msg);
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
