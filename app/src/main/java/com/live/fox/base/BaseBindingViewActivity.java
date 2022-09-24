package com.live.fox.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.device.ScreenUtils;

public abstract class BaseBindingViewActivity extends BaseActivity {

    ImageView ivHeadLeft;
    TextView tvHeadTitle;
    int screenWidth;
    ViewDataBinding viewDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding=setBindLayoutID(onCreateLayoutId());
        initView();
    }

    public <T extends ViewDataBinding> T getViewDataBinding() {
        return (T)viewDataBinding;
    }

    private  <T extends ViewDataBinding> T setBindLayoutID(int layoutResID) {
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_header_layout,null);
        ViewDataBinding binding = DataBindingUtil.bind(getLayoutInflater().inflate(layoutResID, view,false));
        LinearLayout container = view.findViewById(R.id.container);
        container.addView(binding.getRoot());
        setContentView(container);

        screenWidth= ScreenUtils.getScreenWidth(this);
        ivHeadLeft = findViewById(R.id.ivHeadLeft);
        tvHeadTitle = findViewById(R.id.tvHeadTitle);
        ivHeadLeft.setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                finish();
            }
        });

        return (T) binding;
    }

    public void setActivityTitle(String title) {
        tvHeadTitle.setText(title);
    }

    public void setActivityTitle(int titleRes) {
        tvHeadTitle.setText(getResources().getText(titleRes));
    }

    public int getScaleWidth(float ratio) {
        return (int)(screenWidth*ratio);
    }

    public abstract void onClickView(View view);
    public abstract int onCreateLayoutId();
    public abstract void initView();
}
