package com.live.fox.ui.lottery.adapter;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.utils.ScreenUtils;

import java.util.List;

public class KaiJiangResultTvAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    int tvSize = 13;

    public KaiJiangResultTvAdapter(List data) {
        super(R.layout.item_kaijiang_result_tv, data);
    }

    public void setTvSize(int size) {
        this.tvSize = size;
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {
        TextView tv = helper.getView(R.id.tv);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,tvSize);

    }

}
