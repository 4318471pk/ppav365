package com.live.fox.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.helper.SimpleTextWatcher;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 开播时的 密码房间/按次收费房间设置
 */
public class RoomModeForPwdFragment extends DialogFragment implements View.OnClickListener {

    int pageFlag; //1按次收费房间 2密码房间
    private TextView tvTitle;
    private TextView tvRightdes;
    private EditText et;

    String rightDes;

    boolean isShow = true;

    public static RoomModeForPwdFragment newInstance(int pageFlag) {
        RoomModeForPwdFragment fragment = new RoomModeForPwdFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pageFlag", pageFlag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        setCancelable(false);

        View view = inflater.inflate(R.layout.dialog_roommode_pwd, container, false);
        initView(view);
        return view;
    }

    private void initView(View bindSource) {
        tvTitle = bindSource.findViewById(R.id.tv_title);
        tvRightdes = bindSource.findViewById(R.id.tv_rightdes);
        et = bindSource.findViewById(R.id.et_);
        bindSource.findViewById(R.id.tv_cancel).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_sure).setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageFlag = bundle.getInt("pageFlag", 1);
        }

        isShow = true;
        setStyleByPageFlag();
    }

    public void setStyleByPageFlag() {
        if (pageFlag == 1) {//1按次收费房间
            tvTitle.setText(getString(R.string.singleRoomSetting));
            tvRightdes.setText(getString(R.string.zeroGoldTime));
            et.setHint(getString(R.string.oneThousandGoldTime));
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (!StringUtils.isEmpty(rightDes)) {
                tvRightdes.setText(rightDes + getString(R.string.goldTime));
            }

            et.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    tvRightdes.setText(editable + getString(R.string.goldTime));
                }
            });
        } else {
            //2密码房间
            tvTitle.setText(getString(R.string.passwordRoomSet));
            tvRightdes.setText(getString(R.string.passwordNo));
            et.setHint(getString(R.string.setPassword));
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (!StringUtils.isEmpty(rightDes)) {
                tvRightdes.setText(getString(R.string.password) + rightDes);
            }
        }
    }

    public void setTvRightdes(String txt) {
        this.rightDes = txt;
    }


    public boolean isShow() {
        return isShow;
    }

    @Override
    public void dismiss() {
        isShow = false;
        super.dismiss();
    }

    OnBtnSureClick btnSureClick;

    public void setBtnSureClick(OnBtnSureClick btnClick) {
        this.btnSureClick = btnClick;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                String etTxt = et.getText().toString().trim();
                if (StringUtils.isEmpty(etTxt)) {
                    ToastUtils.showShort(getString(R.string.noInputNumber));
                    return;
                }

                if (pageFlag == 1) {
                    tvRightdes.setText(etTxt + getString(R.string.goldTime));
                } else {
                    tvRightdes.setText(getString(R.string.password) + etTxt);
                }

                if (btnSureClick != null) {
                    btnSureClick.onClick(etTxt);
                }
                dismiss();
                break;
        }
    }

    public interface OnBtnSureClick {
        void onClick(String txt);
    }
}
