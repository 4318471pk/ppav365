package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.ui.circle.JavaScriptInterface;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;


/**
 * 直播间的游戏展示
 */
public class GameWebViewFragment extends DialogFragment implements View.OnClickListener {

    WebView webView;

    String title;
    String url;

    protected boolean isError;
    private boolean isShowError;
    boolean isShowTitle;

    int isToWeb = 0;

    boolean isLoadFinish = false;

    public boolean isShow = true;

    public static GameWebViewFragment newInstance(String title, String url) {
        GameWebViewFragment fragment = new GameWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title", "");
            url = bundle.getString("url", "www.baidu.com");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_liveroom_game);
        dialog.setCanceledOnTouchOutside(false); // 外部點擊取消

        initView(dialog);

        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);

        return dialog;
    }


    private void initView(Dialog dialog) {
        isShow = true;

        webView = dialog.findViewById(R.id.webview_);
        dialog.findViewById(R.id.layout_top).setOnClickListener(this);

        WebSettings ws = webView.getSettings();
        //是否允许脚本支持
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(getActivity(), new JavaScriptInterface.OnJsCallListener() {
            @Override
            public void onJsCalled(int type, String data) {
                LogUtils.e("isLoadFinish，" + type + "，" + data);
                if(type==666) {
                    if(btnClick!=null){
                        btnClick.onExit();
                    }
                }
            }
        }), "android");


        ws.setDomStorageEnabled(true);

        //设置是否可以跳转 如果需要调外部浏览器 此值应该设置为false
        ws.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                LogUtils.e("newProgress:" + newProgress);
//                updateProgressBar(newProgress);
            }
        });
        webView.setWebViewClient(getWebViewClient());

        //加载URL
        if (!StringUtils.isEmpty(url)) {
            if (url.endsWith("jpg") || url.endsWith("png")) {
                webView.getSettings().setBlockNetworkImage(false);//解决图片不显示
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//                mWebView.setInitialScale(100);
                //页面自动缩放为设备宽度
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setLoadWithOverviewMode(true);
//                if (isShowTitle) {
////                    setTitle(title, false);
//                }
            }
            webView.setWebViewClient(getWebViewClient());
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    if (url.endsWith("jpg") || url.endsWith("png")) return;
//                    if (isShowTitle) {
////                        setTitle(view.getTitle(), false);
//                    }
                }
            });
            webView.loadUrl(url);
        }
        //加载源代码
        else {
//            LogUtils.e(source);
//            webView.loadDataWithBaseURL(null, source, "text/html;charset=utf-8", "utf-8", null);
//            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//                    try {
//                        if (url.startsWith("alipays://")) {
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                            startActivity(intent);
//                            return true;
//                        }
//                    } catch (Exception e) {
//                        return false;
//                    }
//                    webView.loadUrl(url);
//                    return true;
//                }
//            });
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
        if (webView != null) webView.destroy();
        LogUtils.e("onDismiss");
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
                    Constant.isAppInsideClick=true;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                if (isToWeb != 0) { //外部跳转
                    Constant.isAppInsideClick=true;
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
//                updateProgressBar(100);
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
//                LogUtils.w("errorCode:" + errorCode + "|failingUrl:" + failingUrl);
//                showReceiveError();
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
//                hideReceiveError();
            }
        };
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_top:
                if(btnClick!=null){
                    btnClick.onEmptyClick(view);
                }

                break;

        }
    }

    OnBtnClick btnClick;

    public void setBtnClick(OnBtnClick btnClick) {
        this.btnClick = btnClick;
    }


    public interface OnBtnClick {
        void onExit();
        void onEmptyClick(View view);
    }

}
