package com.live.fox.entity.response;



import com.live.fox.R;
import com.live.fox.common.CommonLiveControlFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChipsVO implements Serializable {

    public int resId;
    public String value;
    public int index;
    public boolean check;
    private String icon;
    private String name;
    private String chinese;
    private int gameType;
    private String playMethod;
    private static final List<ChipsVO> chipsVOS = new ArrayList<>();
    private static int multipleBet;

    public ChipsVO() {

    }

    private static void initData() {
        chipsVOS.add(new ChipsVO(R.drawable.ic_chip_1, "5", false, 0));
        chipsVOS.add(new ChipsVO(R.drawable.ic_chip_2, "10", false, 1));
        chipsVOS.add(new ChipsVO(R.drawable.ic_chips_3, "20", false, 2));
        chipsVOS.add(new ChipsVO(R.drawable.ic_chips_4, "50", false, 3));
        chipsVOS.add(new ChipsVO(R.drawable.ic_chips_5, "100", false, 4));
        chipsVOS.add(new ChipsVO(R.drawable.ic_chips_6, "200", false, 5));
        chipsVOS.add(new ChipsVO(R.drawable.ic_chips_7, "500", false, 6));
        chipsVOS.add(new ChipsVO(R.drawable.ic_chips_8, CommonLiveControlFragment.lotteryCustom, false, 7));
    }

    public ChipsVO(int ic_chips_6, String v, boolean b, int index) {
        this.resId = ic_chips_6;
        this.value = v;
        this.check = b;
        this.index = index;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayMethod() {
        return playMethod;
    }

    public void setPlayMethod(String playMethod) {
        this.playMethod = playMethod;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 筹码
     */
    public static List<ChipsVO> chipsVOS() {
        if (chipsVOS.size() == 0) {
            initData();
        }
        return chipsVOS;
    }

    public static void upChipsData(int index, int drawable) {
        ChipsVO chipsVO = chipsVOS.get(index);
        chipsVO.setResId(drawable);
        chipsVOS.set(index, chipsVO);
    }

    public static void upChipsData(int index, String value) {
        ChipsVO chipsVO = chipsVOS.get(index);
        chipsVO.setValue(value);
        chipsVOS.set(index, chipsVO);
    }

    public static void upMultipleBet(int index) {
        multipleBet = index;
    }

    public static int getMultipleBet() {
        return multipleBet;
    }
}
