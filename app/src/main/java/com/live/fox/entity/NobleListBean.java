package com.live.fox.entity;

public class NobleListBean {

    //[{"id":1,"vipName":"男爵","vipLevel":1,"vipImg":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/86b4ae20-daaf-4bee-a53c-41bfb54f8aa5.png","vipFrams":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/bc4d7869-6b48-4de5-9b7e-29064d72ff07.png","type":1,"vipMount":0,"vipMdeal":"男爵","medalUrl":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/e596483f-5a67-4481-93fb-b8584bf80fff.png","payType":1,"openPrice":1888,"openGiveDiamond":1500,"renewalPrice":1800,"renewalGiveDiamond":1620,"days":7,"status":1,"createTime":1666965497215,"updateTime":1667271644329,"operator":"buck"},{"id":2,"vipName":"伯爵","vipLevel":3,"vipImg":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/909a0313-fecb-4bf8-88d6-5788ee907375.png","vipFrams":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/45dd7e82-b822-4390-9998-59821afbfd90.png","type":1,"vipMount":0,"vipMdeal":"伯爵","medalUrl":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/0bc4abe2-c958-498a-b170-564edc818601.png","payType":1,"openPrice":8888,"openGiveDiamond":7000,"renewalPrice":7777,"renewalGiveDiamond":6999,"days":7,"status":1,"createTime":1667206606096,"updateTime":1667206633260,"operator":"buck"},{"id":3,"vipName":"子爵","vipLevel":2,"vipImg":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/30248f0c-ce82-4916-9278-59b592f405af.png","vipFrams":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/30a8c8c1-9e26-48ad-bf37-91ffed9a62f8.png","type":1,"vipMount":0,"vipMdeal":"子爵","medalUrl":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/8bad9f67-b0aa-45c6-8398-a038040719ef.png","payType":1,"openPrice":3333,"openGiveDiamond":2666,"renewalPrice":3000,"renewalGiveDiamond":2700,"days":7,"status":1,"createTime":1667271104811,"updateTime":1667271656439,"operator":"buck"},{"id":4,"vipName":"侯爵","vipLevel":4,"vipImg":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/87ee385a-fb47-4e89-9b57-7bf49cca17ee.png","vipFrams":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/7ba61ace-8ff7-442d-99a2-bf19c342e84d.png","type":1,"vipMount":0,"vipMdeal":"侯爵","medalUrl":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/b45e5413-b33b-487b-853e-ba958affaf12.png","payType":1,"openPrice":16666,"openGiveDiamond":18888,"renewalPrice":16666,"renewalGiveDiamond":15000,"days":7,"status":1,"createTime":1667271285023,"updateTime":1667271667015,"operator":"buck"},{"id":5,"vipName":"公爵","vipLevel":5,"vipImg":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/4fdd13ca-8082-4a08-9c91-9cc89d4bcf6b.png","vipFrams":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/6770b974-5234-48e9-893b-41c2b2f8da0b.png","type":1,"vipMount":1,"vipMdeal":"公爵","medalUrl":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/b6368da0-7615-4cc1-997d-4135816d643f.png","payType":1,"openPrice":66666,"openGiveDiamond":60000,"renewalPrice":55555,"renewalGiveDiamond":50000,"days":7,"status":1,"createTime":1667271356412,"updateTime":1667271678862,"operator":"buck"},{"id":6,"vipName":"亲王","vipLevel":6,"vipImg":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/07c1a126-80d0-4e01-bd95-1d71566d656e.png","vipFrams":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/c8282a3a-7bac-4ed2-90a8-3a4e4b08a057.png","type":1,"vipMount":3,"vipMdeal":"亲王","medalUrl":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/e8287b1d-da5c-4b80-b68d-4fcfcf0e7ef2.png","payType":1,"openPrice":166666,"openGiveDiamond":150000,"renewalPrice":133333,"renewalGiveDiamond":120000,"days":7,"status":1,"createTime":1667271546669,"updateTime":1667271691964,"operator":"buck"},{"id":7,"vipName":"国王","vipLevel":7,"vipImg":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/3407eca9-ee4c-47d0-b7e6-14f95026a3ad.png","vipFrams":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/6d6dfd02-1a15-41b8-b8e5-d26e98daab07.png","type":1,"vipMount":4,"vipMdeal":"国王","medalUrl":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/f762509b-e5f1-4054-9d49-6afbed7c5420.png","payType":1,"openPrice":288888,"openGiveDiamond":260000,"renewalPrice":222222,"renewalGiveDiamond":200000,"days":7,"status":1,"createTime":1667271612846,"updateTime":1667271702313,"operator":"buck"}]
    int id;
    String vipName;
    int vipLevel;
    String vipImg;
    String vipFrams;
    int type;
    int vipMount;
    String vipMdeal;
    String medalUrl;
    int payType;
    float openPrice;
    float openGiveDiamond;
    float renewalPrice;
    float renewalGiveDiamond;
    int day;
    int status;
    long createTime;

    long expireTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getVipImg() {
        return vipImg;
    }

    public void setVipImg(String vipImg) {
        this.vipImg = vipImg;
    }

    public String getVipFrams() {
        return vipFrams;
    }

    public void setVipFrams(String vipFrams) {
        this.vipFrams = vipFrams;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVipMount() {
        return vipMount;
    }

    public void setVipMount(int vipMount) {
        this.vipMount = vipMount;
    }

    public String getVipMdeal() {
        return vipMdeal;
    }

    public void setVipMdeal(String vipMdeal) {
        this.vipMdeal = vipMdeal;
    }

    public String getMedalUrl() {
        return medalUrl;
    }

    public void setMedalUrl(String medalUrl) {
        this.medalUrl = medalUrl;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public float getOpenGiveDiamond() {
        return openGiveDiamond;
    }

    public void setOpenGiveDiamond(float openGiveDiamond) {
        this.openGiveDiamond = openGiveDiamond;
    }

    public float getRenewalPrice() {
        return renewalPrice;
    }

    public void setRenewalPrice(float renewalPrice) {
        this.renewalPrice = renewalPrice;
    }

    public float getRenewalGiveDiamond() {
        return renewalGiveDiamond;
    }

    public void setRenewalGiveDiamond(float renewalGiveDiamond) {
        this.renewalGiveDiamond = renewalGiveDiamond;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
