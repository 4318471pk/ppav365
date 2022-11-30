package com.live.fox.ui.h5;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityPublicWebBinding;

public class PublicWebActivity extends BaseBindingViewActivity {

    static final String WebTitle="WebTitle";
    static final String WebUrl="WebUrl";

    ActivityPublicWebBinding mBind;

    public static void startActivity(Context context,String title,String url)
    {
        Intent intent=new Intent(context,PublicWebActivity.class);
        intent.putExtra(WebTitle,title);
        intent.putExtra(WebUrl,url);
        context.startActivity(intent);
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_public_web;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        PTIWebSetting.init(mBind.webView);
        AndroidBug5497WorkaroundWebView.assistActivity(this);

        String title=getIntent().getStringExtra(WebTitle);
        String url=getIntent().getStringExtra(WebUrl);
        if(!TextUtils.isEmpty(title))
        {
            setActivityTitle(title);
        }

        if(!TextUtils.isEmpty(url))
        {
            mBind.webView.loadUrl(url);
        }

    }
}
