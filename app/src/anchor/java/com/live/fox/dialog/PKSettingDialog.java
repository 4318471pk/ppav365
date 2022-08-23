package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.PkUser;
import com.live.fox.entity.User;
import com.live.fox.server.Api_LiveRecreation;
import com.live.fox.AnchorLiveActivity;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;


/**
 *
 */
public class PKSettingDialog extends DialogFragment implements View.OnClickListener{

    ImageView ivBack;
    ProgressBar pbInvertSwitch;
    ImageView ivInvertSwitch;
    TextView tvInvertSwitch;
    ImageView ivPkrandom;
    ImageView ivPkfriend;
    TextView tvFriendnum;
    LinearLayout layoutInvertSwitch;

    boolean isAcceptPk = true; //是否接受PK(开播默认打开)

    public boolean isShow = false;

    public static PKSettingDialog newInstance(boolean isAcceptPk) {
        PKSettingDialog fragment = new PKSettingDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isAcceptPk", isAcceptPk);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            isAcceptPk = bundle.getBoolean("isAcceptPk", false);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pk);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消

        isShow = true;

        ivBack = dialog.findViewById(R.id.iv_back);
        pbInvertSwitch = dialog.findViewById(R.id.pb_invert_switch);
        ivInvertSwitch = dialog.findViewById(R.id.iv_invert_switch);
        tvInvertSwitch = dialog.findViewById(R.id.tv_invert_switch);
        layoutInvertSwitch = dialog.findViewById(R.id.layout_invert_switch);

        ivPkrandom = dialog.findViewById(R.id.iv_pkrandom);
        ivPkfriend = dialog.findViewById(R.id.iv_pkfriend);
        tvFriendnum = dialog.findViewById(R.id.tv_friendnum);

        ivBack.setOnClickListener(this);
        dialog.findViewById(R.id.layout_invert_switch).setOnClickListener(this);
        ivPkrandom.setOnClickListener(this);
        ivPkfriend.setOnClickListener(this);

        setPkStatus(isAcceptPk ? 2 : 0);

        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isShow = true;
    }

    public void setPkStatus(int status){
        layoutInvertSwitch.setClickable(true);
        layoutInvertSwitch.setEnabled(true);
        pbInvertSwitch.setVisibility(View.INVISIBLE);
        switch (status){
            case 0: //关闭pk
                ivInvertSwitch.setImageResource(R.drawable.pk_switch_close);
//                tvInvertSwitch.setTextColor(Color.parseColor("#FFFFFF"));
                tvInvertSwitch.setText(getString(R.string.acceptClose));
                ((AnchorLiveActivity)getActivity()).isAcceptPk = false;
                break;
            case 1: //改变pk状态中
                layoutInvertSwitch.setClickable(false);
                layoutInvertSwitch.setEnabled(false);
                pbInvertSwitch.setVisibility(View.VISIBLE);
                break;
            case 2: //开启pk
                ivInvertSwitch.setImageResource(R.drawable.pk_switch_open);
//                tvInvertSwitch.setTextColor(Color.parseColor("#EB4A81"));
                tvInvertSwitch.setText(getString(R.string.acceptOpen));
                ((AnchorLiveActivity)getActivity()).isAcceptPk = true;
                break;
        }
    }

    public void showRoomTypeDes(){
//        switch (roomTyep){
//            case 0:
//                tvRoomtypedes.setText("当前为免费房间");
//                break;
//            case 1:
//                tvRoomtypedes.setText("当前为计时房间:"+roomPrice+"金币/分钟");
//                break;
//            case 2:
//                tvRoomtypedes.setText("当前为单次房间:"+roomPrice+"金币/次");
//                break;
//            case 3:
//                tvRoomtypedes.setText("当前为密码房间");
//                break;
//        }
    }


    @Override
    public void onClick(View view) {
        if(ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.layout_invert_switch:
                LogUtils.e("layout_invert_switch");
                setPkStatus(1);
                Api_LiveRecreation.ins().setPkStatus(!isAcceptPk, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        if(data!=null) LogUtils.e(data);
                        if (code == 0) {
                            isAcceptPk = !isAcceptPk;
                            setPkStatus(isAcceptPk ? 2 : 0);
                        } else {
                            setPkStatus(isAcceptPk ? 2 : 0);
                            ToastUtils.showShort(msg);
                        }
                    }
                });
                break;
            case R.id.iv_pkrandom:
                LogUtils.e("iv_pkrandom");
                ((AnchorLiveActivity)getActivity()).showLoadingDialog(getString(R.string.initiatingPK), false, false);
                Api_LiveRecreation.ins().sendOpenPk(-1, new JsonCallback<PkUser>() {
                    @Override
                    public void onSuccess(int code, String msg, PkUser data) {
                        ((AnchorLiveActivity)getActivity()).hideLoadingDialog();
                        if(data!=null) LogUtils.e(new Gson().toJson(data));
                        if (code == 0) {
                            if(data==null){
                                ToastUtils.showShort(getString(R.string.noReceivePK));
                            }else {
                                dismiss();
                                ((AnchorLiveActivity)getActivity()).showPKInvertLoadingDialog(data.getAnchorId(), data.getNickname());
                            }
                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
                break;
            case R.id.iv_pkfriend:
                LogUtils.e("iv_pkfriend");
                ((AnchorLiveActivity)getActivity()).showLoadingDialog("", false, false);
                Api_LiveRecreation.ins().getPkAcceptlist(new JsonCallback<ArrayList<User>>() {
                    @Override
                    public void onSuccess(int code, String msg, ArrayList<User> data) {
                        if(data!=null) LogUtils.e(data.size());
                        ((AnchorLiveActivity)getActivity()).hideLoadingDialog();
                        if (code == 0) {
                            if(data.size()==0){
                                ((AnchorLiveActivity)getActivity()).showToastTip(false, getString(R.string.noFriendPk));
                            }else {
                                ((AnchorLiveActivity)getActivity()).showPkFriendListDialog(data);
                            }
                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
                break;
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        LogUtils.e("onDismiss");
        isShow = false;
    }


    OnRoomSetClick roomSetClick;

    public void setOnRoomSetClick(OnRoomSetClick btnClick) {
        roomSetClick = btnClick;
    }

    public interface OnRoomSetClick {
        void onClick();
    }

}
