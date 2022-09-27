package com.live.fox.ui.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_AgGame;
import com.live.fox.server.Api_FwGame;
import com.live.fox.server.Api_KyGame;
import com.live.fox.server.Api_Pay;
import com.live.fox.server.Api_TYGame;
import com.live.fox.ui.circle.JavaScriptInterface;
import com.live.fox.ui.mine.MyBalanceActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.DensityUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.MoveBallView;


/**
 * 全屏显示游戏
 */
public class GameFullWebViewActivity extends BaseActivity {

    ProgressBar progressBar;
    LinearLayout vError;
    WebView webView;

    private String url;

    protected boolean isError;
    private boolean isShowError;
    boolean isShowTitle;

    int isToWeb = 0;

    boolean isLoadFinish = false;

    MoveBallView viewMoveBall;

    int type = 1; //0：老朱；1：开元 2：Ag 3：Cp

    public static void startActivity(Activity activity, int type, String url) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, GameFullWebViewActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamefull);

        initData(getIntent());
        setView();
    }


    public void initData(Intent intent) {
        if (intent != null) {
            url = intent.getStringExtra("url");
            type = intent.getIntExtra("type", 1);
        }
    }


    public void setView() {
        BarUtils.setStatusBarVisibility(this, false);

        viewMoveBall = findViewById(R.id.view_moveball);
        initFloatIcon();

        progressBar = findViewById(R.id.progressbar_);
        vError = findViewById(R.id.ll_error);
        webView = findViewById(R.id.webview_);

        progressBar.setMax(100);
        isShowError = addErrorView(vError);
        //是否允许脚本支持
        WebSettings settings = webView.getSettings();
        // 设置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
        //支持自动适配
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);  //支持放大缩小

        settings.setBuiltInZoomControls(true); //显示缩放按钮
