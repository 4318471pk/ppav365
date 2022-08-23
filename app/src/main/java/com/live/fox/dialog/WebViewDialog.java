package com.live.fox.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.entity.WebViewDialogEntity;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;
import com.luck.picture.lib.tools.ScreenUtils;

/**
 * web view 的 dialog
 */
public class WebViewDialog extends DialogFragment {

    public static final String WEB_VIEW_DIALOG_KEY = "web view dialog key";

    private TextView title;
    private WebView webView;
    private WebViewDialogEntity entity;
    private Dialog dialog;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_web_view, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.shape_corners_10_white));
        window.setDimAmount(0.4f);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ScreenUtils.getScreenWidth(requireContext()) - ScreenUtils.dip2px(requireContext(), 64);
        params.height = ScreenUtils.dip2px(requireContext(), 300);
        dialog.getWindow().setAttributes(params);
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            entity = arguments.getParcelable(WEB_VIEW_DIALOG_KEY);
            if (entity != null) {
                setContent();
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setContent() {
        title.setText(entity.getTitle());
        if (!TextUtils.isEmpty(entity.getWebUrl())) {
            webView.loadDataWithBaseURL(null, entity.getWebUrl(),
                    "text/html;charset=utf-8", "utf-8", null);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
            webSettings.setUseWideViewPort(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setDisplayZoomControls(false);
            webSettings.setAllowFileAccess(true);
            //禁止手动缩放
            webSettings.setBuiltInZoomControls(false);
            webSettings.setSupportZoom(true);
            webSettings.setLoadWithOverviewMode(true);
            webView.setOnLongClickListener(view1 -> true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    LogUtils.e(url + ",");
                    if (StringUtils.isEmpty(url)) {
                        return false;
                    }
                    if (url.endsWith(".apk")) {
                        Constant.isAppInsideClick = true;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(url));
                        requireActivity().startActivity(intent);
                        return true;
                    }
                    IntentUtils.toBrowser(requireActivity(), url);
                    return true;
                }
            });
        }
    }

    private void initView(View view) {
        title = view.findViewById(R.id.web_dialog_title);
        view.findViewById(R.id.web_dialog_close).setOnClickListener(view1 -> dismiss());
        webView = view.findViewById(R.id.web_dialog_web_view);
    }

}
