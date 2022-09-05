//package com.live.lianhong.model;
//
//
//import com.live.lianhong.Constant;
//
//import java.io.Serializable;
//
//
//public class GiftBean
//        implements Serializable {
//    /**
//     * 大动画数
//     */
//    private int bimgs;
//    /**
//     * 封面图名称 ,
//     */
//    private String cover;
//    /**
//     * 展示时间
//     */
//    private float duration;
//    /**
//     * 礼物唯一标识
//     */
//    private int gid;
//    /**
//     * 礼物名称
//     */
//    private String gname;
//
//    private String carType = "0";
//    /**
//     * 货币价值
//     */
//    private int goldCoin;
//    /**
//     * 是否显示：0不显示 1显示
//     */
//    private int isShow;
//    /**
//     * 礼物可先择数量
//     */
//    private String numList;
//    /**
//     * 离子数
//     */
//    private int pimgs;
//    /**
//     * 特效类型：0连送礼物1全屏礼物2半屏礼物
//     */
//    private int playType;
//    /**
//     * 资源下载地址
//     */
//    private String resourceUrl;
//    /**
//     * 小动画数
//     */
//    private int simgs;
//    /**
//     * 排序
//     */
//    private int sort;
//    /**
//     * 礼物类型：0礼物1座驾
//     */
//    private int type;
//    /**
//     * 礼物版本
//     */
//    private int version;
//
//    /**
//     * 0:不是 1:是
//     */
//    private int isPackageGift = 0;
//    /**
//     * 背包中该礼物的数量
//     */
//    private int giftCount = 0;
//
//    /*礼物角标*/
//    private String superscript;
//
//    /*当前礼物价值描述*/
//    private String descript;
//
//    /*当前礼物魅力*/
//    private String xl;
//
//    /*当前礼物糧票*/
//    private String xz;
//
//    public String getXl() {
//        return xl;
//    }
//
//    public void setXl(String xl) {
//        this.xl = xl;
//    }
//
//    public String getXz() {
//        return xz;
//    }
//
//    public void setXz(String xz) {
//        this.xz = xz;
//    }
//
//    private int subject = 0;
//
//    private String tags;
//
//    public void setIsPackageGift(int isPackageGift) {
//        this.isPackageGift = isPackageGift;
//    }
//
//    public String getSuperscript() {
//        return superscript;
//    }
//
//    public void setSuperscript(String superscript) {
//        this.superscript = superscript;
//    }
//
//    public int getBimgs() {
//        return bimgs;
//    }
//
//    public void setBimgs(int bimgs) {
//        this.bimgs = bimgs;
//    }
//
//    public String getCover() {
//        return cover;
//    }
//
//    public void setCover(String cover) {
//        this.cover = cover;
//    }
//
//    public float getDuration() {
//        return duration;
//    }
//
//    public void setDuration(float duration) {
//        this.duration = duration;
//    }
//
//    public int getGid() {
//        return gid;
//    }
//
//    public void setGid(int gid) {
//        this.gid = gid;
//    }
//
//    public String getGname() {
//        return gname;
//    }
//
//    public void setGname(String gname) {
//        this.gname = gname;
//    }
//
//    public int getGoldCoin() {
//        return goldCoin;
//    }
//
//    public void setGoldCoin(int goldCoin) {
//        this.goldCoin = goldCoin;
//    }
//
//    public int getIsShow() {
//        return isShow;
//    }
//
//    public void setIsShow(int isShow) {
//        this.isShow = isShow;
//    }
//
//    public String getNumList() {
//        return numList;
//    }
//
//    public void setNumList(String numList) {
//        this.numList = numList;
//    }
//
//    public int getPimgs() {
//        return pimgs;
//    }
//
//    public void setPimgs(int pimgs) {
//        this.pimgs = pimgs;
//    }
//
//    public int getPlayType() {
//        return playType;
//    }
//
//    public void setPlayType(int playType) {
//        this.playType = playType;
//    }
//
//    public String getResourceUrl() {
//        return resourceUrl;
//    }
//
//    public void setResourceUrl(String resourceUrl) {
//        this.resourceUrl = resourceUrl;
//    }
//
//    public int getSimgs() {
//        return simgs;
//    }
//
//    public void setSimgs(int simgs) {
//        this.simgs = simgs;
//    }
//
//    public int getSort() {
//        return sort;
//    }
//
//    public void setSort(int sort) {
//        this.sort = sort;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public int getVersion() {
//        return version;
//    }
//
//    public void setVersion(int version) {
//        this.version = version;
//    }
//
//    public int getIsPackageGift() {
//        return isPackageGift;
//    }
//
//    public void setPackageGift(int packageGift) {
//        isPackageGift = packageGift;
//    }
//
//    public String getDescript() {
//        return descript;
//    }
//
//    public void setDescript(String descript) {
//        this.descript = descript;
//    }
//
////    public int getGiftOrPackage() {
////        if (getIsPackageGift() == 0) {
////            return Constant.GIFT_TYPE_GIFT;
////        } else {
////            return Constant.GIFT_TYPE_PACKAGE;
////        }
////    }
//
//    public int getGiftCount() {
//        return giftCount;
//    }
//
//    public void setGiftCount(int giftCount) {
//        this.giftCount = giftCount;
//    }
//
//    public int getSubject() {
//        return subject;
//    }
//
//    public void setSubject(int subject) {
//        this.subject = subject;
//    }
//
//    public String getTags() {
//        return tags;
//    }
//
//    public void setTags(String tags) {
//        this.tags = tags;
//    }
//
//    public String getCarType() {
//        return carType;
//    }
//
//    public void setCarType(String carType) {
//        this.carType = carType;
//    }
//
//    @Override
//    public String toString() {
//        return "GiftBean{" +
//                "bimgs=" + bimgs +
//                ", cover='" + cover + '\'' +
//                ", duration=" + duration +
//                ", gid=" + gid +
//                ", gname='" + gname + '\'' +
//                ", carType='" + carType + '\'' +
//                ", goldCoin=" + goldCoin +
//                ", isShow=" + isShow +
//                ", numList='" + numList + '\'' +
//                ", pimgs=" + pimgs +
//                ", playType=" + playType +
//                ", resourceUrl='" + resourceUrl + '\'' +
//                ", simgs=" + simgs +
//                ", sort=" + sort +
//                ", type=" + type +
//                ", version=" + version +
//                ", isPackageGift=" + isPackageGift +
//                ", giftCount=" + giftCount +
//                ", superscript='" + superscript + '\'' +
//                ", descript='" + descript + '\'' +
//                ", xl='" + xl + '\'' +
//                ", xz='" + xz + '\'' +
//                ", subject=" + subject +
//                ", tags='" + tags + '\'' +
//                '}';
//    }
//}
