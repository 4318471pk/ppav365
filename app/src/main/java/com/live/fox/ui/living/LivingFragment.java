package com.live.fox.ui.living;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.LivingMsgBoxAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentLivingBinding;
import com.live.fox.dialog.FirstTimeTopUpDialog;
import com.live.fox.dialog.PleaseDontLeaveDialog;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.HomeFragmentRoomListBean;
import com.live.fox.entity.LivingMsgBoxBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.TimeCounter;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.MyFlowLayout;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OVER_SCROLL_NEVER;

public class LivingFragment extends BaseBindingFragment {

    int currentPagePosition;
    FragmentLivingBinding mBind;
    LivingControlPanel livingControlPanel;
    LivingMsgBoxAdapter livingMsgBoxAdapter;
    List<LivingMsgBoxBean> livingMsgBoxBeans = new ArrayList<>();
    TXLivePlayer mLivePlayer = null;
    private TXLivePlayConfig mTXPlayConfig;

    TimeCounter.TimeListener timeListener = new TimeCounter.TimeListener(5) {
        @Override
        public void onSecondTick(TimeCounter.TimeListener listener) {
            super.onSecondTick(listener);
            if (isAdded() && getResources() != null) {
                LivingMsgBoxBean bean = new LivingMsgBoxBean();
                bean.setBackgroundColor(0xffBDA3C8);
                bean.setStrokeColor(0xff9E3FD4);
                SpanUtils spanUtils = new SpanUtils();
                spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(78, getContext()));
                spanUtils.append(System.currentTimeMillis() + " ");

                bean.setCharSequence(spanUtils.create());
                addNewMessage(bean);
            }
        }

