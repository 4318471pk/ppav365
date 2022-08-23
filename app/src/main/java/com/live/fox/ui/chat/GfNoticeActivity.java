package com.live.fox.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.SysNotice;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.TimeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 官列表
 */
public class GfNoticeActivity extends BaseHeadActivity {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;

    BaseQuickAdapter adapter;
    List<SysNotice> LetterList = new ArrayList<>();

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, GfNoticeActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycle_view_with_refresh);
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        String title = getString(R.string.app_name) + " " + getString(R.string.message);
        setHead(title, true, true);

        refreshLayout = findViewById(R.id.refreshLayout);
        rv = findViewById(R.id.rv_);

        LetterList.addAll(GsonUtil.getObjects(SPUtils.getInstance("sysnotice").getString("data"), SysNotice[].class));
        setRecycleView();
    }

    public void setRecycleView() {
        refreshLayout.setEnablePureScrollMode(true);
        refreshLayout.setBackgroundColor(Color.parseColor("#F0F0F0"));
        rv.setBackgroundColor(Color.parseColor("#F0F0F0"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_chat_receive, new ArrayList<SysNotice>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                SysNotice letter = (SysNotice) item;
                helper.setVisible(R.id.chat_time, true);
                helper.setText(R.id.chat_time, TimeUtils.convertShortTime(letter.getCreateTime()));
                helper.setImageResource(R.id.head_view, R.drawable.gfgg);
                helper.setText(R.id.tv_text, LanguageUtilsEntity.getLanguage(letter.getContent()));
                ((TextView) helper.getView(R.id.tv_text)).setAutoLinkMask(Linkify.ALL);
                ((TextView) helper.getView(R.id.tv_text)).setMovementMethod(LinkMovementMethod.getInstance());
            }
        });

        if (LetterList.size() == 0) {
            ViewGroup viewGroup = (ViewGroup) rv.getParent();
            View emptyView = LayoutInflater.from(this).inflate(R.layout.view_empty, viewGroup, false);
            adapter.setEmptyView(emptyView);
        } else {
            adapter.setNewData(LetterList);
        }

        rv.scrollToPosition(adapter.getItemCount() - 1);
    }
}
