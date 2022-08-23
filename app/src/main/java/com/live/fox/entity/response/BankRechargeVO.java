package com.live.fox.entity.response;

/*****************************************************************
 * Created  on 2020\1\13 0013 下午
 * desciption:
 *****************************************************************/
public class BankRechargeVO {
    private int goldCoin;
    private String orderNo;
    private String uid;

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
