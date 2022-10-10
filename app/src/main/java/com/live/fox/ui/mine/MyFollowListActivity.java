package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.MyFollowListAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityMyFollowListBinding;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class MyFollowListActivity extends BaseBindingViewActivity {

    ActivityMyFollowListBinding mBind;
    boolean isFans = false;

    MyFollowListAdapter myFollowListAdapter;



    public static void startActivity(Context context, boolean isFans) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MyFollowListActivity.class);
        i.putExtra("isFans", isFans);
        context.startActivity(i);

    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_my_follow_list;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        isFans = this.getIntent().getBooleanExtra("isFans", isFans);

        setActivityTitle(isFans? getString(R.string.myFan) : getString(R.string.myFocus));


        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");


        myFollowListAdapter = new MyFollowListAdapter(list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rv.setLayoutManager(layoutManager);
        mBind.rv.setAdapter(myFollowListAdapter);

    }


}
