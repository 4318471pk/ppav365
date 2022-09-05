package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.live.fox.R;

/**
 * 自定义下注金额
 * 编辑弹窗
 */
public class EditTextDialog extends DialogFragment {

    private TextView title;
    private TextView editText;
    private TextView submit;

    public static String PARCELABLE_KEY = "edit text parcelable key";
    private OnSubmitClick submitClick;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //背景透明
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.shape_corners_10_white));
        window.setDimAmount(0f);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_text, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        title = view.findViewById(R.id.dialog_edit_text_title);
        editText = view.findViewById(R.id.dialog_edit_content);
        submit = view.findViewById(R.id.dialog_edit_submit);
    }

    private void initData() {
        if (getArguments() != null) {
            EditTextEntity entity = getArguments().getParcelable(PARCELABLE_KEY);
            title.setText(entity.getTitleStr());
            editText.setHint(entity.getEditTextHint());
            submit.setText(entity.getSubmitStr());
        }
        submit.setOnClickListener(view -> submitClick.onSubmitClick(editText.getText().toString()));
    }

    public void setSubmitClick(OnSubmitClick submitClick) {
        this.submitClick = submitClick;
    }

    public interface OnSubmitClick {
        void onSubmitClick(String editContent);
    }

    /**
     * 设置的内容
     */
    public static class EditTextEntity implements Parcelable {

        public String titleStr;
        public String editTextHint;
        public String submitStr;

        public EditTextEntity() {
        }

        protected EditTextEntity(Parcel in) {
            titleStr = in.readString();
            editTextHint = in.readString();
            submitStr = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(titleStr);
            dest.writeString(editTextHint);
            dest.writeString(submitStr);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<EditTextEntity> CREATOR = new Creator<EditTextEntity>() {
            @Override
            public EditTextEntity createFromParcel(Parcel in) {
                return new EditTextEntity(in);
            }

            @Override
            public EditTextEntity[] newArray(int size) {
                return new EditTextEntity[size];
            }
        };

        public String getTitleStr() {
            return titleStr;
        }

        public void setTitleStr(String titleStr) {
            this.titleStr = titleStr;
        }

        public String getEditTextHint() {
            return editTextHint;
        }

        public void setEditTextHint(String editTextHint) {
            this.editTextHint = editTextHint;
        }

        public String getSubmitStr() {
            return submitStr;
        }

        public void setSubmitStr(String submitStr) {
            this.submitStr = submitStr;
        }
    }
}
