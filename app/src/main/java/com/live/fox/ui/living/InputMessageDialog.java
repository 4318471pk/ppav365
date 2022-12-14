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
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.DialogInputMessageBinding;
import com.live.fox.entity.FlowDataBean;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.MyFlowLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//弃用
public class InputMessageDialog  {
//
//    DialogInputMessageBinding mBind;
//    DialogListener dialogListener;
//
//    public static InputMessageDialog getInstance()
//    {
//        return new InputMessageDialog();
//    }
//
//    public void setDialogListener(DialogListener dialogListener) {
//        this.dialogListener = dialogListener;
//    }
//
//    public void setFullscreen(boolean isShowStatusBar, boolean isShowNavigationBar) {
//        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//
//        if (!isShowStatusBar) {
//            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
//        }
//        if (!isShowNavigationBar) {
//            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        }
//        getDialog().getWindow().getDecorView().setSystemUiVisibility(uiOptions);
//
//        //隐藏标题栏
//        // getSupportActionBar().hide();
//        setNavigationStatusColor(Color.TRANSPARENT);
//    }
//
//    public void setNavigationStatusColor(int color) {
//        //VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
//        if (Build.VERSION.SDK_INT >= 21) {
//            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getDialog().getWindow().setNavigationBarColor(color);
//            getDialog().getWindow().setStatusBarColor(color);
//        }
//    }
//
//    private  void setAndroidNativeLightStatusBar( boolean dark) {
//        View decor =getDialog().getWindow().getDecorView();
//        if (dark) {
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        } else {
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
//    }
//
//    @Nullable
//    @org.jetbrains.annotations.Nullable
//    @Override
//    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//
////        //设置dialog背景色为透明色
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        //设置dialog窗体颜色透明
//        getDialog().getWindow().setDimAmount(0);
//        setFullscreen(true, true);
//        setAndroidNativeLightStatusBar( true);
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    @Override
//    public void onClickView(View view) {
//
//    }
//
//    @Override
//    public int onCreateLayoutId() {
//        return R.layout.dialog_input_message;
//    }
//
//    @Override
//    public void initView(View view) {
//        mBind=getViewDataBinding();
////        initBotView();
////        mBind.etDiaMessage.postDelayed(new Runnable() {
////            @RequiresApi(api = Build.VERSION_CODES.M)
////            @Override
////            public void run() {
////                mBind.etDiaMessage.performClick();
////            }
////        },500);
//    }
//
//
//    private void initBotView()
//    {
//        int dip10= ScreenUtils.getDip2px(getActivity(),10);
//        MyFlowLayout myFlowLayout= findViewById(R.id.flTempleLayout);
//        myFlowLayout.setHorizontalMargin(dip10);
//        myFlowLayout.setVerticalMargin(dip10);
//        myFlowLayout.setTextMaxLength(20);
//        myFlowLayout.setTextBackground(R.drawable.bg_d8bde7);
//        myFlowLayout.setTextColor(0xffffffff);
//
//        List<FlowDataBean> mData = new ArrayList<>();
//        mData.add(new FlowDataBean("阿是假的"));
//        mData.add(new FlowDataBean("我气哦额我去哦额我去"));
//        mData.add(new FlowDataBean("i我去恶意我去额"));
//        mData.add(new FlowDataBean("阿是达拉斯空间的合理撒娇的拉萨剪刀手拉大距离撒娇了撒开多久啊深刻的哈萨克"));
//        mData.add(new FlowDataBean("222撒娇了撒开多哈萨克"));
//        mData.add(new FlowDataBean("阿是达拉斯空间的合理撒娇的拉萨剪刀手拉大距离撒娇了撒开多久啊深刻的哈萨克"));
//        mData.add(new FlowDataBean("222撒娇了撒开多哈萨克"));
//        mData.add(new FlowDataBean("阿是达拉斯空间的合理撒娇的拉萨剪刀手拉大距离撒娇了撒开多久啊深刻的哈萨克"));
//        mData.add(new FlowDataBean("222撒娇了撒开多哈萨克"));
//        myFlowLayout.setTextList(mData);
//
//        LivingActivity livingActivity=(LivingActivity) getActivity();
//    }
//
//    @Override
//
//    public void onStart() {
//        super.onStart();
//        mBind.etDiaMessage.performClick();
//    }
//
//    public interface DialogListener
//    {
//        void onShowKeyBorad(int height);
//        void onDismiss();
//    }


}
