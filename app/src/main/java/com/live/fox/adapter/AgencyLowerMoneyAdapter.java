package com.live.fox.adapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.ArrayList;
import java.util.List;

public class AgencyLowerMoneyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


   // LinearLayoutManager layoutManager;
    public AgencyLowerMoneyAdapter(List data) {
        super(R.layout.item_lower_money, data);
        //layoutManager = new LinearLayoutManager(mContext);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {
        List<String> list = new ArrayList<>();
        list.add("1");list.add("1");list.add("1");list.add("1");
        RecyclerView rc = helper.getView(R.id.rc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rc.setLayoutManager(layoutManager);
        AgencyLowerMoneyDetailAdapter adapter = new AgencyLowerMoneyDetailAdapter(list);
        rc.setAdapter(adapter);
    }



    class AgencyLowerMoneyDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder>{

        public AgencyLowerMoneyDetailAdapter(List data) {
            super(R.layout.item_lower_money_detail, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {

        }
    }

}
