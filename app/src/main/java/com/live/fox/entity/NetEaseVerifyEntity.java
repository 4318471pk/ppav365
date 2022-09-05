package com.live.fox.entity;

public class NetEaseVerifyEntity {
    private Integer language;   //语言
    private String verificationNo; //验证方式

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public String getVerificationNo() {
        return verificationNo;
    }

    public void setVerificationNo(String verificationNo) {
        this.verificationNo = verificationNo;
    }
}
