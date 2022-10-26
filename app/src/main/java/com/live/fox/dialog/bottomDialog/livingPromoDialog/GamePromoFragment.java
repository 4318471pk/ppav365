package com.live.fox.dialog.bottomDialog.livingPromoDialog;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.adapter.DialogPromoAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.LayoutListWithRefreshBinding;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;

import java.util.ArrayList;
import java.util.List;

public class GamePromoFragment extends BaseBindingFragment {

    LayoutListWithRefreshBinding mBind;
    DialogPromoAdapter dialogPromoAdapter;

    public static GamePromoFragment getInstance()
    {
        return new GamePromoFragment();
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.layout_list_with_refresh;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(getActivity()));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.rvMain.setLayoutManager(linearLayoutManager);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getActivity(),5)));

        List<String> strings=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("");
        }
        dialogPromoAdapter=new DialogPromoAdapter(getActivity(),strings);
        mBind.rvMain.setAdapter(dialogPromoAdapter);
    }
}
