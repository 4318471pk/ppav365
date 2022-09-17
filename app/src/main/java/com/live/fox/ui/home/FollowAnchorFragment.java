package com.live.fox.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseFragment;
import com.live.fox.utils.device.DeviceUtils;

import org.jetbrains.annotations.NotNull;

public class FollowAnchorFragment extends BaseFragment {

    RecyclerView rvMain;
    public static FollowAnchorFragment newInstance() {
        return new FollowAnchorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_follow_anchor, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMain=view.findViewById(R.id.rvMain);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL, false);
        rvMain.setLayoutManager(layoutManager);
        rvMain.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 4)));

    }
}
