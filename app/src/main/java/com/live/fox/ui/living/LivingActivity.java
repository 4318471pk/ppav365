package com.live.fox.ui.living;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.listener.OnKeyboardStateListener;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.view.panel.IPanelView;
import com.live.fox.R;
import com.live.fox.adapter.LivingFragmentStateAdapter;
import com.live.fox.adapter.RecommendLivingAnchorAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.ActivityLivingBinding;
import com.live.fox.dialog.FirstTimeTopUpDialog;
import com.live.fox.dialog.PersonalContactCardDialog;
import com.live.fox.entity.FlowDataBean;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.ClassicsFooter;
import com.live.fox.view.MyFlowLayout;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class LivingActivity extends BaseBindingViewActivity {

    ActivityLivingBinding mBind;
    LivingFragmentStateAdapter livingFragmentStateAdapter;
    PanelSwitchHelper mHelper;
    DialogListener dialogListener;
    RecommendLivingAnchorAdapter recommendListAdapter;

    public static void startActivity(Context context)
    {
        Log.e("startActivity","LivingActivity");
        context.startActivity(new Intent(context,LivingActivity.class));
    }

    @Override
    public boolean isHasHeader() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public void onClickView(View view) {

    }


    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_living;
    }

    @Override
    public void initView() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mBind=getViewDataBinding();
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

        List<String> strings=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add(i+" ");
        }
        livingFragmentStateAdapter=new LivingFragmentStateAdapter(this,strings);
        mBind.vp2.setAdapter(livingFragmentStateAdapter);
        mBind.vp2.setCurrentItem(Integer.MAX_VALUE/2,false);
        mBind.vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                livingFragmentStateAdapter.getFragment(position)
                        .notifyShow(livingFragmentStateAdapter.getRealPosition(position));

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }
        });

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(this));
        mBind.srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(true);
            }
        });
        mBind.srlRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000,true,false);
            }
        });

        List<String> strs=new ArrayList<>();
        for (int i = 0; i <10; i++) {
            strs.add(i+"");
        }
        recommendListAdapter=new RecommendLivingAnchorAdapter(this,strs);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.rvRecommendList.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(this,5)));
        mBind.rvRecommendList.setLayoutManager(linearLayoutManager);
        mBind.rvRecommendList.setAdapter(recommendListAdapter);

        showFirstTimeTopUpDialog();
        showContactCardDialog();
    }



    public DrawerLayout getDrawLayout()
    {
        return mBind.drawerLayout;
    }

    public void setUserScrollAvailAble(boolean isAvailable)
    {
        mBind.vp2.setUserInputEnabled(isAvailable);
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
}
