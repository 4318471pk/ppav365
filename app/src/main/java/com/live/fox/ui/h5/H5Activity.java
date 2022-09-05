package com.live.fox.ui.h5;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;


public class H5Activity extends BaseHeadActivity {

    private WebView mWebView;
    private String source = "";
    boolean isShare = false;
    static final String TITLE = "title", URL = "url", SOURCE = "source";
    String title;

    private PaxWebChromeClient chromeClient;

    public static void start(Context context, String titleName, String url, boolean share) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, H5Activity.class);
        intent.putExtra(TITLE, titleName);
        intent.putExtra(URL, url);
        intent.putExtra("share", share);
        context.startActivity(intent);
    }

    public static void start(Context context, String titleName, String source) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, H5Activity.class);
        intent.putExtra(TITLE, titleName);
        intent.putExtra(SOURCE, source);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initData(getIntent());
        setView();
    }

    public void initData(Intent intent) {
        if (intent != null) {
            title = intent.getStringExtra(TITLE);
            isShare = intent.getBooleanExtra("share", false);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        mWebView.clearCache(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        String ua = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(ua + " qiezi build-ver:" + AppUtils.getAppVersionName());

        //加载URL
        if (!StringUtils.isEmpty(getIntent().getStringExtra(URL))) {
            String web_url = getIntent().getStringExtra(URL);
            if (web_url != null) {
                if (web_url.endsWith("jpg") || web_url.endsWith("png")) {
                    mWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
                    mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                    mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    //页面自动缩放为设备宽度
                    mWebView.getSettings().setUseWideViewPort(true);
                    mWebView.getSettings().setLoadWithOverviewMode(true);
                    setTitle(title);
                }

                mWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        LogUtils.e("url:" + url);
                        try {
                            if (url.startsWith("alipays://") || url.startsWith("weixin://") || url.startsWith("alipayqr://")) {
                                Constant.isAppInsideClick = true;
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                                return true;
                            }
                        } catch (Exception e) {
                            return false;
                        }
                        mWebView.loadUrl(url);
                        return true;
                    }
                });

                chromeClient = new PaxWebChromeClient(this, web_url, getTvTitle());
                mWebView.setWebChromeClient(chromeClient);
                LogUtils.e("web_url:" + web_url);
                mWebView.loadUrl(web_url);
            }

        } else {  //源代码
            source = getIntent().getStringExtra(SOURCE);
            mWebView.getSettings().setBlockNetworkImage(false);
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            LogUtils.e("PlayHtml: " + source);
            mWebView.loadDataWithBaseURL(null, source, "text/html", "utf-8", null);

            //安卓微信支付测试环境可以打开吗
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                    super.onReceivedSslError(view, handler, error);
                }

                @Nullable
                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Uri url = request.getUrl();
                        LogUtils.e("shouldInterceptRequesturl:" + url);
                    }
                    return super.shouldInterceptRequest(view, request);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    LogUtils.e("shouldOverrideUrlLoadingurl:" + url);
                    try {
                        if (url.startsWith("alipays://") || url.startsWith("weixin://") || url.startsWith("alipayqr://")) {
                            Constant.isAppInsideClick = true;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }
                    } catch (Exception e) {
                        return false;
                    }
                    webView.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    LogUtils.e("onPageFinishedurl:" + url);
                    super.onPageFinished(view, url);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            if (StringUtils.isEmpty(source)) {
                mWebView.goBack();
            } else {
                finish();
            }

        } else {
            super.onBackPressed();
        }
    }
}
