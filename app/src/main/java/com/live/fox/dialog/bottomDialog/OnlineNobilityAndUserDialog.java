package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.live.fox.databinding.DialogOnlineUserNobilityBinding;
import com.live.fox.ui.rank.RankFragment;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OnlineNobilityAndUserDialog extends BaseBindingDialogFragment {

    DialogOnlineUserNobilityBinding mBind;
    List<String> strings=new ArrayList<>();
    List<View> views=new ArrayList<>();
    String amount;
    OnlineUserOrNobilityListAdapter adapter;
    List<String> datas=new ArrayList<>();


    public static OnlineNobilityAndUserDialog getInstance(String amount)
    {
        OnlineNobilityAndUserDialog dialog=new OnlineNobilityAndUserDialog();
        dialog.amount=amount;
        return dialog;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
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

        view.setVisibility(View.GONE);
        String str1=getResources().getString(R.string.onlineNobility);
        String str2=getResources().getString(R.string.onlineUser);
        strings.add(str1);
        strings.add(str2);

        View first=View.inflate(getActivity(),R.layout.layout_online_nobility_user,null);
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
                switch (position)
                {
                    case 0:
                        setNobilityData();
                        break;
                    case 1:
                        setUserData();
                        break;
                }
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
        RecyclerView rvMain=view.findViewById(R.id.rvMain);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvMain.setLayoutManager(linearLayoutManager);
        rrl.setVisibility(View.VISIBLE);
        rvMain.setVisibility(View.GONE);

    }

    private void initUserList()
    {
        View view=views.get(1);
        RoundRelativeLayout rrl=view.findViewById(R.id.rrlTopView);
        LinearLayout linearLayout=view.findViewById(R.id.llEmptyData);
        RecyclerView rvMain=view.findViewById(R.id.rvMain);
        rrl.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvMain.setLayoutManager(linearLayoutManager);
        rvMain.setVisibility(View.VISIBLE);

    }

    private void setNobilityData()
    {

    }

    private void setUserData()
    {
        View view=views.get(1);
        RecyclerView rvMain=view.findViewById(R.id.rvMain);
        datas.clear();
        for (int i = 0; i < 50; i++) {
            datas.add("");
        }
        if(adapter==null)
        {
            adapter=new OnlineUserOrNobilityListAdapter(getActivity(),datas);
        }
        else
        {
            adapter.setNewData(datas);
        }
        rvMain.setAdapter(adapter);

    }
}
