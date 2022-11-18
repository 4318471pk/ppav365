package com.live.fox.ui.lottery;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.SwitchView;
import com.luck.picture.lib.tools.ScreenUtils;

/*
* 预设倍数
* */
public class YusheBeishuDialog extends DialogFragment {

    public final static String TOUZHU_YUSHE = "touzhu_yushe";

    private Dialog dialog;

    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;

    SwitchView switchOpen;
    TextView tvConfirm;


    public static YusheBeishuDialog newInstance() {
        Bundle args = new Bundle();
        YusheBeishuDialog fragment = new YusheBeishuDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.shape_corners_10_white));
        window.setDimAmount(0.5f);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = ScreenUtils.getScreenWidth(requireContext()) - ScreenUtils.dip2px(requireContext(), 32);
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog.getWindow().setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_yushe_beishu, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        et1 = rootView.findViewById(R.id.etYushe1);
        et2 = rootView.findViewById(R.id.etYushe2);
        et3 = rootView.findViewById(R.id.etYushe3);
        et4 = rootView.findViewById(R.id.etYushe4);
        et5 = rootView.findViewById(R.id.etYushe5);

        switchOpen = rootView.findViewById(R.id.switchOpen);
        tvConfirm = rootView.findViewById(R.id.tvConfirm);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String ysData = SPUtils.getInstance().getString(TOUZHU_YUSHE);
        if (TextUtils.isEmpty(ysData)) {
            switchOpen.setOpened(false);
        } else {
            switchOpen.setOpened(true);
            String[] arr = ysData.split(",");
            et1.setText(arr[0]);
            et2.setText(arr[1]);
            et3.setText(arr[2]);
            et4.setText(arr[3]);
            et5.setText(arr[4]);
        }
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et1.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.yushe_beishu_hint_2));
                    return;
                }
                if (TextUtils.isEmpty(et2.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.yushe_beishu_hint_2));
                    return;
                }
                if (TextUtils.isEmpty(et3.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.yushe_beishu_hint_2));
                    return;
                }
                if (TextUtils.isEmpty(et4.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.yushe_beishu_hint_2));
                    return;
                }
                if (TextUtils.isEmpty(et5.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.yushe_beishu_hint_2));
                    return;
                }

                checkBeishu(et1);
                checkBeishu(et2);
                checkBeishu(et3);
                checkBeishu(et4);
                checkBeishu(et5);

                String s = et1.getText().toString().trim() + "," +  et2.getText().toString().trim()
                        + "," +  et3.getText().toString().trim()+ "," +  et4.getText().toString().trim()
                        + "," +  et5.getText().toString().trim();
                if (switchOpen.isOpened()) {
                    SPUtils.getInstance().put(TOUZHU_YUSHE, s);
                } else {
                    SPUtils.getInstance().put(TOUZHU_YUSHE, "");
                }

                if (mListener != null) {
                    mListener.confirm(s);
                    dismiss();
                }

            }
        });
    }

    private void checkBeishu(EditText et) {
        int beishu = Integer.parseInt(et.getText().toString().trim());
        if (beishu > 20) {
            et.setText(20+ "");
        }
    }

    public interface YusheBeishuListener{
        public void confirm(String beishu);
    }

    private YusheBeishuListener mListener;

    public void setListener(YusheBeishuListener listener) {
        this.mListener = listener;
    }

}
