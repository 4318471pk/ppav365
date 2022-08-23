//package com.live.fox.entity.response;
//
//import com.live.fox.entity.OneMinuteTabItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
///*****************************************************************
// * Created by  on 2020\1\16 0016 下午
// * desciption:
// *****************************************************************/
//public class LottoryBcVO {
//    private String lottoryTitle;//一分时时彩  or 一分赛车 or 一分六合
//    private int lottoryType;//一分时时彩1   一分赛车2  一分六合3
//    private List<OneMinuteTabItem> tabItems;//
//
//    public LottoryBcVO() {
//        List<OneMinuteTabItem> tabItems = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            OneMinuteTabItem tabItem = new OneMinuteTabItem();
//            tabItem.setOneMinuteHourHourBc(i);
//            tabItems.add(tabItem);
//        }
//        this.setTabItems(tabItems);
//    }
//
//    public String getLottoryTitle() {
//        return lottoryTitle;
//    }
//
//    public void setLottoryTitle(String lottoryTitle) {
//        this.lottoryTitle = lottoryTitle;
//        List<OneMinuteTabItem> tabItems = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            OneMinuteTabItem tabItem = new OneMinuteTabItem();
//            tabItem.setOneMinuteHourHourBc(i);
//            tabItems.add(tabItem);
//        }
//    }
//
//    public int getLottoryType() {
//        return lottoryType;
//    }
//
//    public void setLottoryType(int lottoryType) {
//        this.lottoryType = lottoryType;
//    }
//
//    public List<OneMinuteTabItem> getTabItems() {
//        return tabItems;
//    }
//
//    public void setTabItems(List<OneMinuteTabItem> tabItems) {
//        this.tabItems = tabItems;
//    }
//
//    //一分时时彩
//
//}
