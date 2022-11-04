package com.live.fox.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.DiamondIncomeAndExpenseBean;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.device.ScreenUtils;

import java.util.List;

public class DiamondExpensesAdapter extends BaseQuickAdapter<DiamondIncomeAndExpenseBean, BaseViewHolder> {

    float ratio[]=new float[]{0.2f,0.3f,0.3f,0.2f};
    int width;
    Context context;

    boolean isGetDiamond = true;
    public DiamondExpensesAdapter(Context context,List<DiamondIncomeAndExpenseBean> data, boolean isGetDiamond) {
        super(R.layout.item_diamond_income_expense, data);
        width= ScreenUtils.getScreenWidth(context)-ScreenUtils.getDip2px(context,10);
        this.isGetDiamond = isGetDiamond;
    }

    @Override
    protected void convert(BaseViewHolder helper, DiamondIncomeAndExpenseBean item) {
        LinearLayout linearLayout=(LinearLayout) helper.itemView;
        linearLayout.setBackgroundColor(helper.getLayoutPosition()%2==1?0xffFAFAFA:0xffffffff);

        for (int i = 0; i <linearLayout.getChildCount(); i++) {
            TextView textView=(TextView)linearLayout.getChildAt(i);
            ViewGroup.LayoutParams vl= textView.getLayoutParams();
            vl.width=(int)(width*ratio[i]);
            textView.setLayoutParams(vl);
            switch (i)
            {
                case 0:
                    textView.setText(TimeUtils.longToTwoLinesString( item.getTime()));
                    break;
                case 1:
                    textView.setText(item.getUserNick());
                    break;
                case 2:
                    textView.setText(item.getAmount() + "");
                    break;
                case 3:
                    if (isGetDiamond) {
                        if (item.getType() == 401) { //401 礼物 402守护收入 403按时收费 404按场收费
                            textView.setText(mContext.getString(R.string.gift_get));
                        } else if (item.getType() == 402)  {
                            textView.setText(mContext.getString(R.string.guard_get));
                        } else if (item.getType() == 403)  {
                            textView.setText(mContext.getString(R.string.charge_on_time));
                        } else if (item.getType() == 404)  {
                            textView.setText(mContext.getString(R.string.charge_per_site));
                        }
                    } else { //消费类型:101送礼102弹幕103购买道具104购买守护105购买贵族201:彩票投注301三方游戏xx转出

                    }
                    break;
            }
        }

    }


}
