package com.live.fox.entity;

import java.util.List;

public class LotteryTypeHistoryBean {

    /**
     * totalProfit : 31.450000000000003
     * totalBet : 80
     * resultList : [{"betAmountAll":20,"profitAmountAll":9.85,"lotteryName":"jsks","lotteryCount":4,"lotteryIcon":"http://bw18.oss-cn-hongkong.aliyuncs.com/game/game_1608364798000.png","totalbetAmountAll":null,"totalProfitAmountAll":null},{"betAmountAll":60,"profitAmountAll":21.6,"lotteryName":"pk10","lotteryCount":4,"lotteryIcon":"http://bw18.oss-cn-hongkong.aliyuncs.com/game/game_1606660873000.png","totalbetAmountAll":null,"totalProfitAmountAll":null}]
     */

    private double totalProfit;
    private int totalBet;
    private List<ResultListBean> resultList;

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public int getTotalBet() {
        return totalBet;
    }

    public void setTotalBet(int totalBet) {
        this.totalBet = totalBet;
    }

    public List<ResultListBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultListBean> resultList) {
        this.resultList = resultList;
    }

    public static class ResultListBean {
        /**
         * betAmountAll : 20
         * profitAmountAll : 9.85
         * lotteryName : jsks
         * lotteryCount : 4
         * lotteryIcon : http://bw18.oss-cn-hongkong.aliyuncs.com/game/game_1608364798000.png
         * totalbetAmountAll : null
         * totalProfitAmountAll : null
         */

        private int betAmountAll;
        private double profitAmountAll;
        private String lotteryName;
        private int lotteryCount;
        private String lotteryIcon;
        private String nickName;

        public int getBetAmountAll() {
            return betAmountAll;
        }

        public void setBetAmountAll(int betAmountAll) {
            this.betAmountAll = betAmountAll;
        }

        public double getProfitAmountAll() {
            return profitAmountAll;
        }

        public void setProfitAmountAll(double profitAmountAll) {
            this.profitAmountAll = profitAmountAll;
        }

        public String getLotteryName() {
            return lotteryName;
        }

        public void setLotteryName(String lotteryName) {
            this.lotteryName = lotteryName;
        }

        public int getLotteryCount() {
            return lotteryCount;
        }

        public void setLotteryCount(int lotteryCount) {
            this.lotteryCount = lotteryCount;
        }

        public String getLotteryIcon() {
            return lotteryIcon;
        }

        public void setLotteryIcon(String lotteryIcon) {
            this.lotteryIcon = lotteryIcon;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
