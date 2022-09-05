package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.flyco.roundview.RoundTextView;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.server.Api_LiveRecreation;
import com.live.fox.AnchorLiveActivity;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.SpanUtils;


/**
 * PK邀请等待框
 */
public class PKInvertLoadingDialog extends DialogFragment implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivPkrandom;
    private TextView ivPkloadingtip;
    private RoundTextView tvCancel;

    public boolean isShow = false;

    long inverUid;
    String invertPkName;

    //邀请PK等待时间 30秒
    int invertWhitTime = 30;

    public static PKInvertLoadingDialog newInstance(long inverUid, String invertPkName) {
        PKInvertLoadingDialog fragment = new PKInvertLoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("inverUid", inverUid);
        bundle.putString("invertPkName", invertPkName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pkinvertloading, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            inverUid = bundle.getLong("inverUid", 0);
            invertPkName = bundle.getString("invertPkName", "");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false); // 外部點擊取消
        dialog.setCancelable(false);

        isShow = true;

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


    public void initView(View bindSource) {
        ivBack = bindSource.findViewById(R.id.iv_back);
        tvTitle = bindSource.findViewById(R.id.tv_title);
        ivPkrandom = bindSource.findViewById(R.id.iv_pkrandom);
        ivPkloadingtip = bindSource.findViewById(R.id.iv_pkloadingtip);
        tvCancel = bindSource.findViewById(R.id.tv_cancel);

        SpanUtils spanUtils = new SpanUtils();
        spanUtils.append(getString(R.string.inviting) + " ").setForegroundColor(Color.WHITE);
        spanUtils.append(invertPkName).setForegroundColor(Color.parseColor("#F0668A"));
        ivPkloadingtip.setText(spanUtils.create());

        tvCancel.setText(getString(R.string.cancel) + "(" + invertWhitTime + ")");
        invertLoadingHandel.sendEmptyMessageAtTime(1, 1000);

        ivBack.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }


    Handler invertLoadingHandel = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            invertWhitTime--;
            invertLoadingHandel.removeMessages(1);
            if (invertWhitTime == 0) {
                dismiss();
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                Api_LiveRecreation.ins().cancelPk(inverUid, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {

                    }
                });
                ((AnchorLiveActivity) getActivity()).showToastTip(false, getString(R.string.noAcceptPK));
//                showCancelPkDialg("对方未接受PK邀请", "知道了", "重新选择");
            } else {
                tvCancel.setText(getString(R.string.cancel) + "(" + invertWhitTime + ")");
                invertLoadingHandel.sendEmptyMessageDelayed(1, 1000);
            }
            return false;
        }
    });

    TipDialog tipDialog;

    public void showCancelPkDialg(String content, String btn1, String btn2) {
        tipDialog = DialogFactory.showTwoBtnDialog(getActivity(), content, btn1, btn2, new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog) {
                dialog.dismiss();
            }
        }, new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog) {
                invertLoadingHandel.removeMessages(1);
                dialog.dismiss();
                if (isShow) {
                    dismiss();
                }
                Api_LiveRecreation.ins().cancelPk(inverUid, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {

                    }
                });
                if (getActivity() == null) return;
                ((AnchorLiveActivity) getActivity()).showPkDialog();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_back:
                showCancelPkDialg(getString(R.string.invitingPK), getString(R.string.waitMore), getString(R.string.noWait));
                break;
            case R.id.tv_cancel:
                showCancelPkDialg(getString(R.string.invitingPK), getString(R.string.waitMore), getString(R.string.noWait));
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
        invertLoadingHandel.removeMessages(1);
    }


    OnRoomSetClick roomSetClick;

    public void setOnRoomSetClick(OnRoomSetClick btnClick) {
        roomSetClick = btnClick;
    }


    public interface OnRoomSetClick {
        void onClick();
    }

}
