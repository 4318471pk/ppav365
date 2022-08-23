package com.live.fox.adapter;

import androidx.annotation.Nullable;

import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.response.MinuteTabItem;

import java.util.List;

/*****************************************************************
 * Created   on 2020\1\7 0007 下午
 * desciption:
 *****************************************************************/
public class HnAdapters extends BaseQuickAdapter<MinuteTabItem, BaseViewHolder> {
    private int whichPagea;
    public HnAdapters(@Nullable List<MinuteTabItem> data, int whichPagea) {
        super(R.layout.adapter_hn_item, data);
        this.whichPagea=whichPagea;
    }

    @Override
    protected void convert(BaseViewHolder helper, MinuteTabItem item) {
        RadioButton box = helper.getView(R.id.tvGameFont);
        if (1 == whichPagea) {
            box.setBackgroundResource(R.drawable.hn_check_selector);
            if (item.hncheck) {
                box.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                box.setTextColor(mContext.getResources().getColor(R.color.colorEB4A81));
            }
        } else {
            box.setBackgroundResource(R.drawable.hn_check_selectorb);
            if (item.hncheck) {
                box.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                box.setTextColor(mContext.getResources().getColor(R.color.lottory_text_color_selector));
            }

        }
        box.setText(item.getTitle());
        box.setClickable(false);
        box.setChecked(item.hncheck);
        helper.addOnClickListener(R.id.llItem);
    }
    public void changedPage(int page){
        whichPagea=page;
    }
}
