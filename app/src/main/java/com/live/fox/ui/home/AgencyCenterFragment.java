package com.live.fox.ui.home;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentAgencyCenterBinding;
import com.live.fox.ui.agency.AgencySaveActivity;
import com.live.fox.ui.agency.BindLowerActivity;
import com.live.fox.ui.agency.TeamManageActivity;
import com.live.fox.ui.mine.MineFragment;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.CommonUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;

public class AgencyCenterFragment extends BaseBindingFragment {

    FragmentAgencyCenterBinding mBind;

    private float dark = 0.35f;
    PopupWindow popupWindowEditCard;
    private EditText etName;
    private EditText etWechat;
    private EditText etPhone;
    private EditText etSign;
    private TextView tvSignNums;

    public static AgencyCenterFragment newInstance() {
        return new AgencyCenterFragment();
    }

    @Override
    public void onClickView(View view) {
        if (view == mBind.tvThisMonth){
            mBind.tvThisMonth.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff_2));
            mBind.tvThisMonth.setTextColor(getResources().getColor(R.color.colorA800FF));
            mBind.tvLastMonth.setBackground(null);
            mBind.tvLastMonth.setTextColor(getResources().getColor(R.color.white));
        } else if (view == mBind.tvLastMonth){
            mBind.tvThisMonth.setBackground(null);
            mBind.tvLastMonth.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff_2));
            mBind.tvLastMonth.setTextColor(getResources().getColor(R.color.colorA800FF));
            mBind.tvThisMonth.setTextColor(getResources().getColor(R.color.white));
        } else if (view == mBind.tvEdit){
            etName.setText(mBind.tvNickName.getText().toString());
            etWechat.setText(mBind.tvWechat.getText().toString());
            etPhone.setText(mBind.tvPhone.getText().toString());
            etSign.setText(mBind.tvSign.getText().toString());
            popupWindowEditCard.showAtLocation(mBind.getRoot(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            setRootAlpha(dark);
        } else if (view == mBind.tvCheck) {
            showPopMySuperior();
        } else if (view == mBind.layoutBindLower) {
            BindLowerActivity.startActivity(this.getActivity());
        } else if (view == mBind.layoutSave) {
            AgencySaveActivity.startActivity(this.getActivity());
        } else if (view == mBind.layoutTeamManage) {
            TeamManageActivity.startActivity(this.getActivity());
        } else if (view == mBind.layoutMoneyRecord) {

        } else if (view == mBind.layoutGameRecord) {

        }
    }


    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_agency_center;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) return;

        StatusBarUtil.setStatusBarFulAlpha(requireActivity());
        BarUtils.setStatusBarVisibility(requireActivity(), true);
        BarUtils.setStatusBarLightMode(requireActivity(), false);
    }


    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);

        mBind.tvCopyCard.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mBind.tvCheck.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        setPopMyCard();
    }

    private void setPopMyCard(){
        View popupView = this.getActivity().getLayoutInflater().inflate(R.layout.pop_edit_my_card,null);

        popupWindowEditCard = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindowEditCard.setFocusable(true);
        popupWindowEditCard.setOutsideTouchable(false);// 设置同意在外点击消失

        popupWindowEditCard.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //在dismiss中恢复透明度
                setRootAlpha(1f);
            }
        });
        popupWindowEditCard.setAnimationStyle(R.style.ActionSheetDialogAnimation);

        etName = popupView.findViewById(R.id.etName);
        etWechat = popupView.findViewById(R.id.etWechat);
        etPhone= popupView.findViewById(R.id.etPhone);
        etSign = popupView.findViewById(R.id.etSign);
        tvSignNums = popupView.findViewById(R.id.tvNums);

        popupView.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowEditCard.dismiss();
            }
        });

        popupView.findViewById(R.id.tvSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getText().toString() != null) {
                    if (!CommonUtils.checkPhone(etPhone.getText().toString())){
                        ToastUtils.showShort(getString(R.string.input_right_phone_num));
                        return;
                    }
                }
                mBind.tvNickName.setText(etName.getText().toString().trim());
                mBind.tvWechat.setText(etWechat.getText().toString().trim());
                mBind.tvPhone.setText(etPhone.getText().toString().trim());
                mBind.tvSign.setText(etSign.getText().toString().trim());
                popupWindowEditCard.dismiss();
            }
        });

    etSign.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {}

          @Override
          public void afterTextChanged(Editable s) {
            if (s.length() > 32) {
              etSign.setText(s.toString().substring(0, 32));
              tvSignNums.setText("32/32");

            } else {
              tvSignNums.setText(s.length() + "/32");
            }
          }
        });
    }

    private void showPopMySuperior(){
        View popupView = this.getActivity().getLayoutInflater().inflate(R.layout.pop_my_superior,null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);// 设置同意在外点击消失

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //在dismiss中恢复透明度
                setRootAlpha(1f);
            }
        });
        popupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);

//        etName = popupView.findViewById(R.id.etName);
//        etWechat = popupView.findViewById(R.id.etWechat);
//        etPhone= popupView.findViewById(R.id.etPhone);
//        etSign = popupView.findViewById(R.id.etSign);
//        tvSignNums = popupView.findViewById(R.id.tvNums);
        TextView tvWechat = popupView.findViewById(R.id.tvWechat);
        TextView tvPhone = popupView.findViewById(R.id.tvPhone);

        popupView.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupView.findViewById(R.id.ivCopyPhone).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardUtils.copyText(tvPhone.getText());
                        ToastUtils.showShort(getString(R.string.phone_copy_suc));
                    }
                });

        popupView.findViewById(R.id.ivCopyWechat).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardUtils.copyText(tvWechat.getText());
                        ToastUtils.showShort(getString(R.string.wechat_copy_suc));
                    }
                });
        popupWindow.showAtLocation(mBind.getRoot(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        setRootAlpha(dark);

    }



    private void setRootAlpha(float al){
        WindowManager.LayoutParams lp = this.getActivity().getWindow().getAttributes();
        lp.alpha= al;
        this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getActivity().getWindow().setAttributes(lp);
    }


}
