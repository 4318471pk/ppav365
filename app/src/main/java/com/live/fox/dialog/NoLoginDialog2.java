package com.live.fox.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.ConstantValue;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_User;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;

import org.json.JSONObject;

/**
 * 您的账号已在其他手机登陆
 */
public class NoLoginDialog2 extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics dm = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_no_login2, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        SPUtils.getInstance().put(ConstantValue.NOTIFICATION_IS_SHOWED, true);
    }


    private void initView(View rootView) {
//        TextView n1 = rootView.findViewById(R.id.n1);
//        TextView n2 = rootView.findViewById(R.id.n2);
//
//        n1.setText(getText(R.string.notification_dialog_title));
//        n1.setText(String.format(getString(R.string.notification_dialog_content), getString(R.string.app_name)));

        rootView.findViewById(R.id.n3).setOnClickListener(view -> {
            LoginModeSelActivity.startActivity(requireActivity());
            dismiss();
        });
        rootView.findViewById(R.id.n4).setOnClickListener(view -> {
            doLoginGuest();
            dismiss();
        });
    }
    public void doLoginGuest() {
        Api_Auth.ins().guestLogin(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                try {
                    if (code == 0) {
                        JSONObject jsonObject = new JSONObject(data);
                        String token = jsonObject.optString("token", "");
                        if (StringUtils.isEmpty(token)) {
                            ToastUtils.showShort(getString(R.string.tokenFail));
                            return;
                        }
                        onLoginSuccess(token);
                    } else {
                        ToastUtils.showShort(msg);
                    }
                } catch (Exception e) {
                    ToastUtils.showShort(e.getMessage());
                }
            }
        });
    }
    //登录成功、完善用户信息成功后的统一处理
    public void onLoginSuccess(String token) {
        DataCenter.getInstance().getUserInfo().setToken(token);
        Api_User.ins().getUserInfo(-1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String userJson) {
                if (code == 0) {
                    ActivityUtils.finishOtherActivities(LoginModeSelActivity.class);
                    MainActivity.startActivity(getContext());
                    dismiss();
                } else {
                    SPManager.clearUserInfo();
                    if (code == 993) {
                        ToastUtils.showShort(getString(R.string.accountStop));
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }
}
