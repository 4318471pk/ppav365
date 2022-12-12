package com.live.fox.ui.living;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.live.fox.AppIMManager;
import com.live.fox.ConstantValue;
import com.live.fox.MessageProtocol;
import com.live.fox.R;
import com.live.fox.adapter.LivingMsgBoxAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentLivingBinding;
import com.live.fox.db.LocalGiftDao;
import com.live.fox.db.LocalMountResourceDao;
import com.live.fox.db.LocalUserVehiclePlayLimitDao;
import com.live.fox.db.LocalWatchHistoryDao;
import com.live.fox.dialog.bottomDialog.LivingProfileBottomDialog;
import com.live.fox.dialog.temple.TempleDialog;
import com.live.fox.dialog.temple.TempleDialog2;
import com.live.fox.entity.EnterRoomBean;
import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.LivingCurrentAnchorBean;
import com.live.fox.entity.LivingFollowMessage;
import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.entity.LivingMessageGiftBean;
import com.live.fox.entity.LivingMsgBoxBean;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.entity.NewBornNobleOrGuardMessageBean;
import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.entity.RoomWatchedHistoryBean;
import com.live.fox.entity.SvgAnimateLivingBean;
import com.live.fox.entity.User;
import com.live.fox.entity.UserVehiclePlayLimitBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.BulletViewUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ImageUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.PlayerUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.LivingClickTextSpan;
import com.live.fox.view.MyViewPager;
import com.live.fox.view.RankProfileView;
import com.live.fox.view.bulletMessage.BulletMessageView;
import com.live.fox.view.bulletMessage.EnterRoomMessageView;
import com.live.fox.view.bulletMessage.FollowMeFloatingView;
import com.lzy.okgo.cache.policy.NoneCacheRequestPolicy;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.live2.V2TXLivePlayer;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.OVER_SCROLL_NEVER;

public class LivingFragment extends BaseBindingFragment {

    final int playSVGA = 123;
    final int userHeartBeat = 987;
    final int alertWhenExit = 87272;//关注主播不迷路弹窗
    final int alertFloatingFollow = 87273;//关注主播不迷路飘窗 左下角
    final int enterRoomRefresh=124;
    final int previewCountDown=91;//收费房间未付费前可预览的倒计时
    final int roomWatchedTime=92;//收费房间已观看的时间

    int currentPagePosition;
    int viewPagePosition;
    FragmentLivingBinding mBind;
    LivingControlPanel livingControlPanel;
    LivingMsgBoxAdapter livingMsgBoxAdapter;
    List<LivingMsgBoxBean> livingMsgBoxBeans = new ArrayList<>();
    List<SvgAnimateLivingBean> livingMessageGiftBeans = new LinkedList<>();//播放SVGA的数组
    TXLivePlayer mLivePlayer = null;//流播放器
    TXVodPlayer mVodPlayer=null;//点播器 用于播放视频，不是直播流
    private TXLivePlayConfig mTXPlayConfig;
    public LivingCurrentAnchorBean livingCurrentAnchorBean;//当前主播的数据
    private RoomListBean currentRoomListBean;
    private EnterRoomBean enterRoomBean;//进入房间的数据 也许倒计时要用到
    View contentViews[]=new View[2];

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            if(!isActivityOK())
            {
                return;
            }

