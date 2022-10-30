package com.live.fox.ui.living;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.TimeCounter;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.MyFlowLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OVER_SCROLL_NEVER;

public class LivingFragment extends BaseBindingFragment {

    int currentPagePosition;
    FragmentLivingBinding mBind;
    LivingControlPanel livingControlPanel;
    LivingMsgBoxAdapter livingMsgBoxAdapter;
    List<LivingMsgBoxBean> livingMsgBoxBeans=new ArrayList<>();

    TimeCounter.TimeListener timeListener=new TimeCounter.TimeListener(5) {
        @Override
        public void onSecondTick(TimeCounter.TimeListener listener) {
            super.onSecondTick(listener);
            if(isAdded() && getResources()!=null)
            {
                LivingMsgBoxBean bean=new LivingMsgBoxBean();
                bean.setBackgroundColor(0xffBDA3C8);
                bean.setStrokeColor(0xff9E3FD4);
                SpanUtils spanUtils=new SpanUtils();
                spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(78,getContext()));
                spanUtils.append(System.currentTimeMillis()+" ");

                bean.setCharSequence(spanUtils.create());
                addNewMessage(bean);
            }
        }

        @Override
        public void onConditionTrigger(TimeCounter.TimeListener listener) {
            super.onConditionTrigger(listener);

        }
    };

    public static LivingFragment getInstance(int position)
    {
        Log.e("LivingFragment",position+" ");
        LivingFragment livingFragment=new LivingFragment();
        livingFragment.currentPagePosition=position;
        return livingFragment;
    }

    public void notifyShow(int position)
    {
        Log.e("LivingFragment22",position+" ");
        if(getView()!=null && isAdded())
        {
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
        mBind=getViewDataBinding();
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TimeCounter.getInstance().remove(timeListener);
    }

    private void initView()
    {
        LivingActivity activity=(LivingActivity) getActivity();
        //是当前页才加载数据 不然就算了
        if(activity.getCurrentPosition()==currentPagePosition)
        {
            loadData();
            TimeCounter.getInstance().add(timeListener);
        }
        else
        {
            GlideUtils.loadDefaultImage(activity,activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(),
                    mBind.ivBG);
        }
    }

    private void loadData()
    {
        LivingActivity activity=(LivingActivity) getActivity();
        if(activity.isFinishing() || activity.isDestroyed())
        {
            return;
        }

        GlideUtils.loadDefaultImage(activity,activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(),
                mBind.ivBG);

        if(activity.getCurrentPosition()==currentPagePosition)
        {
            getRecommendList();
            addViewPage();
            TimeCounter.getInstance().add(timeListener);
            mBind.viewPager.setVisibility(View.VISIBLE);
            //如果刷新了主播的信息 设置可以滑动 但是如果消息框在的话不能设置
            livingControlPanel.viewWatch.setScrollEnable(true);

            if(livingControlPanel!=null)
            {
                livingControlPanel.viewWatch.hideInputLayout();
            }
        }
        else
        {
            mBind.viewPager.setVisibility(View.GONE);
            if(livingMsgBoxAdapter!=null && livingMsgBoxAdapter.getBeans()!=null)
            {
                livingMsgBoxAdapter.getBeans().clear();
                livingMsgBoxAdapter.notifyDataSetChanged();
            }
            TimeCounter.getInstance().remove(timeListener);
        }

    }

    private void addViewPage()
    {
        LivingActivity activity=(LivingActivity) getActivity();

        if(mBind.viewPager.getAdapter()!=null)
        {
            return;
        }
        livingControlPanel= new LivingControlPanel(LivingFragment.this,mBind.viewPager);

        mBind.viewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        mBind.viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
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


        mBind.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position>0)
                {
                    activity.getDrawLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
                }
                else
                {
                    activity.getDrawLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBind.viewPager.setCurrentItem(1);
    }

    private void addNewMessage(LivingMsgBoxBean bean)
    {
        if(livingMsgBoxAdapter==null)
        {
            livingMsgBoxAdapter=new LivingMsgBoxAdapter(getContext(),livingMsgBoxBeans);
            livingControlPanel.mBind.msgBox.setAdapter(livingMsgBoxAdapter);
        }
        livingMsgBoxAdapter.getBeans().add(bean);
        livingMsgBoxAdapter.notifyDataSetChanged();
    }

    private void getRecommendList()
    {
        Api_Live.ins().getRecommendLiveList(new JsonCallback<HomeFragmentRoomListBean>() {
            @Override
            public void onSuccess(int code, String msg, HomeFragmentRoomListBean data) {

            }
        });
    }
}
