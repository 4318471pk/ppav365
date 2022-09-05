package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.R;
import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * PK 10 彩票数据
 * 彩票数据生产
 */
public class PkMakeImpl implements CpMake {

    private final Context context;

    public PkMakeImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type) {
        List<MinuteTabItem> items = new ArrayList<>();
        if (index == 0) {
            tab.setTabTitle(context.getString(R.string.guessChampion));
            tab.tabType = 0;

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("1");
            item.setChineseTitle("1");
            item.setOdds(9.7);
            item.type_text = "猜冠军";
            item.type = "GJ";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle("2");
            item2.setChineseTitle("2");
            item2.setOdds(9.7);
            item2.type_text = "猜冠军";
            item2.type = "GJ";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle("3");
            item3.setChineseTitle("3");
            item3.setOdds(9.7);
            item3.type_text = "猜冠军";
            item3.type = "GJ";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle("4");
            item4.setChineseTitle("4");
            item4.setOdds(9.7);
            item4.type_text = "猜冠军";
            item4.type = "GJ";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle("5");
            item5.setChineseTitle("5");
            item5.setOdds(9.7);
            item5.type_text = "猜冠军";
            item5.type = "GJ";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle("6");
            item6.setChineseTitle("6");
            item6.setOdds(9.7);
            item6.type_text = "猜冠军";
            item6.type = "GJ";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            MinuteTabItem item7 = new MinuteTabItem();
            item7.setTitle("7");
            item7.setChineseTitle("7");
            item7.setOdds(9.7);
            item7.type_text = "猜冠军";
            item7.type = "GJ";
            item7.setId(type + "-" + tab.getTabTitle() + "-7");
            items.add(item7);

            MinuteTabItem item8 = new MinuteTabItem();
            item8.setTitle("8");
            item8.setChineseTitle("8");
            item8.setOdds(9.7);
            item8.type_text = "猜冠军";
            item8.type = "GJ";
            item8.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item8);

            MinuteTabItem item9 = new MinuteTabItem();
            item9.setTitle("9");
            item9.setChineseTitle("9");
            item9.setOdds(9.7);
            item9.type_text = "猜冠军";
            item9.type = "GJ";
            item9.setId(type + "-" + tab.getTabTitle() + "-9");
            items.add(item9);

            MinuteTabItem item10 = new MinuteTabItem();
            item10.setTitle("10");
            item10.setChineseTitle("10");
            item10.setOdds(9.7);
            item10.type_text = "猜冠军";
            item10.type = "GJ";
            item10.setId(type + "-" + tab.getTabTitle() + "-10");
            items.add(item10);
            tab.setSpanCount(5);
            tab.setSpace(DeviceUtils.dp2px(context, 10));

        } else if (index == 1) {
            tab.setTabTitle(context.getString(R.string.GuanyaXiaoshuang));
            tab.tabType = 1;
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.big));
            item.setChineseTitle("大"); //注意这个地方不需要做翻译，这是传递给后台用来做识别的
            item.setOdds(1.97);
            item.type_text = "大小单双-冠亚";
            item.type = "DXDS";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.small));
            item2.setChineseTitle("小");
            item2.setOdds(1.97);
            item2.type_text = "大小单双-冠亚";
            item2.type = "DXDS";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.single));
            item3.setChineseTitle("单");
            item3.setOdds(1.97);
            item3.type_text = "大小单双-冠亚";
            item3.type = "DXDS";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle(context.getString(R.string.doubles));
            item4.setChineseTitle("双");
            item4.setOdds(1.97);
            item4.type_text = "大小单双-冠亚";
            item4.type = "DXDS";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);
            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        }
        return items;
    }
}