            switch (msg.what) {
                case playSVGA:
                    playSVGAAnimal();
                    break;
                case userHeartBeat:
                    Api_Live.ins().watchHeart();
                    sendEmptyMessageDelayed(userHeartBeat, 40000);
                    break;
                case alertWhenExit://主播不迷路需求 没关注的话5分钟后点击关闭直播间要弹窗
                    if (livingControlPanel != null && livingCurrentAnchorBean != null && !livingCurrentAnchorBean.getFollow()) {
                        livingControlPanel.shouldAlertOnExit = true;
                    }
                    break;
                case enterRoomRefresh:
                    removeMessages(enterRoomRefresh);
                    if(isActivityOK() && livingControlPanel!=null)
                    {
                        livingControlPanel.refresh20AudienceList();//刷新头部20个人
                    }
                    break;
                case alertFloatingFollow://关注主播不迷路飘窗 左下角
                    if(livingControlPanel!=null && isActivityOK() && livingCurrentAnchorBean!=null)
                    {
                        FollowMeFloatingView followMeFloatingView=new FollowMeFloatingView(getActivity());
                        followMeFloatingView.postWidow(getActivity(), livingCurrentAnchorBean,
                                livingControlPanel.mBind.rlMain, new FollowMeFloatingView.OnClickFollowListener() {
                                    @Override
                                    public void onClickFollow() {
                                        if(!TextUtils.isEmpty(livingCurrentAnchorBean.getAnchorId()))
                                        {
                                            livingControlPanel.follow(livingCurrentAnchorBean.getAnchorId());
                                        }
                                    }
                                });

                    }
                    break;
                case previewCountDown://收费房间未付费前可预览的倒计时
                    if(livingControlPanel!=null && enterRoomBean!=null )
                    {
                        if(enterRoomBean.getPreviewTime()>0)
                        {
                            int dip10=ScreenUtils.getDip2px(getActivity(),10);
                            livingControlPanel.mBind.gtvCountDown.setSolidBackground(0xffF2AD39,dip10);
                            String textCountDown=String.format(getStringWithoutContext(R.string.previewCountDown),
                                    String.valueOf(enterRoomBean.getPreviewTime()-1));
                            SpannableString spannableString=new SpannableString(textCountDown);
                            spannableString.setSpan(new ForegroundColorSpan(0xffF42C2C),5,spannableString.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            livingControlPanel.mBind.gtvCountDown.setText(spannableString);
                            livingControlPanel.mBind.gtvCountDown.setVisibility(View.VISIBLE);
                            enterRoomBean.setPreviewTime(enterRoomBean.getPreviewTime()-1);
                            sendEmptyMessageDelayed(previewCountDown,1000);
                        }
                        else
                        {
                            //显示收费弹窗
                            if(livingCurrentAnchorBean!=null)
                            {
                                showChangeRoomTypeDialog(livingCurrentAnchorBean.getType(),livingCurrentAnchorBean.getPrice());
                            }
                        }
                    }
                    break;
                case roomWatchedTime:
                    int second=msg.arg1;
                    String unitMin=getStringWithoutContext(R.string.min);
                    String unitSec=getStringWithoutContext(R.string.sec);
                    StringBuilder sbWatchedTime=new StringBuilder();
                    sbWatchedTime.append(getStringWithoutContext(R.string.hadWatched));
                    if(second<60)
                    {
                        sbWatchedTime.append(second).append(unitSec);
                    }
                    else
                    {
                        sbWatchedTime.append(second/60).append(unitMin);
                    }

                    int strokeColor=0xffFF008A;
                    int solidColor=0x3f000000;
                    int dip10=ScreenUtils.getDip2px(getActivity(),10);
                    int dip1_5=ScreenUtils.getDip2px(getActivity(),1.5f);
                    livingControlPanel.mBind.gtvCountDown.setStokeWithSolidBackground(strokeColor,dip1_5,solidColor,dip10);
                    livingControlPanel.mBind.gtvCountDown.setText(sbWatchedTime.toString());
                    livingControlPanel.mBind.gtvCountDown.setVisibility(View.VISIBLE);
                    Message message=new Message();
                    message.arg1=second+1;
                    sendMessageDelayed(message,1000);
                    break;
            }
        }
    };

    public static LivingFragment getInstance(int position, int viewPagePosition) {
        LivingFragment livingFragment = new LivingFragment();
        livingFragment.currentPagePosition = position;
        livingFragment.viewPagePosition = viewPagePosition;
        livingFragment.currentRoomListBean=livingFragment.getRoomBean();
        return livingFragment;
    }

    public void notifyShow(int position, int viewPagePosition) {
        currentPagePosition = position;
        currentRoomListBean=getRoomBean();
        this.viewPagePosition = viewPagePosition;
        if (getView() != null && isAdded()) {
            loadData();
        }
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public void onKeyBack() {
        super.onKeyBack();

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
    public void onPause() {
        super.onPause();
        if (mLivePlayer != null) {
            mLivePlayer.pause();
        }
        if(mVodPlayer!=null)
        {
            mVodPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLivePlayer != null) {
            mLivePlayer.resume();
        }
        if(mVodPlayer!=null)
        {
            mVodPlayer.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay(true);
        }
        if(mVodPlayer!=null)
        {
            mVodPlayer.stopPlay(true);
        }
    }

    private void initView() {
        LivingActivity activity = (LivingActivity) getActivity();
        //是当前页才加载数据 不然就算了
        if (activity.getCurrentPosition() == currentPagePosition && activity.getPagerPosition() == viewPagePosition) {
            loadData();
//            TimeCounter.getInstance().add(timeListener);
        } else {
            GlideUtils.loadDefaultImage(activity, activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(), R.mipmap.icon_anchor_loading,
                    mBind.ivBG);
        }
    }

    private void loadData() {
        LivingActivity activity = (LivingActivity) getActivity();
        if (!isActivityOK()) {
            return;
        }

        GlideUtils.loadDefaultImage(activity, activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(), 0,R.mipmap.icon_anchor_loading,
                mBind.ivBG);

        Log.e("currentPagePosition", currentPagePosition + " " + activity.getCurrentPosition());
        if (activity.getCurrentPosition() == currentPagePosition && activity.getPagerPosition() == viewPagePosition) {
            getRecommendList();
            addViewPage();

            //如果刷新了主播的信息 设置可以滑动 但是如果消息框在的话不能设置
            if (livingControlPanel != null) {

                if(handler!=null){

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //这个地方也不知道怎么处理最好 就延迟1500 才能滑动
                            if (!livingControlPanel.messageViewWatch.isBotViewShow()) {
                                livingControlPanel.messageViewWatch.setScrollEnable(true);
                            }
                        }
                    }, 1500);

//                livingControlPanel.viewWatch.hideInputLayout();

                }



            }
        } else {
            destroyView();
        }

    }

    private void addViewPage() {

        LivingActivity activity = (LivingActivity) getActivity();
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }

        //每次都用新的 就不用重置太多东西
        destroyView();

        livingMsgBoxAdapter = null;
        livingMsgBoxBeans.clear();

        TXCloudVideoView txCloudVideoView = new TXCloudVideoView(getActivity());
        txCloudVideoView.setId(R.id.txCloudVideoView);
        txCloudVideoView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBind.rlContent.addView(txCloudVideoView,1);


        MyViewPager viewPager = new MyViewPager(getActivity());
        viewPager.setId(R.id.livingViewPager);
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBind.rlContent.addView(viewPager);

        livingControlPanel = new LivingControlPanel(LivingFragment.this, viewPager);
        LivingFinishView livingFinishView=new LivingFinishView(LivingFragment.this,viewPager,false);
        contentViews[1]=livingControlPanel;
        contentViews[0]=livingFinishView;

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

                int screenHeight = ScreenUtils.getScreenHeight(getActivity());
                if (position == 1) {
                    container.addView(contentViews[1], ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    return contentViews[1];
                }
                else if(position==0)
                {
                    container.addView(contentViews[0], ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    return contentViews[0];
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

        enterRoom();
    }

    public RoomListBean getRoomBean() {
        LivingActivity activity = (LivingActivity) getActivity();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return currentRoomListBean;
        }

        return activity.getRoomListBeans().get(currentPagePosition);
    }

    private void sendSystemMsgToChat(CharSequence charSequence) {
        LivingMsgBoxBean bean = new LivingMsgBoxBean();
        bean.setBackgroundColor(0x66000000);
        bean.setType(0);

        bean.setCharSequence(charSequence);
        addNewMessage(bean);
    }

    private void sendPersonalMessage(PersonalLivingMessageBean pBean) {
        LivingMsgBoxBean bean = new LivingMsgBoxBean();
        bean.setBackgroundColor(0x66000000);
        bean.setType(1);

        switch (pBean.getProtocol()) {
            case MessageProtocol.LIVE_ROOM_CHAT_FLOATING_MESSAGE:
                playBulletMessage(pBean);
                break;
        }
        SpanUtils spanUtils = new SpanUtils();
        ChatSpanUtils.appendPersonalMessage(spanUtils, pBean, getActivity(), new LivingClickTextSpan.OnClickTextItemListener<PersonalLivingMessageBean>() {
            @Override
            public void onClick(PersonalLivingMessageBean bean) {
                if(getRoomBean()!=null)
                {
                    showBotDialog(getRoomBean().getId(),bean.getUid());
                }
            }
        });
        bean.setCharSequence(spanUtils.create());
        addNewMessage(bean);
    }

    private void personalSendGiftMessage(LivingMessageGiftBean livingMessageGiftBean) {
        LivingMsgBoxBean bean = new LivingMsgBoxBean();
        bean.setBackgroundColor(0x66000000);
        bean.setType(1);

        SpanUtils spanUtils = new SpanUtils();
        ChatSpanUtils.appendPersonalSendGiftMessage(spanUtils, livingMessageGiftBean, getActivity(), new LivingClickTextSpan.OnClickTextItemListener<LivingMessageGiftBean>() {
            @Override
            public void onClick(LivingMessageGiftBean bean) {
                if (bean != null && getRoomBean()!=null) {
                    showBotDialog(getRoomBean().getId(),bean.getUid()+"");
                }
            }
        });
        bean.setCharSequence(spanUtils.create());
        addNewMessage(bean);
    }

    private void addNewMessage(LivingMsgBoxBean bean) {
        if (!isActivityOK()) {
            return;
        }
        if (livingMsgBoxAdapter == null) {
            livingMsgBoxAdapter = new LivingMsgBoxAdapter(getContext(), livingMsgBoxBeans);
            livingControlPanel.mBind.msgBox.setAdapter(livingMsgBoxAdapter);
        }
        if (livingMsgBoxAdapter.getBeans().size() > 499) {
            livingMsgBoxAdapter.getBeans().remove(0);
        }
        livingMsgBoxAdapter.getBeans().add(bean);
        livingMsgBoxAdapter.notifyDataSetChanged();
        livingControlPanel.mBind.msgBox.smoothScrollToPosition(livingMsgBoxAdapter.getBeans().size());
    }

    public void getRecommendList() {
        LivingActivity activity = (LivingActivity) getActivity();
        activity.scrollRecommendViewToTop();
        Api_Live.ins().getRecommendLiveList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                boolean isSuccess = false;
                if (!isActivityOK()) {
                    return;
                }

                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray list = jsonObject.getJSONArray("list");
                        if (list != null && list.length() > 0) {
                            List<RoomListBean> listBeans = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                try {
                                    RoomListBean bean = new Gson().fromJson(list.getJSONObject(i).toString(), RoomListBean.class);
                                    listBeans.add(bean);
                                }catch (Exception e){

                                }

                            }
                            isSuccess = true;
                            activity.setRecommendListData(listBeans);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                activity.setRecommendFinish(isSuccess);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler=null;
        }

    }

    private void destroyView() {

        if (getView() != null) {

            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                livingMessageGiftBeans.clear();
            }

            LivingActivity activity = (LivingActivity) getActivity();
            if (!activity.isFinishing() && !activity.isDestroyed() && activity.getRoomListBeans() != null) {
                if (activity.getRoomListBeans().size() > currentPagePosition) {
                    AppIMManager.ins().loginOutGroup(activity.getRoomListBeans().get(currentPagePosition).getId());
                }
            }

            if (mLivePlayer != null) {
                mLivePlayer.stopPlay(true);
                mLivePlayer = null;
            }
            if (mVodPlayer != null) {
                mVodPlayer.stopPlay(true);
                mLivePlayer = null;
            }

            TXCloudVideoView txCloudVideoView = getView().findViewById(R.id.txLivingVideoView);
            ViewPager viewPager = getView().findViewById(R.id.livingViewPager);

            if (livingControlPanel != null && livingControlPanel.messageViewWatch != null) {
                livingControlPanel.messageViewWatch.onDestroy();
            }

            if (viewPager != null) {
                mBind.rlContent.removeView(viewPager);
            }

            if (mBind.svImage != null) {
                mBind.svImage.stopAnimation();
                mBind.svImage.clear();
            }

            if (txCloudVideoView != null) {
                txCloudVideoView.onDestroy();
                mBind.rlContent.removeView(txCloudVideoView);
            }

            mBind.ivBG.setVisibility(View.VISIBLE);

            livingMsgBoxBeans.clear();
            if (livingMsgBoxAdapter != null) {
                livingMsgBoxAdapter.getBeans().clear();
            }
            livingMsgBoxAdapter = null;
        }
    }

    private void setVodPlayerListener()
    {
        mVodPlayer.setVodListener(new ITXVodPlayListener() {
            @Override
            public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
                LogUtils.e("VodPlayer状态监听 " + event + ", " + bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
                if(!isActivityOK())return;

                if (event == TXLiveConstants.PLAY_EVT_CONNECT_SUCC) {
                    // 2001 連接服務器成功
                } else if (event == TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN) {
                    // 2002 已經連接服務器，開始拉流（僅播放RTMP地址時會抛送）
//            dimissLiveLoadingAnimation();
//            content.setBackground(null);
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                    mBind.ivBG.setVisibility(View.GONE);
                    mBind.txVideoView.setVisibility(View.VISIBLE);
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
                    mBind.txVideoView.setVisibility(View.GONE);
                    mBind.ivBG.setVisibility(View.VISIBLE);
//                    clearStop();
//                    if (isAdded()) {
//                        networkDisconnect();
//                    }
                }
            }

            @Override
            public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

            }
        });
    }

    private void setLivePlayerListener() {
        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int event, Bundle bundle) {
                LogUtils.e("LivePlayer状态监听 " + event + ", " + bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
                if(!isActivityOK())return;

                if (event == TXLiveConstants.PLAY_EVT_CONNECT_SUCC) {
                    // 2001 連接服務器成功
                } else if (event == TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN) {
                    // 2002 已經連接服務器，開始拉流（僅播放RTMP地址時會抛送）
//            dimissLiveLoadingAnimation();
//            content.setBackground(null);
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                    mBind.ivBG.setVisibility(View.GONE);
                    mBind.txVideoView.setVisibility(View.VISIBLE);
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
                    mBind.txVideoView.setVisibility(View.GONE);
                    mBind.ivBG.setVisibility(View.VISIBLE);
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
    private void setPlayMode(int strategy, TXLivePlayer mLivePlayer) {
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

    public void getOutOfRoom(String liveId)
    {
        //即使activity挂壁 也请求
        Api_Live.ins().outRoom(liveId, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {

            }
        });
    }

    private void enterRoom() {
        if (isActivityOK()) {
            LivingActivity activity = (LivingActivity) getActivity();
            final RoomListBean bean = activity.getRoomListBeans().get(currentPagePosition);

            Api_Live.ins().interRoom(bean.getId(), bean.getAid(),bean.getRoomType(),
                    "", 0, new JsonCallback<EnterRoomBean>() {
                        @Override
                        public void onSuccess(int code, String msg, EnterRoomBean enterRoomBean) {
                            if(!isActivityOK() || !getArg().equals(bean.getId()))
                            {
                                return;
                            }

                            if(code==3001)
                            {
                                //关播了
                                getAnchorInfo(enterRoomBean,false);
                                return;
                            }

                            if ( enterRoomBean != null) {
                                LivingFragment.this.enterRoomBean=enterRoomBean;
                                //进入房间成功刷新主播信息
                                getAnchorInfo(enterRoomBean,true);

                                //该(收费)直播间是否可预览标识；1：不可预览，非1（0或者null）：可以预览
                                if( enterRoomBean.getIsPreview()==0 &&  enterRoomBean.getPreviewTime()>0)
                                {

                                    if(handler!=null){
                                        handler.sendEmptyMessageDelayed(previewCountDown,1000);
                                    }

                                }

                                if (!TextUtils.isEmpty(enterRoomBean.getPullStreamUrl())) {
                                    if (!PlayerUtils.checkPlayUrl(enterRoomBean.getPullStreamUrl(), getActivity())) {
                                        return;
                                    }

                                    TXCloudVideoView txCloudVideoView= mBind.rlContent.findViewById(R.id.txCloudVideoView);
                                    //是否真实直播间(0虚拟 1真实)
                                    switch (enterRoomBean.getIsReal()) {
                                        case 0:
                                            mVodPlayer= new TXVodPlayer(getActivity());
                                            setVodPlayerListener();
                                            mVodPlayer.setLoop(true);
                                            mVodPlayer.setPlayerView(txCloudVideoView);
                                            mVodPlayer.startPlay(enterRoomBean.getPullStreamUrl());
                                            break;
                                        case 1:
                                            mTXPlayConfig = new TXLivePlayConfig();
                                            mLivePlayer = new TXLivePlayer(getActivity());
                                            mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                                            mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
                                            mLivePlayer.enableHardwareDecode(false);
                                            setLivePlayerListener();
                                            setPlayMode(2, mLivePlayer);

                                            mLivePlayer.setPlayerView(txCloudVideoView);
                                            mLivePlayer.startPlay(enterRoomBean.getPullStreamUrl(), PlayerUtils.getVideoType(enterRoomBean.getPullStreamUrl()));
                                            break;
                                    }

                                }
                                LocalWatchHistoryDao.getInstance().insert(RoomWatchedHistoryBean.convertBean(bean));
                            }
                        }
                    });
        }

    }

    /**
     * 加入群聊前
     * 先判断用户是否连接IM
     * 如果未连接
     * 则先连接IM后再加入IM群聊
     */
    public void checkAndJoinIM(String liveId) {

        String currentUser = V2TIMManager.getInstance().getLoginUser();
        if (TextUtils.isEmpty(currentUser)) {
            //当前IM未连接用户 则先让用户连接IM后
            AppIMManager.ins().connectIM(new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    LogUtils.e("IMGroup->onError:" + code + "，" + desc);
                    hideLoadingDialog();
                    if(isActivityOK())
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joinGroupFailed(liveId, 1, code, desc);
                            }
                        });
                    }

                }

                @Override
                public void onSuccess() {
                    //连接IM成功后 加入群聊
                    joinIMGroup(liveId);
                }
            });
        } else {
            LogUtils.e("IMGroup-> 当前连接IM的用户:" + currentUser + " liveid " + liveId);
            joinIMGroup(liveId);
        }
    }


    /**
     * 加入聊天群失败
     *
     * @param type 失败代码
     * @param code 失败代码 类型
     * @param desc 失败的原因
     */
    private void joinGroupFailed(String liveId, int type, int code, String desc) {
        switch (code) {
            case 6017:
                if ("sdk not initialized".equals(desc)) {
                    LogUtils.e("sdk not initialized");
                }
                break;
            case 10010: //群组不存在，或者曾经存在过，但是目前已经被解散
//                if (0 != currentAnchor.getLiveStatus()) {
//                    closeRoomAndStopPlay(false, false, false);
//                    sendSystemMsgToChat(getString(R.string.chatNoExist));
//                    showLiveFinishFragment(currentAnchor, getString(R.string.chatNoExist));
//                }
                break;

            case 6014://SDK 未登∂录，请先登录，成功回调之后重试，或者被踢下线，可使用 TIMManager getLoginUser 检查当前是否在线
                AppIMManager.ins().connectIM(null);
                joinIMGroup(liveId);
                break;

            case 6012: //请求超时，请等网络恢复后重试。（Android SDK 1.8.0 以上需要参考 Android 服务进程配置 方式进行配置，否则会出现此错误）
                SpanUtils spanUtils = ChatSpanUtils.appendSystemMessageType(MessageProtocol.LIVE_ENTER_OUT_ROOM, getStringWithoutContext(R.string.discRetry), getActivity());
                sendSystemMsgToChat(spanUtils.create());
                if (type == 1) {
                    checkAndJoinIM(liveId);
                } else {
                    joinIMGroup(liveId);
                }
                break;

            case 10013://被邀请加入的用户已经是群成员
                LogUtils.e("IMIMGroup->10013 被邀请加入的用户已经是群成员");
                break;
            case 10015://group id错误
                if(!TextUtils.isEmpty(desc))
                {
                    sendSystemMsgToChat(ChatSpanUtils.appendSystemMessageType(MessageProtocol.LIVE_ENTER_OUT_ROOM, desc, getActivity()).create());
                }
                break;
            case 9506:
            case 9520:
                //直播结束
//                showLiveFinishFragment(currentAnchor, desc);
            default:
                //直播结束
//                if (0 != currentAnchor.getLiveStatus()) {
//                    sendSystemMsgToChat(getString(R.string.app_network_error_unknown) + code);
//                    showLiveFinishFragment(currentAnchor, desc);
//                }
                break;
        }
    }

    /**
     * 加入如聊天群组
     */
    private void joinIMGroup(String liveId) {
        AppIMManager.ins().loginGroup(String.valueOf(liveId),
                getStringWithoutContext(R.string.openJoinChat), new V2TIMCallback() {
                    @Override
                    public void onSuccess() {
                        if (isActivityOK() && livingCurrentAnchorBean != null) {
                            if (!TextUtils.isEmpty(livingCurrentAnchorBean.nickname)) {
                                String welcome = String.format(getString(R.string.chatWelcome), livingCurrentAnchorBean.nickname);
                                SpanUtils spanUtils = ChatSpanUtils.appendSystemMessageType(MessageProtocol.LIVE_ENTER_OUT_ROOM, welcome, getActivity());
                                sendSystemMsgToChat(spanUtils.create());

                                if(handler!=null){

                                    handler.sendEmptyMessageDelayed(userHeartBeat, 40000);
                                    if(!livingCurrentAnchorBean.getFollow())
                                    {
                                        handler.sendEmptyMessageDelayed(alertWhenExit, 5 * 60000);
                                        handler.sendEmptyMessageDelayed(alertFloatingFollow,30000);
                                    }

                                }


                            }
                        }


//                        if (currentAnchor.getShowType() == 0) {
//                            if (getLiveInFragment() != null && currentAnchor.getRoomHide() == 0) {
//                                showAdmission();
//                            }
//                        }
                    }

                    @Override
                    public void onError(int code, String desc) {
                        LogUtils.e("IMGroup-> 加入聊天失敗: code->" + code + "  , desc->" + desc);
                        if(isActivityOK())
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    joinGroupFailed(liveId, 2, code, desc);
                                }
                            });
                        }
                    }
                });
    }


    public void onNewMessageReceived(int protocol, String msg) {
        if (!isActivityOK() || livingControlPanel == null) {
            return;
        }

        Log.e("onNewMessageReceived", msg);

        if (!TextUtils.isEmpty(msg) && getRoomBean() != null) {
            try {
                JSONObject msgJson = new JSONObject(msg);
                String protocolCode = msgJson.optString("protocol", "");
                String liveId = msgJson.optString("liveId", "");
                boolean isHasProtocolCode = !TextUtils.isEmpty(msgJson.optString("protocol", ""));
                boolean isCurrentLiveId =TextUtils.isEmpty(liveId) || getRoomBean().getId().equals(liveId);
                //liveId如果是空就是全局消息 所有房间都推送了
                if (isHasProtocolCode && isCurrentLiveId) {
                    switch (protocolCode) {
                        case MessageProtocol.SYSTEM_NOTICE:
                        case MessageProtocol.GAME_CP_WIN:
                            break;
                        case MessageProtocol.LIVE_BLACK_CHAT:
                        case MessageProtocol.LIVE_ROOM_SET_MANAGER_MSG:
                        case MessageProtocol.LIVE_BAN_USER:
                            roomOperate(msgJson);
                            break;
                        case MessageProtocol.LIVE_ENTER_OUT_ROOM:
                            livingMessageEnterOrOutOfRoom(liveId,msg);
                            break;
                        case MessageProtocol.LIVE_ROOM_CHAT_FLOATING_MESSAGE:
                        case MessageProtocol.LIVE_ROOM_CHAT:
                            PersonalLivingMessageBean pBean = new Gson().fromJson(msg, PersonalLivingMessageBean.class);
                            sendPersonalMessage(pBean);
                            break;
                        case MessageProtocol.LIVE_SEND_GIFT:
                            LivingMessageGiftBean gBean = new Gson().fromJson(msg, LivingMessageGiftBean.class);
                            if (gBean != null) {
                                personalSendGiftMessage(gBean);
                                GiftResourceBean giftResourceBean = LocalGiftDao.getInstance().getGift(gBean.getGid());
                                if (giftResourceBean != null && !TextUtils.isEmpty(giftResourceBean.getLocalSvgPath())) {
                                    SvgAnimateLivingBean svgAnimateLivingBean = new SvgAnimateLivingBean();
                                    svgAnimateLivingBean.setLocalSvgPath(giftResourceBean.getLocalSvgPath());
                                    svgAnimateLivingBean.setLoopTimes(gBean.getCount());
                                    livingMessageGiftBeans.add(svgAnimateLivingBean);

                                    if(handler!=null){
                                        handler.sendEmptyMessage(playSVGA);
                                    }

                                }
                            }
                            break;
                        case MessageProtocol.LIVE_FOLLOW:
                            LivingFollowMessage followMessage = new Gson().fromJson(msg, LivingFollowMessage.class);
                            if (followMessage != null && livingCurrentAnchorBean != null) {
                                followMessage.setAnchorNickName(livingCurrentAnchorBean.nickname);
                                SpanUtils spanUtils = ChatSpanUtils.appendFollowMessage(new SpanUtils(), followMessage, getActivity());
                                sendSystemMsgToChat(spanUtils.create());
                            }
                            break;
                        case MessageProtocol.GOLD_COIN_CHANGE:
                            JSONObject jsonObject = new JSONObject(msg);
                            try {
                                String diamondCoin = jsonObject.optString("diamondCoin", "");
                                String goldCoin = jsonObject.optString("goldCoin", "");
                                User user = new User();
                                user.setDiamond(new BigDecimal(diamondCoin));
                                user.setGold(new BigDecimal(goldCoin));
                                DataCenter.getInstance().getUserInfo().updateUser(user);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            break;
                        case MessageProtocol.CHARGE_ROOM_CHANGE:
                            livingMessageChangeRoomType(liveId,msgJson);
                            break;
                        case MessageProtocol.InsufficientBalanceOnWatching:
                            getOutOfRoom(getRoomBean().getId());
                            mBind.ivBG.setVisibility(View.VISIBLE);
                            mBind.txVideoView.setVisibility(View.GONE);
                            if(mLivePlayer!=null)
                            {
                                mLivePlayer.pause();
                            }

                            BigDecimal gold= DataCenter.getInstance().getUserInfo().getUser().getGold();
                            if(gold!=null && gold.compareTo(new BigDecimal(0))!=1)
                            {
                                showInsufficientBalanceDialog();
                            }
                            else
                            {
                                showInsufficientDiamondDialog();
                            }
                            break;
                        case MessageProtocol.LIVE_STOP_LIVE:
                            //显示下播页面
                            MyViewPager viewPager=mBind.getRoot().findViewById(R.id.livingViewPager);
                            viewPager.setCurrentItem(0);
                            viewPager.setScrollEnable(false);

                            LivingFinishView livingFinishView=(LivingFinishView)contentViews[0];
                            livingFinishView.showView();

                            if(livingControlPanel!=null){
                                livingControlPanel.dissmissTreasureBoxDialog();
                            }
                            break;
                        case MessageProtocol.LIVE_BUY_GUARD:
                            NewBornNobleOrGuardMessageBean bean=new Gson().fromJson(msg,NewBornNobleOrGuardMessageBean.class);
                            if(bean!=null && bean.getGuardCount()!=null)
                            {
                                livingControlPanel.mBind.gtvProtection.setText(bean.getGuardCount()+"");
                            }
                            LivingClickTextSpan.OnClickTextItemListener listener=new LivingClickTextSpan.OnClickTextItemListener() {
                                @Override
                                public void onClick(Object bean) {
                                    if(bean!=null && bean instanceof NewBornNobleOrGuardMessageBean)
                                    {
                                        NewBornNobleOrGuardMessageBean nBean=(NewBornNobleOrGuardMessageBean)bean;
                                        showBotDialog(liveId,nBean.getUid()+"");
                                    }
                                }
                            };
                            SpanUtils spanUtils=ChatSpanUtils.appendNewBornGuard(bean,getActivity(),listener);
                            if(spanUtils.getLength()>0)
                            {
                                sendSystemMsgToChat(spanUtils.create());
                            }
                            break;
                        case MessageProtocol.LIVE_BUY_VIP:
                            livingControlPanel.refresh20AudienceList();
                            NewBornNobleOrGuardMessageBean nBean=new Gson().fromJson(msg,NewBornNobleOrGuardMessageBean.class);
                            livingControlPanel.mBind.rlNewBornMessage.postNewMessage(nBean,getActivity());
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 房间收费方式更改消息
     */
    private void livingMessageChangeRoomType(String liveId, JSONObject msgJson)
    {
        int roomType=msgJson.optInt("type",-1);
        String price=msgJson.optString("price","");
        //0免费 1收费（计时收费）； 2收费（按次收费）
        switch (roomType)
        {
            case 0:
                break;
            case 1:
            case 2:
                if(livingCurrentAnchorBean!=null && livingCurrentAnchorBean.getType()!=roomType)
                {
                    showChangeRoomTypeDialog(roomType,price);
                }
                break;
        }
    }


    /**
     * 计时房间付费
     */
    private void payByTime(String liveId,String uid)
    {
        if(!isActivityOK())
        {
            return;
        }

        showLoadingDialogWithNoBgBlack();
        Api_Live.ins().payForRoom(liveId, uid, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(isActivityOK())
                {
                    hideLoadingDialog();
                    if(code==0 && getArg().equals(liveId))
                    {
                        enterRoom();
                    }
                    else
                    {
                        //{"code":999,"msg":"可用余额不足"}
                       if(code==999)
                       {
                           //余额小于等于0 显示余额不足
                          BigDecimal gold= DataCenter.getInstance().getUserInfo().getUser().getGold();
                          if(gold!=null && gold.compareTo(new BigDecimal(0))!=1)
                          {
                              showInsufficientBalanceDialog();
                          }
                          else
                          {
                              showInsufficientDiamondDialog();
                          }
                       }
                    }
                }
            }
        });
    }

    /**
     * 进入 退出 房间消息
     */
    private void livingMessageEnterOrOutOfRoom(String liveId, String msg) {
        LivingEnterLivingRoomBean livingEnterLivingRoomBean = new Gson().fromJson(msg, LivingEnterLivingRoomBean.class);
        livingEnterLivingRoomBean.setMessage(getStringWithoutContext(R.string.comeWelcome));
        //刷新总人数
        livingControlPanel.mBind.gtvOnlineAmount.setText(livingEnterLivingRoomBean.getLiveSum()+"");
        if(!livingEnterLivingRoomBean.isInter())
        {
            //出去房间的不处理
            return;
        }

        if (livingControlPanel == null) return;
        livingControlPanel.mBind.vtEnterRoom.
                addCharSequence(ChatSpanUtils.enterRoom(livingEnterLivingRoomBean, getActivity()).create());
        livingControlPanel.refresh20AudienceList();

        long uid = DataCenter.getInstance().getUserInfo().getUser().getUid();
        boolean isPlayAvailable = false;
        UserVehiclePlayLimitBean userVehiclePlayLimitBean = null;
        if(livingEnterLivingRoomBean.getUid()!=uid)
        {
            userVehiclePlayLimitBean= LocalUserVehiclePlayLimitDao.getInstance()
                    .selectByLiveIDAndUID(liveId,String.valueOf(uid),LocalUserVehiclePlayLimitDao.Anchor);
            if(userVehiclePlayLimitBean!=null)
            {
                isPlayAvailable=System.currentTimeMillis()- userVehiclePlayLimitBean.getShowTime()>10*60*1000;
            }
            else
            {
                isPlayAvailable=true;
            }
        }
        else
        {
            isPlayAvailable=true;
        }

        //播放进房 是自己不限制 不是自己限制10分钟内只播放一次
        if (isPlayAvailable) {
            //播放进房漂房
            livingControlPanel.mBind.rlEnterRoom.postEnterRoomMessage(livingEnterLivingRoomBean, getActivity());

            //播放SVGA进房座驾动画
            if (Strings.isDigitOnly(livingEnterLivingRoomBean.getCarId())) {
                //播放进房座驾
                livingControlPanel.mBind.rlVehicleParentView.postEnterRoomMessage(livingEnterLivingRoomBean, getActivity());

                MountResourceBean mountResourceBean = LocalMountResourceDao.getInstance().getVehicleById(Long.valueOf(livingEnterLivingRoomBean.getCarId()));
                if (mountResourceBean != null) {
                    SvgAnimateLivingBean svgAnimateLivingBean = new SvgAnimateLivingBean();
                    svgAnimateLivingBean.setLocalSvgPath(mountResourceBean.getLocalSvgPath());
                    svgAnimateLivingBean.setLoopTimes(1);
                    livingMessageGiftBeans.add(svgAnimateLivingBean);

                    if(handler!=null){
                        handler.sendEmptyMessage(playSVGA);
                    }

                }
            }
            if (userVehiclePlayLimitBean == null) {
                userVehiclePlayLimitBean = new UserVehiclePlayLimitBean();
                userVehiclePlayLimitBean.setShowTime(System.currentTimeMillis());
                userVehiclePlayLimitBean.setType(LocalUserVehiclePlayLimitDao.Anchor);
                userVehiclePlayLimitBean.setUid(String.valueOf(uid));
                userVehiclePlayLimitBean.setLiveId(liveId);
                LocalUserVehiclePlayLimitDao.getInstance().insert(userVehiclePlayLimitBean);
            } else {
                userVehiclePlayLimitBean.setShowTime(System.currentTimeMillis());
                LocalUserVehiclePlayLimitDao.getInstance().updateData(userVehiclePlayLimitBean);
            }
        }

    }


    /**
     * 获取当前主播数据
     */
    public void getAnchorInfo(EnterRoomBean bean,boolean isRoomLiving) {
        if (!isActivityOK()) {
            return;
        }

        Api_Live.ins().getAnchorInfo(getRoomBean().getId(), getRoomBean().getAid(), new JsonCallback<LivingCurrentAnchorBean>() {
            @Override
            public void onSuccess(int code, String msg, LivingCurrentAnchorBean data) {
                if (code == 0) {
                    if (livingControlPanel != null && data != null && isActivityOK() && getArg().equals(getRoomBean().getId())) {
                        LivingFragment.this.livingCurrentAnchorBean = data;
                        GlideUtils.loadCircleImage(getActivity(), data.getAvatar(), R.mipmap.user_head_error, R.mipmap.user_head_error,
                                livingControlPanel.mBind.rivProfileImage.getProfileImage());
                        getAnchorInfo(Long.valueOf(data.getAnchorId()));
                        livingControlPanel.mBind.rivProfileImage.setIndex(RankProfileView.NONE, data.getVipLevel(),false);
                        livingControlPanel.mBind.gtvOnlineAmount.setText(data.getLiveSum() + "");
                        livingControlPanel.mBind.gtvOnlineAmount.setVisibility(View.VISIBLE);
                        livingControlPanel.mBind.gtvProtection.setText(data.getGuardCount()+"");
                        livingControlPanel.setData(getRoomBean());
                        if (data.getFollow() != null) {
                            livingControlPanel.mBind.ivFollow.setVisibility(data.getFollow() ? View.GONE : View.VISIBLE);
                        }
                        livingControlPanel.mBind.tvAnchorName.setText(data.getNickname());

                        if(isRoomLiving)
                        {
                            switch (getRoomBean().getRoomType())
                            {
                                //0免费 1收费（计时收费）； 2收费（按次收费）
                                case 0:
                                    livingControlPanel.mBind.gtvUnitPrice.setVisibility(View.INVISIBLE);
                                    break;
                                case 1:
                                    if(!TextUtils.isEmpty(data.getPrice()))
                                    {
                                        livingControlPanel.mBind.gtvUnitPrice.setVisibility(View.VISIBLE);
                                        livingControlPanel.mBind.gtvUnitPrice.setText(String.format(getStringWithoutContext(R.string.diamondPerMin),data.getPrice()));
                                    }
                                    break;
                                case 2:
                                    if(!TextUtils.isEmpty(data.getPrice()))
                                    {
                                        livingControlPanel.mBind.gtvUnitPrice.setVisibility(View.VISIBLE);
                                        livingControlPanel.mBind.gtvUnitPrice.setText(String.format(getStringWithoutContext(R.string.diamondPerShow),data.getPrice()));
                                    }
                                    break;
                            }

                            boolean shouldConnectIM=true;
                            //如果不可以预览收费直播间 弹窗收费
                            //或者可以预览 但是预览时间没了 弹窗收费
                            if(bean.getIsPreview()==1 || (bean.getIsPreview()==0 && bean.getPreviewTime()<=0))
                            {
                                if(Strings.isDigitOnly(data.getIsPayOver()))
                                {
                                    //0 未付费 1 已经付费
                                    if(Integer.valueOf(data.getIsPayOver())==0 && Strings.isDigitOnly(data.getPrice()))
                                    {
                                        shouldConnectIM=false;
                                        showChangeRoomTypeDialog(data.getType(),data.getPrice());
                                    }
                                }
                            }

                            if(shouldConnectIM)
                            {
                                checkAndJoinIM(getRoomBean().getId());
                            }

                            boolean isPayNeededRoom=getRoomBean().getRoomType()==1 || getRoomBean().getRoomType()==2;
                            //0 未付费 1 已经付费
                            if(isPayNeededRoom && Integer.valueOf(data.getIsPayOver())==1 && Strings.isDigitOnly(data.getPrice()) )
                            {
                                Message message=new Message();
                                message.arg1=1;
                                if(handler!=null){
                                    handler.sendMessageDelayed(message,1000);
                                }

                            }
                        }

                    }

                } else {
                    if(isRoomLiving)
                    {
                        ToastUtils.showShort(msg);
                    }
                }

                //房间关闭了显示这个
                if(!isRoomLiving)
                {
                    //显示下播页面
                    MyViewPager viewPager=mBind.getRoot().findViewById(R.id.livingViewPager);
                    viewPager.setCurrentItem(0);
                    viewPager.setScrollEnable(false);

                    LivingFinishView livingFinishView=(LivingFinishView)contentViews[0];
                    livingFinishView.showView();
                    return;
                }
            }
        });
    }

    public void playSVGAAnimal() {
        if (livingMessageGiftBeans.size() < 1) {
            return;
        }
        if (mBind.svImage.isAnimating()) {
            return;
        }

        Log.e("anisize", livingMessageGiftBeans.size() + " ");
        SvgAnimateLivingBean bean = livingMessageGiftBeans.get(0);

        File file = new File(bean.getLocalSvgPath());
        if (file == null || !file.exists()) {
            return;
        }

        mBind.svImage.setLoops(bean.getLoopTimes());
        SVGAParser parser = SVGAParser.Companion.shareParser();
        mBind.svImage.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
            }

            @Override
            public void onFinished() {
                if (mBind.svImage != null) {
                    mBind.svImage.clear();
                }
                if (livingMessageGiftBeans!=null&&livingMessageGiftBeans.size() > 0) {
                    livingMessageGiftBeans.remove(0);
                    if (livingMessageGiftBeans.size() > 0) {

                        if(handler!=null){
                            handler.sendEmptyMessage(playSVGA);
                        }

                    }
                }
            }

            @Override
            public void onRepeat() {
            }

            @Override
            public void onStep(int i, double v) {
            }
        });

        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            parser.decodeFromInputStream(bufferedInputStream, file.getAbsolutePath(),
                    new SVGAParser.ParseCompletion() {
                        @Override
                        public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                            SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                            mBind.svImage.setImageDrawable(drawable);
                            mBind.svImage.startAnimation();
                        }

                        @Override
                        public void onError() {
                            Log.e("playSvg", "onError: ");
                        }
                    }, true, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void playBulletMessage(PersonalLivingMessageBean bean) {
        if (getActivity() != null && livingControlPanel != null) {
            livingControlPanel.mBind.rlMidView.postBulletMessage(bean, getActivity());
        }
    }

    private void showBotDialog(String liveId, String uid) {
        if (livingControlPanel != null && !ClickUtil.isClickWithShortTime(uid.hashCode(), 500)) {
            if (livingControlPanel.messageViewWatch.isKeyboardShow() || livingControlPanel.messageViewWatch.isMessagesPanelOpen()) {
                livingControlPanel.messageViewWatch.hideInputLayout();
                livingControlPanel.messageViewWatch.hideKeyboard();
                livingControlPanel.messageViewWatch.setScrollEnable(true);
            } else {
                if (!DialogFramentManager.getInstance().isShowLoading(LivingProfileBottomDialog.class.getName())) {

                    LivingProfileBottomDialog dialog = LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.Audience,liveId, uid);
                    dialog.setButtonClickListener(new LivingProfileBottomDialog.ButtonClickListener() {
                        @Override
                        public void onClick(String uid, boolean follow, boolean tagSomeone, String nickName) {
                            if (tagSomeone) {
                                livingControlPanel.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        livingControlPanel.mBind.etDiaMessage.setText("@" + nickName + " ");
                                        livingControlPanel.mBind.etDiaMessage.setSelection(livingControlPanel.mBind.etDiaMessage.getText().length());
                                        livingControlPanel.messageViewWatch.showInputLayout();
                                    }
                                }, 200);
                            }
                        }
                    });
                    DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), dialog);
                }
            }

        }
    }

    private void showChangeRoomTypeDialog(int type,String price)
    {
        getOutOfRoom(getRoomBean().getId());
        mBind.ivBG.setVisibility(View.VISIBLE);
        mBind.txVideoView.setVisibility(View.GONE);
        if(mLivePlayer!=null)
        {
            mLivePlayer.pause();
        }

        TempleDialog2 templeDialog= TempleDialog2.getInstance();
        templeDialog.setOnCreateDialogListener(new TempleDialog2.OnCreateDialogListener() {
            @Override
            public void onCreate(TempleDialog2 dialog) {

                dialog.mBind.tvTitle.setText(getStringWithoutContext(R.string.paidLiving));
                dialog.mBind.gtCommit.setText(getStringWithoutContext(R.string.confirm));
                dialog.mBind.gtCancel.setText(getStringWithoutContext(R.string.nextRoom));
                SpanUtils spanUtils=new SpanUtils();
                spanUtils.append(getStringWithoutContext(R.string.live_change_to_paid));
                spanUtils.append(",");
                spanUtils.append(String.valueOf(price));
                Bitmap iconDiamond= BitmapFactory.decodeResource(getResources(),R.mipmap.icon_diamond);
                int width=ScreenUtils.getDip2px(getActivity(),12.5f);
                int height=ScreenUtils.getDip2px(getActivity(),9.5f);
                spanUtils.appendImage(ImageUtils.scale(iconDiamond, width, height),SpanUtils.ALIGN_BASELINE);

                switch (type)
                {
                    case 1:
                        spanUtils.append(getStringWithoutContext(R.string.dialogText4));
                        dialog.mBind.tvContent.setText(spanUtils.create());
                        break;
                    case 2:
                        spanUtils.append(getStringWithoutContext(R.string.dialogText2));
                        dialog.mBind.tvContent.setText(spanUtils.create());
                        break;
                }
            }

            @Override
            public void clickCancel(TempleDialog2 dialog) {
                dialog.dismissAllowingStateLoss();
                LivingActivity activity = (LivingActivity) getActivity();
                activity.goNextRoom();
            }

            @Override
            public void clickOk(TempleDialog2 dialog) {
                dialog.dismissAllowingStateLoss();
                payByTime(getRoomBean().getId(),getRoomBean().getAid());
            }

            @Override
            public void clickClose(TempleDialog2 dialog) {
                dialog.dismissAllowingStateLoss();
                getActivity().finish();
            }
        });
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),templeDialog);
    }

    private void showInsufficientBalanceDialog()
    {
        if(DialogFramentManager.getInstance().isShowLoading(TempleDialog2.class.getName()))
        {
            return;
        }
        TempleDialog2 templeDialog= TempleDialog2.getInstance();
        templeDialog.setOnCreateDialogListener(new TempleDialog2.OnCreateDialogListener() {
            @Override
            public void onCreate(TempleDialog2 dialog) {
                dialog.mBind.tvTitle.setText(getStringWithoutContext(R.string.dialogTitle2));
                dialog.mBind.gtCommit.setText(getStringWithoutContext(R.string.confirm));
                dialog.mBind.gtCancel.setText(getStringWithoutContext(R.string.cancel));
                dialog.mBind.tvContent.setText(getStringWithoutContext(R.string.InsufficientBalance2));
                dialog.mBind.ivClose.setVisibility(View.GONE);
            }

            @Override
            public void clickCancel(TempleDialog2 dialog) {
                LivingActivity activity = (LivingActivity) getActivity();
                activity.goNextRoom();
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void clickOk(TempleDialog2 dialog) {
                RechargeActivity.startActivity(requireActivity());
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void clickClose(TempleDialog2 dialog) {
                LivingActivity activity = (LivingActivity) getActivity();
                activity.goNextRoom();
                dialog.dismissAllowingStateLoss();
            }
        });
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),templeDialog);
    }

    private void showInsufficientDiamondDialog()
    {
        if(DialogFramentManager.getInstance().isShowLoading(TempleDialog2.class.getName()))
        {
            return;
        }
        TempleDialog2 templeDialog= TempleDialog2.getInstance();
        templeDialog.setOnCreateDialogListener(new TempleDialog2.OnCreateDialogListener() {
            @Override
            public void onCreate(TempleDialog2 dialog) {
                dialog.mBind.tvTitle.setText(getStringWithoutContext(R.string.dialogTitle2));
                dialog.mBind.gtCommit.setText(getStringWithoutContext(R.string.confirm));
                dialog.mBind.gtCancel.setText(getStringWithoutContext(R.string.cancel));
                dialog.mBind.tvContent.setText(getStringWithoutContext(R.string.InsufficientDiamond));
                dialog.mBind.ivClose.setVisibility(View.GONE);
            }

            @Override
            public void clickCancel(TempleDialog2 dialog) {
                LivingActivity activity = (LivingActivity) getActivity();
                activity.goNextRoom();
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void clickOk(TempleDialog2 dialog) {
                RechargeActivity.startActivity(requireActivity(), false);
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void clickClose(TempleDialog2 dialog) {
                LivingActivity activity = (LivingActivity) getActivity();
                activity.goNextRoom();
                dialog.dismissAllowingStateLoss();
            }
        });
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),templeDialog);
    }

    /**
     * MessageProtocol.LIVE_BLACK_CHAT: 禁言
     * MessageProtocol.LIVE_ROOM_SET_MANAGER_MSG:设置房管
     * MessageProtocol.LIVE_BAN_USER:踢人（拉黑）
     * MessageProtocol.LIVE_BLACK_CHAT_CANCEL 取消禁言
     */
    private void roomOperate(JSONObject jsonObject)
    {
        if(jsonObject!=null)
        {
            String nickname=jsonObject.optString("nickname","");
            String protocol=jsonObject.optString("protocol","");
            String uid=jsonObject.optString("uid","");
            String type=jsonObject.optString("type","");

            SpanUtils spanUtils=ChatSpanUtils.appendSystemMessageType(protocol, "",getActivity());

            LivingClickTextSpan.OnClickTextItemListener listener=new LivingClickTextSpan.OnClickTextItemListener() {
                @Override
                public void onClick(Object bean) {
                    if(bean!=null && bean instanceof JSONObject)
                    {
                        JSONObject data=(JSONObject)bean;
                        String uid=data.optString("uid","");
                        String liveId=data.optString("liveId","");
                        showBotDialog(liveId,uid);
                    }
                }
            };
            LivingClickTextSpan livingClickTextSpan = new LivingClickTextSpan(jsonObject, 0xff85EFFF);
            livingClickTextSpan.setOnClickTextItemListener(listener);
            int length1 = 0;
            int length2 = 0;

            if(!TextUtils.isEmpty(protocol))
            {
                switch (protocol)
                {
                    case MessageProtocol.LIVE_BLACK_CHAT:
                        Boolean isBlack=jsonObject.optBoolean("isBlack");
                        if(isBlack==null)return;
                        spanUtils.append(nickname + ": ");
                        length1 = spanUtils.getLength();
                        if(isBlack)
                        {
                            spanUtils.append(getStringWithoutContext(R.string.muted)).setForegroundColor(0xffffffff);
                            length2 = spanUtils.getLength();
                            spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spanUtils.append(" "+getStringWithoutContext(R.string.forever)).setForegroundColor(0xffFF565A);
                            sendSystemMsgToChat(spanUtils.create());
                        }
                        else
                        {
                            spanUtils.append(getStringWithoutContext(R.string.unMuted)).setForegroundColor(0xffffffff);
                            length2 = spanUtils.getLength();
                            spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            sendSystemMsgToChat(spanUtils.create());
                            break;
                        }
                    case MessageProtocol.LIVE_ROOM_SET_MANAGER_MSG:
                        boolean isSetAdmin=Strings.isDigitOnly(type) && Integer.valueOf(type)==1;
                        String whiteText=isSetAdmin?
                                getStringWithoutContext(R.string.obtainAdmin):getStringWithoutContext(R.string.cancelAdminRights);
                        spanUtils.append(nickname + " ");
                        length1 = spanUtils.getLength();
                        spanUtils.append(whiteText).setForegroundColor(0xffffffff);
                        length2 = spanUtils.getLength();
                        spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sendSystemMsgToChat(spanUtils.create());
                        if(isSetAdmin)
                        {
                            ToastUtils.showShort(getStringWithoutContext(R.string.livingTips1));
                        }
                        break;
                    case MessageProtocol.LIVE_BAN_USER:
                        spanUtils.append(nickname + " ");
                        length1 = spanUtils.getLength();
                        spanUtils.append(getStringWithoutContext(R.string.kickedOut)).setForegroundColor(0xffffffff);
                        length2 = spanUtils.getLength();
                        spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sendSystemMsgToChat(spanUtils.create());
                        ToastUtils.showShort(getStringWithoutContext(R.string.livingTips2));
                        break;
                }
            }
        }
    }


    private void getAnchorInfo(Long uid)
    {
        Api_User.ins().getUserInfo(uid, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(!isActivityOK() && livingControlPanel!=null)
                {
                    return;
                }
                if(!TextUtils.isEmpty(data))
                {
                    User user=new Gson().fromJson(data,User.class);
                    livingControlPanel.mBind.rivProfileImage.setIndex(RankProfileView.NONE,user.getVipLevel(),false);
                }
            }
        });
    }
}
