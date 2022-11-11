package com.live.fox.ui.living;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.live.fox.AppIMManager;
import com.live.fox.R;
import com.live.fox.adapter.LivingFragmentStateAdapter;
import com.live.fox.adapter.RecommendLivingAnchorAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityLivingBinding;
import com.live.fox.dialog.FirstTimeTopUpDialog;
import com.live.fox.dialog.PersonalContactCardDialog;
import com.live.fox.dialog.temple.FreeRoomToPrepaidRoomDialog;
import com.live.fox.entity.FlowDataBean;
import com.live.fox.entity.HomeFragmentRoomListBean;
import com.live.fox.entity.LivingGiftBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.entity.SendGiftAmountBean;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.ClassicsFooter;
import com.live.fox.view.MyFlowLayout;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.opensource.svgaplayer.SVGAParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class LivingActivity extends BaseBindingViewActivity implements AppIMManager.OnMessageReceivedListener{

    static final String RoomList="RoomList";
    static final String positionTag="position";
    ActivityLivingBinding mBind;
    LivingFragmentStateAdapter livingFragmentStateAdapter;
    DialogListener dialogListener;
    RecommendLivingAnchorAdapter recommendListAdapter;
    ArrayList<RoomListBean> roomListBeans;
    int pagerPosition;
    boolean isLoop=false;//开启无限循环上下拉
    List<LivingGiftBean> giftListData;//礼物列表;
    List<SendGiftAmountBean> sendGiftAmountBeans;//礼物可发送列表

    public static void startActivity(Context context, List<RoomListBean> roomListBeans,int position)
    {
        if(ClickUtil.isClickWithShortTime(LivingActivity.class.hashCode(),1000))
        {
            return;
        }
        Intent intent=new Intent(context,LivingActivity.class);
        ArrayList<RoomListBean> listBeans=new ArrayList<>();
        for (RoomListBean bean:roomListBeans) {
            listBeans.add(bean);
        }
        intent.putParcelableArrayListExtra(RoomList,listBeans);
        intent.putExtra(positionTag,position);
        context.startActivity(intent);
    }

    @Override
    public boolean isHasHeader() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.ivSideIcon:
                mBind.drawerLayout.closeDrawers();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_living;
    }

    @Override
    public void initView() {
        setWindowsFlag();

        SVGAParser.Companion.shareParser().init(this);
        roomListBeans=getIntent().getParcelableArrayListExtra(RoomList);
        mBind=getViewDataBinding();
        mBind.setClick(this);
        mBind.drawerLayout.setScrimColor(0x00000000);

        mBind.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                mBind.drawerLayout.requestFocus();
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.e("onDrawerStateChanged",newState+" ");
            }
        });

        mBind.drawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        StatusBarUtil.setStatusBarAlpha(this,0,mBind.rlMain);

        mBind.vp2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        mBind.vp2.setOffscreenPageLimit(1);

        setViewPagerAdapter(getIntent().getIntExtra(positionTag,0));

        mBind.vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);



            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                Log.e("onPageScrollSted",state+" ");
                //限制用户滑动得太快 输入法反应不了那么快

                if(state==0 && pagerPosition!=mBind.vp2.getCurrentItem())
                {
                    pagerPosition=mBind.vp2.getCurrentItem();
                    mBind.vp2.setUserInputEnabled(false);

                    int position=mBind.vp2.getCurrentItem();
                    if(livingFragmentStateAdapter.getFragment(position)!=null)
                    {
                        //上中下页面都通知一下 做一些停止的操作
                        if(position-1>-1)
                        {
                            LivingFragment livingFragment=livingFragmentStateAdapter.getFragment(position-1);
                            if(livingFragment!=null)
                            {
                                livingFragment.notifyShow(livingFragmentStateAdapter.getRealPosition(position-1),position);
                            }
                        }

                        if(position+1<Integer.MAX_VALUE)
                        {
                            LivingFragment livingFragment=livingFragmentStateAdapter.getFragment(position+1);
                            if(livingFragment!=null)
                            {
                                livingFragment.notifyShow(livingFragmentStateAdapter.getRealPosition(position+1),position);
                            }
                        }

                        livingFragmentStateAdapter.getFragment(position)
                                .notifyShow(livingFragmentStateAdapter.getRealPosition(position),position);

                    }
                }

            }
        });

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(this));
        mBind.srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                livingFragmentStateAdapter.getFragment(pagerPosition)
                        .getRecommendList();
            }
        });
        mBind.srlRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000,true,false);
            }
        });

        recommendListAdapter=new RecommendLivingAnchorAdapter(this,new ArrayList<>());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.rvRecommendList.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(this,5)));
        mBind.rvRecommendList.setLayoutManager(linearLayoutManager);
        mBind.rvRecommendList.setAdapter(recommendListAdapter);
        recommendListAdapter.setOnItemClickListener(new RecommendLivingAnchorAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, List<RoomListBean> data) {
                mBind.drawerLayout.closeDrawers();
                roomListBeans.clear();
                roomListBeans.addAll(data);
                setViewPagerAdapter(position);
            }
        });

        AppIMManager.ins().addMessageListener(LivingActivity.class, this);
