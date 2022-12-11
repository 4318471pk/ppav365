package com.live.fox.ui.home;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.adapter.LocationAreaSelectorAdapter;
import com.live.fox.adapter.NearByPeopleListAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentNearbyPeopleBinding;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.LocationAreaSelectorBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.AssetsUtils;
import com.live.fox.utils.IOUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.luck.picture.lib.tools.DoubleUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearByPeopleFragment extends BaseBindingFragment {

    NearByPeopleListAdapter nearByPeopleListAdapter;
    FragmentNearbyPeopleBinding mBind;
    LocationAreaSelectorAdapter adapter;
    List<LocationAreaSelectorBean> list=new ArrayList<>();
    LocationAreaSelectorBean currentBean;
    public static NearByPeopleFragment newInstance()
    {
        return new NearByPeopleFragment();
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.gtvSelector:
                if(mBind.llDropDownDialog.getVisibility()==View.VISIBLE)
                {
                    mBind.llDropDownDialog.setVisibility(View.GONE);
                    Drawable drawable=getResources().getDrawable(R.drawable.arrow_down);
                    drawable.setBounds(0,0,55,55);
                    mBind.gtvSelector.setCompoundDrawables(null,null,drawable,null);
                }
                else if(mBind.llDropDownDialog.getVisibility()==View.GONE)
                {
                    mBind.llDropDownDialog.setVisibility(View.VISIBLE);
                    Drawable drawable=getResources().getDrawable(R.drawable.arrow_up);
                    drawable.setBounds(0,0,55,55);
                    mBind.gtvSelector.setCompoundDrawables(null,null,drawable,null);
                }
                break;
            case R.id.llDropDownDialog:
                mBind.llDropDownDialog.setVisibility(View.GONE);
                Drawable drawable=getResources().getDrawable(R.drawable.arrow_down);
                drawable.setBounds(0,0,55,55);
                mBind.gtvSelector.setCompoundDrawables(null,null,drawable,null);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden && list.size()<1)
        {
            setLocationList();
        }
        if(currentBean!=null)
        {
            getNearByList(currentBean);
        }
    }

    @Override
    public void onResumeFromPause() {
        super.onResumeFromPause();
        //页面返回数据刷新
        if(currentBean!=null)
        {
            getNearByList(currentBean);
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_nearby_people;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(getActivity()));
        mBind.srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                getNearByList(currentBean);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL, false);
        mBind.rvMain.setLayoutManager(layoutManager);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 2.5f)));

        int dip10= ScreenUtils.getDip2px(getActivity(),10);

        nearByPeopleListAdapter=new NearByPeopleListAdapter(getActivity(),new ArrayList<>());
        mBind.rvMain.setAdapter(nearByPeopleListAdapter);
        nearByPeopleListAdapter.setOnItemClickListener((adapter, v, position) -> {
            if (DoubleUtils.isFastDoubleClick()) return;
            if (nearByPeopleListAdapter.getItem(position) == null) return;
            if (!DataCenter.getInstance().getUserInfo().isLogin()) {
                LoginModeSelActivity.startActivity(requireContext());
                return;
            }

            LivingActivity.startActivity(getActivity(),(ArrayList<RoomListBean>)adapter.getData(),position);
        });
        if(DataCenter.getInstance().getUserInfo().isLogin() && list.size()<1)
        {
            setLocationList();
        }

    }

    private void setLocationList()
    {
        String jsonstr= AssetsUtils.getStringFromAssert(getActivity(),"NearbyAchorLocation.json");
        try {
            JSONArray jsonArray=new JSONArray(jsonstr);
            list.add(new LocationAreaSelectorBean("",-1));//头部
            list.add(new LocationAreaSelectorBean(getStringWithoutContext(R.string.noLimit),0));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.optJSONObject(i);
                LocationAreaSelectorBean bean=new LocationAreaSelectorBean();
                bean.setAreaName(jsonObject.optString("name",""));
                bean.setType(jsonObject.optInt("type",0));
                bean.setSelected(false);
                list.add(bean);
            }
            adapter=new LocationAreaSelectorAdapter(getActivity(),list);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position==0?3:1;
                }
            });
            mBind.rvSelector.setLayoutManager(gridLayoutManager);
            mBind.rvSelector.setAdapter(adapter);

            adapter.setOnLocationSelectedListener(new LocationAreaSelectorAdapter.OnLocationSelectedListener() {
                @Override
                public void onSelected(LocationAreaSelectorBean bean) {
                    mBind.llDropDownDialog.performClick();
                    mBind.gtvSelector.setText(bean.getAreaName());
                    getNearByList(bean);
                }
            });
            mBind.gtvSelector.setText(getStringWithoutContext(R.string.noLimit));
            getNearByList(list.get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNearByList(LocationAreaSelectorBean bean)
    {
        if(bean==null)
        {
            return;
        }
        currentBean=bean;
        String title=bean.getAreaName();
        if(bean.getType()==0)
        {
            title=null;
        }

        Api_Live.ins().nearbyAnchorList(title, bean.getType(), new JsonCallback<List<RoomListBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<RoomListBean> data) {
                if(isActivityOK())
                {
                    mBind.srlRefresh.finishRefresh(code==0);
                    if(code==0)
                    {
                        nearByPeopleListAdapter.setNewData(data);
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }

    @Subscribe(tags = {@Tag(ConstantValue.refreshLive)})
    public void skipRegister(String jumpType) {


        switch (jumpType)
        {
            case "1":
                LogUtils.i("HotAnchorFragment......", "refreshLive onFinish");

                mBind.srlRefresh.autoRefresh();
                break;
        }
    }
}
