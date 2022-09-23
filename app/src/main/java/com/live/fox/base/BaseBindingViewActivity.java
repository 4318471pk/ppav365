package com.live.fox.base;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.live.fox.Constant;
import com.live.fox.R;

public class BaseBindingViewActivity extends BaseActivity{

    ImageView ivLeft;
    TextView tvLeft;
    TextView tvTitle;
    ImageView ivTitle;
    ImageView ivRight;
    public TextView tvRight;
    public Toolbar toolbar;
    ViewDataBinding mBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.head_line_include);
        LinearLayout container = findViewById(R.id.container);
        View view = getLayoutInflater().inflate(layoutResID, container, false);
        container.addView(view);
        initHeadView();
        mBinding = DataBindingUtil.bind(container);
    }

    @Override
    public void setContentView(View mView) {
        LinearLayout container = findViewById(R.id.container);
        container.addView(mView);
        initHeadView();
        super.setContentView(container);
    }

    private void initHeadView() {
        toolbar = findViewById(R.id.toolbar);
        ivLeft = findViewById(R.id.iv_head_left);
        tvLeft = findViewById(R.id.tv_head_left);
        tvTitle = findViewById(R.id.tv_head_title);
        ivTitle = findViewById(R.id.iv_head_title);
        ivRight = findViewById(R.id.iv_head_right);
        tvRight = findViewById(R.id.tv_head_right);

        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.head_back_sel);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon_arrow_left);

        //设置ToolBar的标题不显示
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(view -> {
            Constant.isAppInsideClick = true;
            finish();
        });
    }

}
