package com.live.fox.ui.mine.myBagAndStore;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.live.fox.db.LocalGiftDao;
import com.live.fox.db.LocalMountResourceDao;
import com.live.fox.entity.BagAndStoreBean;
import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.entity.MyBagStoreListItemBean;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.Strings;
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

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyBagAndStoreFragment extends BaseBindingFragment {


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
        isStore = this.getArguments().getBoolean("isStore",isStore);
        parser = SVGAParser.Companion.shareParser();
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
                if (isOpen) {
                    Api_Order.ins().openCar(getCarJsonBack(isOpen), commonParams);
                } else {
                    Api_Order.ins().closeCar(getCarJsonBack(isOpen), commonParams);
                }
            }

            @Override
            public void buySuccess() {
                getData(true);
            }

        });

        getData(true);
       // setSvgStatus();
    }

    private JsonCallback getCarJsonBack(boolean isOpenCar){
        showLoadingDialogWithNoBgBlack();
        return new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(!isActivityOK())
                {
                    return;
                }
                hideLoadingDialog();
                if (code == 0) {
                    ToastUtils.showShort(App.getInstance().getString(R.string.open_car_suc));
                    if(getActivity()!=null && getActivity() instanceof MyBagAndStoreActivity)
                    {
                        MyBagAndStoreActivity myBagAndStoreActivity=(MyBagAndStoreActivity)getActivity();
                        myBagAndStoreActivity.reFreshBoth();
                    }
                    return;
                } else {
                    ToastUtils.showShort(msg);
                }
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

    public void getData(boolean isRefresh){
        if(isRefresh)
        {
            pageNum=1;
        }

        if (isStore) {
            Api_Order.ins().getStoreList(new JsonCallback<BagAndStoreBean>() {
                @Override
                public void onSuccess(int code, String msg, BagAndStoreBean data) {
                    if(!isActivityOK())
                    {
                        return;
                    }
                    mBind.srlMyBag.finishRefresh(code == 0);
                    mBind.srlMyBag.finishLoadMore(code == 0);
                    if (code == 0) {
                        if (isRefresh) {
                            beans.clear();
                        }
                        if (data.getRecords() != null && data.getRecords().size() >0) {
                            replaceLocalResource(data);
                            beans.addAll(data.getRecords());
                            if (data.getRecords().size() < pageSize ) {
                                mBind.srlMyBag.setEnableLoadMore(true);
                            }
                            setMyBagAdapter();
                        } else {
                            mBind.srlMyBag.setEnableLoadMore(true);
                        }
                    } else {
                        if (!isRefresh) {
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
                    if(!isActivityOK())
                    {
                        return;
                    }
                    mBind.srlMyBag.finishRefresh(code == 0);
                    mBind.srlMyBag.finishLoadMore(code == 0);
                    if (code == 0 ) {
                        if (isRefresh) {
                            beans.clear();
                        }
                        if (data.getRecords() != null && data.getRecords().size() >0) {
                            replaceLocalResource(data);
                            beans.addAll(data.getRecords());

                            if (data.getRecords().size() < pageSize ) {
                                mBind.srlMyBag.setEnableLoadMore(true);
                            }
                            setMyBagAdapter();
                        } else {
                            mBind.srlMyBag.setEnableLoadMore(true);
                        }
                    } else {
                        if (!isRefresh) {
                            pageNum --;
                        }
                        ToastUtils.showShort(msg);
                    }
                }
            }, pageNum, pageSize);
        }
    }

    private void replaceLocalResource(BagAndStoreBean data)
    {
        for (int i = 0; i < data.getRecords().size(); i++) {
            int type=isStore?data.getRecords().get(i).getType():data.getRecords().get(i).getPropType();
            switch (type)
            {
                case 1:
                    GiftResourceBean giftResourceBean= LocalGiftDao.getInstance().getGift(data.getRecords().get(i).getPropId());
                    if(giftResourceBean!=null)
                    {
                        data.getRecords().get(i).setName(giftResourceBean.getName());
                        data.getRecords().get(i).setPropName(giftResourceBean.getName());
                        data.getRecords().get(i).setEname(giftResourceBean.getEname());
                        data.getRecords().get(i).setDescript(giftResourceBean.getRemark());
                        data.getRecords().get(i).setLogUrl(giftResourceBean.getGitficon());
                        data.getRecords().get(i).setAnimationUrl(giftResourceBean.getLocalSvgPath());
                    }
                    else
                    {
                        MyBagStoreListItemBean bean=data.getRecords().get(i);
                        data.getRecords().get(i).setLogUrl(Strings.urlConnect(bean.getLogUrl()));
                        data.getRecords().get(i).setAnimationUrl(Strings.urlConnect(bean.getAnimationUrl()));
                    }
                    break;
                case 2:
                    MountResourceBean mountResourceBean= LocalMountResourceDao.getInstance().getVehicleById(data.getRecords().get(i).getPropId());
                    if(mountResourceBean!=null)
                    {
                        data.getRecords().get(i).setName(mountResourceBean.getName());
                        data.getRecords().get(i).setPropName(mountResourceBean.getName());
                        data.getRecords().get(i).setDescript(mountResourceBean.getDescript());
                        data.getRecords().get(i).setEname(mountResourceBean.getEname());
                        data.getRecords().get(i).setLogUrl(mountResourceBean.getLogUrl());
                        data.getRecords().get(i).setAnimationUrl(mountResourceBean.getLocalSvgPath());
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setSvg(String url)   {
        mBind.ivSvg.clearAnimation();
        if(TextUtils.isEmpty(url))
        {
            return;
        }
        if(mBind.ivSvg.isAnimating() && !mBind.ivSvg.isEnabled())
        {
            return;
        }

        try {
            if(url.toLowerCase().startsWith("http"))
            {
                mBind.ivSvg.setEnabled(false);
                parser.decodeFromURL(new URL(url), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                        mBind.ivSvg.setEnabled(true);
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
            }
            else
            {
                File file=new File(url);
                if(file==null || !file.exists())
                {
                    return;
                }
                FileInputStream fileInputStream = new FileInputStream(file);
                parser.decodeFromInputStream(fileInputStream, file.getAbsolutePath(),new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                        SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                        mBind.ivSvg.setImageDrawable(drawable);
                        mBind.ivSvg.setLoops(1);
                        mBind.ivSvg.startAnimation();
                        setSvgStatus();
                    }

                    @Override
                    public void onError() {

                    }
                },true, null, null);
            }

        }catch (MalformedURLException | FileNotFoundException e) {
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
                mBind.ivSvg.clear();
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
