package com.live.fox.ui.h5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;


public class UserIndexActivity extends BaseHeadActivity {

    private WebView mWebView;
    boolean isShare = false;
    static final String TITLE = "title", URL = "url", SOURCE = "source";


    private Bitmap totalBp;
    String title;
//    MyShareDialog shareDialog;

    private PaxWebChromeClient chromeClient;

    public static void start(Context context, String titleName, String url, boolean share) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, UserIndexActivity.class);
        intent.putExtra(TITLE, titleName);
        intent.putExtra(URL, url);
        intent.putExtra("share", share);
        context.startActivity(intent);
    }

    public static void start(Context context, String titleName, String source) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, UserIndexActivity.class);
        intent.putExtra(TITLE, titleName);
        intent.putExtra(SOURCE, source);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);

        initData(getIntent());
        setView();
    }


    public void initData(Intent intent) {
        if (intent != null) {
            title = intent.getStringExtra(TITLE);
            isShare = intent.getBooleanExtra("share", false);
        }
    }


    public void setView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        if (!isShare) {
            setHead(title, true, true);
        } else {
            setHead(title, true, getString(R.string.share), true);
            getTvRight().setOnClickListener(view -> {
            });
        }

        mWebView = findViewById(R.id.webview);
        String web_url = getIntent().getStringExtra(URL);
        LogUtils.e(web_url);
        mWebView.loadUrl(web_url);
    }


    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            String source = "";
            if (StringUtils.isEmpty(source)) {
                mWebView.goBack();
            } else {
                finish();
            }

        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        chromeClient.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}
