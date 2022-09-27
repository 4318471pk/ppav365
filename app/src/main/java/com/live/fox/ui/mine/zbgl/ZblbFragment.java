package com.live.fox.ui.mine.zbgl;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.ZblbBean;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.ZblbActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ZblbFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView rv;
    private EditText etSearch;
    BaseQuickAdapter adapter;
    private Long uid;


    public static ZblbFragment newInstance(Long uid) {
        ZblbFragment fragment = new ZblbFragment();
        Bundle args = new Bundle();
        args.putLong("uid",uid);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_zbgl, container, false);
        setView(rootView);
        initData(getArguments());
        return rootView;
    }

    public void initData(Bundle arguments) {
        if (arguments != null) {
            uid=arguments.getLong("uid");
            doSearchApi(uid,null);
        }
    }

    public void setView(View bindSource) {
        rv = bindSource.findViewById(R.id.rv_);
        etSearch = bindSource.findViewById(R.id.et_searchhead);
        bindSource.findViewById(R.id.tv_searchhead_search).setOnClickListener(this);
        setRecycleView();
        etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
                return true;
            }
            return false;
        });
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_zblb, new ArrayList<ZblbBean>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                ZblbBean bean = (ZblbBean) item;
                TextView tv_yxb = helper.getView(R.id.tv_yxb);
                helper.setText(R.id.tv_name, bean.getNickName());
                helper.setText(R.id.tv_ud, "ID:"+bean.getUid());
                if (0 == bean.getStartStatus()) {//0下播 1开播
                    tv_yxb.setText(getString(R.string.noliving));
                    tv_yxb.setTextColor(Color.parseColor("#686C7B"));
                } else {
                    tv_yxb.setText(getString(R.string.living));
                    tv_yxb.setTextColor(Color.parseColor("#A63794"));
                }
                GlideUtils.loadDefaultCircleImage(getCtx(), bean.getImg(), helper.getView(R.id.iv_head));
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            ZblbBean bean= (ZblbBean) adapter.getData().get(position);
            ZblbActivity.startActivity(getCtx(),bean.getUid());
        });
    }


    public void doSearch() {
        String content = etSearch.getText().toString().trim();
        if (StringUtils.isEmpty(content)) {
            doSearchApi(uid,null);
            return;
        }
        doSearchApi(uid,content);
    }

    public void doSearchApi(Long uid,String anchorNickName) {
        showLoadingView();
        Api_User.ins().searchAnchor(uid,anchorNickName, new JsonCallback<List<ZblbBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<ZblbBean> data) {
                hideLoadingView();
                if (code == 0) {
                    if (data == null || data.size() == 0) {
                        showEmptyView(getString(R.string.noData));
                    } else {
                        hideEmptyView();
                        adapter.setNewData(data);
                    }
                } else {
                    showEmptyView(msg);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_searchhead_search:
                doSearch();
                break;
        }
    }
}

