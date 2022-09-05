package com.live.fox.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flyco.roundview.RoundTextView;
import com.live.fox.R;
import com.live.fox.utils.ClickUtil;


/**
 * 升级提示框
 */
public class LevelUpDialog extends DialogFragment implements View.OnClickListener {

    ImageView iv_;
    TextView tv_leveltip;
    RoundTextView tv_cancel;

    int level;
    boolean isAnchor;

    public boolean isShow =false;

    public static LevelUpDialog newInstance() {
        LevelUpDialog fragment = new LevelUpDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
//            level = bundle.getInt("level", 1);
//            isAnchor = bundle.getBoolean("isAnchor", false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }

        return inflater.inflate(R.layout.dialog_levelup, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isShow = true;
        initView(view);
        refreshPage();
    }


    public void initView(View view) {
        iv_ = view.findViewById(R.id.iv_);
        tv_leveltip = view.findViewById(R.id.tv_leveltip);
        tv_cancel = view.findViewById(R.id.tv_cancel);

        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    public void setData(int level, boolean isAnchor){
        this.level = level;
        this.isAnchor = isAnchor;
        if(isShow){
            refreshPage();
        }
    }


    public void refreshPage() {
       if(isAnchor){
           iv_.setImageResource(R.drawable.levelup_anchor_ic);
           tv_leveltip.setText(getString(R.string.congratGrading)+level);
       }else {
           iv_.setImageResource(R.drawable.levelup_user_ic);
           tv_leveltip.setText(getString(R.string.congratGrad)+level);
       }
    }



    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
        }

    }

    @Override
    public void show(FragmentManager manager, String tag) {
//            mDismissed = false;
//            mShownByMe = true;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
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

    public interface OnBtnSureClick {
        void onClick();
    }

}
