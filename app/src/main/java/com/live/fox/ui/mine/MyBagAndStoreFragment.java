package com.live.fox.ui.mine;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.live.fox.App;
import com.live.fox.R;
import com.live.fox.adapter.MyBagAndStoreAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentMybagStoreBinding;
import com.live.fox.entity.BagAndStoreBean;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.MyBagStoreListItemBean;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyBagAndStoreFragment extends BaseBindingFragment {

    public static final int EventCodeStore = 10001;

    FragmentMybagStoreBinding mBind;

    boolean isStore= true;

    MyBagAndStoreAdapter myBagAndStoreAdapter;
    List<MyBagStoreListItemBean> beans;

    SVGAParser parser;


    int pageSize = 20;
    int pageNum = 1;

    public static MyBagAndStoreFragment newInstance(boolean isStore) {
        MyBagAndStoreFragment fragment = new MyBagAndStoreFragment();
        Bundle args = new Bundle();
        args.putBoolean("isStore", isStore);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_mybag_store;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        EventBus.getDefault().register(this);

        isStore = this.getArguments().getBoolean("isStore",isStore);
        parser = new SVGAParser(this.getContext());
        mBind.ivSvg.setLoops(0);

        GridLayoutManager grid = new GridLayoutManager(this.getContext(), 2);
        RecyclerSpace recyclerSpace = new RecyclerSpace(ScreenUtils.getDip2px(this.getContext(), 5));
        mBind.rvMain.addItemDecoration(recyclerSpace);
        mBind.rvMain.setLayoutManager(grid);
        mBind.srlMyBag.setRefreshHeader(new MyWaterDropHeader(this.getContext()));

        beans=new ArrayList<>();

        mBind.srlMyBag.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum ++ ;
                getData(false);
            }
        });

        mBind.srlMyBag.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                beans.clear();
                pageNum = 1;
                getData(true);
            }
        });

        setMyBagAdapter();
        myBagAndStoreAdapter.setMyBagStoreClick(new MyBagAndStoreAdapter.MyBagStoreClick() {
            @Override
            public void startAni(int pos, String url)  {
                setSvg(url);
            }

            @Override
            public void openCar(int pos, int id, boolean isOpen) {
                HashMap<String, Object> commonParams = BaseApi.getCommonParams();
                commonParams.put("propId", id);
                showLoadingDialog();
                if (isOpen) {
                    Api_Order.ins().openCar(getCarJsonBack(isOpen), commonParams);
                } else {
                    Api_Order.ins().closeCar(getCarJsonBack(isOpen), commonParams);
                }
            }

        });

        getData(true);
       // setSvgStatus();
    }

    private JsonCallback getCarJsonBack(boolean isOpenCar){
        return new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    ToastUtils.showShort(App.getInstance().getString(R.string.open_car_suc));
                    pageNum = 1;
                    getData(true);
                    return;
                } else {
                    ToastUtils.showShort(msg);
                }
                dismissLoadingDialog();
            }
        };
    }


    private void setMyBagAdapter()
    {
        if(myBagAndStoreAdapter ==null)
        {
            myBagAndStoreAdapter =new MyBagAndStoreAdapter((BaseActivity) this.getContext(),beans, isStore);
            mBind.rvMain.setAdapter(myBagAndStoreAdapter);
        }
        else
        {
            myBagAndStoreAdapter.notifyDataSetChanged();
        }
    }

    private void getData(boolean isFrash){
        showLoadingDialog();
        if (isStore) {
            Api_Order.ins().getStoreList(new JsonCallback<BagAndStoreBean>() {
                @Override
                public void onSuccess(int code, String msg, BagAndStoreBean data) {
                    dismissLoadingDialog();
                    mBind.srlMyBag.finishRefresh();
                    mBind.srlMyBag.finishLoadMore();
                    if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                        if (data.getRecords() != null && data.getRecords().size() >0) {
                            if (isFrash) {
                                beans.clear();
                            }
                            beans.addAll(data.getRecords());
                            if (data.getRecords().size() < pageSize ) {
                                mBind.srlMyBag.setEnableLoadMore(true);
                            }
                            setMyBagAdapter();
                        } else {
                            mBind.srlMyBag.setEnableLoadMore(true);
                        }
                    } else {
                        if (!isFrash) {
                            pageNum --;
                        }
                        ToastUtils.showShort(msg);
                    }
                }
            }, pageNum, pageSize);
        } else {
            Api_Order.ins().getBagList(new JsonCallback<BagAndStoreBean>() {
                @Override
                public void onSuccess(int code, String msg, BagAndStoreBean data) {
                    dismissLoadingDialog();
                    mBind.srlMyBag.finishRefresh();
                    mBind.srlMyBag.finishLoadMore();
                    if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                        if (data.getRecords() != null && data.getRecords().size() >0) {
                            beans.addAll(data.getRecords());

                            if (data.getRecords().size() < pageSize ) {
                                mBind.srlMyBag.setEnableLoadMore(true);
                            }
                            setMyBagAdapter();
                        } else {
                            mBind.srlMyBag.setEnableLoadMore(true);
                        }
                    } else {
                        if (!isFrash) {
                            pageNum --;
                        }
                        ToastUtils.showShort(msg);
                    }
                }
            }, pageNum, pageSize);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent msg) {
        if (msg.getType() == EventCodeStore) { //刷新列表
            pageNum = 0;
            getData(true);
        }
    }


    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void setSvg(String url)   {
        mBind.ivSvg.clearAnimation();
        try {
            parser.decodeFromURL(new URL(url), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    mBind.ivSvg.setImageDrawable(drawable);
                    mBind.ivSvg.setLoops(1);
                    mBind.ivSvg.startAnimation();
                    setSvgStatus();
                }

                @Override
                public void onError() {

                }
            }, new SVGAParser.PlayCallback() {
                @Override
                public void onPlay(@NotNull List<? extends File> list) {
                    //mBind.ivSvg.stopAnimation();
                }
            });
        }catch (MalformedURLException e) {
            LogUtils.e(e.getMessage());
        }

    }
    
    private void setSvgStatus(){
        mBind.ivSvg.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                mBind.ivSvg.stopAnimation();
                mBind.ivSvg.clearAnimation();
                mBind.ivSvg.setBackground(null);
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }
}