//        settings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
        settings.setAllowFileAccess(true); // 允许访问文件

        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);/// 支持通过JS打开新窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(new JavaScriptInterface(GameFullWebViewActivity.this, new JavaScriptInterface.OnJsCallListener() {
            @Override
            public void onJsCalled(int type, String data) {
                LogUtils.e("isLoadFinish，" + type + "，" + data);
                if (type == 666) {
                    doLogout();
                }
            }
        }), "android");

        webView.setWebViewClient(getWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (url.endsWith("jpg") || url.endsWith("png")) return;
                if (isShowTitle) {
//                        setTitle(view.getTitle(), false);
                }
            }
        });
        webView.loadUrl(url);
    }


    //初始化浮动的图标
    public void initFloatIcon() {

        int x = ScreenUtils.getScreenWidth(this) - DensityUtils.dp2px(this, 100);
        int y = DensityUtils.dp2px(this, 80);

        viewMoveBall.setView(this, x, y, DensityUtils.dp2px(this, 45),
                DensityUtils.dp2px(this, 45));
        viewMoveBall.setOnClick(new MoveBallView.OnClick() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_left1:
                    case R.id.iv_right1:
                        webView.reload();
                        break;
                    case R.id.iv_left2:
                    case R.id.iv_right2:
                        if (type == 0) {
                            ToastUtils.showShort(getString(R.string.comingSoon));
                            return;
                        }
                        MyBalanceActivity.startActivity(GameFullWebViewActivity.this);
                        break;
                    case R.id.iv_left3:
                    case R.id.iv_right3:
                        doLogout();
                        break;
                }

            }
        });
    }

    public WebViewClient getWebViewClient() {
        return new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.d("startUrl:" + url);
                isError = false;
                webView.setVisibility(View.VISIBLE);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e("url:" + url + ", isToWeb" + isToWeb);
                LogUtils.e("isLoadFinish" + isLoadFinish);
                if (!isLoadFinish) {
                    return super.shouldOverrideUrlLoading(view, url);
                }

                //里面的跳转拦截 如果是APK包 则跳浏览器下载
                if (url.endsWith(".apk") || url.contains("openinstall")) {
//                    Constant.isAppInsideClick=true;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                if (isToWeb != 0) { //外部跳转
//                    Constant.isAppInsideClick=true;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);

            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                updateProgressBar(100);
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
//                LogUtils.w("errorCode:" + errorCode + "|failingUrl:" + failingUrl);
                showReceiveError();
            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.cancel();
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isLoadFinish = true;
                LogUtils.e("isLoadFinish" + isLoadFinish);
                hideReceiveError();
            }
        };
    }

    /**
     * @param group
     * @return true表示已添加ErrorView并显示ErrorView/false表示不处理
     */
    public boolean addErrorView(ViewGroup group) {
        group.addView(LayoutInflater.from(GameFullWebViewActivity.this).inflate(R.layout.view_error, null));
        return true;
    }

    private void showReceiveError() {
        isError = true;
//        if(SystemUtils.isNetWorkActive(context)){
//            LogUtils.w("Page loading failed.");
//        }else{
//            LogUtils.w("Network unavailable.");
//        }

        if (isShowError) {
            webView.setVisibility(View.GONE);
            vError.setVisibility(View.VISIBLE);

        }


    }

    private void hideReceiveError() {
        if (isError) {
            showReceiveError();
        } else {
            webView.setVisibility(View.VISIBLE);
            vError.setVisibility(View.GONE);
        }

    }


    @Override
    public void onBackPressed() {

        doLogout();
    }

    public void doLogout() {
        //0：至尊；1：开元 2: AG
        if (type == 0) {
            User user = DataCenter.getInstance().getUserInfo().getUser();
            if (user != null) {
                showLoadingDialog();
                Api_Pay.ins().kickout(user.getUid() + "", new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        LogUtils.e(code + "," + msg + ", " + data);
                        hideLoadingDialog();
                        if (code == 0) {
                            Constant.isAppInsideClick = true;
                            finish();
                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
            } else {
                ToastUtils.showShort(getString(R.string.useMeexce));
                finish();
            }

        } else if (type == 1) {
            showLoadingDialog();
            Api_KyGame.ins().logout(new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    LogUtils.e(code + "," + msg + ", " + data);
                    hideLoadingDialog();
                    if (code == 0) {
                        Constant.isAppInsideClick = true;
                        finish();
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else if (type == 2) {
            showLoadingDialog();
            Api_AgGame.ins().logout(new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    LogUtils.e(code + "," + msg + ", " + data);
                    hideLoadingDialog();
                    if (code == 0) {
                        Constant.isAppInsideClick = true;
                        finish();
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else if (type == 3) {
            Constant.isAppInsideClick = true;
            finish();
        } else if (type == 4) {
            showLoadingDialog();
            Api_FwGame.ins().logout(new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    LogUtils.e(code + "," + msg + ", " + data);
                    hideLoadingDialog();
                    if (code == 0) {
                        Constant.isAppInsideClick = true;
                        finish();
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else if (type == 5 || type == 6) {
            showLoadingDialog();
            Api_FwGame.ins().logoutBg(new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    LogUtils.e(code + "," + msg + ", " + data);
                    hideLoadingDialog();
                    if (code == 0) {
                        Constant.isAppInsideClick = true;
                        finish();
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else if (type == 7) {
            showLoadingDialog();
            Api_TYGame.ins().logout(new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    LogUtils.e(code + "," + msg + ", " + data);
                    hideLoadingDialog();
                    if (code == 0) {
                        Constant.isAppInsideClick = true;
                        finish();
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else if (type == 12) {
            showLoadingDialog();
            Api_TYGame.ins().logoutSaba(new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    hideLoadingDialog();
                    if (code == 0) {
                        Constant.isAppInsideClick = true;
                        finish();
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else {
            ToastUtils.showShort(getString(R.string.comingSoon));
        }
    }

    /**
     * 加载url
     *
     * @param webView
     * @param url
     */
    private void load(WebView webView, String url) {
//        LogUtils.d("url:" + url);
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }

    }

    private boolean isGoBack() {
        return webView != null && webView.canGoBack();
    }


    private void updateProgressBar(int progress) {
        updateProgressBar(true, progress);
    }


    private void updateProgressBar(boolean isVisibility, int progress) {
        progressBar.setVisibility((isVisibility && progress < 100) ? View.VISIBLE : View.GONE);
        progressBar.setProgress(progress);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) webView.destroy();

    }
}
