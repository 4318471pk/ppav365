package com.live.fox.ui.living;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.listener.OnKeyboardStateListener;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.view.panel.IPanelView;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.DialogInputMessageBinding;

import org.jetbrains.annotations.NotNull;

public class InputMessageDialog extends BaseBindingDialogFragment {

    PanelSwitchHelper mHelper;
    DialogInputMessageBinding mBind;
    DialogListener dialogListener;

    public static InputMessageDialog getInstance()
    {
        return new InputMessageDialog();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
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
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_input_message;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        initBotView();
//        mBind.etDiaMessage.postDelayed(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void run() {
//                mBind.etDiaMessage.performClick();
//            }
//        },500);
    }


    private void initBotView()
    {
        LivingActivity livingActivity=(LivingActivity) getActivity();
        if (mHelper == null) {
            mHelper = new PanelSwitchHelper.Builder(this)
                    .addKeyboardStateListener(new OnKeyboardStateListener() {
                        @Override
                        public void onKeyboardChange(boolean b, int i) {
                            Log.e("onKeyboardChange",b+" "+i+" "+mHelper.isPanelState());
                            if(b && i>0)
                            {
                                if(dialogListener!=null)
                                {
                                    dialogListener.onShowKeyBorad(i);
                                }
                            }
                            else
                            {
//                                if(!mHelper.isPanelState())
//                                {
//                                    dismissAllowingStateLoss();
//                                    if(dialogListener!=null)
//                                    {
//                                        dialogListener.onDismiss();
//                                    }
//                                }
                            }
                        }
                    })
                    //可选
                    .addPanelChangeListener(new OnPanelChangeListener() {

                        @Override
                        public void onKeyboard() {
//                            livingActivity.setUserScrollAvailAble(false);
//                            int height=ScreenUtils.getScreenHeightWithoutBtnsBar(getActivity());
//                            livingControlPanel.mBind.rlMain.getLayoutParams().height=height;
                        }

                        @Override
                        public void onNone() {
                            dismissAllowingStateLoss();
                            if(dialogListener!=null)
                            {
                                dialogListener.onDismiss();
                            }
                            Log.e("onNone",mHelper.isKeyboardState()+" "+mHelper.isPanelState()+" "+mHelper.isResetState());
//                            livingActivity.setUserScrollAvailAble(true);
//                            int height=ScreenUtils.getScreenHeightWithoutBtnsBar(getActivity());
//                            livingControlPanel.mBind.rlMain.getLayoutParams().height=height;
                        }

                        @Override
                        public void onPanel(IPanelView view) {
//                            livingActivity.setUserScrollAvailAble(false);
//                            int height=ScreenUtils.getScreenHeightWithoutBtnsBar(getActivity());
//                            livingControlPanel.mBind.rlMain.getLayoutParams().height=height;
                        }

                        @Override
                        public void onPanelSizeChange(IPanelView panelView, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
                            Log.e("onPanelSizeChange",oldHeight+" "+height);
                        }
                    })
                    .logTrack(true)             //output log
                    .build();

            mHelper.setContentScrollOutsideEnable(false);
        }
    }

    @Override

    public void onStart() {
        super.onStart();
        mBind.etDiaMessage.performClick();
    }

    public interface DialogListener
    {
        void onShowKeyBorad(int height);
        void onDismiss();
    }


}
