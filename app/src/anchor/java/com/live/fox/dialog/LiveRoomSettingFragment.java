package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.AnchorLiveActivity;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.server.Api_Cp;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.ToastUtils;
import com.lovense.sdklibrary.LovenseToy;


/**
 * 开播时的 密码房间/按次收费房间设置
 */
public class LiveRoomSettingFragment extends DialogFragment implements View.OnClickListener {

    TextView tvRoomtypedes;
    LinearLayout llSet1;
    LinearLayout llSet2;
    LinearLayout llSet3;
    LinearLayout llSet4;
    TextView tvJx;
    TextView tvCarame;
    LinearLayout llPk;
    TextView tvPk;
    TextView tvJingyin;
    ImageView iv_jingyin;
    LinearLayout llToy;
    TextView tvToy;
    LinearLayout layout_cp;
    ImageView iv_cp;
    TextView tv_cp;

    int roomTyep;
    int roomPrice;
    String roomPwd;
    boolean mirror;
    boolean isFrontCarame;

    public boolean isShow = false;

    public static LiveRoomSettingFragment newInstance(int roomTyep, int roomPrice, String roomPwd, boolean mirror, boolean isFrontCarame) {
        LiveRoomSettingFragment fragment = new LiveRoomSettingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("roomTyep", roomTyep);
        bundle.putInt("roomPrice", roomPrice);
        bundle.putString("roomPwd", roomPwd);
        bundle.putBoolean("mirror", mirror);
        bundle.putBoolean("isFrontCarame", isFrontCarame);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            roomTyep = bundle.getInt("roomTyep", 0);
            roomPrice = bundle.getInt("roomPrice", 0);
            roomPwd = bundle.getString("roomPwd", "");
            mirror = bundle.getBoolean("mirror", true);
            isFrontCarame = bundle.getBoolean("isFrontCarame", true);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_liveroom_setting);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消

        isShow = true;

        tvRoomtypedes = dialog.findViewById(R.id.tv_roomtypedes);
        llSet1 = dialog.findViewById(R.id.ll_roomset);
        llSet2 = dialog.findViewById(R.id.ll_beauty);
        llSet3 = dialog.findViewById(R.id.ll_jx);
        llSet4 = dialog.findViewById(R.id.ll_camerarotate);
        tvJx = dialog.findViewById(R.id.tv_jx);
        tvCarame = dialog.findViewById(R.id.tv_carame);
        llPk = dialog.findViewById(R.id.layout_pk);
        tvPk = dialog.findViewById(R.id.tv_pk);
        llToy = dialog.findViewById(R.id.layout_toy);
        tvToy = dialog.findViewById(R.id.tv_toy);
        tvJingyin = dialog.findViewById(R.id.tv_jingyin);
        iv_jingyin = dialog.findViewById(R.id.iv_jingyin);
        layout_cp = dialog.findViewById(R.id.layout_cp);
        iv_cp = dialog.findViewById(R.id.iv_cp);
        tv_cp = dialog.findViewById(R.id.tv_cp);
        llSet1.setOnClickListener(this);
        llSet2.setOnClickListener(this);
        llSet3.setOnClickListener(this);
        llSet4.setOnClickListener(this);
        llPk.setOnClickListener(this);
        llToy.setOnClickListener(this);
        layout_cp.setOnClickListener(this);
        dialog.findViewById(R.id.layout_jingyin).setOnClickListener(this);

        showRoomTypeDes();
        updateJx(mirror);
        updateCarame(isFrontCarame);
        updatePk();
        updateMute();
        updateToy();
        updateCp();

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


    public void showRoomTypeDes() {
        switch (roomTyep) {
            case 0:
                tvRoomtypedes.setText(getString(R.string.freeRoom));
                break;
            case 1:
                tvRoomtypedes.setText(roomPrice + getString(R.string.goldMinute));
                break;
            case 2:
                tvRoomtypedes.setText(roomPrice + getString(R.string.goldOnce));
                break;
            case 3:
                tvRoomtypedes.setText(getString(R.string.currentPwRoom));
                break;
        }
    }

    public void updateJx(boolean mirror) {
        this.mirror = mirror;
        if (mirror) {
            tvJx.setTextColor(Color.parseColor("#FFFFFF"));
            tvJx.setText(getString(R.string.mirrorOpen));
        } else {
            tvJx.setTextColor(Color.parseColor("#EB4A81"));
            tvJx.setText(getString(R.string.mirrorClose));
        }
    }

    public void updateCarame(boolean isFrontCarame) {
        this.isFrontCarame = isFrontCarame;
        if (isFrontCarame) {
            tvCarame.setTextColor(Color.parseColor("#FFFFFF"));
            tvCarame.setText(getString(R.string.cameraFront));
        } else {
            tvCarame.setTextColor(Color.parseColor("#EB4A81"));
            tvCarame.setText(getString(R.string.cameraBack));
        }
    }

