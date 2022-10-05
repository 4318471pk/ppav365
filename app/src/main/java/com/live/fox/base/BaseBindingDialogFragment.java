package com.live.fox.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

import com.live.fox.App;
import com.live.fox.R;
import com.live.fox.dialog.LoadingBindingDialogFragment;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

/**
 * <p>类描述： DialogFragment 基类
 * <p>创建人：Simon
 * <p>创建时间：2019-02-14
 * <p>修改人：Simon
 * <p>修改时间：2019-02-14
 * <p>修改备注：
 **/

public abstract class BaseBindingDialogFragment extends DialogFragment  {
    //宽高比例 最高1 最低0
    protected double intScreenWProportion = 1; //宽比例
    protected double intScreenHProportion = 1; //高比例
    protected boolean mIsOutCanback = false; //是否点击 dialog外的地方dismiss
    protected boolean mIsKeyCanback = true; //是否点击 点击物理返回键可以取消
    protected WeakReference<Context> mWeakContext;

    ViewDataBinding viewDataBinding;
    LoadingBindingDialogFragment loadingBindingDialogFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    public WeakReference<Context> getFragmentContext()
    {
        return mWeakContext;
    }

    public boolean isConditionOk()
    {
        return mWeakContext!=null && mWeakContext.get()!=null ;
    }

    public <T extends ViewDataBinding> T getViewDataBinding() {
        return (T)viewDataBinding;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        if(getView()==null)
        {
            return null;
        }
        return getView().findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewDataBinding= DataBindingUtil.inflate(inflater,onCreateLayoutId(),container,false);
        viewDataBinding.setLifecycleOwner(this);
        return viewDataBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    getDialog().setCanceledOnTouchOutside(mIsKeyCanback);//键盘点击时是否可以取消--不需要设置了
                    return !mIsKeyCanback;//return true 不往上传递则关闭不了，默认是可以取消，即return false
                } else {
                    return false;
                }
            }
        });
        getDialog().setCanceledOnTouchOutside(mIsOutCanback);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mWeakContext = new WeakReference<>(context);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("web", "横屏");
            // 横屏
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("web", "竖屏");
            // 竖屏
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        DialogFramentManager.getInstance().removeDialog(this);
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
        DialogFramentManager.getInstance().removeDialog(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewDataBinding.unbind();
        viewDataBinding=null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void setDialogViewRatio(float percent,View view)
    {
        int screenWidth= ScreenUtils.getScreenWidth(getFragmentContext().get());
        float viewWidth=1.0f* screenWidth*percent;
        float margin=(screenWidth-viewWidth)/2;
        ViewGroup.LayoutParams vl= view.getLayoutParams();
        vl.width=(int) viewWidth;

        if(vl instanceof RelativeLayout.LayoutParams)
        {
            RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams)vl;
            rl.leftMargin=(int) margin;
            view.setLayoutParams(rl);
        }
        else if(vl instanceof LinearLayout.LayoutParams)
        {
            LinearLayout.LayoutParams ll=(LinearLayout.LayoutParams)vl;
            ll.leftMargin=(int) margin;
            view.setLayoutParams(ll);
        }
        else if(vl instanceof FrameLayout.LayoutParams)
        {
            FrameLayout.LayoutParams fl=(FrameLayout.LayoutParams)vl;
            fl.leftMargin=(int) margin;
            view.setLayoutParams(fl);
        }

    }

    public void showLoadingDialog() {
            loadingBindingDialogFragment= LoadingBindingDialogFragment.getInstance();
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), loadingBindingDialogFragment);
    }

    public void dismissLoadingDialog()
    {
        if(loadingBindingDialogFragment!=null)
        {
            loadingBindingDialogFragment.dismissAllowingStateLoss();
            loadingBindingDialogFragment=null;
        }
    }

    public String getStringWithoutContext(int idRes)
    {
        if(isAdded() && getResources()!=null)
        {
            return getString(idRes);
        }
        else
        {
            return App.getInstance().getResources().getString(idRes);
        }
    }

    public abstract void onClickView(View view);
    public abstract int onCreateLayoutId();
    public abstract void initView(View view);
}
