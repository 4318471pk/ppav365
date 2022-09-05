package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.R;
import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class JsksMakeImpl implements CpMake {

    private Context context;

    public JsksMakeImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type) {
        List<MinuteTabItem> items = new ArrayList<>();
        if (index == 0) {
            tab.setTabTitle(context.getString(R.string.sumValue));
            tab.tabType = 1;
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.big));
            item.setChineseTitle("大");
            item.setOdds(1.97);
            item.type_text = "一分快三";
            item.type = "1";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.small));
            item2.setChineseTitle("小");
            item2.setOdds(1.97);
            item2.type_text = "一分快三";
            item2.type = "1";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.single));
            item3.setChineseTitle("单");
            item3.setOdds(1.97);
            item3.type_text = "一分快三";
            item3.type = "1";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle(context.getString(R.string.doubles));
            item4.setChineseTitle("双");
            item4.setOdds(1.97);
            item4.type_text = "一分快三";
            item4.type = "1";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        } else if (index == 1) {
            tab.setTabTitle(context.getString(R.string.twoIdenticalNumbers));
            tab.tabType = 1;
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("1,1");
            item.setChineseTitle("1,1");
            item.setOdds(12.8);
            item.type_text = "二同号复选";
            item.type = "6";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle("2,2");
            item2.setChineseTitle("2,2");
            item2.setOdds(12.8);
            item2.type_text = "二同号复选";
            item2.type = "6";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle("3,3");
            item3.setChineseTitle("3,3");
            item3.setOdds(12.8);
            item3.type_text = "二同号复选";
            item3.type = "6";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle("4,4");
            item4.setChineseTitle("4,4");
            item4.setOdds(12.8);
            item4.type_text = "二同号复选";
            item4.type = "6";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle("5,5");
            item5.setChineseTitle("5,5");
            item5.setOdds(12.8);
            item5.type_text = "二同号复选";
            item5.type = "6";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle("6,6");
            item6.setChineseTitle("6,6");
            item6.setOdds(12.8);
            item6.type_text = "二同号复选";
            item6.type = "6";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            tab.setSpanCount(3);
            tab.setSpace(DeviceUtils.dp2px(context, 40));
        } else if (index == 2) {
            tab.setTabTitle(context.getString(R.string.twoDifferentNumbers));
            tab.tabType = 2;

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("1");
            item.setChineseTitle("1");
            item.setOdds(1.97);
            item.type_text = "一同号";
            item.type = "7";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle("2");
            item2.setChineseTitle("2");
            item2.setOdds(1.97);
            item2.type_text = "一同号";
            item2.type = "7";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle("3");
            item3.setChineseTitle("3");
            item3.setOdds(1.97);
            item3.type_text = "一同号";
            item3.type = "7";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle("4");
            item4.setChineseTitle("4");
            item4.setOdds(1.97);
            item4.type_text = "一同号";
            item4.type = "7";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle("5");
            item5.setChineseTitle("5");
            item5.setOdds(1.97);
            item5.type_text = "一同号";
            item5.type = "7";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle("6");
            item6.setChineseTitle("6");
            item6.setOdds(1.97);
            item6.type_text = "一同号";
            item6.type = "7";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            tab.setSpanCount(3);
            tab.setSpace(DeviceUtils.dp2px(context, 40));
        }
        return items;
    }
}
