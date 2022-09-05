package com.live.fox.ui.circle;


import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.utils.BarUtils;


public abstract class BaseHeadFragment extends BaseFragment {

    ImageView ivLeft;
    TextView tvLeft;
    TextView tvTitle;
    ImageView ivTitle;
    ImageView ivRight;
    TextView tvRight;
    public Toolbar toolbar;


    private void initHeadView(View view, boolean isPaddingTopStatusBarHeight) {
        toolbar = view.findViewById(R.id.toolbar);
        ivLeft = view.findViewById(R.id.iv_head_left);
        tvLeft = view.findViewById(R.id.tv_head_left);
        tvTitle = view.findViewById(R.id.tv_head_title);
        ivTitle = view.findViewById(R.id.iv_head_title);
        ivRight = view.findViewById(R.id.iv_head_right);
        tvRight = view.findViewById(R.id.tv_head_right);

        if(isPaddingTopStatusBarHeight) setTopPaddingStatusBarHeight(toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.head_back_sel);

        //设置ToolBar的标题不显示
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
//        //返回按钮的监听
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                getActivity().finish();
//            }
//        });
    }



    //设置头部padding出StatusBarH的高度 沉浸式布局时需要用得到 比如状态栏
    public void setTopPaddingStatusBarHeight(View view){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height += BarUtils.getStatusBarHeight();
        view.setLayoutParams(layoutParams);
        view.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
    }

    public void initToolHead(View view, String title, boolean isShowBack, boolean isPaddingTopStatusBarHeight) {
        initHeadView(view, isPaddingTopStatusBarHeight);

        setTitle(title, true);
        setBackBtnShown(isShowBack);
    }


    public void initHead(View view, String title, boolean isShowBack) {
        initHead(view, title, isShowBack, false);
    }

    public void initHead(View view, String title, boolean isShowBack, boolean isPaddingTopStatusBarHeight) {
        initHeadView(view, isPaddingTopStatusBarHeight);

        setTitle(title, false);
        setBackBtnShown(isShowBack);
    }


    public void initHead(View view, String title, int leftImgResourceId, int rightImgResourceId, boolean isPaddingTopStatusBarHeight) {
        initHeadView(view, isPaddingTopStatusBarHeight);

        setTitle(title, false);
        setLeftImgId(leftImgResourceId);
        setRightImgId(rightImgResourceId);
    }

    public void initHead(View view, int titleResourceId, int leftImgResourceId, int rightImgResourceId, boolean isPaddingTopStatusBarHeight) {
        initHeadView(view, isPaddingTopStatusBarHeight);

        setImgTitleResourceId(titleResourceId);
        setLeftImgId(leftImgResourceId);
        setRightImgId(rightImgResourceId);
    }


    public void initHead(View view, String title, int leftImgResourceId, String rightText, boolean isPaddingTopStatusBarHeight) {
        initHeadView(view, isPaddingTopStatusBarHeight);

        setTitle(title, false);
        setLeftImgId(leftImgResourceId);
        setRightText(rightText);
    }

    public void initHead(View view, String title, boolean isShowBack, int rightImgResourceId) {
        initHeadView(view, false);

        setTitle(title, false);
        setBackBtnShown(isShowBack);
        setRightImgId(rightImgResourceId);
    }

    public void initHead(View view, String title, boolean isShowBack, String rightText, boolean isPaddingTopStatusBarHeight) {
        initHeadView(view, isPaddingTopStatusBarHeight);

        setTitle(title, false);
        setBackBtnShown(isShowBack);
        setRightText(rightText);
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
        if (isShow) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }


    public void setBackBtnImg(int resourceId) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(resourceId);
    }

    public void setBackgroundColor(int color) {
        toolbar.setBackgroundResource(color);
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(getResources().getColor(color));
    }

    public void setWhiteBackBtn(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(0);
    }

    public void setTitle(String title, boolean isToolBarTitle) {
        if(isToolBarTitle){
            //ToolBar样式的Title
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).setTitle(title);
            toolbar.setTitleTextColor(getActivity().getResources().getColor(R.color.black));
            tvTitle.setVisibility(View.GONE);
            ivTitle.setVisibility(View.GONE);
        }else {
            //设置ToolBar的标题显示
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            tvTitle.setVisibility(View.VISIBLE);
            ivTitle.setVisibility(View.GONE);
            tvTitle.setText(title);
        }

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
}
