package com.live.fox.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.DepositeHistoryBean;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.device.ScreenUtils;

import java.util.List;

public class DepositHistoryAdapter extends BaseQuickAdapter<DepositeHistoryBean, BaseViewHolder> {

    Context context;
    int screenWidth;

    public DepositHistoryAdapter(Context context, List<DepositeHistoryBean> data) {
        super(R.layout.item_deposit_history, data);
        this.context=context;
        screenWidth= ScreenUtils.getScreenWidth(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, DepositeHistoryBean item) {
        LinearLayout linearLayout=(LinearLayout)helper.itemView;
        linearLayout.setBackgroundColor(helper.getLayoutPosition()%2==1?0xffFAFAFA:0xffffffff);
        for (int i = 0; i <linearLayout.getChildCount(); i++) {
            TextView textView=(TextView)linearLayout.getChildAt(i);
            ViewGroup.LayoutParams vl= textView.getLayoutParams();
            vl.width=(int)(screenWidth/3);
            textView.setLayoutParams(vl);
            switch (i)
            {
                case 0:
                    textView.setText(TimeUtils.longToTwoLinesString( item.getTime()/1000));
                    break;
                case 1:
                    textView.setText(item.getMoney().toPlainString());
                    break;
                case 2:
                    textView.setText(item.getStatusStr());
                    break;
            }
        }


    }
}
