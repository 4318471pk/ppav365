package com.live.fox.svga;

import com.live.fox.entity.response.ChipsVO;
import com.live.fox.entity.response.MinuteTabItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BetCartDataManager {
    private final LinkedHashMap<String, MinuteTabItem> cartMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, MinuteTabItem> link = new LinkedHashMap<>();
    private int chipsIndex = 0;
    public static int betGameIndex = 0;//投注倍数

    private static class InstanceHolder {
        private static final BetCartDataManager INSTANCE = new BetCartDataManager();
    }

    public static BetCartDataManager getInstance() {
        return InstanceHolder.INSTANCE;
    }


    public void addOddeField(MinuteTabItem item) {
        if (item != null) {
            cartMap.put(item.getId(), item);
        }
    }

    public MinuteTabItem removeOddeField(MinuteTabItem item) {
        if (item != null && cartMap.containsKey(item.getId())) {
            item.check = false;
            return cartMap.remove(item.getId());
        }
        return null;
    }

    public MinuteTabItem removeOddeFieldhn(MinuteTabItem item) {
        if (item != null && cartMap.containsKey(item.getId())) {
            return cartMap.remove(item.getId());
        }
        return null;
    }

    public MinuteTabItem findOddeFieldById(String id) {
        if (cartMap.containsKey(id)) {
            return cartMap.get(id);
        }
        return null;
    }

    public int getChipsIndex() {
        return chipsIndex;
    }

    public void setChipsIndex(int chipsIndex) {
        this.chipsIndex = chipsIndex;
    }

    public List<MinuteTabItem> asList(boolean isMix) {
        if (isMix) {
            if (link.size() > 0) return new ArrayList<>(link.values());
            ArrayList<MinuteTabItem> items = new ArrayList<>(cartMap.values());
            for (int i = 0; i < items.size(); i++) {
                MinuteTabItem minuteTabItem = items.get(i);
                MinuteTabItem item = link.get(minuteTabItem.type);
                if (item == null) {
                    MinuteTabItem newItem = new MinuteTabItem();
                    newItem.setTitle(minuteTabItem.getTitle());
                    newItem.setOdds(minuteTabItem.getOdds());
                    newItem.type_text = minuteTabItem.type_text;
                    newItem.type = minuteTabItem.type;
                    newItem.setId(minuteTabItem.getId());
                    newItem.setTabTitle(minuteTabItem.getTabTitle());
                    link.put(newItem.type, newItem);
                } else {
                    item.setTitle(item.getTitle() + "," + minuteTabItem.getTitle());
                }
            }

            Iterator<Map.Entry<String, MinuteTabItem>> it = link.entrySet().iterator();
            StringBuilder sb = new StringBuilder();
            while (it.hasNext()) {
                Map.Entry<String, MinuteTabItem> entry = it.next();
                MinuteTabItem value = (MinuteTabItem) entry.getValue();
                String title = value.getTitle();
                String[] split = title.split(",");
                List<String> strings = Arrays.asList(split);
                Collections.sort(strings);
                for (int i = 0; i < strings.size(); i++) {
                    String s = strings.get(i);
                    sb.append(s).append(",");
                }
                sb.setLength(sb.length() - 1);
                value.setTitle(sb.toString());
                sb.setLength(0);
            }
            return new ArrayList<>(link.values());
        } else {
            return new ArrayList<>(cartMap.values());
        }
    }

    public int size() {
        return cartMap.size();
    }

    public void clear() {
        Iterator<Map.Entry<String, MinuteTabItem>> iterator = cartMap.entrySet().iterator();
        String value = ChipsVO.chipsVOS().get(BetCartDataManager.getInstance().getChipsIndex()).value;
        while (iterator.hasNext()) {
            Map.Entry<String, MinuteTabItem> next = iterator.next();
            next.getValue().mutiple = 1;
            next.getValue().check = false;
            next.getValue().betMoney = value;
        }
        cartMap.clear();
        link.clear();
    }

//    public void clearhn() {
//        Iterator<Map.Entry<String, MinuteTabItem>> iterator = cartMap.entrySet().iterator();
//        int value = ChipsVO.chipsVOS().get(BetCartDataManager.getInstance().getChipsIndex()).value;
//        while (iterator.hasNext()) {
//            Map.Entry<String, MinuteTabItem> next = iterator.next();
//            next.getValue().mutiple = 1;
//            next.getValue().check = false;
//            next.getValue().hncheck = false;
//            next.getValue().betMoney = value;
//        }
//        cartMap.clear();
//        link.clear();
//    }
//    public void clearhn() {
//        Iterator<Map.Entry<String, MinuteTabItem>> iterator = cartMap.entrySet().iterator();
//        int value = ChipsVO.chipsVOS().get(BetCartDataManager.getInstance().getChipsIndex()).value;
//        while (iterator.hasNext()) {
//            Map.Entry<String, MinuteTabItem> next = iterator.next();
//            next.getValue().mutiple = next.getValue().mutiple;
//            next.getValue().check = false;
//            next.getValue().betMoney = value;
//        }
//        cartMap.clear();
//        link.clear();
//
//    }

    public LinkedHashMap<String, MinuteTabItem> getLink() {
        return link;
    }

    public void clearLink() {
        link.clear();

    }
}
