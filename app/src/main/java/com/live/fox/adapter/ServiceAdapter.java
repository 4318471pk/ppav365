package com.live.fox.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.ServiceItemBean;

import java.util.List;

public class ServiceAdapter extends BaseQuickAdapter<ServiceItemBean, BaseViewHolder> {

    Context context;

    public ServiceAdapter(Context context,List<ServiceItemBean> data) {
        super(R.layout.item_service, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceItemBean data) {

        helper.setImageDrawable(R.id.ivLogo,context.getResources().getDrawable(data.getResourceId()));
        helper.setText(R.id.tvService,data.getTitle());
        helper.setText(R.id.tvServiceTips,data.getDetail());

    }


}
