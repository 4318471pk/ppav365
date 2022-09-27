package com.live.fox.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogUpdateBinding;
import com.live.fox.okhttp.downloadbigfile.DownloadBigFileUtil;
import com.live.fox.utils.ClickUtil;


/**
 * App 更新提示框
 */
public class UpdateFragmentBinding extends BaseBindingDialogFragment {

    DialogUpdateBinding mBind;
    String version;
    String updateDes;
    String apkUrl;
    boolean isForce;//是否强制更新

    public static UpdateFragmentBinding newInstance(String version, String updateDes, String apkUrl, boolean isForce) {
        UpdateFragmentBinding fragment = new UpdateFragmentBinding();
        Bundle bundle = new Bundle();
        bundle.putString("version", version);
        bundle.putString("updateDes", updateDes);
        bundle.putString("apkUrl", apkUrl);
        bundle.putBoolean("isForce", isForce);
        fragment.setArguments(bundle);
        return fragment;
    }

    //如果需要修改大小 修改显示动画 重写此方法
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
    }

    @Override
    public void onClickView(View view) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_sure:
            case R.id.tv_sure2:
                mBind.layoutBottom1.setVisibility(View.INVISIBLE);
                mBind.layoutBottom2.setVisibility(View.INVISIBLE);
                mBind.layoutBottom3.setVisibility(View.VISIBLE);
                mBind.tvVersion.setVisibility(View.INVISIBLE);
                download();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_update;
    }

    @Override
    public void initView(View view) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            version = bundle.getString("version");
            updateDes = bundle.getString("updateDes");
            apkUrl = bundle.getString("apkUrl");
            isForce = bundle.getBoolean("isForce");
        }

        mBind.layoutBottom1.setVisibility(View.INVISIBLE);
        mBind.layoutBottom2.setVisibility(View.INVISIBLE);
        mBind.layoutBottom3.setVisibility(View.INVISIBLE);

        if (isForce) {
            //强更
            mBind.layoutBottom2.setVisibility(View.VISIBLE);
        } else {
            //非强更
            mBind.layoutBottom1.setVisibility(View.VISIBLE);
            mBind.tvCancel.setOnClickListener(view1 -> dismiss());
        }
        mBind.tvDescription.setText(updateDes);
        String strVersion = "V" + version;
        mBind.tvVersion.setText(strVersion);

    }



    long fileSoFar;
    long fileTotleSize;

    public void download() {
        DownloadBigFileUtil downloadApkUtil = new DownloadBigFileUtil(requireContext());
        downloadApkUtil.downloadAPK(apkUrl);
        downloadApkUtil.setOnProgressListener((fileSoFar1, fileTotleSize1) -> {
            fileSoFar = fileSoFar1;
            fileTotleSize = fileTotleSize1;
        });
        mProgressHandler.sendEmptyMessage(100);
    }


    private final Handler mProgressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (fileTotleSize == 0) {
                String dataDef = "0.0% (0.00M/0.00M)";
                mBind.tvDownvalue.setText(dataDef);
                mBind.tvDownvalue.setVisibility(View.VISIBLE);
                mBind.progressDown.setVisibility(View.VISIBLE);
            } else {
                mBind.tvDownvalue.setVisibility(View.VISIBLE);
                String str = format1(((float) fileSoFar / (float) fileTotleSize) * 100) + "% ("
                        + format2((float) fileSoFar / 1024 / 1024) + "M" +
                        "/" + format2((float) fileTotleSize / 1024 / 1024) + "M)";
                mBind.tvDownvalue.setText(str);
                mBind.progressDown.setMax((int) fileTotleSize);
                mBind.progressDown.setProgress((int) fileSoFar);
            }

            if (fileTotleSize > fileSoFar || fileSoFar + fileTotleSize == 0) {
                mProgressHandler.sendEmptyMessageDelayed(100, 200);
            }
            return false;
        }
    });

    public static String format1(float value) {
        return String.format("%.1f", value).replace(",", ".");
    }

    public static String format2(float value) {
        return String.format("%.2f", value).replace(",", ".");
    }

    OnBtnSureClick btnSureClick;

    public void setBtnSureClick(OnBtnSureClick btnClick) {
        this.btnSureClick = btnClick;
    }

    public interface OnBtnSureClick {
        void onClick();
    }

}
