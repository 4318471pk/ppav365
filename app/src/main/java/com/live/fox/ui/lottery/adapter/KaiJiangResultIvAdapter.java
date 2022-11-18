package com.live.fox.ui.lottery.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class KaiJiangResultIvAdapter extends BaseQuickAdapter<String, BaseViewHolder> {



    public KaiJiangResultIvAdapter(List data) {
        super(R.layout.item_kaijiang_result_iv, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String data) {
        ImageView iv = helper.getView(R.id.iv);
        if (data.equals("1")) {
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.game1));
        } else if (data.equals("2")) {
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.game2));
        } else if (data.equals("3")) {
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.game3));
        } else if (data.equals("4")) {
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.game4));
        } else if (data.equals("5")) {
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.game5));
        } else if (data.equals("6")) {
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.game6));
        }

    }

}
