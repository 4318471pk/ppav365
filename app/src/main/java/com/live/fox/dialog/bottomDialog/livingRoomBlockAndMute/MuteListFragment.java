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
import com.live.fox.entity.BlackOrMuteListItemBean;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;

import java.util.ArrayList;
import java.util.List;

public class MuteListFragment extends BaseBindingFragment {

    FragmentMuteListBinding mBind;
    String liveId;
    BlockOrMuteListAdapter blockOrMuteListAdapter;

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

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(getActivity()));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.rvMain.setLayoutManager(linearLayoutManager);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(ScreenUtils.dp2px(getActivity(),5)));
        blockOrMuteListAdapter=new BlockOrMuteListAdapter(getActivity(),new ArrayList<>());
        blockOrMuteListAdapter.setOnClickRemoveListener(new BlockOrMuteListAdapter.OnClickRemoveListener() {
            @Override
            public void onClickFollow(BlackOrMuteListItemBean bean) {
                    remove(bean);
            }
        });
        mBind.rvMain.setAdapter(blockOrMuteListAdapter);
        getMuteList();
    }

    private void getMuteList()
    {
        Api_Live.ins().getLivingMuteList(liveId, new JsonCallback<List<BlackOrMuteListItemBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BlackOrMuteListItemBean> data) {
                if(isActivityOK())
                {
                    String temple=getStringWithoutContext(R.string.amountOfMutes);
                    if(code==0)
                    {
                        if(data!=null && data.size()>0)
                        {
                            blockOrMuteListAdapter.setNewData(data);
                            mBind.rlEmptyDataView.setVisibility(View.GONE);
                            mBind.srlRefresh.setVisibility(View.VISIBLE);
                            mBind.tvAmount.setText(String.format(temple,data.size()+""));
                        }
                        else
                        {
                            mBind.rlEmptyDataView.setVisibility(View.VISIBLE);
                            mBind.srlRefresh.setVisibility(View.GONE);
                            mBind.tvAmount.setText(String.format(temple,"0"));
                        }
                    }
                    else
                    {
                        ToastUtils.showShort(msg);

                    }
                }
            }
        });
    }

    private void remove(BlackOrMuteListItemBean bean)
    {
        showLoadingDialogWithNoBgBlack();
        Api_Live.ins().blackChat(liveId, bean.getUid(), false, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(isActivityOK())
                {
                    hideLoadingDialog();
                    if(code==0)
                    {
                        getMuteList();
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }
}
