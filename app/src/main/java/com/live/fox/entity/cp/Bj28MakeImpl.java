package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.R;
import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.MinuteTabItem;

import java.util.ArrayList;
import java.util.List;

public class Bj28MakeImpl implements CpMake {

    private final Context context;

    public Bj28MakeImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type) {
        List<MinuteTabItem> items = new ArrayList<>();
//        混合  大 小 单 双 大单 小单 大双 小双 极大 极小

        if (index == 0) {
            tab.setTabTitle(context.getString(R.string.mix));
            tab.tabType = 1;
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.big));
            item.setChineseTitle("大");
            item.setOdds(1.97);
            item.type_text = "混合";
            item.type = "1";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.small));
            item2.setChineseTitle("小");
            item2.setOdds(1.97);
            item2.type_text = "混合";
            item2.type = "1";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.single));
            item3.setChineseTitle("单");
            item3.setOdds(1.97);
            item3.type_text = "混合";
            item3.type = "1";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);
            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle(context.getString(R.string.doubles));
            item4.setChineseTitle("双");
            item4.setOdds(1.97);
            item4.type_text = "混合";
            item4.type = "1";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle(context.getString(R.string.bigSingle));
            item5.setChineseTitle("大单");
            item5.setOdds(3.75);
            item5.type_text = "混合";
            item5.type = "1";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle(context.getString(R.string.smallSingle));
            item6.setChineseTitle("小单");
            item6.setOdds(3.55);
            item6.type_text = "混合";
            item6.type = "1";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            MinuteTabItem item7 = new MinuteTabItem();
            item7.setTitle(context.getString(R.string.bigDouble));
            item7.setChineseTitle("小双");
            item7.setOdds(3.55);
            item7.type_text = "混合";
            item7.type = "1";
            item7.setId(type + "-" + tab.getTabTitle() + "-7");
            items.add(item7);

            MinuteTabItem item8 = new MinuteTabItem();
            item8.setTitle(context.getString(R.string.smallDouble));
            item8.setChineseTitle("大双");
            item8.setOdds(3.75);
            item8.type_text = "混合";
            item8.type = "1";
            item8.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item8);

            MinuteTabItem item9 = new MinuteTabItem();
            item9.setTitle(context.getString(R.string.biggest));
            item9.setChineseTitle("极大");
            item9.setOdds(10);
            item9.type_text = "混合";
            item9.type = "1";
            item9.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item9);

            MinuteTabItem item10 = new MinuteTabItem();
            item10.setTitle(context.getString(R.string.smallest));
            item10.setChineseTitle("极小");
            item10.setOdds(10);
            item10.type_text = "混合";
            item10.type = "1";
            item10.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item10);
            tab.setSpanCount(4);
            tab.setSpace(18);
        } else if (index == 1) {
            tab.setTabTitle(context.getString(R.string.colorWave));
            tab.tabType = 1;
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.greenWave));
            item.setChineseTitle("绿波");
            item.setOdds(2.97);
            item.type_text = "色波";
            item.type = "2";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.blueWave));
            item2.setChineseTitle("蓝波");
            item2.setOdds(2.97);
            item2.type_text = "色波";
            item2.type = "2";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.redWave));
            item3.setChineseTitle("红波");
            item3.setOdds(2.97);
            item3.type_text = "色波";
            item3.type = "2";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            tab.setSpanCount(4);
            tab.setSpace(26);

        } else if (index == 2) {
            tab.setTabTitle(context.getString(R.string.leopard));
            tab.tabType = 0;

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.leopard));
            item.setChineseTitle("豹子");
            item.setOdds(60);
            item.type_text = "豹子";
            item.type = "3";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);
            tab.setSpanCount(1);
        }
        return items;
    }
}
