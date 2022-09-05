package com.live.fox.dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.live.fox.R;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxAlertDialog extends RxBaseDialog {

    private ImageView mIvLogo;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView columnline;

    public RxAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxAlertDialog(Context context) {
        super(context);
        initView();
    }

    public RxAlertDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public ImageView getLogoView() {
        return mIvLogo;
    }

    public TextView getTitleView() {
        return mTvTitle;
    }

    public TextView getSureView() {
        return mTvSure;
    }
    public TextView getCancelView() {
        return mTvCancel;
    }

    public void setSureListener(View.OnClickListener listener) {
        mTvSure.setOnClickListener(listener);
    }

    public TextView getContentView() {
        return mTvContent;
    }

    public void setLogo(int resId) {
        mIvLogo.setImageResource(resId);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setSure(String content) {
        mTvSure.setText(content);
    }

    public void setContent(String str) {
        mTvContent.setText(str);
    }

    public void setStyle(int style){
        switch (style){
            case 1: //1个按钮风格
                mTvCancel.setVisibility(View.GONE);
                columnline.setVisibility(View.GONE);
                break;
            default: //2个按钮风格
                mTvCancel.setVisibility(View.VISIBLE);
                columnline.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rxalert, null);
        mTvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
        mTvTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        mTvTitle.setTextIsSelectable(true);
        mTvContent = (TextView) dialogView.findViewById(R.id.tv_content);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvContent.setTextIsSelectable(true);
        mIvLogo = (ImageView) dialogView.findViewById(R.id.iv_logo);
        mTvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        columnline = (TextView) dialogView.findViewById(R.id.textView10);
        setContentView(dialogView);

        //默认一个按钮风格
        setStyle(1);
    }

}
