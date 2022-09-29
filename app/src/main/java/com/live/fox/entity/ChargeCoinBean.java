package com.live.fox.entity;

import java.util.List;

public class ChargeCoinBean {

    private double diamondBalance;
    private double goldCoin;

    private List<RechargeOptional> rechargeOptionalList;

    public double getDiamondBalance() {
        return diamondBalance;
    }

    public void setDiamondBalance(double diamondBalance) {
        this.diamondBalance = diamondBalance;
    }

    public double getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(double goldCoin) {
        this.goldCoin = goldCoin;
    }

    public List<RechargeOptional> getRechargeOptionalList() {
        return rechargeOptionalList;
    }

    public void setRechargeOptionalList(List<RechargeOptional> rechargeOptionalList) {
        this.rechargeOptionalList = rechargeOptionalList;
    }



    public static class RechargeOptional{
        private double amount;
        private double sort;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        private boolean select = false;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getSort() {
            return sort;
        }

        public void setSort(double sort) {
            this.sort = sort;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private int id;
    }
}
