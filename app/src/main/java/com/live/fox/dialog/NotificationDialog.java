package com.live.fox.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.utils.SPUtils;

/**
 * 通知提示弹窗
 */
public class NotificationDialog extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics dm = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_notification, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        SPUtils.getInstance().put(ConstantValue.NOTIFICATION_IS_SHOWED, true);
    }


    private void initView(View rootView) {
        TextView title = rootView.findViewById(R.id.notification_dialog_title);
        TextView content = rootView.findViewById(R.id.notification_dialog_content);

        title.setText(getText(R.string.notification_dialog_title));
        content.setText(String.format(getString(R.string.notification_dialog_content), getString(R.string.app_name)));

        rootView.findViewById(R.id.notification_dialog_cancel).setOnClickListener(view -> dismiss());
        rootView.findViewById(R.id.notification_dialog_confirm).setOnClickListener(view -> {
            goToSetting();
            dismiss();
        });
    }

    private void goToSetting() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {// android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", requireActivity().getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) { // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", requireActivity().getPackageName());
            intent.putExtra("app_uid", requireActivity().getApplicationInfo().uid);
        } else {//其它
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", requireActivity().getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
