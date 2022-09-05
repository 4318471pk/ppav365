package com.live.fox.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.server.Api_LiveRecreation;
import com.live.fox.AnchorLiveActivity;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 邀请PK 弹框
 */
public class PKInvertDialog extends DialogFragment {

    TextView tvTitle;
    public boolean isShow = false;

    String pkStreamUrl;
    long pkUid;
    String pkAvatar;
    String pkNickName;


    public static PKInvertDialog newInstance(String pkStreamUrl, long pkUid, String pkAvatar, String pkNickName) {
        PKInvertDialog fragment = new PKInvertDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("pkUid", pkUid);
        bundle.putString("pkAvatar", pkAvatar);
        bundle.putString("pkNickName", pkNickName);
        bundle.putString("pkStreamUrl", pkStreamUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogsWithoutBlack);

        Bundle bundle = getArguments();
        if (bundle != null) {
            pkUid = bundle.getLong("pkUid", 0);
            pkAvatar = bundle.getString("pkAvatar", "");
            pkNickName = bundle.getString("pkNickName", "");
            pkStreamUrl = bundle.getString("pkStreamUrl", "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_pkinvert, container, false);
    }

    //如果需要修改大小 修改显示动画 重写此方法
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        window.setGravity(Gravity.BOTTOM);  //设置Dialog的显示位置
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation_RightIn); // 进出动画效果
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.dimAmount = 0f;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(wlp);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isShow = true;

        initView(view);

        SpanUtils spanUtils = new SpanUtils();
//        spanUtils.append(getString(R.string.anchor)+" ").setForegroundColor(Color.WHITE);
        spanUtils.append(pkNickName).setForegroundColor(Color.parseColor("#FFC57C"));
        spanUtils.append(" " + getString(R.string.sendPK)).setForegroundColor(Color.WHITE);
        tvTitle.setText(spanUtils.create());
    }


    public void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        view.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AnchorLiveActivity) getActivity()).showLoadingDialog("", false, false);
                Api_LiveRecreation.ins().sendRsp(pkUid, true, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        ((AnchorLiveActivity) getActivity()).hideLoadingDialog();
                        if (data != null) LogUtils.e(data);
                        if (code == 0) {
                            ((AnchorLiveActivity) getActivity()).startPK(pkStreamUrl, pkUid, pkAvatar, pkNickName);
                        } else {
                            ToastUtils.showShort(msg);
                        }
                        dismiss();
                    }
                });

            }
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Api_LiveRecreation.ins().sendRsp(pkUid, false, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        if (data != null) LogUtils.e(data);
                        if (code != 0) {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
            }
        });
    }


    @Override
    public void dismiss() {
        isShow = false;
        super.dismiss();
    }

    OnBtnSureClick btnSureClick;

    public void setBtnSureClick(OnBtnSureClick btnClick) {
        btnSureClick = btnClick;
    }

    public interface OnBtnSureClick {
        void onClick();
    }

}
