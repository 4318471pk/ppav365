package com.live.fox.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.live.fox.App;
import com.live.fox.R;
import com.live.fox.dialog.LoadingBindingDialogFragment;

import org.jetbrains.annotations.NotNull;

public abstract class BaseBindingFragment extends BaseFragment {

    ViewDataBinding viewDataBinding;
    LoadingBindingDialogFragment loadingBindingDialogFragment;
    InputMethodManager imm;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        if(onCreateLayoutId()==0)
        {
            return null;
        }
        viewDataBinding= DataBindingUtil.inflate(inflater,onCreateLayoutId(),container,false);
        viewDataBinding.setLifecycleOwner(this);
        return viewDataBinding.getRoot();
    }

    public <T extends ViewDataBinding> T getViewDataBinding() {
        return (T)viewDataBinding;
    }

    public void showLoadingDialog() {
        if(loadingBindingDialogFragment==null)
        {
            loadingBindingDialogFragment=LoadingBindingDialogFragment.getInstance();
            DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), loadingBindingDialogFragment);
        }

    }

    public void dismissLoadingDialog()
    {
        if(loadingBindingDialogFragment!=null)
        {
            loadingBindingDialogFragment.dismissAllowingStateLoss();
            loadingBindingDialogFragment=null;
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        initView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(viewDataBinding!=null)
        {
            viewDataBinding.unbind();
            viewDataBinding=null;
        }
    }

    public boolean isActivityOK()
    {
        return getActivity()!=null && !getActivity().isDestroyed() && !getActivity().isFinishing() && getView()!=null && isAdded();
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


    public void setWindowsFlag()
    {
        if(getActivity()!=null && !getActivity().isFinishing() && !getActivity().isDestroyed())
        {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                    WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

//        setFullscreen(true, true);
    }

    public void hideKeyBoard(View view)
    {
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public void showKeyBoard(View view)
    {
        imm.showSoftInput(view,0);
    }

    public void notifyFragment(){}
    public void onKeyBack(){}
    public abstract void onClickView(View view);
    public abstract int onCreateLayoutId();
    public abstract void initView(View view);
}
