package com.live.fox.ui.home;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.LocationAreaSelectorAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentNearbyPeopleBinding;
import com.live.fox.entity.LocationAreaSelectorBean;
import com.live.fox.utils.device.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class NearByPeopleFragment extends BaseBindingFragment {

    FragmentNearbyPeopleBinding mBind;
    LocationAreaSelectorAdapter adapter;
    List<LocationAreaSelectorBean> list;
    public static NearByPeopleFragment newInstance()
    {
        return new NearByPeopleFragment();
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.gtvSelector:
                mBind.llDropDownDialog.setVisibility(View.VISIBLE);
                break;
            case R.id.llDropDownDialog:
                mBind.llDropDownDialog.setVisibility(View.GONE);
                break;
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

        mBind.gtvSelector.setText("海外");
        int dip10= ScreenUtils.getDip2px(getActivity(),10);

        list=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            LocationAreaSelectorBean bean=new LocationAreaSelectorBean();
            bean.setAreaName("江西");
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

    }
}
