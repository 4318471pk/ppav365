package com.live.fox.ui.h5;

import android.annotation.SuppressLint;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

public class PTIWebSetting {

    @SuppressLint("SetJavaScriptEnabled")
    public static void init(WebView webView){
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setCursiveFontFamily("cursive");
        webSettings.setDisplayZoomControls(true);
        webSettings.setDatabaseEnabled(true);
//        webSettings.setDomStorageEnabled(true);
        webSettings.setFantasyFontFamily("fantasy");
        webSettings.setFixedFontFamily("monospace");
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.NORMAL);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        //解决ag直播 显示不支持浏览器的问题
//        webSettings.setUserAgent("Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36");
//        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36");

        //解决bug2 https://site.cq9web.com/common/#/activities 外网打不开的问题
        webSettings.setUserAgentString("Mozilla/5.0 (Android) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 /604.1");

        webSettings.setSansSerifFontFamily("sans-serif");
        webSettings.setSaveFormData(true);

        webSettings.setSerifFontFamily("serif");
        webSettings.setStandardFontFamily("sans-serif");
        webSettings.setSupportMultipleWindows(false);
        webSettings.setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= 16) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }

        webSettings.setEnableSmoothTransition(false);

        webSettings.setGeolocationEnabled(true);

        webSettings.setSaveFormData(true);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
            webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webSettings.setLightTouchEnabled(false);
        webSettings.setSavePassword(false);
    }
}
