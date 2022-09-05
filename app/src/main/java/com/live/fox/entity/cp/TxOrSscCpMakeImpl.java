package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.R;
import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class TxOrSscCpMakeImpl implements CpMake {
    private Context context;

    public TxOrSscCpMakeImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type) {
        List<MinuteTabItem> items = new ArrayList<>();
        if (index == 0) {
            tab.setTabTitle(context.getString(R.string.one_place));
            tab.tabType = 0;

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.big));
            item.setChineseTitle("大");
            item.setOdds(1.97);
            item.type_text = "个位";
            item.type = "7.1";
            item.setId(type + "-个-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.small));
            item2.setChineseTitle("小");
            item2.setOdds(1.97);
            item2.type_text = "个位";
            item2.type = "7.1";
            item2.setId(type + "-个-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.single));
            item3.setChineseTitle("单");
            item3.setOdds(1.97);
            item3.type_text = "个位";
            item3.type = "7.1";
            item3.setId(type + "-个-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle(context.getString(R.string.doubles));
            item4.setChineseTitle("双");
            item4.setOdds(1.97);
            item4.type_text = "个位";
            item4.type = "7.1";
            item4.setId(type + "-个-4");
            items.add(item4);
            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        } else if (index == 1) {
            tab.setTabTitle(context.getString(R.string.thousandsDragonsTigers));
            tab.tabType = 1;
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.dragons));
            item.setChineseTitle("龙");
            item.setOdds(1.99);
            item.type_text = "龙虎万千";
            item.type = "9.1";
            item.setId(type + "-animal-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.tiger));
            item2.setChineseTitle("虎");
            item2.setOdds(1.99);
            item2.type_text ="龙虎万千";
            item2.type = "9.1";
            item2.setId(type + "-animal-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.togethers));
            item3.setChineseTitle("和");
            item3.setOdds(10.18);
            item3.type_text = "龙虎万千";
            item3.type = "9.1";
            item3.setId(type + "-animal-3");
            items.add(item3);
            tab.setSpanCount(3);
            tab.setSpace(DeviceUtils.dp2px(context, 40));
        } else if (index == 2) {
            tab.setTabTitle(context.getString(R.string.shiwei));
            tab.tabType = 0;

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.big));
            item.setChineseTitle("大");
            item.setOdds(1.97);
            item.type_text = "十位";
            item.type = "7.1";
            item.setId(type + "-十-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.small));
            item2.setChineseTitle("小");
            item2.setOdds(1.97);
            item2.type_text = "十位";
            item2.type = "7.1";
            item2.setId(type + "-十-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.single));
            item3.setChineseTitle("单");
            item3.setOdds(1.97);
            item3.type_text = "十位";
            item3.type = "7.1";
            item3.setId(type + "-十-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle(context.getString(R.string.doubles));
            item4.setChineseTitle("双");
            item4.setOdds(1.97);
            item4.type_text = "十位";
            item4.type = "7.1";
            item4.setId(type + "-十-4");

            items.add(item4);
            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        }
        return items;
    }
}
