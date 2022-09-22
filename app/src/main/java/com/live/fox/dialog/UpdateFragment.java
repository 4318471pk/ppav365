package com.live.fox.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.BaseDialogFragment;
import com.live.fox.okhttp.downloadbigfile.DownloadBigFileUtil;
import com.live.fox.utils.ClickUtil;


/**
 * App 更新提示框
 */
public class UpdateFragment extends BaseDialogFragment implements View.OnClickListener {

    TextView tv_version;
    TextView tv_cancel;
    TextView tv_sure;
    TextView tv_sure2;
    TextView tv_description;

    TextView tv_downvalue;
    ProgressBar progress_down;

    LinearLayout layout_bottom1;
    LinearLayout layout_bottom2;
    LinearLayout layout_bottom3;

    String version;
    String updateDes;
    String apkUrl;
    boolean isForce;//是否强制更新

    public static UpdateFragment newInstance(String version, String updateDes, String apkUrl, boolean isForce) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("version", version);
        bundle.putString("updateDes", updateDes);
        bundle.putString("apkUrl", apkUrl);
        bundle.putBoolean("isForce", isForce);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getViewId() {
        return R.layout.dialog_update;
    }

    @Override
    protected void onCreateView(View view) {
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
    }

    @Override
    protected void initViews(View view) {
        initView(view);
    }


    //如果需要修改大小 修改显示动画 重写此方法
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    public void initView(View view) {
        tv_version = view.findViewById(R.id.tv_version);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_sure = view.findViewById(R.id.tv_sure);
        tv_sure2 = view.findViewById(R.id.tv_sure2);
        tv_description = view.findViewById(R.id.tv_description);
        tv_downvalue = view.findViewById(R.id.tv_downvalue);
        progress_down = view.findViewById(R.id.progress_down);
        layout_bottom1 = view.findViewById(R.id.layout_bottom1);
        layout_bottom2 = view.findViewById(R.id.layout_bottom2);
        layout_bottom3 = view.findViewById(R.id.layout_bottom3);

        layout_bottom1.setVisibility(View.INVISIBLE);
        layout_bottom2.setVisibility(View.INVISIBLE);
        layout_bottom3.setVisibility(View.INVISIBLE);

        if (isForce) {
            //强更
            layout_bottom2.setVisibility(View.VISIBLE);
        } else {
            //非强更
            layout_bottom1.setVisibility(View.VISIBLE);
            tv_cancel.setOnClickListener(view1 -> dismiss());
        }
        tv_description.setText(updateDes);
        String strVersion = "V" + version;
        tv_version.setText(strVersion);

        tv_sure.setOnClickListener(this);
        tv_sure2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_sure:
            case R.id.tv_sure2:
                layout_bottom1.setVisibility(View.INVISIBLE);
                layout_bottom2.setVisibility(View.INVISIBLE);
                layout_bottom3.setVisibility(View.VISIBLE);
                tv_version.setVisibility(View.INVISIBLE);
                download();
                break;
        }

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
                tv_downvalue.setText(dataDef);
                tv_downvalue.setVisibility(View.VISIBLE);
                progress_down.setVisibility(View.VISIBLE);
            } else {
                tv_downvalue.setVisibility(View.VISIBLE);
                String str = format1(((float) fileSoFar / (float) fileTotleSize) * 100) + "% ("
                        + format2((float) fileSoFar / 1024 / 1024) + "M" +
                        "/" + format2((float) fileTotleSize / 1024 / 1024) + "M)";
                tv_downvalue.setText(str);
                progress_down.setMax((int) fileTotleSize);
                progress_down.setProgress((int) fileSoFar);
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
