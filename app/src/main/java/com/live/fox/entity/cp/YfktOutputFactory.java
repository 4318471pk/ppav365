package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.LotteryCpVO;
import com.live.fox.entity.response.MinuteTabItem;

import java.util.ArrayList;
import java.util.List;

public class YfktOutputFactory extends CpOutputFactory {
    public YfktOutputFactory(String type) {
        super(type);
    }

    @Override
    public LotteryCpVO getCpVoByType(Context context) {
        LotteryCpVO lotteryCpVOl = new LotteryCpVO();
        List<MinuteTabItem> tabItems = new ArrayList<>();
        CpMake cpMake = new YfktMakeImpl(context);
        for (int i = 0; i < 2; i++) {
            MinuteTabItem tabItem = new MinuteTabItem();
            tabItem.setBetItems(cpMake.outPut(tabItem, i, type));
            tabItems.add(tabItem);
        }
        lotteryCpVOl.setTabItems(tabItems);
        return lotteryCpVOl;
    }
}
