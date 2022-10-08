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

    public DiamondExpensesAdapter(Context context,List<DiamondIncomeAndExpenseBean> data) {
        super(R.layout.item_diamond_income_expense, data);
        width= ScreenUtils.getScreenWidth(context)-ScreenUtils.getDip2px(context,20);

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
                    textView.setText(TimeUtils.longToTwoLinesString( item.getTime()/1000));
                    break;
                case 1:
                    textView.setText(item.getNickname());
                    break;
                case 2:
                    textView.setText(item.getAmountOfDiamond()+"");
                    break;
                case 3:
                    textView.setText(item.getType());
                    break;
            }
        }

    }


}
