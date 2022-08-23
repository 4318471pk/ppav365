package com.live.fox.adapter;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.App;
import com.live.fox.AppConfig;
import com.live.fox.R;
import com.live.fox.entity.response.AgentInfoVO;
import com.live.fox.utils.DensityUtils;

import java.util.List;


public class AgentAdapter extends BaseQuickAdapter<AgentInfoVO, BaseViewHolder> {
    public AgentAdapter(@Nullable List<AgentInfoVO> data) {
        super(R.layout.adapter_agent_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AgentInfoVO item) {
        helper.setText(R.id.tvAgentName, item.getAgentName());
        helper.setText(R.id.tvBalance, mContext.getString(R.string.agencyBalance) + item.getBalance() + AppConfig.getCurrencySymbol());
        if (TextUtils.isEmpty(item.getQq())) {
            helper.getView(R.id.ivQQ).setVisibility(View.INVISIBLE);
        } else {
            helper.getView(R.id.ivQQ).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.ivQQ);
        }
        if (TextUtils.isEmpty(item.getQq())) {
            helper.getView(R.id.ivWechat).setVisibility(View.INVISIBLE);
        } else {
            helper.getView(R.id.ivWechat).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.ivWechat);
        }


    }


    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = DensityUtils.dp2px(App.getInstance(), 8);
        }
    }
}
