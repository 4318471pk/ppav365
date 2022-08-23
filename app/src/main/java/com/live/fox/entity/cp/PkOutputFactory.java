package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.contract.CpMake;
import com.live.fox.entity.response.LotteryCpVO;
import com.live.fox.entity.response.MinuteTabItem;

import java.util.ArrayList;
import java.util.List;

/**
 * PK 10
 * 彩票数据源
 */
public class PkOutputFactory extends CpOutputFactory {
    public PkOutputFactory(String type) {
        super(type);
    }

    @Override
    public LotteryCpVO getCpVoByType(Context context) {
        LotteryCpVO lotteryCpVOl = new LotteryCpVO();
        List<MinuteTabItem> tabItems = new ArrayList<>();
        CpMake cpMake = new PkMakeImpl(context);
        for (int i = 0; i < 2; i++) {
            MinuteTabItem tabItem = new MinuteTabItem();
            tabItem.setBetItems(cpMake.outPut(tabItem, i, type));
            tabItems.add(tabItem);
        }
        lotteryCpVOl.setTabItems(tabItems);
        return lotteryCpVOl;
    }
}
