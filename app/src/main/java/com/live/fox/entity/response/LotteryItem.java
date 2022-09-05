package com.live.fox.entity.response;

import static com.live.fox.dialog.HNDialogFragment.whichPage;

import com.live.fox.svga.BetCartDataManager;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 投注参数制作
 */
public class LotteryItem {

    private int money;
    private int notes;
    private String num;
    private String numShoW;
    private int rebate;
    private String type;
    private String type_text;
    private String type_textShow;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getNotes() {
        return notes;
    }

    public void setNotes(int notes) {
        this.notes = notes;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNumShoW() {
        return numShoW;
    }

    public void setNumShoW(String numShoW) {
        this.numShoW = numShoW;
    }

    public String getType_textShow() {
        return type_textShow;
    }

    public void setType_textShow(String type_textShow) {
        this.type_textShow = type_textShow;
    }

    public int getRebate() {
        return rebate;
    }

    public void setRebate(int rebate) {
        this.rebate = rebate;
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

    /**
     * 添加参数
     *
     * @param isMix isMix
     * @return 返回处理后的数据
     */
    public static List<LotteryItem> addParameter(boolean isMix) {
        List<LotteryItem> items2 = new ArrayList<>();
        List<MinuteTabItem> items = BetCartDataManager.getInstance().asList(isMix);
        for (int k = 0; k < items.size(); k++) {
            MinuteTabItem minuteTabItem = items.get(k);
            LotteryItem lotteryItem = new LotteryItem();
            lotteryItem.setMoney(Integer.parseInt(minuteTabItem.betMoney));
            lotteryItem.setNotes(1);
            lotteryItem.setRebate(0);
            if (21 == whichPage) {
                lotteryItem.setType("TM_XH");
            } else if (22 == whichPage) {
                lotteryItem.setType("TM_SR");
            } else if (23 == whichPage) {
                lotteryItem.setType("TM_KX");
            } else {
                lotteryItem.setType(minuteTabItem.type);
            }
            String typeText = "";
            String minuteTypeText = minuteTabItem.type_text;

            switch (minuteTypeText) {
                case "个位":
                    typeText = "个位";
                    break;

                case "龙虎万千":
                    typeText = "龙虎万千";
                    break;
                case "十位":
                    typeText = "十位";
                    break;

                case "猜冠军":
                    typeText = "猜冠军";
                    break;

                case "大小单双-冠亚":
                case "冠亚和大小单双":
                    typeText = "冠亚和大小单双";
                    break;

                case "特码":
                case "特码两面":
                    typeText = "特码两面";
                    break;

                case "特码生肖":
                    typeText = "特码生肖";
                    break;

                case "特码色波":
                    typeText = "特码色波";
                    break;

                case "一分快三":
                case "和值":
                    typeText = "和值";
                    break;

                case "二同号复选":
                case "二同号":
                    if ("ETH".equals(minuteTabItem.type)) {
                        typeText = "二同号";
                    } else {
                        typeText = "二同号复选";
                    }
                    break;

                case "一同号":
                case "三军":
                    if ("YTH".equals(minuteTabItem.type)) {
                        typeText = "一同号";
                    } else {
                        typeText = "三军";
                    }
                    break;

                case "二不同号":
                case "二不同":
                    typeText = "二不同";
                    break;

                case "选号码":
                    if (11 == whichPage) {
                        typeText = "普通号-选号";
                    } else {
                        typeText = "特码-选号";
                    }
                    break;

                case "输入号码":
                    if (12 == whichPage) {
                        typeText = "普通号-输入";
                    } else {
                        typeText = "特码-输入";
                    }
                    break;

                case "快选":
                    if (13 == whichPage) {
                        typeText = "普通号-快选";
                    } else {
                        typeText = "特码-快选";
                    }
                    break;

                default:
                    ToastUtils.showShort("type_text is Null");
                    return null;
            }

            lotteryItem.setType_text(typeText);
            if (minuteTabItem.getId().contains("个")) {
                lotteryItem.setNum("0|" + minuteTabItem.getChineseTitle());
            } else if (minuteTabItem.getId().contains("十")) {
                if (!minuteTabItem.getChineseTitle().contains("|0")) {
                    lotteryItem.setNum(minuteTabItem.getChineseTitle() + "|0");
                } else {
                    lotteryItem.setNum(minuteTabItem.getChineseTitle());
                }
            } else {
                switch (minuteTypeText) {
                    case "选号码":
                    case "快选":
                    case "输入号码":
                        lotteryItem.setNum(minuteTabItem.getHeNum());
                        break;
                    default:
                        lotteryItem.setNum(minuteTabItem.getChineseTitle());
                        break;
                }
            }
            items2.add(lotteryItem);
        }
        return items2;
    }
}
