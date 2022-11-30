package com.live.fox.dialog.bottomDialog.livingPromoDialog;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.live.fox.R;
import com.live.fox.adapter.DialogPromoAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.LayoutListWithRefreshBinding;
import com.live.fox.entity.ActBean;
import com.live.fox.server.Api_Order;
import com.live.fox.ui.h5.H5Activity;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.JumpLinkUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

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
        mBind.srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                getPromoList2();
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.rvMain.setLayoutManager(linearLayoutManager);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getActivity(),5)));

        getPromoList2();
        dialogPromoAdapter=new DialogPromoAdapter(getActivity(),new ArrayList<>());
        dialogPromoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JumpLinkUtils.jumpActivityLinks(getContext(),dialogPromoAdapter.getData().get(position));
            }
        });
        mBind.rvMain.setAdapter(dialogPromoAdapter);
    }

    /**
     *  获取游戏活动
     */
    public void getPromoList2() {
        Api_Order.ins().getAct(2, new JsonCallback<List<ActBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<ActBean> data) {
                boolean isSuccess=false;
                if(isActivityOK())
                {
                    if(code==0)
                    {
                        if(data!=null)
                        {
                            isSuccess=true;
                            dialogPromoAdapter.setNewData(data);
                        }
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                    mBind.srlRefresh.finishRefresh(isSuccess);
                }

            }
        });
    }
}
