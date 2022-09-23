package com.live.fox.base;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.Toolbar;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.utils.BarUtils;



/**
 * @author cheng
 * 功能：1.此类封装了一些修改头部的方法
 * 2.继承此类就不需要再xml中加头部了
 */
public abstract class BaseHeadActivity extends BaseActivity implements View.OnClickListener {

    ImageView ivLeft;
    TextView tvLeft;
    TextView tvTitle;
    ImageView ivTitle;
    ImageView ivRight;
    public TextView tvRight;
    public Toolbar toolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.head_line_include);
        LinearLayout container = findViewById(R.id.container);
        View view = getLayoutInflater().inflate(layoutResID, container, false);
        container.addView(view);
        initHeadView();
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
        onBackClick();
    }

    public void onBackClick() {
        //返回按钮的监听
        toolbar.setNavigationOnClickListener(view -> {
            Constant.isAppInsideClick = true;
            finish();
        });
    }

    //设置头部padding出StatusBarH的高度 沉浸式布局时需要用得到 比如状态栏
    public void setTopPaddingStatusBarHeight(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height += BarUtils.getStatusBarHeight();
        view.setLayoutParams(layoutParams);
        view.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
    }


    public void toggleHeadView(boolean isVisibility) {
        toolbar.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    public void setHead(String title, boolean isShowBack, boolean isPaddingTopStatusBarHeight) {
        setTitle(title);
        setBackBtnShown(isShowBack);
        if (isPaddingTopStatusBarHeight) setTopPaddingStatusBarHeight(toolbar);
    }


    public void setHead(String title, int leftImgResourceId, int rightImgResourceId, boolean isPaddingTopStatusBarHeight) {
        setTitle(title);
        setLeftImgId(leftImgResourceId);
        setRightImgId(rightImgResourceId);
        if (isPaddingTopStatusBarHeight) setTopPaddingStatusBarHeight(toolbar);
    }

    public void setHead(int titleResourceId, int leftImgResourceId, int rightImgResourceId, boolean isPaddingTopStatusBarHeight) {
        setImgTitleResourceId(titleResourceId);
        setLeftImgId(leftImgResourceId);
        setRightImgId(rightImgResourceId);
        if (isPaddingTopStatusBarHeight) setTopPaddingStatusBarHeight(toolbar);
    }

    public void setHead(String title, int leftImgResourceId, String rightText, boolean isPaddingTopStatusBarHeight) {
        setTitle(title);
        setLeftImgId(leftImgResourceId);
        setRightText(rightText);
        if (isPaddingTopStatusBarHeight) setTopPaddingStatusBarHeight(toolbar);
    }

    public void setHead(String title, boolean isShowBack, int rightImgResourceId, boolean isPaddingTopStatusBarHeight) {
        setTitle(title);
        setBackBtnShown(isShowBack);
        setRightImgId(rightImgResourceId);
        if (isPaddingTopStatusBarHeight) setTopPaddingStatusBarHeight(toolbar);
    }

    public void setHead(String title, boolean isShowBack, String rightText, boolean isPaddingTopStatusBarHeight) {
        setTitle(title);
        setBackBtnShown(isShowBack);
        setRightText(rightText);
        if (isPaddingTopStatusBarHeight) setTopPaddingStatusBarHeight(toolbar);
    }

    public void setLeftImgId(int resourceId) {
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(resourceId);
    }

    public void setLeftText(String text) {
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText(text);
    }

    public void setRightImgId(int resourceId) {
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(resourceId);
    }

    public void setRightText(String rightText) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(rightText);
    }


    public void setBackBtnShown(boolean isShow) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isShow);
    }


    public void setBackBtnImg(int resourceId) {
        getSupportActionBar().setHomeAsUpIndicator(resourceId);
    }

    public void setBackgroundColor(int color) {
        toolbar.setBackgroundResource(color);
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(getResources().getColor(color));
    }

    public void setWhiteBackBtn() {
        getSupportActionBar().setHomeAsUpIndicator(0);
    }


    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
        tvTitle.setVisibility(View.GONE);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(title);
    }

    public void setTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(title);
    }

    public void setImgTitleResourceId(int resourceId) {
        tvTitle.setVisibility(View.GONE);
        ivTitle.setVisibility(View.VISIBLE);
        ivTitle.setImageResource(resourceId);
    }

    public void setHeadElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= 21) {
            toolbar.setElevation(elevation);
        }
    }

    public ImageView getIvLeft() {
        return ivLeft;
    }

    public TextView getTvLeft() {
        return tvLeft;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getIvTitle() {
        return ivTitle;
    }

    public ImageView getIvRight() {
        return ivRight;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onClick(View v) {

    }

}
