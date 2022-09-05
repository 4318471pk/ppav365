package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.R;
import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class LHCMakeImpl implements CpMake {

    private Context context;

    public LHCMakeImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type) {
        List<MinuteTabItem> items = new ArrayList<>();
        if (index == 0) {
            tab.setTabTitle(context.getString(R.string.special_topics));
            tab.tabType = 0;

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.single));
            item.setChineseTitle("单");
            item.setOdds(1.97);
            item.type_text = "特码";
            item.type = "TMLM";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.doubles));
            item2.setChineseTitle("双");
            item2.setOdds(1.97);
            item2.type_text = "特码";
            item2.type = "TMLM";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.big));
            item3.setChineseTitle("大");
            item3.setOdds(1.97);
            item3.type_text ="特码";
            item3.type = "TMLM";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle(context.getString(R.string.small));
            item4.setChineseTitle("小");
            item4.setOdds(1.97);
            item4.type_text = "特码";
            item4.type = "TMLM";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            tab.setSpanCount(4);
            tab.setSpace(DeviceUtils.dp2px(context, 20));
        } else if (index == 1) {
            tab.setTabTitle(context.getString(R.string.specialColorWave));
            tab.tabType = 1;

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.redString));
            item.setChineseTitle("红");
            item.setOdds(2.82);
            item.type_text = "特码色波";
            item.type = "TMSB";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.greenString));
            item2.setChineseTitle("绿");
            item2.setOdds(2.97);
            item2.type_text = "特码色波";
            item2.type = "TMSB";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.blueString));
            item3.setChineseTitle("蓝");
            item3.setOdds(2.97);
            item3.type_text = "特码色波";
            item3.type = "TMSB";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            tab.setSpanCount(3);
            tab.setSpace(DeviceUtils.dp2px(context, 40));
        }
        return items;
    }
}
