package com.live.fox.entity.response;

import java.util.List;

/*****************************************************************
 * Created  on 2020\1\16 0016 下午
 * desciption:
 *****************************************************************/
public class LotteryCpVO {
    private String lotteryTitle;//一分时时彩  or 一分赛车 or 一分六合
    private int lotteryType;//一分时时彩1   一分赛车2  一分六合3
    private List<MinuteTabItem> tabItems;//

    public LotteryCpVO() {

    }

    public String getLotteryTitle() {
        return lotteryTitle;
    }

    public void setLotteryTitle(String lotteryTitle) {
        this.lotteryTitle = lotteryTitle;
    }

    public int getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(int lotteryType) {
        this.lotteryType = lotteryType;
    }

    public List<MinuteTabItem> getTabItems() {
        return tabItems;
    }

    public void setTabItems(List<MinuteTabItem> tabItems) {
        this.tabItems = tabItems;
    }
}