    public void updateMute() {
        boolean isMute = ((AnchorLiveActivity) requireActivity()).isMute;
        if (isMute) {
            tvJingyin.setTextColor(Color.parseColor("#EB4A81"));
            iv_jingyin.setImageResource(R.drawable.dialog_live_syr);
        } else {
            tvJingyin.setTextColor(Color.parseColor("#FFFFFF"));
            iv_jingyin.setImageResource(R.drawable.dialog_live_sy);
        }
    }

    public void updateCp() {
        if (0 == ((AnchorLiveActivity) requireActivity()).cpFlag) {//开启弹窗
            tv_cp.setTextColor(Color.parseColor("#FFFFFF"));
            iv_cp.setImageResource(R.drawable.dialog_live_cl);
        } else {
            tv_cp.setTextColor(Color.parseColor("#EB4A81"));
            iv_cp.setImageResource(R.drawable.dialog_live_cr);
        }
    }

    public void updatePk() {
        boolean isPking = ((AnchorLiveActivity) requireActivity()).isPking;
        boolean isPkCFing = ((AnchorLiveActivity) requireActivity()).isPkCFing;
        if (isPking) {
            tvPk.setTextColor(Color.parseColor("#EB4A81"));
            tvPk.setText("");
        } else if (isPkCFing) {
            tvPk.setTextColor(Color.parseColor("#EB4A81"));
            tvPk.setText(getString(R.string.punishment));
        } else {
            tvPk.setTextColor(Color.parseColor("#FFFFFF"));
            tvPk.setText("PK");
        }
    }

    public void updateToy() {
        LovenseToy mLovenseToy = ((AnchorLiveActivity) requireActivity()).mLovenseToy;
        if (mLovenseToy != null) {
            tvToy.setTextColor(Color.parseColor("#EB4A81"));
            tvToy.setText(getString(R.string.connected));
        } else {
            tvToy.setTextColor(Color.parseColor("#FFFFFF"));
            tvToy.setText(getString(R.string.skipping));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_roomset:
                dismiss();
                if (roomSetClick != null) {
                    roomSetClick.onClick(1);
                }
                break;
            case R.id.ll_beauty:
                dismiss();
                if (roomSetClick != null) {
                    roomSetClick.onClick(2);
                }
                break;
            case R.id.ll_jx:
                if (getActivity() instanceof AnchorLiveActivity) {
                    updateJx(!mirror);
                    ((AnchorLiveActivity) getActivity()).switchJx();
                }
                break;
            case R.id.ll_camerarotate:
                if (getActivity() instanceof AnchorLiveActivity) {
                    updateCarame(!isFrontCarame);
                    ((AnchorLiveActivity) getActivity()).switchCamera();
                }
                break;
            case R.id.layout_jingyin:  //关闭音频
                ((AnchorLiveActivity) requireActivity()).setAnchorMute(!((AnchorLiveActivity) requireActivity()).isMute);
                updateMute();
                break;
            case R.id.layout_pk:
                ((AnchorLiveActivity) requireActivity()).showPkDialog();
                break;
            case R.id.layout_toy:
                if (((AnchorLiveActivity) requireActivity()).mLovenseToy != null) {
                    DialogFactory.showTwoBtnDialog(getActivity(), getString(R.string.toy_change),
                            (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                        dialog.dismiss();
                        dismiss();
                        ((AnchorLiveActivity) requireActivity()).searchJoy();
                    });
                } else {
                    dismiss();
                    ((AnchorLiveActivity) requireActivity()).searchJoy();
                }

                break;
            case R.id.layout_cp:
                String liveId = "";

                if (SPUtils.getInstance("liveforanchor").contains("liveId")) {
                    liveId = SPUtils.getInstance("liveforanchor").getString("liveId");
                    String name;
                    if (1 == ((AnchorLiveActivity) requireActivity()).names.size()) {
                        name = ((AnchorLiveActivity) requireActivity()).names.get(0);
                    } else if (2 == ((AnchorLiveActivity) requireActivity()).names.size()) {
                        name = ((AnchorLiveActivity) requireActivity()).names.get(0) + "," +
                                ((AnchorLiveActivity) requireActivity()).names.get(1);
                    } else {
                        ToastUtils.showShort("彩种数量不对");
                        return;
                    }

                    Api_Cp.ins().getPushResultMsgFlag(Long.parseLong(liveId),
                            ((AnchorLiveActivity) requireActivity()).cpFlag, name, new JsonCallback<String>() {
                        @Override
                        public void onSuccess(int code, String msg, String data) {
                            if (code == 0 && "success".equals(data)) {
                                if (0 == ((AnchorLiveActivity) requireActivity()).cpFlag) {
                                    ((AnchorLiveActivity) requireActivity()).cpFlag = 1;
                                } else {
                                    ((AnchorLiveActivity) requireActivity()).cpFlag = 0;
                                }
                                updateCp();
                            } else {
                                ToastUtils.showShort(msg);
                            }
                        }
                    });
                }
                break;
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
    }

    OnRoomSetClick roomSetClick;

    public void setOnRoomSetClick(OnRoomSetClick btnClick) {
        roomSetClick = btnClick;
    }

    public interface OnRoomSetClick {
        //1房间设置 2美颜设置
        void onClick(int type);
    }

}
