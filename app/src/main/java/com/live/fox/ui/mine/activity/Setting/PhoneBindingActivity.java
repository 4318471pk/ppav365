package com.live.fox.ui.mine.activity.Setting;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.ui.login.LoginViewModel;


public class PhoneBindingActivity extends BaseActivity {

//    Handler handler = new Handler(Looper.myLooper()) {
//
//        @Override
//        public void dispatchMessage(@NonNull @NotNull Message msg) {
//            super.dispatchMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    remainSecond--;
//                    if (remainSecond > 0) {
//                        StringBuilder sb = new StringBuilder();
//                        sb.append(String.valueOf(remainSecond)).append("s").append(getString(R.string.reGet));
//                        sendVerifyCode.setText(sb.toString());
//                        sendVerifyCode.setEnabled(false);
//                        sendEmptyMessageDelayed(0, 1000);
//                    } else {
//                        remainSecond = 60;
//                        sendVerifyCode.setText(getString(R.string.get_verification_code));
//                        sendVerifyCode.setEnabled(true);
//                    }
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_phone_binding);

//        tvSendVerifyCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
       ViewDataBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_phone_binding);
        mBinding.setLifecycleOwner(this);

    }


}
