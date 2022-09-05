package com.live.fox.entity;


public class BankInfo {

    private long cardId;
    private String cardNo;
    private String bankCity;
    private String bankName;
    private String bankProvince;
    private String bankSub;
    private String trueName;
    private int type;  //0银行 1支付宝
    private double reward;
    private long bankId;
    private int ishaveCashPwd;
    private String mobile;
    private String logs;
    private int bankCount;
    private String remark;
    private boolean isCheck;

    public int getBankCount() {
        return bankCount;
    }

    public void setBankCount(int bankCount) {
        this.bankCount = bankCount;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public int getIshaveCashPwd() {
        return ishaveCashPwd;
    }

    public void setIshaveCashPwd(int ishaveCashPwd) {
        this.ishaveCashPwd = ishaveCashPwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankSub() {
        return bankSub;
    }

    public void setBankSub(String bankSub) {
        this.bankSub = bankSub;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
