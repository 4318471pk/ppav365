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
import com.live.fox.databinding.FragmentBlockListBinding;
import com.live.fox.entity.BlackOrMuteListItemBean;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;

import java.util.ArrayList;
import java.util.List;

public class BlockListFragment extends BaseBindingFragment {

    FragmentBlockListBinding mBind;
    String liveId;
    BlockOrMuteListAdapter blockOrMuteListAdapter;

    public static BlockListFragment getInstance(String liveId)
    {
        BlockListFragment blockListFragment=new BlockListFragment();
        blockListFragment.liveId=liveId;
        return blockListFragment;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_block_list;
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
        getBlockList();
    }

    private void getBlockList()
    {
        Api_Live.ins().getLivingBlackList(liveId, new JsonCallback<List<BlackOrMuteListItemBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BlackOrMuteListItemBean> data) {
                Log.e("getBlockList",data+" ");
                if(isActivityOK())
                {
                    if(code==0)
                    {
                        String temple=getStringWithoutContext(R.string.amountOfBlock);
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
        Api_Live.ins().removeLivingBlackOrMuteUser(liveId,bean.getUid(),new JsonCallback<String>(){
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(isActivityOK())
                {
                    hideLoadingDialog();
                    if(code==0)
                    {
                        getBlockList();
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
