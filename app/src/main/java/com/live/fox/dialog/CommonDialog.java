package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.live.fox.R;
import com.luck.picture.lib.tools.ScreenUtils;

/**
 * 项目先共同的弹窗
 */
public class CommonDialog extends DialogFragment {

    private TextView cancelView, confirmView, contentView, titleView;
    private String title, content, cancel, confirm;
    private View.OnClickListener cancelListener, confirmListener;
    private Dialog dialog;

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
        View rootView = inflater.inflate(R.layout.dialog_common, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        titleView = rootView.findViewById(R.id.dialog_common_title);
        contentView = rootView.findViewById(R.id.dialog_common_content);
        cancelView = rootView.findViewById(R.id.dialog_common_cancel);
        confirmView = rootView.findViewById(R.id.dialog_common_confirm);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(title)) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setText(title);
        }

        if (!TextUtils.isEmpty(content)) {
            contentView.setText(content);
        }

        if (!TextUtils.isEmpty(cancel)) {
            cancelView.setText(cancel);
        }

        if (!TextUtils.isEmpty(confirm)) {
            confirmView.setText(confirm);
        }

        if (cancelListener == null) {
            cancelView.setVisibility(View.GONE);
        } else {
            cancelView.setOnClickListener(cancelListener);
        }

        if (confirmListener == null) {
            confirmView.setVisibility(View.GONE);
        } else {
            confirmView.setOnClickListener(confirmListener);
        }
    }

    /**
     * 设置弹窗内容
     *
     * @param title           标题
     * @param content         内容
     * @param cancel          取消文本
     * @param confirm         确认文本
     * @param cancelListener  取消监听
     * @param confirmListener 确认监听
     */
    public void setDialogContent(String title, String content, String cancel, String confirm,
                                 View.OnClickListener cancelListener,
                                 View.OnClickListener confirmListener) {
        this.title = title;
        this.content = content;
        this.cancel = cancel;
        this.confirm = confirm;
        this.cancelListener = cancelListener;
        this.confirmListener = confirmListener;
    }
}
