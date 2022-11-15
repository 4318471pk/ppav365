package com.live.fox.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogPlzDontLeaveBinding;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_User;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

public class PleaseDontLeaveDialog extends BaseBindingDialogFragment {

    DialogPlzDontLeaveBinding mBind;
    String image,uid;

    public static PleaseDontLeaveDialog getInstance(String image,String uid) {
        PleaseDontLeaveDialog pleaseDontLeaveDialog=new PleaseDontLeaveDialog();
        pleaseDontLeaveDialog.image=image;
        pleaseDontLeaveDialog.uid=uid;
        return pleaseDontLeaveDialog;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        setWindowsFlag();
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
            case R.id.ivCloseDialog:
                dismissAllowingStateLoss();
                break;
            case R.id.tvFollowAndExit:
                if(!TextUtils.isEmpty(uid))
                {
                    Api_User.ins().followUser(uid, true, new JsonCallback<String>() {
                        @Override
                        public void onSuccess(int code, String msg, String data) {

                        }
                    });
                }
                getActivity().finish();
                dismissAllowingStateLoss();
                break;
            case R.id.tvJustLeave:
                getActivity().finish();
                dismissAllowingStateLoss();
                break;

        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_plz_dont_leave;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);
        int screenWidth = ScreenUtils.getScreenWidth(getActivity());
        int dialogWidth = (int) (screenWidth * 0.75f);
        mBind.llContent.getLayoutParams().width = dialogWidth;
        LinearLayout.LayoutParams imgLL = (LinearLayout.LayoutParams) mBind.ivCenterIMG.getLayoutParams();
        imgLL.width = (int) (dialogWidth * 0.4f);
        imgLL.height = imgLL.width * 238 / 226;
        mBind.ivCenterIMG.setLayoutParams(imgLL);

        LinearLayout.LayoutParams followAndLeaveLL = (LinearLayout.LayoutParams) mBind.tvFollowAndExit.getLayoutParams();
        followAndLeaveLL.width = (int) (dialogWidth * 0.89f);
        followAndLeaveLL.height = followAndLeaveLL.width * 120 / 750;
        mBind.tvFollowAndExit.setLayoutParams(followAndLeaveLL);
        view.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(image))
        {
            GlideUtils.loadCircleImage(getContext(),image,R.mipmap.user_head_error,R.mipmap.user_head_error,mBind.rivImage);
        }

    }

}
