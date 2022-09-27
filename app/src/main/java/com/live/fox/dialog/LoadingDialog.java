package com.live.fox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.live.fox.R;


//方便改变Dialog中的内容
public class LoadingDialog extends Dialog {

    protected Context mContext;

    protected LayoutParams mLayoutParams;

    TextView contentTv;

    public LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public LoadingDialog(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        contentTv = view.findViewById(R.id.tv_);

        setContentView(view);
        //设置点击屏幕不自动消失
        setCanceledOnTouchOutside(false);
        //设置返回键也不消失
        setCancelable(false);


    }

    public void setText(String content){
        contentTv.setText(content);
    }



}
