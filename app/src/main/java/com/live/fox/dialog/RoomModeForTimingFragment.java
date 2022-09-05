package com.live.fox.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;


/**
 * 开播时的 计时房间设置
 */
public class RoomModeForTimingFragment extends DialogFragment {

    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private TextView tvCancel;

    public int roomPrice = 0;

    public static RoomModeForTimingFragment newInstance() {
        RoomModeForTimingFragment fragment = new RoomModeForTimingFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        setCancelable(false);

        View view = inflater.inflate(R.layout.dialog_roommode_timing, container, false);
        initView(view);
        return view;
    }

    private void initView(View bindSource) {
        rg = bindSource.findViewById(R.id.rg_);
        rb1 = bindSource.findViewById(R.id.rb_1);
        rb2 = bindSource.findViewById(R.id.rb_2);
        rb3 = bindSource.findViewById(R.id.rb_3);
        tvCancel = bindSource.findViewById(R.id.tv_cancel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        rg.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.rb_1:
                    if (btnClick != null) btnClick.onClick(0, 2);
                    break;
                case R.id.rb_2:
                    if (btnClick != null) btnClick.onClick(1, 4);
                    break;
                case R.id.rb_3:
                    if (btnClick != null) btnClick.onClick(2, 6);
                    break;
            }
            dismiss();
        });
        tvCancel.setOnClickListener(view1 -> dismiss());
    }

    OnSelBtnClick btnClick;

    public void setBtnClick(OnSelBtnClick btnClick) {
        this.btnClick = btnClick;
    }

    public interface OnSelBtnClick {
        void onClick(int selPos, int money);
    }

}
