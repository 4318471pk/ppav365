package com.live.fox.ui.lottery.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/*
* 一分六合彩开奖
* */
public class KaiJiangRecordYflhcAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private boolean isNiuniu = false;

    public KaiJiangRecordYflhcAdapter(List data, boolean isNiuniu) {
        super(R.layout.item_kaijiang_record_yflhc, data);
        this.isNiuniu = isNiuniu;
    }

    public void setNiuniu(boolean isNiuniu) {
        this.isNiuniu = isNiuniu;
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {

        LinearLayout layoutRc = helper.getView(R.id.layoutRc);
        RecyclerView rcKj1 = helper.getView(R.id.rcKj1);
        RecyclerView rcKj2 = helper.getView(R.id.rcKj2);

        LinearLayoutManager layoutManagerKjTv1 = new LinearLayoutManager(mContext);
        layoutManagerKjTv1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcKj1.setLayoutManager(layoutManagerKjTv1);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (isNiuniu) {
            params.setMargins(0, ScreenUtils.dp2px(mContext, 2), 0, ScreenUtils.dp2px(mContext, 2));

            rcKj2.setVisibility(View.GONE);
            NiuNiuAdapter niuniuAdapter;
            List<String> niuniuList = new ArrayList<>();
            niuniuList.add("1");niuniuList.add("1");
            niuniuAdapter = new NiuNiuAdapter(niuniuList);
            rcKj1.setAdapter(niuniuAdapter);

        } else {
            params.setMargins(0, ScreenUtils.dp2px(mContext, 5), 0, ScreenUtils.dp2px(mContext, 5));


            List<String> list1 = new ArrayList<>();
            list1.add("1");list1.add("1");list1.add("1");list1.add("1");list1.add("1");list1.add("1");list1.add("1");
            KaiJiangResultTvAdapter adapter1 = new KaiJiangResultTvAdapter(list1);
            adapter1.setTvSize(11);
            adapter1 = new KaiJiangResultTvAdapter(list1);

            rcKj1.setAdapter(adapter1);


            List<String> list2 = new ArrayList<>();
            list2.add("1");list2.add("1");list2.add("1");list2.add("1");list2.add("1");
            KaiJiangResultTvAdapter adapter2 = new KaiJiangResultTvAdapter(list2);
            adapter2 = new KaiJiangResultTvAdapter(list2);
            LinearLayoutManager layoutManagerKjTv2 = new LinearLayoutManager(mContext);
            layoutManagerKjTv2.setOrientation(LinearLayoutManager.HORIZONTAL);
            rcKj2.setLayoutManager(layoutManagerKjTv2);
            rcKj2.setAdapter(adapter2);
        }
        layoutRc.setLayoutParams(params);




    }

}
