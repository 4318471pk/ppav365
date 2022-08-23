package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.R;
import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class Js11MakeImpl implements CpMake {
    private Context context;

    public Js11MakeImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type) {
        List<MinuteTabItem> items = new ArrayList<>();
        if (index == 0) {
            tab.setTabTitle(context.getString(R.string.previousCompound));
            tab.tabType = 1;
            tab.type = "8.1";
            tab.setLimit(1);

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("01");
            item.setOdds(7.1);
            item.type_text = "前一复式";
            item.type = "8.1";
            item.setId(type + "-" + tab.getTabTitle() + "-1");

            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle("02");
            item2.setOdds(7.1);
            item2.type_text = "前一复式";
            item2.type = "8.1";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle("03");
            item3.setOdds(7.1);
            item3.type_text = "前一复式";
            item3.type = "8.1";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle("04");
            item4.setOdds(7.1);
            item4.type_text = "前一复式";
            item4.type = "8.1";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);
            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle("05");
            item5.setOdds(7.1);
            item5.type_text = "前一复式";
            item5.type = "8.1";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);
            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle("06");
            item6.setOdds(7.1);
            item6.type_text = "前一复式";
            item6.type = "8.1";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            MinuteTabItem item7 = new MinuteTabItem();
            item7.setTitle("07");
            item7.setOdds(7.1);
            item7.type_text = "前一复式";
            item7.type = "8.1";
            item7.setId(type + "-" + tab.getTabTitle() + "-7");
            items.add(item7);
            MinuteTabItem item8 = new MinuteTabItem();
            item8.setTitle("08");
            item8.setOdds(7.1);
            item8.type_text = "前一复式";
            item8.type = "8.1";
            item8.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item8);
            MinuteTabItem item9 = new MinuteTabItem();
            item9.setTitle("09");
            item9.setOdds(7.1);
            item9.type_text = "前一复式";
            item9.type = "8.1";
            item9.setId(type + "-" + tab.getTabTitle() + "-9");
            items.add(item9);
            MinuteTabItem item10 = new MinuteTabItem();
            item10.setTitle("10");
            item10.setOdds(7.1);
            item10.type_text = "前一复式";
            item10.type = "8.1";
            item10.setId(type + "-" + tab.getTabTitle() + "-10");
            items.add(item10);

            MinuteTabItem item11 = new MinuteTabItem();
            item11.setTitle("11");
            item11.setOdds(7.1);
            item11.type_text = "前一复式";
            item11.type = "8.1";
            item11.setId(type + "-" + tab.getTabTitle() + "-11");
            items.add(item11);

            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        } else if (index == 1) {
            tab.setTabTitle(context.getString(R.string.twoSelectedCompound));
            tab.tabType = 1;
            tab.setLimit(2);
            tab.type = "9.1";

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("01");
            item.setOdds(7.1);
            item.type_text = "前二组选复式";
            item.type = "9.1";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle("02");
            item2.setOdds(7.1);
            item2.type_text = "前二组选复式";
            item2.type = "9.1";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle("03");
            item3.setOdds(7.1);
            item3.type_text = "前二组选复式";
            item3.type = "9.1";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle("04");
            item4.setOdds(7.1);
            item4.type_text = "前二组选复式";
            item4.type = "9.1";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);
            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle("05");
            item5.setOdds(7.1);
            item5.type_text = "前二组选复式";
            item5.type = "9.1";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);
            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle("06");
            item6.setOdds(7.1);
            item6.type_text = "前二组选复式";
            item6.type = "9.1";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            MinuteTabItem item7 = new MinuteTabItem();
            item7.setTitle("07");
            item7.setOdds(7.1);
            item7.type_text = "前二组选复式";
            item7.type = "9.1";
            item7.setId(type + "-" + tab.getTabTitle() + "-7");
            items.add(item7);
            MinuteTabItem item8 = new MinuteTabItem();
            item8.setTitle("08");
            item8.setOdds(7.1);
            item8.type_text = "前二组选复式";
            item8.type = "9.1";
            item8.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item8);
            MinuteTabItem item9 = new MinuteTabItem();
            item9.setTitle("09");
            item9.setOdds(7.1);
            item9.type_text = "前二组选复式";
            item9.type = "9.1";
            item9.setId(type + "-" + tab.getTabTitle() + "-9");
            items.add(item9);
            MinuteTabItem item10 = new MinuteTabItem();
            item10.setTitle("10");
            item10.setOdds(7.1);
            item10.type_text = "前二组选复式";
            item10.type = "9.1";
            item10.setId(type + "-" + tab.getTabTitle() + "-10");
            items.add(item10);

            MinuteTabItem item11 = new MinuteTabItem();
            item11.setTitle("11");
            item11.setOdds(7.1);
            item11.type_text = "前二组选复式";
            item11.type = "9.1";
            item11.setId(type + "-" + tab.getTabTitle() + "-11");
            items.add(item11);

            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        } else if (index == 2) {
            tab.setTabTitle(context.getString(R.string.threeSelected));
            tab.tabType = 1;
            tab.setLimit(3);
            tab.type = "11.1";

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("01");
            item.setOdds(7.1);
            item.type_text = "前三组选复式";
            item.type = "11.1";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle("02");
            item2.setOdds(7.1);
            item2.type_text = "前三组选复式";
            item2.type = "11.1";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle("03");
            item3.setOdds(7.1);
            item3.type_text = "前三组选复式";
            item3.type = "11.1";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle("04");
            item4.setOdds(7.1);
            item4.type_text = "前三组选复式";
            item4.type = "11.1";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);
            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle("05");
            item5.setOdds(7.1);
            item5.type_text = "前三组选复式";
            item5.type = "11.1";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);
            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle("06");
            item6.setOdds(7.1);
            item6.type_text = "前三组选复式";
            item6.type = "11.1";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            MinuteTabItem item7 = new MinuteTabItem();
            item7.setTitle("07");
            item7.setOdds(7.1);
            item7.type_text = "前三组选复式";
            item7.type = "11.1";
            item7.setId(type + "-" + tab.getTabTitle() + "-7");
            items.add(item7);
            MinuteTabItem item8 = new MinuteTabItem();
            item8.setTitle("08");
            item8.setOdds(7.1);
            item8.type_text = "前三组选复式";
            item8.type = "11.1";
            item8.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item8);
            MinuteTabItem item9 = new MinuteTabItem();
            item9.setTitle("09");
            item9.setOdds(7.1);
            item9.type_text = "前三组选复式";
            item9.type = "11.1";
            item9.setId(type + "-" + tab.getTabTitle() + "-9");
            items.add(item9);
            MinuteTabItem item10 = new MinuteTabItem();
            item10.setTitle("10");
            item10.setOdds(7.1);
            item10.type_text = "前三组选复式";
            item10.type = "11.1";
            item10.setId(type + "-" + tab.getTabTitle() + "-10");
            items.add(item10);

            MinuteTabItem item11 = new MinuteTabItem();
            item11.setTitle("11");
            item11.setOdds(7.1);
            item11.type_text = "前三组选复式";
            item11.type = "11.1";
            item11.setId(type + "-" + tab.getTabTitle() + "-11");
            items.add(item11);

            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        }
        return items;
    }
}
