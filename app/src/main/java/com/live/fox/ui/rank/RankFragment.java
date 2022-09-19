package com.live.fox.ui.rank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.Rank;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.ui.mine.activity.UserDetailActivity;
import com.live.fox.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;



public class RankFragment extends BaseFragment {

    public static RankFragment newInstance(int type, int rankType) {
        RankFragment fragment = new RankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rank_fragment, null, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(getArguments());
    }

    public void initData(Bundle bundle) {
        String[] stringArray = getResources().getStringArray(R.array.rank_tab);


    }


}

