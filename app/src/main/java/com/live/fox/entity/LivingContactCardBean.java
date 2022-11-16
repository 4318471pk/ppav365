package com.live.fox.entity;

import java.math.BigDecimal;

public class LivingContactCardBean {


    public int anchorId;
    public String nickname;
    public String avatar;
    public String signature;
    public String contactType;
    public String contactDetails;
    public BigDecimal showContactPrice;
    public BigDecimal sendGifPrice;
    public Boolean doneFlag;

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public BigDecimal getShowContactPrice() {
        return showContactPrice;
    }

    public void setShowContactPrice(BigDecimal showContactPrice) {
        this.showContactPrice = showContactPrice;
    }

    public BigDecimal getSendGifPrice() {
        return sendGifPrice;
    }

    public void setSendGifPrice(BigDecimal sendGifPrice) {
        this.sendGifPrice = sendGifPrice;
    }

    public Boolean isDoneFlag() {
        return doneFlag;
    }

    public void setDoneFlag(Boolean doneFlag) {
        this.doneFlag = doneFlag;
    }
}
