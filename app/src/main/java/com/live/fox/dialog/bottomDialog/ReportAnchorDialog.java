package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogReportAnchorBinding;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

public class ReportAnchorDialog extends BaseBindingDialogFragment {

    DialogReportAnchorBinding mBind;
    String uid;

    public static ReportAnchorDialog getInstance(String uid)
    {
        ReportAnchorDialog reportAnchorDialog =new ReportAnchorDialog();
        reportAnchorDialog.uid=uid;
        return reportAnchorDialog;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        setWindowsFlag();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.llMain,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.gtvCancel:
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.llMain,false);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_report_anchor;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        for (int i = 0; i < mBind.rllTVs.getChildCount(); i++) {
            if(mBind.rllTVs.getChildAt(i) instanceof TextView)
            {
                mBind.rllTVs.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textView=(TextView)v;
                        reportAnchor(textView.getText().toString());
                    }
                });
            }
        }

        startAnimate(mBind.llMain,true);
    }

    private void reportAnchor(String content)
    {
        Api_Live.ins().reportAnchor(uid, content, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(isConditionOk())
                {
                    if(code==0)
                    {
                        ToastUtils.showShort(getStringWithoutContext(R.string.reportSuccess));
                        dismissAllowingStateLoss();
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }

            }
        });
    }
}
