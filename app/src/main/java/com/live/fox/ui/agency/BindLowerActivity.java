package com.live.fox.ui.agency;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityBindLowerBinding;
import com.live.fox.ui.mine.noble.NobleActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;

public class BindLowerActivity extends BaseBindingViewActivity {

    ActivityBindLowerBinding mBind;


    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, BindLowerActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_bind_lower;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        //StatusBarUtil.setStatusBarAlpha(this,0,mBind.head);
        setHeadGone();
        mBind.ivHeadLeft.setOnClickListener(view -> {finish();});

        mBind.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mBind.etId.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.input_lower_id));
                    return;
                }
                if(TextUtils.isEmpty(mBind.etCode.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.input_bind_code));
                    return;
                }
                finish();
            }
        });

    }
}
