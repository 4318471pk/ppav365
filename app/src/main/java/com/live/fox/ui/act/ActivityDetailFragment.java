package com.live.fox.ui.act;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.adapter.ActAdapter;
import com.live.fox.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ActivityDetailFragment extends BaseFragment {

    RecyclerView rc;
    private List<String> data = new ArrayList<>();
    ActAdapter mAdapter;

    boolean isGame = true;

    public static ActivityDetailFragment newInstance(boolean isGame) {
        ActivityDetailFragment fragment = new ActivityDetailFragment();
        Bundle args = new Bundle();
        args.putBoolean("isGame", isGame);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_refresh_recycler_view, container, false);
        setView(rootView);
        return rootView;
    }


    private void setView(View bindSource) {
        isGame = getArguments().getBoolean("isGame");
        rc = bindSource.findViewById(R.id.refresh_recycler_view);
        data.add("1");data.add("1");data.add("1");data.add("1");data.add("1");
        mAdapter = new ActAdapter(data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rc.setLayoutManager(layoutManager);
        rc.setAdapter(mAdapter);
    }

}
