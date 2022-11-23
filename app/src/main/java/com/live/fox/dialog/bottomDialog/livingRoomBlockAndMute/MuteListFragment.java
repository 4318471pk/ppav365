package com.live.fox.dialog.bottomDialog.livingRoomBlockAndMute;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.adapter.BlockOrMuteListAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentMuteListBinding;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;

import java.util.ArrayList;
import java.util.List;

public class MuteListFragment extends BaseBindingFragment {

    FragmentMuteListBinding mBind;
    String liveId;

    public static MuteListFragment getInstance(String liveId)
    {
        MuteListFragment fragment=new MuteListFragment();
        fragment.liveId=liveId;
        return fragment;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_mute_list;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        List<String> strings=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            strings.add("");
        }

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(getActivity()));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.rvMain.setLayoutManager(linearLayoutManager);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(ScreenUtils.dp2px(getActivity(),5)));
        mBind.rvMain.setAdapter(new BlockOrMuteListAdapter(getActivity(),strings));
        getMuteList();
    }

    private void getMuteList()
    {
        Api_Live.ins().getLivingMuteList(liveId, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                Log.e("getMuteList",data);
            }
        });
    }
}
