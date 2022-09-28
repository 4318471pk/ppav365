package com.live.fox.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.live.fox.App;
import com.live.fox.R;
import com.live.fox.dialog.LoadingBindingDialogFragment;

import org.jetbrains.annotations.NotNull;

public abstract class BaseBindingFragment extends Fragment {

    ViewDataBinding viewDataBinding;
    LoadingBindingDialogFragment loadingBindingDialogFragment;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
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
        initView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewDataBinding.unbind();
        viewDataBinding=null;
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
