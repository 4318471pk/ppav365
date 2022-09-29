package com.live.fox.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.device.ScreenUtils;

public abstract class BaseBindingViewActivity extends BaseActivity {

    ImageView ivHeadLeft;
    TextView tvHeadTitle;
    TextView tvTitleRight;
    int screenWidth;
    ViewDataBinding viewDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding=setBindLayoutID(onCreateLayoutId());
        viewDataBinding.setLifecycleOwner(this);
        initView();
    }

    public boolean isHasHeader() {
        //重写这个方法 返回false就不会加头部
        return true;
    }

    public <T extends ViewDataBinding> T getViewDataBinding() {
        return (T)viewDataBinding;
    }

    private  <T extends ViewDataBinding> T setBindLayoutID(int layoutResID) {
        if(layoutResID==0)return null;

        ViewDataBinding binding=null;
        if(isHasHeader())
        {
            LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_header_layout,null);
            binding = DataBindingUtil.bind(getLayoutInflater().inflate(layoutResID, view,false));
            LinearLayout container = view.findViewById(R.id.container);
            container.addView(binding.getRoot());
            setContentView(container);

            StatusBarUtil.setStatusBarAlpha(this,0,view.findViewById(R.id.rlTopHead));

            screenWidth= ScreenUtils.getScreenWidth(this);
            ivHeadLeft = findViewById(R.id.ivHeadLeft);
            tvHeadTitle = findViewById(R.id.tvHeadTitle);
            tvTitleRight=findViewById(R.id.tvTitleRight);
            ivHeadLeft.setOnClickListener(new OnClickFrequentlyListener() {
                @Override
                public void onClickView(View view) {
                    finish();
                }
            });
        }
        else
        {
            binding = DataBindingUtil.setContentView(this,layoutResID);
        }

        return (T) binding;
    }

    public void setActivityTitle(String title) {
        tvHeadTitle.setText(title);
    }

    public void setActivityTitle(int titleRes) {
        tvHeadTitle.setText(getResources().getText(titleRes));
    }

    public TextView getTvTitleRight() {
        return tvTitleRight;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewDataBinding.unbind();
        viewDataBinding=null;
    }

    public int getScaleWidth(float ratio) {
        return (int)(screenWidth*ratio);
    }

    public abstract void onClickView(View view);
    public abstract int onCreateLayoutId();
    public abstract void initView();

}
