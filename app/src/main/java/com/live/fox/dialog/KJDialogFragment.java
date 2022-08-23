package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;


public class KJDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_kj);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0.05f);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
        return dialog;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kj, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView ivLisi = view.findViewById(R.id.iv_lisi);
        ImageView ivMykj = view.findViewById(R.id.iv_mykj);
        ivLisi.setOnClickListener(v -> {
            LSDialogFragment lsDialogFragment = LSDialogFragment.newInstance();
            if (!lsDialogFragment.isAdded()) {
                lsDialogFragment.show(requireActivity().getSupportFragmentManager(), LSDialogFragment.class.getSimpleName());
            }
        });
        ivMykj.setOnClickListener(v -> {
            MyTZDialogFragment myTZDialogFragment = MyTZDialogFragment.newInstance();
            if (!myTZDialogFragment.isAdded()) {
                myTZDialogFragment.show(requireActivity().getSupportFragmentManager(), MyTZDialogFragment.class.getSimpleName());
            }
        });
    }

    public static KJDialogFragment newInstance() {
        return new KJDialogFragment();
    }
}
