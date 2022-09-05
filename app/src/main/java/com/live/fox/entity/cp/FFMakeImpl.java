package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.R;
import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/*****************************************************************
 * desciption:
 *****************************************************************/

public class FFMakeImpl implements CpMake {
    private Context context;

    public FFMakeImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type) {
        List<MinuteTabItem> items = new ArrayList<>();
        if (index == 0) {
            tab.setTabTitle(context.getString(R.string.twoDifferentNumbers));
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.lu));
            item.setChineseTitle("1");
            item.setOdds(1.98);
            item.type_text = "一同号";
            item.type = "YTH";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.hulu));
            item2.setChineseTitle("2");
            item2.setOdds(1.98);
            item2.type_text = "一同号";
            item2.type = "YTH";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.ji));
            item3.setChineseTitle("3");
            item3.setOdds(1.98);
            item3.type_text = "一同号";
            item3.type = "YTH";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle(context.getString(R.string.yu));
            item4.setChineseTitle("4");
            item4.setOdds(1.98);
            item4.type_text = "一同号";
            item4.type = "YTH";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle(context.getString(R.string.pangxie));
            item5.setChineseTitle("5");
            item5.setOdds(1.98);
            item5.type_text = "一同号";
            item5.type = "YTH";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle(context.getString(R.string.xia));
            item6.setChineseTitle("6");
            item6.setOdds(1.98);
            item6.type_text = "一同号";
            item6.type = "YTH";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            tab.setSpanCount(3);
            tab.setSpace(DeviceUtils.dp2px(context, 40));

        } else if (index == 1) {
            tab.setTabTitle(context.getString(R.string.twoIdenticalNumbers));
            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.lu) + "," + context.getString(R.string.lu));
            item.setChineseTitle("11");
            item.setOdds(12.88);
            item.type_text = "二同号复选";
            item.type = "ETH";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.hulu) + "," + context.getString(R.string.hulu));
            item2.setChineseTitle("22");
            item2.setOdds(12.88);
            item2.type_text = "二同号复选";
            item2.type = "ETH";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.ji) + "," + context.getString(R.string.ji));
            item3.setChineseTitle("33");
            item3.setOdds(12.88);
            item3.type_text = "二同号复选";
            item3.type = "ETH";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle(context.getString(R.string.yu) + "," + context.getString(R.string.yu));
            item4.setChineseTitle("44");
            item4.setOdds(12.88);
            item4.type_text = "二同号复选";
            item4.type = "ETH";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle(context.getString(R.string.pangxie) + "," + context.getString(R.string.pangxie));
            item5.setChineseTitle("55");
            item5.setOdds(12.88);
            item5.type_text = "二同号复选";
            item5.type = "ETH";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle(context.getString(R.string.xia) + "," + context.getString(R.string.xia));
            item6.setChineseTitle("66");
            item6.setOdds(12.88);
            item6.type_text = "二同号复选";
            item6.type = "ETH";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            tab.setSpanCount(3);
            tab.setSpace(DeviceUtils.dp2px(context, 26));
        } else if (index == 2) {
            tab.setTabTitle(context.getString(R.string.erbutong));

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle(context.getString(R.string.lu) + "," + context.getString(R.string.hulu));
            item.setChineseTitle("1,2");
            item.setOdds(6.98);
            item.type_text = "二不同号";
            item.type = "EBT";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle(context.getString(R.string.lu) + "," + context.getString(R.string.ji));
            item2.setChineseTitle("1,3");
            item2.setOdds(6.98);
            item2.type_text = "二不同号";
            item2.type = "EBT";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle(context.getString(R.string.lu) + "," + context.getString(R.string.yu));
            item3.setChineseTitle("1,4");
            item3.setOdds(6.98);
            item3.type_text = "二不同号";
            item3.type = "EBT";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle(context.getString(R.string.lu) + "," + context.getString(R.string.pangxie));
            item4.setChineseTitle("1,5");
            item4.setOdds(6.98);
            item4.type_text = "二不同号";
            item4.type = "EBT";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle(context.getString(R.string.lu) + "," + context.getString(R.string.xia));
            item5.setChineseTitle("1,6");
            item5.setOdds(6.98);
            item5.type_text = "二不同号";
            item5.type = "EBT";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle(context.getString(R.string.hulu) + "," + context.getString(R.string.ji));
            item6.setChineseTitle("2,3");
            item6.setOdds(6.98);
            item6.type_text = "二不同号";
            item6.type = "EBT";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            MinuteTabItem item7 = new MinuteTabItem();
            item7.setTitle(context.getString(R.string.hulu) + "," + context.getString(R.string.yu));
            item7.setChineseTitle("2,4");
            item7.setOdds(6.98);
            item7.type_text = "二不同号";
            item7.type = "EBT";
            item7.setId(type + "-" + tab.getTabTitle() + "-7");
            items.add(item7);

            MinuteTabItem item8 = new MinuteTabItem();
            item8.setTitle(context.getString(R.string.hulu) + "," + context.getString(R.string.pangxie));
            item8.setChineseTitle("2,5");
            item8.setOdds(6.98);
            item8.type_text = "二不同号";
            item8.type = "EBT";
            item8.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item8);

            MinuteTabItem item9 = new MinuteTabItem();
            item9.setTitle(context.getString(R.string.hulu) + "," + context.getString(R.string.xia));
            item9.setChineseTitle("2,6");
            item9.setOdds(6.98);
            item9.type_text = "二不同号";
            item9.type = "EBT";
            item9.setId(type + "-" + tab.getTabTitle() + "-9");
            items.add(item9);

            MinuteTabItem item10 = new MinuteTabItem();
            item10.setTitle(context.getString(R.string.ji) + "," + context.getString(R.string.yu));
            item10.setChineseTitle("3,4");
            item10.setOdds(6.98);
            item10.type_text = "二不同号";
            item10.type = "EBT";
            item10.setId(type + "-" + tab.getTabTitle() + "-10");
            items.add(item10);

            MinuteTabItem item11 = new MinuteTabItem();
            item11.setTitle(context.getString(R.string.ji) + "," + context.getString(R.string.pangxie));
            item11.setChineseTitle("3,5");
            item11.setOdds(6.98);
            item11.type_text = "二不同号";
            item11.type = "EBT";
            item11.setId(type + "-" + tab.getTabTitle() + "-11");
            items.add(item11);

            MinuteTabItem item12 = new MinuteTabItem();
            item12.setTitle(context.getString(R.string.ji) + "," + context.getString(R.string.xia));
            item12.setChineseTitle("3,6");
            item12.setOdds(6.98);
            item12.type_text = "二不同号";
            item12.type = "EBT";
            item12.setId(type + "-" + tab.getTabTitle() + "-12");
            items.add(item12);

            MinuteTabItem item13 = new MinuteTabItem();
            item13.setTitle(context.getString(R.string.yu) + "," + context.getString(R.string.pangxie));
            item13.setChineseTitle("4,5");
            item13.setOdds(6.98);
            item13.type_text = "二不同号";
            item13.type = "EBT";
            item13.setId(type + "-" + tab.getTabTitle() + "-13");
            items.add(item13);

            MinuteTabItem item14 = new MinuteTabItem();
            item14.setTitle(context.getString(R.string.yu) + "," + context.getString(R.string.xia));
            item14.setChineseTitle("4,6");
            item14.setOdds(6.98);
            item14.type_text = "二不同号";
            item14.type = "EBT";
            item14.setId(type + "-" + tab.getTabTitle() + "-14");
            items.add(item14);

            MinuteTabItem item15 = new MinuteTabItem();
            item15.setTitle(context.getString(R.string.pangxie) + "," + context.getString(R.string.xia));
            item15.setChineseTitle("5,6");
            item15.setOdds(6.98);
            item15.type_text = "二不同号";
            item15.type = "EBT";
            item15.setId(type + "-" + tab.getTabTitle() + "-15");
            items.add(item15);

            tab.setSpanCount(5);
            tab.setSpace(DeviceUtils.dp2px(context, 4));
        }
        return items;
    }
}
