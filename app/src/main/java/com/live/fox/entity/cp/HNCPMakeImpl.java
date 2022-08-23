package com.live.fox.entity.cp;

import android.content.Context;


import com.live.fox.R;
import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.MinuteTabItem;

import java.util.ArrayList;
import java.util.List;

/*****************************************************************
 * desciption:
 *****************************************************************/

public class HNCPMakeImpl implements CpMake {

    private Context context;

    public HNCPMakeImpl(Context context){
        this.context = context;
    }


    @Override
    public List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type) {
        List<MinuteTabItem> items = new ArrayList<>();
        if (index == 0) {
            tab.setTabTitle(context.getString(R.string.xzhm));
            tab.tabType = 0;

            MinuteTabItem item = new MinuteTabItem();
            item.setTitle("0");
            item.setChineseTitle("0");//投注号码

            item.setOdds(1.97);
            item.type_text = "选号码";
            item.type = "PTH_XH";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);

            MinuteTabItem item2 = new MinuteTabItem();
            item2.setTitle("1");
            item2.setChineseTitle("1");
            item2.setOdds(1.97);
            item2.type_text = "选号码";
            item2.type = "PTH_XH";
            item2.setId(type + "-" + tab.getTabTitle() + "-2");
            items.add(item2);

            MinuteTabItem item3 = new MinuteTabItem();
            item3.setTitle("2");
            item3.setChineseTitle("2");
            item3.setOdds(1.97);
            item3.type_text = "选号码";
            item3.type = "PTH_XH";
            item3.setId(type + "-" + tab.getTabTitle() + "-3");
            items.add(item3);

            MinuteTabItem item4 = new MinuteTabItem();
            item4.setTitle("3");
            item4.setChineseTitle("3");
            item4.setOdds(1.97);
            item4.type_text = "选号码";
            item4.type = "PTH_XH";
            item4.setId(type + "-" + tab.getTabTitle() + "-4");
            items.add(item4);

            MinuteTabItem item5 = new MinuteTabItem();
            item5.setTitle("4");
            item5.setChineseTitle("4");//投注号码
            item5.setOdds(1.97);
            item5.type_text = "选号码";
            item5.type = "PTH_XH";
            item5.setId(type + "-" + tab.getTabTitle() + "-5");
            items.add(item5);

            MinuteTabItem item6 = new MinuteTabItem();
            item6.setTitle("5");
            item6.setChineseTitle("5");
            item6.setOdds(1.97);
            item6.type_text = "选号码";
            item6.type = "PTH_XH";
            item6.setId(type + "-" + tab.getTabTitle() + "-6");
            items.add(item6);

            MinuteTabItem item7 = new MinuteTabItem();
            item7.setTitle("6");
            item7.setChineseTitle("6");
            item7.setOdds(1.97);
            item7.type_text = "选号码";
            item7.type = "PTH_XH";
            item7.setId(type + "-" + tab.getTabTitle() + "-7");
            items.add(item7);

            MinuteTabItem item8 = new MinuteTabItem();
            item8.setTitle("7");
            item8.setChineseTitle("7");
            item8.setOdds(1.97);
            item8.type_text = "选号码";
            item8.type = "PTH_XH";
            item8.setId(type + "-" + tab.getTabTitle() + "-8");
            items.add(item8);

            MinuteTabItem item9 = new MinuteTabItem();
            item9.setTitle("8");
            item9.setChineseTitle("8");
            item9.setOdds(1.97);
            item9.type_text = "选号码";
            item9.type = "PTH_XH";
            item9.setId(type + "-" + tab.getTabTitle() + "-9");
            items.add(item9);

            MinuteTabItem item10 = new MinuteTabItem();
            item10.setTitle("9");
            item10.setChineseTitle("9");
            item10.setOdds(1.97);
            item10.type_text = "选号码";
            item10.type = "PTH_XH";
            item10.setId(type + "-" + tab.getTabTitle() + "-10");
            items.add(item10);

        } else if (index == 1) {
            tab.setTabTitle(context.getString(R.string.srhm));
            tab.tabType = 1;

            MinuteTabItem item = new MinuteTabItem();
            item.type_text = "输入号码";
            item.type = "PTH_SR";
            item.setId(type + "-" + tab.getTabTitle() + "-1");
            items.add(item);
        } else if (index == 2) {
            tab.setTabTitle(context.getString(R.string.kxhm));
            tab.tabType = 1;

            MinuteTabItem item = new MinuteTabItem();
            item.type_text = "快选";
            item.type = "PTH_KX";
            item.setId(type + "-" + tab.getTabTitle() + "-1");

            items.add(item);
        }
        return items;
    }
}
