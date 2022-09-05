package com.live.fox.contract;


import com.live.fox.entity.response.MinuteTabItem;

import java.util.List;


public interface CpMake {
    List<MinuteTabItem> outPut(MinuteTabItem tab, int index, String type);
}