//        showFirstTimeTopUpDialog();
//        showContactCardDialog();
//        showFreeRoomToPrepaidRoom();
        getGiftList();//请求获取礼物
        getAmountListOfGift();//请求获取发送礼物数量列表
    }

    private void setViewPagerAdapter(int currentPosition)
    {
        if(livingFragmentStateAdapter!=null)
        {
            livingFragmentStateAdapter.clearCache();
            livingFragmentStateAdapter=null;
        }
        livingFragmentStateAdapter=new LivingFragmentStateAdapter(this,roomListBeans.size(),isLoop);
        mBind.vp2.setAdapter(livingFragmentStateAdapter);
        if(isLoop)
        {
            pagerPosition=Integer.MAX_VALUE/2+currentPosition;
            mBind.vp2.setCurrentItem(pagerPosition,false);
        }
        else
        {
            pagerPosition=currentPosition;
            mBind.vp2.setCurrentItem(currentPosition,false);
        }
    }

    public void setRecommendListData(List<RoomListBean> list)
    {
        recommendListAdapter.setNewData(list);
    }

    public List<SendGiftAmountBean> getSendGiftAmountBeans() {
        return sendGiftAmountBeans;
    }

    public List<LivingGiftBean> getGiftListData() {
        return giftListData;
    }

    public void scrollRecommendViewToTop()
    {
        mBind.rvRecommendList.getLayoutManager().scrollToPosition(0);
    }

    public void setRecommendFinish(boolean isSuccess)
    {
        mBind.srlRefresh.finishRefresh(isSuccess);
    }


    public ArrayList<RoomListBean> getRoomListBeans() {
        return roomListBeans;
    }

    public DrawerLayout getDrawLayout()
    {
        return mBind.drawerLayout;
    }

    public ViewPager2 getViewPager()
    {
        return mBind.vp2;
    }

    public int getCurrentPosition()
    {
        if(livingFragmentStateAdapter!=null)
        {
            return livingFragmentStateAdapter.getRealPosition(mBind.vp2.getCurrentItem());
        }
        return 0;
    }

    public int getPagerPosition()
    {
        return mBind.vp2.getCurrentItem();
    }

    @Override
    public void onIMReceived(int protocol, String msg) {
        LogUtils.e(protocol + ", onIMReceived msg : " + msg);

        if(livingFragmentStateAdapter!=null )
        {
            LivingFragment livingFragment=livingFragmentStateAdapter.getFragment(mBind.vp2.getCurrentItem());
            if(livingFragment!=null)
            {
                livingFragment.onNewMessageReceived(protocol,msg);
            }
        }
    }

    public interface DialogListener
    {
        void onShowKeyBorad(int height);
        void onDismiss();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void showFirstTimeTopUpDialog()
    {
        FirstTimeTopUpDialog firstTimeTopUpDialog=FirstTimeTopUpDialog.getInstance();
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),firstTimeTopUpDialog);
    }

    private void showContactCardDialog()
    {
        PersonalContactCardDialog personalContactCardDialog=PersonalContactCardDialog.getInstance();
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),personalContactCardDialog);
    }

    private void showFreeRoomToPrepaidRoom()
    {
        FreeRoomToPrepaidRoomDialog dialog=FreeRoomToPrepaidRoomDialog.getInstance();
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),dialog);
    }

    public void setFullscreen(boolean isShowStatusBar, boolean isShowNavigationBar) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (!isShowStatusBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (!isShowNavigationBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        //隐藏标题栏
        // getSupportActionBar().hide();
        setNavigationStatusColor(Color.TRANSPARENT);
    }

    public void setNavigationStatusColor(int color) {
        //VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setNavigationBarColor(color);
            getWindow().setStatusBarColor(color);
        }
    }

    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public void setWindowsFlag()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setAndroidNativeLightStatusBar(this, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppIMManager.ins().removeMessageReceivedListener(LivingActivity.class);
    }

    private void getGiftList()
    {
        Api_Live.ins().getGiftList(0, new JsonCallback<List<LivingGiftBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<LivingGiftBean> data) {
                if(code==0)
                {
                    if(giftListData==null)
                    {
                        giftListData=new ArrayList<>();
                    }
                    if(data!=null)
                    {
                        for (int i = 0; i < data.size(); i++) {
                            LivingGiftBean livingGiftBean=data.get(i);
                            livingGiftBean.setName(livingGiftBean.getName());
                            livingGiftBean.setSelected(false);
                            livingGiftBean.setItemId(livingGiftBean.getId()+"");
                            livingGiftBean.setImgUrl(livingGiftBean.getGitficon());
                            livingGiftBean.setCostDiamond(livingGiftBean.getNeeddiamond());
                            giftListData.add(data.get(i));
                        }
                    }
                }
                else
                {
                    ToastUtils.showShort(msg);
                }

            }
        });
    }

    private void getAmountListOfGift()
    {
        Api_Live.ins().getGiftAmountList( new JsonCallback<List<SendGiftAmountBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<SendGiftAmountBean> data) {
                if(code==0)
                {
                    LivingActivity.this.sendGiftAmountBeans=data;
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
