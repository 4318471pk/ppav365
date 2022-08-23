package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.live.fox.R;

/**
 * 改变房间的验证
 */
public class LiveRoomVerify extends DialogFragment {

    private LiveRoomConfirmListener roomConfirmListener;
    private EditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setDimAmount(0f);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        editText.setText("");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_live_room_verify, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        editText = rootView.findViewById(R.id.live_room_verify_edit_text);
        rootView.findViewById(R.id.live_room_verify_cancel).setOnClickListener(view -> dismiss());
        rootView.findViewById(R.id.live_room_verify_confirm).setOnClickListener(view -> {
            String strText = editText.getText().toString();
            if (roomConfirmListener != null && !TextUtils.isEmpty(strText)) {
                roomConfirmListener.onConfirmListener(strText);
            }
        });
    }


    public void setRoomConfirmListener(LiveRoomConfirmListener listener) {
        roomConfirmListener = listener;
    }

    public interface LiveRoomConfirmListener {
        void onConfirmListener(String strText);
    }
}
