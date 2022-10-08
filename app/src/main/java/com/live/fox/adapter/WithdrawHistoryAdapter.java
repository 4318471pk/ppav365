package com.live.fox.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.WithdrawHistoryBean;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.device.ScreenUtils;

import java.util.List;

public class WithdrawHistoryAdapter extends BaseQuickAdapter<WithdrawHistoryBean, BaseViewHolder> {

    Context context;
    float ratio[]=new float[]{0.2f,0.3f,0.3f,0.2f};
    int width;

    public WithdrawHistoryAdapter(Context context, List<WithdrawHistoryBean> data) {
        super(R.layout.item_withdraw_history, data);
        width= ScreenUtils.getScreenWidth(context)-ScreenUtils.getDip2px(context,10);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithdrawHistoryBean item) {
        LinearLayout linearLayout=(LinearLayout)helper.itemView;
        linearLayout.setBackgroundColor(helper.getLayoutPosition()%2==1?0xffFAFAFA:0xffffffff);
        for (int i = 0; i <linearLayout.getChildCount(); i++) {
            TextView textView=(TextView)linearLayout.getChildAt(i);
            ViewGroup.LayoutParams vl= textView.getLayoutParams();
            vl.width=(int)(width*ratio[i]);
            textView.setLayoutParams(vl);
            switch (i)
            {
                case 0:
                    textView.setText(TimeUtils.longToTwoLinesString( item.getWithdrawTime()/1000));
                    break;
                case 1:
                    textView.setText(item.getAmountOfMoney().toPlainString());
                    break;
                case 2:
                    textView.setText(item.getWithdrawWay());
                    break;
                case 3:
                    textView.setText(item.getStatusStr());
                    break;
            }
        }
    }
}
