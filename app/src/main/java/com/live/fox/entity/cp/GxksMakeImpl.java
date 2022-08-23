package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.R;
import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class GxksMakeImpl implements CpMake {

    private final Context context;

    public GxksMakeImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type) {
        List<MinuteTabItem> items = new ArrayList<>();
        if (index == 0) {
            tab.setTabTitle(context.getString(R.string.threeIdentical));
            tab.tabType = 1;
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("111");
            item.setOdds(180);
            item.type_text = "三同号单选";
            item.type = "3";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle("222");
            item2.setOdds(180);
            item2.type_text = "三同号单选";
            item2.type = "3";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle("333");
            item3.setOdds(180);
            item3.type_text = "三同号单选";
            item3.type = "3";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);
            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle("444");
            item4.setOdds(180);
            item4.type_text = "三同号单选";
            item4.type = "3";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle("555");
            item5.setOdds(180);
            item5.type_text = "三同号单选";
            item5.type = "3";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle("666");
            item6.setOdds(180);
            item6.type_text = "三同号单选";
            item6.type = "3";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        } else if (index == 1) {
            //1,2,3  124 125 126  133 134  135  136  145 146 156
            tab.setTabTitle(context.getString(R.string.threeNoIdentical));
            tab.tabType = 1;
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("1,2,3");
            item.setOdds(30);
            item.type_text = "三不同号";
            item.type = "4";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle("1,2,4");
            item2.setOdds(30);
            item2.type_text = "三不同号";
            item2.type = "4";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle("1,2,5");
            item3.setOdds(30);
            item3.type_text = "三不同号";
            item3.type = "4";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);
            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle("1,2,6");
            item4.setOdds(30);
            item4.type_text = "三不同号";
            item4.type = "4";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle("1,3,4");
            item5.setOdds(30);
            item5.type_text = "三不同号";
            item5.type = "4";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle("1,3,5");
            item6.setOdds(30);
            item6.type_text = "三不同号";
            item6.type = "4";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);


            MinuteTabItem item8 = new MinuteTabItem();
            item8.setTitle("1,3,6");
            item8.setOdds(30);
            item8.type_text = "三不同号";
            item8.type = "4";
            item8.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item8);

            MinuteTabItem item15 = new MinuteTabItem();
            item15.setTitle("1,4,5");
            item15.setOdds(30);
            item15.type_text = "三不同号";
            item15.type = "4";
            item15.setId(type + "-" + tab.getTabTitle() + "-15");
            items.add(item15);

            MinuteTabItem item16 = new MinuteTabItem();
            item16.setTitle("1,4,6");
            item16.setOdds(30);
            item16.type_text = "三不同号";
            item16.type = "4";
            item16.setId(type + "-" + tab.getTabTitle() + "-16");
            items.add(item16);

            MinuteTabItem item17 = new MinuteTabItem();
            item17.setTitle("1,5,6");
            item17.setOdds(30);
            item17.type_text = "三不同号";
            item17.type = "4";
            item17.setId(type + "-" + tab.getTabTitle() + "-17");
            items.add(item17);

            MinuteTabItem item9 = new MinuteTabItem();
            item9.setTitle("2,3,4");
            item9.setOdds(30);
            item9.type_text = "三不同号";
            item9.type = "4";
            item9.setId(type + "-" + tab.getTabTitle() + "-9");
            items.add(item9);

            MinuteTabItem item10 = new MinuteTabItem();
            item10.setTitle("2,3,5");
            item10.setOdds(30);
            item10.type_text = "三不同号";
            item10.type = "4";
            item10.setId(type + "-" + tab.getTabTitle() + "-10");
            items.add(item10);

            MinuteTabItem item7 = new MinuteTabItem();
            item7.setTitle("2,3,6");
            item7.setOdds(30);
            item7.type_text = "三不同号";
            item7.type = "4";
            item7.setId(type + "-" + tab.getTabTitle() + "-7");
            items.add(item7);


            MinuteTabItem item18 = new MinuteTabItem();
            item18.setTitle("2,4,5");
            item18.setOdds(30);
            item18.type_text = "三不同号";
            item18.type = "4";
            item18.setId(type + "-" + tab.getTabTitle() + "-18");
            items.add(item18);

            MinuteTabItem item19 = new MinuteTabItem();
            item19.setTitle("2,4,6");
            item19.setOdds(30);
            item19.type_text = "三不同号";
            item19.type = "4";
            item19.setId(type + "-" + tab.getTabTitle() + "-19");
            items.add(item19);

            MinuteTabItem item20 = new MinuteTabItem();
            item20.setTitle("2,5,6");
            item20.setOdds(30);
            item20.type_text = "三不同号";
            item20.type = "4";
            item20.setId(type + "-" + tab.getTabTitle() + "-20");
            items.add(item20);

            MinuteTabItem item11 = new MinuteTabItem();
            item11.setTitle("3,4,5");
            item11.setOdds(30);
            item11.type_text = "三不同号";
            item11.type = "4";
            item11.setId(type + "-" + tab.getTabTitle() + "-11");
            items.add(item11);

            MinuteTabItem item12 = new MinuteTabItem();
            item12.setTitle("3,4,6");
            item12.setOdds(30);
            item12.type_text = "三不同号";
            item12.type = "4";
            item12.setId(type + "-" + tab.getTabTitle() + "-12");
            items.add(item12);

            MinuteTabItem item13 = new MinuteTabItem();
            item13.setTitle("3,5,6");
            item13.setOdds(30);
            item13.type_text = "三不同号";
            item13.type = "4";
            item13.setId(type + "-" + tab.getTabTitle() + "-13");
            items.add(item13);

            MinuteTabItem item14 = new MinuteTabItem();
            item14.setTitle("4,5,6");
            item14.setOdds(30);
            item14.type_text = "三不同号";
            item14.type = "4";
            item14.setId(type + "-" + tab.getTabTitle() + "-14");
            items.add(item14);

            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        } else if (index == 2) {
            tab.setTabTitle(context.getString(R.string.threeNumberElection));
            tab.tabType = 0;

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("123*234*345*456");
            item.setOdds(6.5);
            item.type_text = "三连号通选";
            item.type = "5";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);
            tab.setSpanCount(1);
        }
        return items;
    }
}