        @Override
        public void onConditionTrigger(TimeCounter.TimeListener listener) {
            super.onConditionTrigger(listener);

        }
    };

    public static LivingFragment getInstance(int position) {
        Log.e("LivingFragment", position + " ");
        LivingFragment livingFragment = new LivingFragment();
        livingFragment.currentPagePosition = position;
        return livingFragment;
    }

    public void notifyShow(int position) {
        Log.e("LivingFragment22", position + " ");
        currentPagePosition = position;
        if (getView() != null && isAdded()) {
            loadData();
        }
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_living;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TimeCounter.getInstance().remove(timeListener);
    }

    private void initView() {
        LivingActivity activity = (LivingActivity) getActivity();
        //是当前页才加载数据 不然就算了
        if (activity.getCurrentPosition() == currentPagePosition) {
            loadData();
//            TimeCounter.getInstance().add(timeListener);
        } else {
            GlideUtils.loadDefaultImage(activity, activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(),
                    mBind.ivBG);
        }
    }

    private void loadData() {
        LivingActivity activity = (LivingActivity) getActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        GlideUtils.loadDefaultImage(activity, activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(),
                mBind.ivBG);

        Log.e("currentPagePosition", currentPagePosition + " " + activity.getCurrentPosition());
        if (activity.getCurrentPosition() == currentPagePosition) {
            getRecommendList();
            addViewPage();

            boolean isSuccess = TimeCounter.getInstance().add(timeListener);
            Log.e("currentPagePosition333", isSuccess + " ");
            //如果刷新了主播的信息 设置可以滑动 但是如果消息框在的话不能设置
            livingControlPanel.viewWatch.setScrollEnable(true);

            if (livingControlPanel != null) {
                livingControlPanel.viewWatch.hideInputLayout();
            }
        } else {
            destroyView();

            Log.e("currentPagePosition222", currentPagePosition + " " + activity.getCurrentPosition());
            TimeCounter.getInstance().remove(timeListener);
        }

    }

    private void addViewPage() {
        //每次都用新的 就不用重置太多东西
        destroyView();

        livingMsgBoxAdapter = null;
        livingMsgBoxBeans.clear();

        mTXPlayConfig = new TXLivePlayConfig();
        mLivePlayer=new TXLivePlayer(getActivity());
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mLivePlayer.enableHardwareDecode(false);
        setLivePlayerListener();
        setPlayMode(2,mLivePlayer);
        mLivePlayer.startPlay("rtmp://pull1.tencent live.xyz/live/781100_zb",TXLivePlayer.PLAY_TYPE_LIVE_RTMP);

        TXCloudVideoView txCloudVideoView=new TXCloudVideoView(getActivity());
        txCloudVideoView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBind.rlContent.addView(txCloudVideoView);
        mLivePlayer.setPlayerView(txCloudVideoView);

        LivingActivity activity = (LivingActivity) getActivity();
        ViewPager viewPager = new ViewPager(getActivity());
        viewPager.setId(R.id.livingViewPager);
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBind.rlContent.addView(viewPager);

        livingControlPanel = new LivingControlPanel(LivingFragment.this, viewPager);

        viewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
//                super.destroyItem(container, position, object);
                ((ViewPager) container).removeView((View) object);
            }

            public Object instantiateItem(ViewGroup container, int position) {

                if (position == 1) {
                    container.addView(livingControlPanel);
                    container.post(new Runnable() {
                        @Override
                        public void run() {
//                            initBotView();
                        }
                    });
                    return livingControlPanel;
                }
                return null;
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    activity.getDrawLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
                } else {
                    activity.getDrawLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(1);
    }

    public RoomListBean getRoomBean() {
        LivingActivity activity = (LivingActivity) getActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            return null;
        }
        return activity.getRoomListBeans().get(currentPagePosition);
    }

    private void addNewMessage(LivingMsgBoxBean bean) {
        if (livingMsgBoxAdapter == null) {
            livingMsgBoxAdapter = new LivingMsgBoxAdapter(getContext(), livingMsgBoxBeans);
            livingControlPanel.mBind.msgBox.setAdapter(livingMsgBoxAdapter);
        }
        livingMsgBoxAdapter.getBeans().add(bean);
        livingMsgBoxAdapter.notifyDataSetChanged();
    }

    private void getRecommendList() {
        Api_Live.ins().getRecommendLiveList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray list = jsonObject.getJSONArray("list");
                        if (list != null && list.length() > 0) {
                            List<RoomListBean> listBeans = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                RoomListBean bean = new Gson().fromJson(list.getJSONObject(i).toString(), RoomListBean.class);
                                listBeans.add(bean);
                            }
                            LivingActivity activity = (LivingActivity) getActivity();
                            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                                activity.setRecommendListData(listBeans);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyView();
    }

    private void destroyView()
    {
        if (getView() != null) {
            if (mLivePlayer != null) {
                mLivePlayer.stopPlay(true);
                mLivePlayer = null;
            }
            TXCloudVideoView txCloudVideoView = getView().findViewById(R.id.txLivingVideoView);
            ViewPager viewPager = getView().findViewById(R.id.livingViewPager);
            if (viewPager != null) {
                mBind.rlContent.removeView(viewPager);
            }

            if (txCloudVideoView != null) {
                txCloudVideoView.onDestroy();
                mBind.rlContent.removeView(txCloudVideoView);
            }
        }
    }


    private void setLivePlayerListener( )
    {
        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int event, Bundle bundle) {
                LogUtils.e("视频播放状态监听 " + event + ", " + bundle.getString(TXLiveConstants.EVT_DESCRIPTION));

                if (event == TXLiveConstants.PLAY_EVT_CONNECT_SUCC) {
                    // 2001 連接服務器成功
                } else if (event == TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN) {
                    // 2002 已經連接服務器，開始拉流（僅播放RTMP地址時會抛送）
//            dimissLiveLoadingAnimation();
//            content.setBackground(null);
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                    mBind.ivBG.setVisibility(View.GONE);
                    // 2003 網絡接收到首個可渲染的視頻數據包(IDR)
//                    dismissLiveLoadingAnimation();
//                    LogUtils.e(Constant.mTXLivePlayer.isPlaying() + ",");
//                    LogUtils.e((Constant.mTXLivePlayer == null) + ",");
//                    LogUtils.e((mTXCloudVideoView == null) + ",");
//                    listener.onPlayIsFinish(true);
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    // 2004 視頻播放開始，如果有轉菊花什麽的這個時候該停了
//                    if (coverIv.getVisibility() == View.VISIBLE) {
//                        //说明是第一次加载 则不做任何处理
//                    } else {
//                        // 卡顿后的流恢复
//                        dismissLiveLoadingAnimation();
//                    }

                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                    // 2007 視頻播放loading，如果能夠恢複，之後會有BEGIN事件
//                    showLiveLoadingAnimation();
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                    //2009 分辨率改变

                } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
                }

                /**
                 *  結束事件
                 */
                if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    // 2006 視頻播放結束

                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                    //  -2301 网络多次重连失败失败后 会返回此值
//                    clearStop();
//                    if (isAdded()) {
//                        networkDisconnect();
//                    }
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        });
    }


    //设置播放器模式
    private void setPlayMode(int strategy,TXLivePlayer mLivePlayer) {
        if (mTXPlayConfig == null) {
            return;
        }
        switch (strategy) {
            case 0: // 極速
                mTXPlayConfig.setAutoAdjustCacheTime(true);
                mTXPlayConfig.setMinAutoAdjustCacheTime(1);
                mTXPlayConfig.setMaxAutoAdjustCacheTime(1);
                mLivePlayer.setConfig(mTXPlayConfig);
                break;
            case 1: // 流暢
                mTXPlayConfig.setAutoAdjustCacheTime(false);
                mTXPlayConfig.setMinAutoAdjustCacheTime(5);
                mTXPlayConfig.setMaxAutoAdjustCacheTime(5);
                mLivePlayer.setConfig(mTXPlayConfig);
                break;

            case 2: // 自動
                mTXPlayConfig.setAutoAdjustCacheTime(true);
                mTXPlayConfig.setMinAutoAdjustCacheTime(1);
                mTXPlayConfig.setMaxAutoAdjustCacheTime(5);
                mLivePlayer.setConfig(mTXPlayConfig);
                break;
        }
    }
}
