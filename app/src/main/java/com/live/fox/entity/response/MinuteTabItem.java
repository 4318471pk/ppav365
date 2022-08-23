package com.live.fox.entity.response;

import com.live.fox.svga.BetCartDataManager;

import java.util.List;

public class MinuteTabItem {
    public static String lotteryTitle;
    private double Odds;//赔率
    public Integer mutiple = 1;//倍数
    private String tabTitle;//第一球两面
    public int tabType;//第一球两面
    public String type;//
    public String type_text;//
    public String type_text_show;
    public boolean check = false;//是否选择
    public boolean hncheck = false;//河内个位是否选择
    private String id;//投注id
    private String title;//小  大 龙  虎
    private String chineseTitle;
    private List<MinuteTabItem> betItems;//注选项
    public String betMoney;//注选项金额
    private int spanCount;//tab  下是几列
    private int space;//tab  下是几列
    private Integer limit;//限制  选中数量
    private String heNum;
    private int betCount;
    private int betAmount;

    public static String getLotteryTitle() {
        return lotteryTitle;
    }

    public static void setLotteryTitle(String lotteryTitle) {
        MinuteTabItem.lotteryTitle = lotteryTitle;
    }

    public Integer getMutiple() {
        return mutiple;
    }

    public void setMutiple(Integer mutiple) {
        this.mutiple = mutiple;
    }

    public int getTabType() {
        return tabType;
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_text() {
        return type_text;
    }

    public void setType_text(String type_text) {
        this.type_text = type_text;
    }

    public String getType_text_show() {
        return type_text_show;
    }

    public void setType_text_show(String type_text_show) {
        this.type_text_show = type_text_show;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isHncheck() {
        return hncheck;
    }

    public void setHncheck(boolean hncheck) {
        this.hncheck = hncheck;
    }

    public String getBetMoney() {
        return betMoney;
    }

    public void setBetMoney(String betMoney) {
        this.betMoney = betMoney;
    }

    public MinuteTabItem() {
        this.betMoney = ChipsVO.chipsVOS().get(BetCartDataManager.getInstance().getChipsIndex()).value;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getHeNum() {
        return heNum;
    }

    public void setHeNum(String heNum) {
        this.heNum = heNum;
    }

    public int getBetCount() {
        return betCount;
    }

    public void setBetCount(int betCount) {
        this.betCount = betCount;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public double getOdds() {
        return Odds;
    }

    public void setOdds(double odds) {
        Odds = odds;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public List<MinuteTabItem> getBetItems() {
        return betItems;
    }

    public void setBetItems(List<MinuteTabItem> betItems) {
        this.betItems = betItems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChineseTitle() {//xiaofei
        return chineseTitle;
    }

    public void setChineseTitle(String chineseTitle) {
        this.chineseTitle = chineseTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void appendBet(List<LotteryItem> lotteryItems, String lotteryName) {
        if (lotteryItems != null) {
            for (int i = 0; i < lotteryItems.size(); i++) {
                MinuteTabItem it = new MinuteTabItem();
                LotteryItem lotteryItem = lotteryItems.get(i);
                it.type_text = lotteryItem.getType_text();
                it.type = lotteryItem.getType();
                it.type_text_show = lotteryItem.getType_textShow();
                it.setId(lotteryName + "-" + lotteryItem.getType_text() + "-" + i);//careful
                it.betMoney = String.valueOf(lotteryItem.getMoney());
                it.setTitle(lotteryItem.getNumShoW());
                it.setChineseTitle(lotteryItem.getNum());
                BetCartDataManager.getInstance().addOddeField(it);
            }
        }
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }
}
