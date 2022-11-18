package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.MyBagAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityMybagBinding;
import com.live.fox.entity.MyBagListItemBean;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;

import java.util.ArrayList;
import java.util.List;

public class MyBagActivity extends BaseBindingViewActivity {

    ActivityMybagBinding mBind;
    MyBagAdapter myBagAdapter;
    List<MyBagListItemBean> beans;

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,MyBagActivity.class));
    }
    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_mybag;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setActivityTitle(R.string.store_package);

        GridLayoutManager grid = new GridLayoutManager(this, 2);
        RecyclerSpace recyclerSpace = new RecyclerSpace(ScreenUtils.getDip2px(this, 5));
        mBind.rvMain.addItemDecoration(recyclerSpace);
        mBind.rvMain.setLayoutManager(grid);
        mBind.layoutSmartRefresh.setRefreshHeader(new MyWaterDropHeader(this));

        beans=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyBagListItemBean myBagStoreListItemBean = new MyBagListItemBean();
            myBagStoreListItemBean.setDes("中文啊啊啊");
            myBagStoreListItemBean.setName("布加迪威龙哦");
            beans.add(myBagStoreListItemBean);
        }
        setMyBagAdapter();
        myBagAdapter.setNewData(beans);
    }

    private void setMyBagAdapter()
    {
        if(myBagAdapter ==null)
        {
            myBagAdapter =new MyBagAdapter();
            mBind.rvMain.setAdapter(myBagAdapter);
        }
        else
        {
            myBagAdapter.notifyDataSetChanged();
        }
    }
}
