package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.roundview.RoundRelativeLayout;
import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.adapter.OnlineUserOrNobilityListAdapter;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogOnlineUserNobilityBinding;
import com.live.fox.entity.OnlineUserBean;
import com.live.fox.entity.User;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.mine.noble.NobleActivity;
import com.live.fox.ui.rank.RankFragment;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OnlineNobilityAndUserDialog extends BaseBindingDialogFragment {

    DialogOnlineUserNobilityBinding mBind;
    List<String> strings=new ArrayList<>();
    List<View> views=new ArrayList<>();
    String amount,liveId;
    List<OnlineUserBean> userList=new ArrayList<>();
    List<OnlineUserBean> vipUserList=new ArrayList<>();
    DataChangeListener dataChangeListener;

    public static OnlineNobilityAndUserDialog getInstance(String amount, String liveId, List<OnlineUserBean> userList, List<OnlineUserBean> vipUserList)
    {
        OnlineNobilityAndUserDialog dialog=new OnlineNobilityAndUserDialog();
        dialog.amount=amount;
        dialog.liveId=liveId;
        if(userList!=null && userList.size()>0)
        {
            dialog.userList.clear();
            dialog.userList.addAll(userList);
        }

        if(vipUserList!=null && vipUserList.size()>0)
        {
            dialog.vipUserList.clear();
            dialog.vipUserList.addAll(vipUserList);
        }
        return dialog;
    }

    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
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
        getDialog().getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        //???????????????
        // getSupportActionBar().hide();
        setNavigationStatusColor(Color.TRANSPARENT);
    }

    public void setNavigationStatusColor(int color) {
        //VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        if (Build.VERSION.SDK_INT >= 21) {
            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getDialog().getWindow().setNavigationBarColor(color);
            getDialog().getWindow().setStatusBarColor(color);
        }
    }

    private  void setAndroidNativeLightStatusBar( boolean dark) {
        View decor =getDialog().getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

//        //??????dialog?????????????????????
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //??????dialog??????????????????
        getDialog().getWindow().setDimAmount(0);
        setFullscreen(true, true);
        setAndroidNativeLightStatusBar( true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.rllContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.rllContent,false);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_online_user_nobility;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        doGetAudienceListApi();
        doGetVipAudienceListApi();
        view.setVisibility(View.GONE);
        String str1=getResources().getString(R.string.onlineNobility);
        String str2=getResources().getString(R.string.onlineUser);
        strings.add(str1);
        strings.add(str2);

        View first=View.inflate(getActivity(),R.layout.layout_online_nobility_user,null);
        first.findViewById(R.id.tvGoNoble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NobleActivity.startActivity(getActivity());
            }
        });
        View second=View.inflate(getActivity(),R.layout.layout_online_nobility_user,null);
        views.add(first);
        views.add(second);

        int widthScreen=ScreenUtils.getScreenWidth(getActivity());
        mBind.tabLayout.setGradient(0xffA800FF,0xffEA00FF);
        mBind.vpMain.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return strings.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, Object view) {
                ((ViewPager) container).removeView((View) view);
            }

            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return strings.get(position);
            }

            @NonNull
            public Object instantiateItem(ViewGroup container, int position) {
                switch (position)
                {
                    case 0:
                        initNobilityList();
                        break;
                    case 1:
                        initUserList();
                        break;
                }
                container.addView(views.get(position));
                return views.get(position);
            }

        });

        mBind.tabLayout.setTabWidthPX(widthScreen/2);
        mBind.tabLayout.setViewPager(mBind.vpMain);
        mBind.vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int screenHeight=ScreenUtils.getScreenHeight(getActivity());
        mBind.rllContent.getLayoutParams().height=(int)(screenHeight*0.57f);

        view.setVisibility(View.VISIBLE);
        startAnimate(mBind.rllContent,true);
    }

    private void initNobilityList()
    {
        View view=views.get(0);
        RoundRelativeLayout rrl=view.findViewById(R.id.rrlTopView);
        LinearLayout linearLayout=view.findViewById(R.id.llEmptyData);
        RecyclerView rvMain=view.findViewById(R.id.rvMain);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvMain.setLayoutManager(linearLayoutManager);

        if(vipUserList.size()==0)
        {
            linearLayout.setVisibility(View.VISIBLE);
            rvMain.setVisibility(View.GONE);
        }
        else
        {
            linearLayout.setVisibility(View.GONE);
            rvMain.setVisibility(View.VISIBLE);
        }

    }

    private void initUserList()
    {
        View view=views.get(1);
        RoundRelativeLayout rrl=view.findViewById(R.id.rrlTopView);
        LinearLayout linearLayout=view.findViewById(R.id.llEmptyData);
        RecyclerView rvMain=view.findViewById(R.id.rvMain);
        rrl.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvMain.setLayoutManager(linearLayoutManager);

        if(userList.size()==0)
        {
            linearLayout.setVisibility(View.VISIBLE);
            rvMain.setVisibility(View.GONE);
        }
        else
        {
            linearLayout.setVisibility(View.GONE);
            rvMain.setVisibility(View.VISIBLE);
        }

    }

    private void setData()
    {
        View view=views.get(mBind.vpMain.getCurrentItem());
        LinearLayout linearLayout=view.findViewById(R.id.llEmptyData);
        RecyclerView rvMain=view.findViewById(R.id.rvMain);
        List<OnlineUserBean> tempList=mBind.vpMain.getCurrentItem()==0?vipUserList:userList;

        if(tempList.size()==0)
        {
            linearLayout.setVisibility(View.VISIBLE);
            rvMain.setVisibility(View.GONE);
        }
        else
        {
            linearLayout.setVisibility(View.GONE);
            rvMain.setVisibility(View.VISIBLE);
        }

        if(rvMain.getAdapter()==null)
        {
            rvMain.setAdapter(new OnlineUserOrNobilityListAdapter(getActivity(),tempList));
        }
        else
        {
            OnlineUserOrNobilityListAdapter adapter=(OnlineUserOrNobilityListAdapter)rvMain.getAdapter();
            adapter.setNewData(tempList);
        }
    }

    /**
     * ????????????
     */
    public void doGetAudienceListApi() {
        if(!isConditionOk())
        {
            return;
        }

        Api_Live.ins().getRoomUserList(liveId, new JsonCallback<List<OnlineUserBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<OnlineUserBean> data) {
                if (code == 0 ) {
                    if(isConditionOk() && getArg().equals(liveId) && data!=null && data.size()>0)
                    {
                        userList.clear();
                        userList.addAll(data);
                        if(dataChangeListener!=null)
                        {
                            dataChangeListener.onChange(userList,null);
                        }
                        initUserList();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * ????????????
     */
    public void doGetVipAudienceListApi() {
        if(!isConditionOk())
        {
            return;
        }

        Api_Live.ins().getRoomVipList(liveId, new JsonCallback<List<OnlineUserBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<OnlineUserBean> data) {
                if (code == 0 ) {
                    if(isConditionOk() && getArg().equals(liveId) && data!=null)
                    {
                        vipUserList.clear();
                        vipUserList.addAll(data);
                        if(dataChangeListener!=null)
                        {
                            dataChangeListener.onChange(null,vipUserList);
                        }
                        setData();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    public interface DataChangeListener
    {
        void onChange(List<OnlineUserBean> userList,List<OnlineUserBean> vipUserList);
    }
}
