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
import com.live.fox.utils.ToastUtils;
import com.luck.picture.lib.tools.ScreenUtils;

/*
* 自定义筹码
* */
public class InputChouMaDialog extends DialogFragment {

    private Dialog dialog;

    private EditText etChouma;
    private TextView tvCancel;
    private TextView tvConfirm;

    boolean isChouma = true;

    public static InputChouMaDialog newInstance(String title, boolean isChouma, String money) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("isChouma", isChouma);
        args.putString("money", money);
        InputChouMaDialog fragment = new InputChouMaDialog();
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
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = ScreenUtils.getScreenWidth(requireContext()) - ScreenUtils.dip2px(requireContext(), 32);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_input_chouma, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        String title = getArguments().getString("title");
        isChouma = getArguments().getBoolean("isChouma", isChouma);
        etChouma = rootView.findViewById(R.id.etChouma);
        tvCancel = rootView.findViewById(R.id.tvCancel);
        tvConfirm = rootView.findViewById(R.id.tvConfirm);
        TextView tvTitle = rootView.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        if (!isChouma) {
            String money = getArguments().getString("money");
            etChouma.setText(money);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etChouma.getText().toString().trim())) {
                    ToastUtils.showShort(getResources().getString(R.string.please_input_chouma));
                }else {
                    int money = Integer.parseInt(etChouma.getText().toString().trim());
                    if (money < 2 || money >10000) {
                        ToastUtils.showShort(getResources().getString(R.string.please_input_score_2));
                        return;
                    }
                    if (chouMaInputListener != null) {
                        chouMaInputListener.confirm(etChouma.getText().toString().trim(), isChouma);
                        dismiss();
                    }
                }
            }
        });
    }

    public interface ChouMaInputListener{
        public void confirm(String money, boolean isChouma);
    }

    private ChouMaInputListener chouMaInputListener;

    public void setChouMaInputListener(ChouMaInputListener listener) {
        this.chouMaInputListener = listener;
    }

}
