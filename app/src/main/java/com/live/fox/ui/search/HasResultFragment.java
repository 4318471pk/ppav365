package com.live.fox.ui.search;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.AnchorSearchListAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentSearchResultBinding;

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

        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");


        adapter = new AnchorSearchListAdapter(getActivity(),list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rvMain.setLayoutManager(layoutManager);
        mBind.rvMain.setAdapter(adapter);

    }
}
