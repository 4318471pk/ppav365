package com.live.fox.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.adapter.HeaderHorListAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.entity.HomeBanner;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.RoomListBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.view.convenientbanner.ConvenientBanner;
import com.live.fox.view.convenientbanner.holder.Holder;
import com.marquee.dingrui.marqueeviewlib.MarqueeView;

import java.util.ArrayList;
import java.util.List;

public class LiveListHeader extends RelativeLayout {

    MarqueeView ivHomeHotRecommendRolling;
    MarqueeView ivHomePlayGameRolling;
    RecyclerView hsPlayGameList;
    RecyclerView hsHotRecommendList;
    List<RoomListBean> gameLists=new ArrayList<>();
    List<RoomListBean> AdultLists=new ArrayList<>();
    List<HomeBanner> homeBanners=new ArrayList<>();
    LinearLayout llHot;
    LinearLayout llGame;

    public LiveListHeader(Context context) {
        super(context);
        initView();
    }

    public LiveListHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LiveListHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setGameLists(List<RoomListBean> gameLists) {
        this.gameLists.clear();
        if(gameLists!=null && gameLists.size()>0)
        {
            this.gameLists.addAll(gameLists);
            if(hsPlayGameList.getAdapter()!=null)
            {
                llGame.setVisibility(VISIBLE);
                HeaderHorListAdapter headerHorListAdapter=(HeaderHorListAdapter) hsPlayGameList.getAdapter();
                headerHorListAdapter.setNewData(this.gameLists);
            }
        }
        else
        {
            llGame.setVisibility(GONE);
        }


    }

    //hsHotRecommendList  llHot
    public void setAdultLists(List<RoomListBean> adultLists) {
        this.AdultLists.clear();
        if(adultLists!=null && adultLists.size()>0)
        {
            this.AdultLists.addAll(adultLists);
            if(hsHotRecommendList.getAdapter()!=null)
            {
                llHot.setVisibility(VISIBLE);
                HeaderHorListAdapter headerHorListAdapter=(HeaderHorListAdapter) hsHotRecommendList.getAdapter();
                headerHorListAdapter.setNewData(this.AdultLists);
            }
        }
        else
        {
            llHot.setVisibility(GONE);
        }

    }

    private void initView()
    {
        addView(View.inflate(getContext(), R.layout.fragment_header_livelist,null));
        hsHotRecommendList=findViewById(R.id.hsHotRecommendList);
        hsPlayGameList=findViewById(R.id.hsPlayGameList);
        ivHomePlayGameRolling=findViewById(R.id.ivHomePlayGameRolling);
        ivHomeHotRecommendRolling=findViewById(R.id.ivHomeHotRecommendRolling);


        llHot=findViewById(R.id.llHot);
        llGame=findViewById(R.id.llGame);

        int dip5=ScreenUtils.dp2px(getContext(),5);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        hsHotRecommendList.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        hsPlayGameList.setLayoutManager(linearLayoutManager2);
        hsHotRecommendList.addItemDecoration(new RecyclerSpace(dip5,RecyclerSpace.TypeLeft));
        hsPlayGameList.addItemDecoration(new RecyclerSpace(dip5,RecyclerSpace.TypeLeft));

        HeaderHorListAdapter adultAdapter=new HeaderHorListAdapter(getContext(),AdultLists);
        hsHotRecommendList.setAdapter(adultAdapter);

        HeaderHorListAdapter gameAdapter=new HeaderHorListAdapter(getContext(),gameLists);
        hsPlayGameList.setAdapter(gameAdapter);

        adultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toLiveRoom((ArrayList<RoomListBean>)adapter.getData(),position);
            }
        });
        gameAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toLiveRoom((ArrayList<RoomListBean>)adapter.getData(),position);
            }
        });

    }

    //跳往直播间
    public void toLiveRoom(ArrayList<RoomListBean> roomListBeans,int position) {
        if (!DataCenter.getInstance().getUserInfo().isLogin()) {
            LoginModeSelActivity.startActivity(getContext());
            return;
        }

        LivingActivity.startActivity(getContext(),roomListBeans,position);
    }


}
