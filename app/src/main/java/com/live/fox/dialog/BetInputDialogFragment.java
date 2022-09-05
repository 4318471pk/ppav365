package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.live.fox.R;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.svga.BetCartDataManager;
import com.live.fox.mvp.MvpDialogFragment;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.device.ScreenUtils;

public class BetInputDialogFragment extends MvpDialogFragment implements View.OnClickListener {

    private AppCompatEditText acetInput;
    private Dialog dialog;
    private BetInputChangeListener listener;
    private String betKey;
    private int position;
    private boolean isMixed;

    public static BetInputDialogFragment newInstance(String key, int position, boolean mixed) {
        BetInputDialogFragment fragment = new BetInputDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putInt("position", position);
        bundle.putBoolean("mixed", mixed);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        Bundle arguments = getArguments();
        if (arguments != null) {
            betKey = arguments.getString("key", "");
            position = arguments.getInt("position", -1);
            isMixed = arguments.getBoolean("mixed", false);
        }

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = ScreenUtils.getScreenWidth(requireActivity()) - DeviceUtils.dp2px(requireContext(), 60);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        acetInput = (AppCompatEditText) view.findViewById(R.id.acetInput);
        view.findViewById(R.id.rtvInputOk).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rtvInputOk) {
            String input = acetInput.getText().toString();
            if (TextUtils.isEmpty(input)) {
                showToastTip(false, getString(R.string.canNotEmpty));
            } else {
                int anInt = Integer.parseInt(input);
                if (anInt >= 1 && anInt <= 100000) {
                    Bundle arguments = getArguments();
                    if (arguments != null && !TextUtils.isEmpty(betKey)) {
                        MinuteTabItem it = isMixed ? BetCartDataManager.getInstance().getLink().get(betKey)
                                : BetCartDataManager.getInstance().findOddeFieldById(betKey);
                        assert it != null;
                        it.betMoney = String.valueOf(anInt);
                        dismiss();
                    }
                } else {
                    showToastTip(false, getString(R.string.betRange));
                }
            }
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onChangeListener(position);
    }

    public void setInputChangeListener(BetInputChangeListener changeListener) {
        this.listener = changeListener;
    }


    interface BetInputChangeListener {
        void onChangeListener(int position);
    }
}
