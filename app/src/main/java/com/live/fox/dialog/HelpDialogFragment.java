package com.live.fox.dialog;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.view.View;
import androidx.annotation.Nullable;

import com.live.fox.R;


public class HelpDialogFragment extends DialogFragment {

    private WebView webView;
    private String web_url = "";
    private TextView tvTitle;
    private String chinese;

    public static HelpDialogFragment newInstance(String playMethod, String chinese) {
        HelpDialogFragment fragment = new HelpDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("playMethod", playMethod);
        bundle.putString("chinese", chinese);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        webView = view.findViewById(R.id.webview);
        tvTitle = view.findViewById(R.id.tvTitle);
        Bundle bundle = getArguments();
        if (bundle != null) {
            web_url = bundle.getString("playMethod");
            chinese = bundle.getString("chinese");
            tvTitle.setText(chinese);
            webView.loadDataWithBaseURL(null, web_url, "text/html;charset=utf-8", "utf-8", null);
        }
//        webView.setEnabled(false);
//        webView.setPressed(false);
//        webView.setSelected(false);
//        webView.setFocusableInTouchMode(false);
//        webView.clearFocus();
//        webView.setLongClickable(false);
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
//        webSettings.setNeedInitialFocus(false);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

}
