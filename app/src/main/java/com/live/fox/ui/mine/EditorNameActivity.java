package com.live.fox.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 修改昵称
 */
public class EditorNameActivity extends BaseHeadActivity implements View.OnClickListener {


    private TextInputEditText nickName;
    private String name;
    private int mNum = 10; // 輸入字數的最大值


    public static void start(Activity activity, String name) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, EditorNameActivity.class);
        intent.putExtra("name", name);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editname_activity);

        initData(getIntent());
        setView();
    }


    public void initData(Intent intent) {
        if (intent != null) {
            name = intent.getStringExtra("name");
        }
    }


    public void setView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.nickNameModify), true, getString(R.string.save), true);
        getTvRight().setTextColor(Color.parseColor("#EB4A81"));

        nickName = findViewById(R.id.nick_editor);
        if (StringUtils.isEmpty(name)) {
            nickName.setHint(getString(R.string.qInputNickname));
        } else {
            nickName.setText(name);
            Editable etext = nickName.getText();
            Selection.setSelection(etext, etext.length());
        }
    }


    private TextWatcher textWatcher = new TextWatcher() {
        private CharSequence mTemp;
        private int mSelectionStart;
        private int mSelectionEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            mTemp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int number = mNum - nickName.length();
            if (number < 0) {
                mSelectionStart = nickName.getSelectionStart();
                mSelectionEnd = nickName.getSelectionEnd();
                if (mTemp.length() > mNum) {
                    ToastUtils.showShort(getString(R.string.nickNameTen));
                    editable.delete(mSelectionStart - 1, mSelectionEnd);// 刪掉多輸入的文字
                    int tempSelection = mSelectionEnd;
                    nickName.setText(editable);
                    nickName.setSelection(tempSelection - 1);
                }
            }
        }
    };


    //事件绑定
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_head_right) {
            KeyboardUtils.hideSoftInput(view);
            String newName = nickName.getText().toString().trim();
            if (StringUtils.isEmpty(newName)) {
                ToastUtils.showShort(getString(R.string.qInputNickname));
                return;
            }
            LogUtils.e(newName);
            if (newName.length() > 10) {
                ToastUtils.showShort(getString(R.string.nickNameTen));
                return;
            }

            DialogFactory.showTwoBtnDialog(EditorNameActivity.this,
                    getString(R.string.changeNickname), (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                dialog.dismiss();
                doUpdateUserInfoApi(newName);
            });
        }
    }

    public void doUpdateUserInfoApi(String newName) {
        User user = DataCenter.getInstance().getUserInfo().getUser();
        user.setNickname(newName);
        Api_User.ins().modifyUserInfo(user, 2, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    DataCenter.getInstance().getUserInfo().updateUser(user);
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
