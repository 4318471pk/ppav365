package com.live.fox.adapter;

import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_FF;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_JSKS;

/*****************************************************************
 * Created   on 2020\1\7 0007 下午
 * desciption:
 *****************************************************************/
public class PrizeShowAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private final String lottryName;

    public PrizeShowAdapter(@Nullable List<String> data, String lottryName) {
        super(R.layout.adapter_item_prizer, data);
        this.lottryName = lottryName;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView textView = helper.getView(R.id.tvPrize);
        if ("+".equals(item)) {
            textView.setText(item);
            textView.setBackground(null);
        } else if (item.contains("#")) {
            String[] split = item.split("#");
            textView.setText(split[0]);
            if ("3".equals(split[1])) {
                textView.setBackgroundResource(R.drawable.prizer_num_bg_blue);
            } else if ("2".equals(split[1])) {
                textView.setBackgroundResource(R.drawable.prizer_num_bg_green);
            } else {
                textView.setBackgroundResource(R.drawable.prizer_num_bg_red);
            }
        } else {
            if (TYPE_CP_JSKS.equals(lottryName)) {
                if ("1".equals(item)) {
                    textView.setBackgroundResource(R.drawable.dot01);
                } else if ("2".equals(item)) {
                    textView.setBackgroundResource(R.drawable.dot02);
                } else if ("3".equals(item)) {
                    textView.setBackgroundResource(R.drawable.dot03);
                } else if ("4".equals(item)) {
                    textView.setBackgroundResource(R.drawable.dot04);
                } else if ("5".equals(item)) {
                    textView.setBackgroundResource(R.drawable.dot05);
                } else if ("6".equals(item)) {
                    textView.setBackgroundResource(R.drawable.dot06);
                }
            } else if (TYPE_CP_FF.equals(lottryName)) {
                if ("1".equals(item)) {
                    textView.setBackgroundResource(R.drawable.fllu);
                } else if ("2".equals(item)) {
                    textView.setBackgroundResource(R.drawable.flxie);
                } else if ("3".equals(item)) {
                    textView.setBackgroundResource(R.drawable.frji);
                } else if ("4".equals(item)) {
                    textView.setBackgroundResource(R.drawable.frfish);
                } else if ("5".equals(item)) {
                    textView.setBackgroundResource(R.drawable.flpangxie);
                } else if ("6".equals(item)) {
                    textView.setBackgroundResource(R.drawable.flxia);
                }
            } else {
                textView.setText(item);
                textView.setBackgroundResource(R.drawable.prizer_num_bg);
            }
        }

    }

}
