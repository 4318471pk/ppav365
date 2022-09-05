package com.live.fox.entity;


import com.live.fox.AppConfig;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private double anchorCoin;  // 主播金币
    private long anchorExp;  //主播经验
    private int anchorLevel;  //主播等级
    private int auth;  //0:否 1:認證中 2:認證成功
    private String avatar;  //头像
    private String constellation;  //星座
    private double goldCoin; //金币
    private String hobby;  // 爱好
    private String nickname; //昵称
    private String phone;  // 手机号
    private Integer sex;  //性别0未知1男2女
    private String signature;  //个性签名
    private long uid;  //用户唯一标识
    private long userCoin;  //用户币
    private double userExp;  //用户经验
    private int userLevel;  //用户等级
    private String city;  //地理位置
    private long fans;  //粉丝数
    private long follows;  //关注数
    private Boolean isReject = false;  //是否拉黑
    private Boolean isFollow = false;  //是否关注
    private Boolean isRename = false;  //是否改过名
    private Boolean isFans = false; //是否互为关注
    private String imToken;
    private Boolean isFirstLogin = false;  //直播间活动充值按钮是否展示
    private Boolean isSignIn = false; //今日是否签到
    private Integer signInNum;  //连续签到次数
    public Integer manage = 0; //是否超管：0否 1是
    public int surplusMovTime = 0;  //剩余观影次数
    public int totalMovTime = 0; //总观影次数
    public int chatHide = 0;
    public int roomHide = 0;
    public int rankHide = 0;
    public long destUid;
    private boolean isVip = false;
    private long vipExp = 0;
    private long receiveCoin = 0L;
    private long sendCoin = 0L;
    private String gxAvatar;
    private String gxId;
    private String shAvatar;
    private String shId;
    private Integer gxLevel = 0;
    public Long vipUid;
    private Integer shType = 0;
    private boolean isNotification;
    private ArrayList<Integer> badgeList;
    String address;
    String province;
    int gameQuota; //可提现额度
    boolean isBlackChat;
    int autoUpdownBalance; //是否自动上下分 1 不自动 2自动
    public boolean come = false;

    public ArrayList<Integer> getBadgeList() {
        return badgeList;
    }

    public void setBadgeList(ArrayList<Integer> badgeList) {
        this.badgeList = badgeList;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public boolean isFans() {
        return isFans;
    }

    public void setFans(Boolean fans) {
        isFans = fans;
    }

    public double getAnchorCoin() {
        if (AppConfig.isThLive()) {
            return anchorCoin;
        }
        return (long) anchorCoin;
    }

    public void setAnchorCoin(double anchorCoin) {
        this.anchorCoin = anchorCoin;
    }

    public long getAnchorExp() {
        return anchorExp;
    }

    public void setAnchorExp(long anchorExp) {
        this.anchorExp = anchorExp;
    }

    public int getAnchorLevel() {
        return anchorLevel;
    }

    public void setAnchorLevel(Integer anchorLevel) {
        this.anchorLevel = anchorLevel;
    }

    public int getAuth() {
        return auth;
    }

    public int getSurplusMovTime() {
        return surplusMovTime;
    }

    public void setSurplusMovTime(int surplusMovTime) {
        this.surplusMovTime = surplusMovTime;
    }

    public int getTotalMovTime() {
        return totalMovTime;
    }

    public void setTotalMovTime(int totalMovTime) {
        this.totalMovTime = totalMovTime;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getAuthStatus() {
        String str = null;
        switch (auth) {
            case 0:
                str = "未认证";
                break;
            case 1:
                str = "审核中";
                break;
            case 2:
                str = "已认证";
                break;
        }
        return str;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getAvatar() {
        if (StringUtils.isEmpty(avatar) || avatar.contains("live-api") || avatar.contains("www.baidu.com")) {
            return "";
        }
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public double getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(double goldCoin) {
        this.goldCoin = goldCoin;
    }


    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex == null ? 1 : sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSignature() {
        if (StringUtils.isEmpty(signature)) {
            return "";
        }
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUserCoin() {
        return userCoin;
    }

    public void setUserCoin(long userCoin) {
        this.userCoin = userCoin;
    }

    public double getUserExp() {
        return userExp;
    }

    public void setUserExp(double userExp) {
        this.userExp = userExp;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public long getFans() {
        return fans;
    }

    public void setFans(long fans) {
        this.fans = fans;
    }

    public long getFollows() {
        return follows;
    }

    public void setFollows(long follows) {
        this.follows = follows;
    }

    public boolean isReject() {
        return isReject == null ? false : isReject.booleanValue();
    }

    public void setReject(Boolean reject) {
        isReject = reject;
    }

    public boolean isFollow() {
        return isFollow == null ? false : isFollow.booleanValue();
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
    }


    public String getCity() {
        if (StringUtils.isEmpty(city)) {
            return "unknow";
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setSignIn(Boolean signIn) {
        isSignIn = signIn;
    }


    public long getReceiveCoin() {
        return receiveCoin;
    }

    public void setReceiveCoin(long receiveCoin) {
        this.receiveCoin = receiveCoin;
    }

    public long getSendCoin() {
        return sendCoin;
    }

    public void setSendCoin(long sendCoin) {
        this.sendCoin = sendCoin;
    }

    public String getGxAvatar() {
        return gxAvatar;
    }

    public void setGxAvatar(String gxAvatar) {
        this.gxAvatar = gxAvatar;
    }

    public String getGxId() {
        return gxId;
    }

    public void setGxId(String gxId) {
        this.gxId = gxId;
    }

    public String getShAvatar() {
        return shAvatar;
    }

    public void setShAvatar(String shAvatar) {
        this.shAvatar = shAvatar;
    }

    public String getShId() {
        return shId;
    }

    public void setShId(String shId) {
        this.shId = shId;
    }

    public long getVipExp() {
        return vipExp / 1000;
    }

    public void setVipExp(long vipExp) {
        this.vipExp = vipExp;
    }

    public int getManage() {
        return manage == null ? 0 : manage.intValue();
    }

    public void setManage(Integer manage) {
        this.manage = manage;
    }

    public Boolean getReject() {
        return isReject;
    }

    public Boolean getFollow() {
        return isFollow;
    }

    public Boolean getFirstLogin() {
        return isFirstLogin;
    }

    public Boolean getSignIn() {
        return isSignIn;
    }

    public Integer getGxLevel() {
        return gxLevel;
    }

    public void setGxLevel(Integer gxLevel) {
        this.gxLevel = gxLevel;
    }

    public Integer getShType() {
        return shType;
    }

    public void setShType(Integer shType) {
        this.shType = shType;
    }

    public Boolean getRename() {
        return isRename;
    }

    public void setRename(Boolean rename) {
        isRename = rename;
    }

    public void setAnchorLevel(int anchorLevel) {
        this.anchorLevel = anchorLevel;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean isBlackChat() {
        return isBlackChat;
    }

    public void setBlackChat(boolean blackChat) {
        isBlackChat = blackChat;
    }

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    public long getDestUid() {
        return destUid;
    }

    public void setDestUid(long destUid) {
        this.destUid = destUid;
    }

    public Anchor covertToAnchor() {
        Anchor anchor = new Anchor();
        anchor.setAnchorId(getUid());
        anchor.setAvatar(getAvatar());
        anchor.setNickname(getNickname());
        return anchor;
    }

    //登录是用户是否是超管
    public boolean isSuperManager() {
        return manage == 1;
    }

    //登录是用户是否是家族长
    // 2主播 ，3 家族长 ，4超管
    public boolean isFamilyManager() {
        for (Integer i : badgeList) {
            if (i == 3) {
                return true;
            }
        }
        return false;
    }

    public int getGameQuota() {
        return gameQuota;
    }

    public void setGameQuota(int gameQuota) {
        this.gameQuota = gameQuota;
    }

    public int getAutoUpdownBalance() {
        return autoUpdownBalance;
    }

    public void setAutoUpdownBalance(int autoUpdownBalance) {
        this.autoUpdownBalance = autoUpdownBalance;
    }

    @Override
    public String toString() {
        return "User{" +
                "anchorCoin=" + anchorCoin +
                ", anchorExp=" + anchorExp +
                ", anchorLevel=" + anchorLevel +
                ", auth=" + auth +
                ", avatar='" + avatar + '\'' +
                ", constellation='" + constellation + '\'' +
                ", goldCoin=" + goldCoin +
                ", hobby='" + hobby + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", signature='" + signature + '\'' +
                ", uid=" + uid +
                ", userCoin=" + userCoin +
                ", userExp=" + userExp +
                ", userLevel=" + userLevel +
                ", city='" + city + '\'' +
                ", fans=" + fans +
                ", follows=" + follows +
                ", isReject=" + isReject +
                ", isFollow=" + isFollow +
                ", isRename=" + isRename +
                ", isFans=" + isFans +
                ", imToken='" + imToken + '\'' +
                ", isFirstLogin=" + isFirstLogin +
                ", isSignIn=" + isSignIn +
                ", signInNum=" + signInNum +
                ", manage=" + manage +
                ", surplusMovTime=" + surplusMovTime +
                ", totalMovTime=" + totalMovTime +
                ", isVip=" + isVip +
                ", vipExp=" + vipExp +
                ", receiveCoin=" + receiveCoin +
                ", sendCoin=" + sendCoin +
                ", gxAvatar='" + gxAvatar + '\'' +
                ", gxId='" + gxId + '\'' +
                ", shAvatar='" + shAvatar + '\'' +
                ", shId='" + shId + '\'' +
                ", gxLevel=" + gxLevel +
                ", shType=" + shType +
                ", badgeList=" + badgeList +
                '}';
    }

}
