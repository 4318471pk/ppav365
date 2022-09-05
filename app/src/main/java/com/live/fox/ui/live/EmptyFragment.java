package com.live.fox.ui.live;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.live.fox.R;


/**
 * User: yc
 * Date: 2016/3/27
 * Time: 15:02
 * 直播界面中的空白页面
 */
public class EmptyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_framelayout, container, false);
    }
}
