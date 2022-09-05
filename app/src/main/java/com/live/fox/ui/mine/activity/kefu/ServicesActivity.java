package com.live.fox.ui.mine.activity.kefu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.MvpBaseHeadActivity;
import com.live.fox.entity.Kefu;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 客服服务
 */
public class ServicesActivity extends MvpBaseHeadActivity<KefuContract.Presenter>
        implements KefuContract.View {

    RecyclerView rv;

    BaseQuickAdapter adapter;

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, ServicesActivity.class);
        context.startActivity(i);
    }

    @Override
    protected KefuContract.Presenter bindPresenter() {
        return new KefuPrestener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kefu_activity);

        initView();
    }

    private void initView() {
        rv = findViewById(R.id.rv_);
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.customerService), true, true);

        initRecycleView();
        mPresenter.loadData();
    }

    public void initRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(ServicesActivity.this, 3);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(this, 6)));
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_kefu, new ArrayList<Kefu>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Kefu kefu = (Kefu) item;
                String jsonStr = kefu.getNickname();
                if (!TextUtils.isEmpty(jsonStr) &&
                        jsonStr.startsWith("{") &&
                        jsonStr.endsWith("}")) {
                    String language = LanguageUtilsEntity.getLanguage(new Gson().fromJson(jsonStr, LanguageUtilsEntity.class));
                    helper.setText(R.id.tv_name, language);
                } else {
                    helper.setText(R.id.tv_name, jsonStr);
                }
                GlideUtils.loadImage(mContext, kefu.getIcon(), helper.getView(R.id.iv_));
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Kefu kefu = (Kefu) adapter.getItem(position);
            assert kefu != null;
            if (kefu.getType() == 2) {
                mPresenter.toBrowserByKefu(kefu.getUrl());
            } else {
                mPresenter.openQQ(kefu.getCustomId() + "", this);
            }
        });
    }

    @Override
    public void onGetServiceListSuccess(List<Kefu> data) {
        adapter.setNewData(data);
    }

}
