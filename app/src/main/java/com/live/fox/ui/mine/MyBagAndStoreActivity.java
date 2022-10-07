package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.MyBagAndStoreAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityMybagStoreBinding;
import com.live.fox.entity.MyBagStoreListItemBean;
import com.live.fox.utils.device.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class MyBagAndStoreActivity extends BaseBindingViewActivity {

    ActivityMybagStoreBinding mBind;
    MyBagAndStoreAdapter myBagAndStoreAdapter;
    List<MyBagStoreListItemBean> beans;

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context, MyBagAndStoreActivity.class));
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_mybag_store;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setActivityTitle(R.string.store_package);

        GridLayoutManager grid = new GridLayoutManager(this, 2);
        RecyclerSpace recyclerSpace = new RecyclerSpace(ScreenUtils.getDip2px(this, 5));
        mBind.rvMain.addItemDecoration(recyclerSpace);
        mBind.rvMain.setLayoutManager(grid);

        beans=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyBagStoreListItemBean myBagStoreListItemBean = new MyBagStoreListItemBean();
            myBagStoreListItemBean.setDes("中文啊啊啊");
            myBagStoreListItemBean.setName("布加迪威龙哦");
            myBagStoreListItemBean.setUsing(i%2==0);
            myBagStoreListItemBean.setPurchased(i%3==0);
            beans.add(myBagStoreListItemBean);
        }
        setMyBagAdapter();

    }

    private void setMyBagAdapter()
    {
        if(myBagAndStoreAdapter ==null)
        {
            myBagAndStoreAdapter =new MyBagAndStoreAdapter(this,beans);
            mBind.rvMain.setAdapter(myBagAndStoreAdapter);
        }
        else
        {
            myBagAndStoreAdapter.notifyDataSetChanged();
        }
    }

}
