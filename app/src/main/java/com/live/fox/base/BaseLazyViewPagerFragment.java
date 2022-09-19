package com.live.fox.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.helper.IBaseLazyViewPager;
import com.live.fox.helper.mvp.AbsBaseMvpPresenter;


/**
 * Date:    2019/1/21.
 * Description: ViewPager与Fragment结合使用时 保证每次只加载一个且每个Fragment只加载一次
 */

public abstract class BaseLazyViewPagerFragment extends BaseFragment implements IBaseLazyViewPager {
    protected final String TAG = getClass().getSimpleName();
    protected boolean hasInit = false;
    protected Handler mHandler = new Handler();

    protected Activity activity;

    //是否可见
    public boolean isVisible = false;
    //是否初始化完成
    public boolean isInit = false;
    //是否已经加载过
    public boolean isLoadOver = false;

    /**
     * get intent value in this method if need
     */
    protected void getIntentValue() {
    }

    //界面可见时再加载数据(该方法在onCreate()方法之前执行。)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisible = isVisibleToUser;
    }

    @Override
    public AbsBaseMvpPresenter getPresenter() {
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = requireActivity();
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            if (getContentLayout() > 0) {
                rootView = createView(inflater, container, savedInstanceState);
            }
            else
            {
                rootView=container;
            }
        }
        return rootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentLayout(), container, false);
        isInit = true;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(savedInstanceState);
    }

    public <T> T $(int id) {
        return (T) getView().findViewById(id);
    }

    @Nullable
    @Override
    public View getView() {
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!hasInit) {
            getIntentValue();
        }
    }

    /**
     * 初始化一些参数，完成懒加载和数据只加载一次的效果
     * isInit = true：此Fragment初始化完成
     * isLoadOver = false：此Fragment没有加载过
     * isVisible = true：此Fragment可见
     */
    private void setParam() {
//        if (isInit && !isLoadOver && isVisible && isAdded()) {
//            isLoadOver = true;
//            bindView(null);
//        }
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
    public void onDestroy() {
        if (null != rootView) {
            if (rootView.getParent() != null) {
                ((ViewGroup) rootView.getParent()).removeView(rootView);
            }
        }
        super.onDestroy();
    }

    /**
     * whether the fragment is visible
     *
     * @param context
     * @return
     */
    public boolean isVisible(Context context) {
        return isAdded() && !isHidden() && getView() != null
                && getView().getVisibility() == View.VISIBLE;
    }

//    Interlude mAnimLoadingDialog;
//
//    public void showAnimLoadingDialog(FragmentManager fragmentManager) {
//        if (mAnimLoadingDialog == null) {
//            mAnimLoadingDialog = new Interlude();
//        }
//        mAnimLoadingDialog.setDim(0);
//        mAnimLoadingDialog.setIndicatorColorResource(R.color.coloTheme);
//        mAnimLoadingDialog.setBackGroundColorResource(R.drawable.shape_round_white80);
//        mAnimLoadingDialog.setIndicatorType(IndicatorType.LineScaleIndicator);
//        mAnimLoadingDialog.setCancelable(true);
//        mAnimLoadingDialog.setCanceledOnTouchOutside(true);
//
//        mAnimLoadingDialog.show(fragmentManager);
//    }
//
//
//    public void dimissAnimLoadingDialog() {
//        if (mAnimLoadingDialog != null && mAnimLoadingDialog.isShowing()) {
//            mAnimLoadingDialog.dismiss();
//        }
//    }


}
