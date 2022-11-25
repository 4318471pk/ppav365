package com.live.fox.ui.search;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.AnchorSearchListAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentSearchResultBinding;
import com.live.fox.entity.SearchAnchorBean;
import com.live.fox.server.Api_User;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class HasResultFragment extends BaseBindingFragment {

    AnchorSearchListAdapter adapter;
    FragmentSearchResultBinding mBind;

    public static HasResultFragment getInstance()
    {
        return new HasResultFragment();
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        adapter = new AnchorSearchListAdapter(getActivity(),new ArrayList<>());
        adapter.setOnClickFollowListener(new AnchorSearchListAdapter.OnClickFollowListener() {
            @Override
            public void onClickFollow(SearchAnchorBean bean) {
                follow(bean.getUid());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rvMain.setLayoutManager(layoutManager);
        mBind.rvMain.setAdapter(adapter);

    }

    public void setData(List<SearchAnchorBean> list)
    {

        if(list!=null && list.size()>0)
        {
            adapter.setNewData(list);
        }
        else
        {
            adapter.setNewData(new ArrayList<>());
        }
    }

    private SearchAnchorActivity getMainActivity()
    {
        return (SearchAnchorActivity)getActivity();
    }

    private void follow(String targetId)
    {
        showLoadingDialogWithNoBgBlack();
        Api_User.ins().followUser(targetId, true, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if(isActivityOK())
                {
                    if(code==0)
                    {
                        getMainActivity().mBind.gtvSearch.performClick();
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
