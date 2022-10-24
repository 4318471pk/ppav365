package com.live.fox.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundLinearLayout;
import com.live.fox.R;
import com.live.fox.entity.ColumnBean;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ScreenUtils;

import java.util.List;

public class AgencyColumnAdapter extends BaseQuickAdapter<ColumnBean, BaseViewHolder> {

    private float max = 0;

    private float itemSpe = 0;

    public AgencyColumnAdapter(List data) {
        super(R.layout.item_agency_column, data);
    }


    public void setItemNum(float item) {
        this.itemSpe = item;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, ColumnBean data) {

        RoundLinearLayout viewColumn = helper.getView(R.id.view_column);
        if (itemSpe > 0) {
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) viewColumn.getLayoutParams(); //取控件textView当前的布局参数
            linearParams.width = ScreenUtils.dp2px(mContext, 25);
            float height = ScreenUtils.dp2px(mContext,20) * (data.getColumn() / itemSpe);
            LogUtils.e("除法" + itemSpe, "item: "+ height + "  " + data.getColumn());
            if (height < 1) {
                linearParams.height = 1;
            } else {
                linearParams.height = (int) height;
            }

            viewColumn.setLayoutParams(linearParams);
        }



    }




}
