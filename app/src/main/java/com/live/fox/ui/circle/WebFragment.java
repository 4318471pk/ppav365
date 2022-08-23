package com.live.fox.ui.circle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
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

import androidx.annotation.Nullable;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.User;
import com.live.fox.server.Api_Pay;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.AppUserManger;


/**
 * Date: 2018/4/1
 * Time: 下午 4:30
 */

public class WebFragment extends BaseHeadFragment {

    ProgressBar progressBar;
    LinearLayout vError;
    WebView webView;

    private String url;
    private String title;
    private String source;

    protected boolean isError;
    private boolean isShowError;
    boolean isShowTitle = false;

    int isToWeb = 0;

    boolean isLoadFinish = false;

    public static WebFragment newInstance(String url, String title) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static WebFragment newInstance(String url, boolean isShowTitle) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isShowTitle", isShowTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static WebFragment newInstance(String url, boolean isShowTitle, int isToWeb) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isShowTitle", isShowTitle);
        bundle.putInt("isToWeb", isToWeb);
        fragment.setArguments(bundle);
        return fragment;
    }

    //网页源代码
    public static WebFragment newInstance(String source) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("source", source);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.web_fragment, container, false);
        initData(getArguments());
        setView(rootView);
        return rootView;
    }


    public void initData(Bundle bundle) {
        if (bundle != null) {
            url = bundle.getString("url", "");
            source = bundle.getString("source", "");
            title = bundle.getString("title", "");
            isShowTitle = bundle.getBoolean("isShowTitle", true);
            isToWeb = bundle.getInt("isToWeb", 0);
        }
    }


    public void setView(View view) {
        StatusBarUtil.setStatusBarFulAlpha(requireActivity());
        BarUtils.setStatusBarVisibility(requireActivity(), true);
        BarUtils.setStatusBarLightMode(requireActivity(), false);
        if (isShowTitle) {
            initHead(view, getString(R.string.activities), true, true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    requireActivity().onBackPressed();
                }
            });
        } else {
            view.findViewById(R.id.toolbar).setVisibility(View.GONE);
        }
        progressBar = view.findViewById(R.id.progressbar_);
        vError = view.findViewById(R.id.ll_error);
        webView = view.findViewById(R.id.webview_);

        progressBar.setMax(100);
        isShowError = addErrorView(vError);

        WebSettings ws = webView.getSettings();
        //是否允许脚本支持
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(getActivity(), new JavaScriptInterface.OnJsCallListener() {
            @Override
            public void onJsCalled(int type, String data) {
                LogUtils.e("isLoadFinish，" + type + "，" + data);
                User user = AppUserManger.getUserInfo();
                if (user != null && type == 666) {
                    Api_Pay.ins().kickout(user.getUid() + "", new JsonCallback<String>() {
                        @Override
                        public void onSuccess(int code, String msg, String data) {
                            LogUtils.e("111 " + code);
                            if (code == 0) {
                                getActivity().finish();
                            } else {
                                ToastUtils.showShort(msg);
                            }
                        }
                    });
                }
//                switch (type) {
//                    case 501:
//                        if (ClickUtil.isFastDoubleClick()) {
//                            return;
//                        }
////                        LiveDialogManager.ins().showGame501HistoryDialog(LiveInFragment.this, data);
//                        break;
//                    case 502:
//                        if (ClickUtil.isFastDoubleClick()) {
//                            return;
//                        }
////                        LiveDialogManager.ins().showGame502HistoryDialog(LiveInFragment.this, data);
//                        break;
//                    case 504:
//                        if (ClickUtil.isFastDoubleClick()) {
//                            return;
//                        }
////                        LiveDialogManager.ins().showGame504HistoryDialog(LiveInFragment.this, data);
//                        break;
//                }
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
                updateProgressBar(newProgress);
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
                if (isShowTitle) {
//                    setTitle(title, false);
                }
            }
            webView.setWebViewClient(getWebViewClient());
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    if (url.endsWith("jpg") || url.endsWith("png")) return;
                    if (isShowTitle) {
                        setTitle(view.getTitle(), false);
                    }
                }
            });
            webView.loadUrl(url);
        }
        //加载源代码
        else {
            LogUtils.e(source);
            webView.loadDataWithBaseURL(null, source, "text/html;charset=utf-8", "utf-8", null);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    try {
                        if (url.startsWith("alipays://")) {
                            Constant.isAppInsideClick=true;
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
            });
        }
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
        group.addView(LayoutInflater.from(getActivity()).inflate(R.layout.view_error, null));
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


}
