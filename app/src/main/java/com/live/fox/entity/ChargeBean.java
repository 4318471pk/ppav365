package com.live.fox.entity;

public class ChargeBean {

    private String money;
    private boolean select = false;
    private String diamond;

    public ChargeBean(String money) {
        this.money = money;
    }

    public ChargeBean(String money, boolean select) {
        this.money = money;
        this.select = select;
    }

    public ChargeBean(String money, String diamond,boolean select) {
        this.money = money;
        this.diamond = diamond;
        this.select = select;
    }

    public ChargeBean(String money, String diamond) {
        this.money = money;
        this.diamond = diamond;
    }


    public boolean getSelect(){
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getMoney(){
        return money;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getDiamond() {
         return this.diamond;
    }

}
